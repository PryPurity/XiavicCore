package com.github.prypurity.xiaviccore.Commands.StaffCmds.cheats;

import com.github.prypurity.xiaviccore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.prypurity.xiaviccore.Main.messages;
import static com.github.prypurity.xiaviccore.Main.permissions;

/**
 * Represents a command for sudo.
 */
public class SudoCommand implements TabExecutor {

    @Override //Target: /sudo <player> <command> [args]
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
        @NotNull final String label, @NotNull final String[] args) {
        if (!sender.hasPermission(permissions.getString("Sudo")) || !sender.isOp()) {
            Utils.chat(sender, messages.getString("NoPerms"));
            return true;
        }
        if (args.length < 2) {
            return false;
        }
        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            Utils.chat(sender, messages.getString("PlayerNotFound"));
            return true;
        }
        if (args[1].equalsIgnoreCase("c:")) { //Send chat message as the player.
            player.chat(Arrays.toString(Arrays.copyOfRange(args, 2, args.length - 1))); //TODO Needs testing
            return true;
        }
        final Command targetCommand = Bukkit.getPluginCommand(args[1]);
        if (targetCommand == null) {

            Utils.chat(sender, messages.getString("NoSuchCommand"));
            return true;
        }
        targetCommand.execute(sender, targetCommand.getLabel(), Arrays.copyOfRange(args, 1, args.length - 1));
        return true;
    }

    /**
     * Tab completer for sudo.
     * This tab-completer will attempt to tab complete:
     * - The player to execute as
     * - The command to execute as (in the context of the player)
     * - The args of the command {@link Command#tabComplete(CommandSender, String, String[])}
     * {@inheritDoc}
     */
    @Override public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
        @NotNull final Command command, @NotNull final String unusedAlias,
        @NotNull final String[] args) {
        if (!sender.hasPermission(permissions.getString("Sudo")) && !sender.isOp()) {
            return Collections.emptyList();
        }
        final Stream<? extends Command> commands =
            Bukkit.getCommandAliases().keySet().parallelStream().map(Bukkit::getPluginCommand)
                .filter(Objects::nonNull).filter((cmd) -> {
                if (!sender.isOp()) {
                    if (cmd.getPermission() != null) {
                        return sender.hasPermission(cmd.getPermission());
                    }
                }
                return true;
            }); //Get all commands.

        final Iterator<? extends Command> iterator = commands.iterator();
        Collection<String> combined = new HashSet<>(Math.toIntExact(commands.count() * 2));
        while (iterator.hasNext()) {
            //Combine command label and aliases.
            final Command cmd = iterator.next();
            combined.add(cmd.getLabel());
            combined.addAll(cmd.getAliases());
        }
        Stream<String> ret = combined.stream();
        switch (args.length) {
            case 0:
                ret = Bukkit.getOnlinePlayers().stream().map(Player::getName);
                break;
            case 1: //If no args after player decleration, tab complete all commands which the player has perms for. Which means break-ing here.
                ret = ret.filter(name -> name.startsWith(args[0]));
                break;
            case 2:
                //Start filtering commands and aliases.
                final String target = args[1];
                if (target.isEmpty()) {
                    break;
                }
                if (target.equalsIgnoreCase("c:")) {
                    ret = Stream.empty();
                    break;
                }
                //Filter by if the command/alias starts with the target string passed.
                ret = ret.filter(str -> str.startsWith(target));
                break;
            default: //If arg length > 2 then tab based off the known command.
                final Player targetPlayer = Bukkit.getPlayer(args[0]);
                final PluginCommand pluginCommand =
                    Bukkit.getPluginCommand(args[1]); //args[1] is the target command.
                if (pluginCommand == null) {
                    return Collections.emptyList();
                }
                ret = targetPlayer == null ?
                    Stream.empty() : //If player is null --> empty stream.
                    pluginCommand.tabComplete(targetPlayer, unusedAlias,
                        Arrays.copyOfRange(args, 2, args.length - 1)).stream();
        }
        //Returns the alphabetically sorted results.
        return ret.sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
}

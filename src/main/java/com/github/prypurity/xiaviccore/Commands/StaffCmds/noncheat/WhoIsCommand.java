package com.github.prypurity.xiaviccore.Commands.StaffCmds.noncheat;

import com.github.prypurity.xiaviccore.Main;
import com.github.prypurity.xiaviccore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//This command cannot be edited @ Files.
public class WhoIsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Whois")) || player.isOp()) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        Location loc = target.getLocation();
                        Utils.chat(player, "&6Name: &9" + target.getName() + "&6, Nickname: &9" + target.getDisplayName());
                        Utils.chat(player, "&6Player UUID: &9" + target.getUniqueId());
                        Utils.chat(player, "&6Exp: &9" + target.getTotalExperience() + "&6, Next Level: &9" + target.getExpToLevel());
                        Utils.chat(player, "&6Health: &9" + target.getHealth() + "&6, Food: &9" + target.getFoodLevel());
                        Utils.chat(player, "&6Time: &9" + target.getPlayerTime());
                        Utils.chat(player, "&6Location: &9" + target.getWorld().getName().toUpperCase() + " | &cX &9" + loc.getBlockX() + " | &cY &9" + loc.getBlockY() + " | &cZ &9" + loc.getBlockZ());
                        Utils.chat(player, "&6Gamemode: &9" + target.getGameMode() + "&6, Can Fly: &9" + target.getAllowFlight());
                        Utils.chat(player, "&6First Joined: &9" + target.getFirstPlayed() + "&6, Last Played: &9" + target.getLastPlayed());
                    } else {
                        Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                    }
                } else {
                    Utils.chat(player, Main.messages.getString("WhoIsHelp"));
                }
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        }
        Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        return false;
    }
}
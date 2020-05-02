package com.github.prypurity.xiaviccore.Commands.UserCmds.Essential.Teleport;

import com.github.prypurity.xiaviccore.Utils.Listeners.TeleportHandler;
import com.github.prypurity.xiaviccore.Utils.Utils;
import com.github.prypurity.xiaviccore.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RandomTPCommand implements CommandExecutor {
    Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("RandomTP")) || player.isOp()) {
                double randomX = getCoord();
                double randomZ = getCoord();
                double randomY = player.getWorld().getHighestBlockYAt((int) randomX, (int) randomZ) + 1.5;
                Location rtp = new Location(player.getWorld(), randomX, randomY, randomZ);
//                player.teleport(rtp);
                TeleportHandler.teleport(player, rtp);
                Utils.chat(player, Main.messages.getString("RTP"));
                Block block = rtp.getBlock().getRelative(0, -1, 0);
                if (block.getType().equals(Material.WATER) || block.getType().equals(Material.LAVA)) {
                    block.setType(Material.DIRT);
                }
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }

    public double getCoord() {
        double distance = plugin.getConfig().getDouble("RTPDistance");
        return (Math.random() * (distance * 2)) - distance;
    }

}

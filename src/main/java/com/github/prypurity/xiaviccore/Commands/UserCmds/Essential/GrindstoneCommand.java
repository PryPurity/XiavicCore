package com.github.prypurity.xiaviccore.Commands.UserCmds.Essential;

import com.github.prypurity.xiaviccore.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class GrindstoneCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Grindstone"))) {
                Inventory i = Bukkit.createInventory(player, InventoryType.GRINDSTONE);
                player.openInventory(i);
                return true;
            }
        }
        return false;
    }
}

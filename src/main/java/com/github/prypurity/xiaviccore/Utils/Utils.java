package com.github.prypurity.xiaviccore.Utils;

import com.github.prypurity.xiaviccore.Utils.Listeners.TeleportHandler;
import com.github.prypurity.xiaviccore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {

    //EZ Chat Colors

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Sends messages to a player directly and makes the 'chat' name make more sense
    public static void chat(Player player, String string) {
        player.sendMessage(chat(string));
    }

    // An overload so you can do the same thing when you need to send a message to console
    // from inside a command class
    public static void chat(CommandSender sender, String string) {
        sender.sendMessage(chat(string));
    }

    /**
     * Attempt to replace the currently held item to a new one. The old item will be moved to
     * the next empty slot.
     * @param player The player's {@link Player} instance.
     * @param itemStack The {@link ItemStack} to replace - if null, air will be set instead.
     * @return Returns whether or not the swap was successful.
     */
    public static boolean placeInMainHand(@NotNull final Player player, @Nullable final ItemStack itemStack) {
        final PlayerInventory inventory = player.getInventory();
        final int firstEmpty = inventory.firstEmpty();
        if (firstEmpty == -1) {
            return false;
        }
        final int held = inventory.getHeldItemSlot();
        inventory.setItem(firstEmpty, inventory.getItem(held));
        inventory.setItem(held, itemStack == null ? null : itemStack.clone());
        return true;
    }

    @Deprecated
    public static void teleport(Player player, Location location) {
        TeleportHandler.processPlayerTeleport(player);
        player.teleport(location);
    }

    // This teleport method lets you send a message to the player here instead of
    // having to do it where ever you called this method
    @Deprecated
    public static void teleport(Player player, Location location, String messagePath) {
        TeleportHandler.processPlayerTeleport(player);
        player.teleport(location);
        chat(player, Main.messages.getString(messagePath));
    }


}

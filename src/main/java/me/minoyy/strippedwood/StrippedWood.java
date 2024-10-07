package me.minoyy.strippedwood;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class StrippedWood extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void StrippedWoodListener(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        Material blockType = block.getType();

        if (!blockType.name().startsWith("STRIPPED_")) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item.getType() != Material.BONE_MEAL)
            item = event.getPlayer().getInventory().getItemInOffHand();
        if (item.getType() != Material.BONE_MEAL) return;

        block.setType(Material.valueOf(blockType.name().replace("STRIPPED_", "")));

        item.setAmount(item.getAmount() - 1);
    }
}
package me.minoyy.strippedwood;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
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
        if (block == null || !block.getType().name().startsWith("STRIPPED_")) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() != Material.BONE_MEAL)
            item = event.getPlayer().getInventory().getItemInOffHand();
        if (item.getType() != Material.BONE_MEAL) return;

        BlockData blockData = block.getBlockData();
        Orientable orientable = (Orientable) blockData;
        Axis axis = orientable.getAxis();

        block.setType(Material.valueOf(block.getType().name().replace("STRIPPED_", "")));

        BlockData newBlockData = block.getBlockData();

        Orientable newOrientable = (Orientable) newBlockData;
        newOrientable.setAxis(axis);
        block.setBlockData(newOrientable);

        Location loc = block.getLocation().add(0.5, 0.5, 0.5);

        block.getWorld().spawnParticle(Particle.COMPOSTER, loc, 50, 0.4, 0.4, 0.4);

        item.setAmount(item.getAmount() - 1);
    }
}
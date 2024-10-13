package me.minoyy.strippedwood;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class StrippedWood extends JavaPlugin implements Listener {
    private CoreProtectAPI coreProtect;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Plugin depend = Bukkit.getPluginManager().getPlugin("CoreProtect");
        if (depend == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        coreProtect = ((CoreProtect) depend).getAPI();
    }

    @EventHandler
    public void StrippedWoodListener(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || !block.getType().name().startsWith("STRIPPED_")) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.BONE_MEAL)
            item = player.getInventory().getItemInOffHand();
        if (item.getType() != Material.BONE_MEAL) return;

        Axis axis = ((Orientable) block.getBlockData()).getAxis();

        block.setType(Material.valueOf(block.getType().name().replace("STRIPPED_", "")));

        Orientable newOrientable = (Orientable) block.getBlockData();
        newOrientable.setAxis(axis);
        block.setBlockData(newOrientable);

        Location loc = block.getLocation().add(0.5, 0.5, 0.5);

        World blockWorld = block.getWorld();
        blockWorld.spawnParticle(Particle.COMPOSTER, loc, 50, 0.4, 0.4, 0.4);
        blockWorld.playSound(player.getLocation(), Sound.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);

        item.setAmount(item.getAmount() - 1);

        String oldBlockName = "STRIPPED_" + block.getType().name();
        Material oldBlock = Material.getMaterial(oldBlockName);

        String reason = "#StrippedWood-" + player.getName();
        coreProtect.logRemoval(reason, block.getLocation(), oldBlock, block.getBlockData());
        coreProtect.logPlacement(reason, block.getLocation(), block.getType(), block.getBlockData());
    }
}
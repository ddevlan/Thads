package me.ohvalsgod.thads.util;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.ohvalsgod.thads.Thads;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldGuardUtil {

    public static boolean isPlayerInPvP(Player player) {
        final RegionManager regionManager = getWorldGuard().getRegionManager(player.getLocation().getWorld());
        final ApplicableRegionSet set = regionManager.getApplicableRegions(player.getLocation());
        final LocalPlayer localPlayer = getWorldGuard().wrapPlayer(player);
        return set.testState(localPlayer, DefaultFlag.PVP);
    }

    public static boolean isEntityInPVP(Entity entity){
        final RegionManager regionManager = getWorldGuard().getRegionManager(entity.getLocation().getWorld());
        final ApplicableRegionSet set = regionManager.getApplicableRegions(entity.getLocation());
        return set.testState(null, DefaultFlag.PVP);
    }

    public static boolean canBuildHere(Player player, Location location) {
        if (location == null) {
            return true;
        }

        final WorldGuardPlugin wg = getWorldGuard();
        return wg == null || wg.canBuild(player, location);
    }

    public static boolean canBuildHere(Player player, Block block) {
        if (block == null) {
            return true;
        }

        final WorldGuardPlugin wg = getWorldGuard();
        return wg == null || wg.canBuild(player, block);
    }

    public static WorldGuardPlugin getWorldGuard() {
        return Thads.get().getWorldGuard();
    }

}

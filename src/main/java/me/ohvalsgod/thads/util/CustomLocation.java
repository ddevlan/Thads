package me.ohvalsgod.thads.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.thads.Thads;
import org.bukkit.Location;

@AllArgsConstructor
@Getter
public class CustomLocation {
    private final long timestamp = System.currentTimeMillis();
    @Setter private double x, y, z;
    @Setter private float yaw, pitch;

    public static CustomLocation fromBukkitLocation(Location location) {
        return new CustomLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public Location toBukkitLocation() {
        return new Location(Thads.getInstance().getServer().getWorlds().get(0), x, y, z, yaw, pitch);
    }

    public double getGroundDistanceTo(CustomLocation location) {
        return Math.sqrt(Math.pow(x - location.x, 2.0) + Math.pow(z - location.z, 2.0));
    }

    public double getDistanceTo(CustomLocation location) {
        return Math.sqrt(Math.pow(x - location.x, 2.0) + Math.pow(y - location.y, 2.0) + Math.pow(z - location.z, 2.0));
    }
}


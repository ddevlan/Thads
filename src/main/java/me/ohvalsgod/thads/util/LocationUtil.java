package me.ohvalsgod.thads.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class LocationUtil {

    public static Location[] getFaces(Location start) {
        Location[] faces = new Location[4];
        faces[0] = new Location(start.getWorld(), start.getX() + 1, start.getY(), start.getZ());
        faces[1] = new Location(start.getWorld(), start.getX() - 1, start.getY(), start.getZ());
        faces[2] = new Location(start.getWorld(), start.getX(), start.getY() + 1, start.getZ());
        faces[3] = new Location(start.getWorld(), start.getX(), start.getY() - 1, start.getZ());
        return faces;
    }

    public static ArrayList<Location> getCircle(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = (2*Math.PI)/amount;
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int i = 0;i < amount; i++){
            double angle = i*increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY() + 1, z));
        }
        return locations;
    }

    public static String serialize(final Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static Location deserialize(final String source) {
        final String[] split = source.split(":");
        return new Location(Bukkit.getServer().getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

}

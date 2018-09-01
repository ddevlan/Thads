package me.ohvalsgod.thads.util;

import me.ohvalsgod.thads.Thads;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void run(Runnable runnable) {
        Thads.getInstance().getServer().getScheduler().runTask(Thads.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Thads.getInstance().getServer().getScheduler().runTaskTimer(Thads.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Thads.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        Thads.getInstance().getServer().getScheduler().runTaskLater(Thads.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        Thads.getInstance().getServer().getScheduler().runTaskAsynchronously(Thads.getInstance(), runnable);
    }

}

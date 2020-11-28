package me.ohvalsgod.thads.dev.visibility;

import me.ohvalsgod.thads.Thads;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class ExtendedVisibilityHandler {

    private static final Map<String, VisibilityHandler> handlers = new LinkedHashMap<>();

    private static final Map<String, OverrideHandler> overrideHandlers = new LinkedHashMap<>();

    private static boolean initiated = false;

    public static void init() {
        if (!initiated) {
            initiated = !initiated;
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler(priority = EventPriority.LOWEST)
                public void onPlayerJoin(PlayerJoinEvent event) {
                    ExtendedVisibilityHandler.update(event.getPlayer());
                }

                @EventHandler(priority = EventPriority.LOWEST)
                public void onTabComplete(PlayerChatTabCompleteEvent event) {
                    String token = event.getLastToken();
                    Collection<String> completions = event.getTabCompletions();
                    completions.clear();
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        if (!ExtendedVisibilityHandler.treatAsOnline(target, event.getPlayer()))
                            continue;
                        if (StringUtils.startsWithIgnoreCase(target.getName(), token))
                            completions.add(target.getName());
                    }
                }
            }, Thads.get());
        }
    }

    public static void registerHandler(String identifier, VisibilityHandler handler) {
        handlers.put(identifier, handler);
    }

    public static void registerOverride(String identifier, OverrideHandler handler) {
        overrideHandlers.put(identifier, handler);
    }

    public static void update(Player player) {
        if (handlers.isEmpty() && overrideHandlers.isEmpty())
            return;
        updateAllTo(player);
        updateToAll(player);
    }

    @Deprecated
    public static void updateAllTo(Player viewer) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!shouldSee(target, viewer)) {
                viewer.hidePlayer(target);
                continue;
            }
            viewer.showPlayer(target);
        }
    }

    @Deprecated
    public static void updateToAll(Player target) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (!shouldSee(target, viewer)) {
                viewer.hidePlayer(target);
                continue;
            }
            viewer.showPlayer(target);
        }
    }

    public static boolean treatAsOnline(Player target, Player viewer) {
        return (viewer.canSee(target) || !target.hasMetadata("invisible") || viewer.hasPermission("alfax.staff"));
    }

    private static boolean shouldSee(Player target, Player viewer) {
        for (OverrideHandler handler : overrideHandlers.values()) {
            if (handler.getAction(target, viewer) == OverrideAction.SHOW)
                return true;
        }
        for (VisibilityHandler handler : handlers.values()) {
            if (handler.getAction(target, viewer) == VisibilityAction.HIDE)
                return false;
        }
        return true;
    }

    public static List<String> getDebugInfo(Player target, Player viewer) {
        List<String> debug = new ArrayList<>();
        Boolean canSee = null;
        for (Map.Entry<String, OverrideHandler> entry : overrideHandlers.entrySet()) {
            OverrideHandler handler = entry.getValue();
            OverrideAction action = handler.getAction(target, viewer);
            ChatColor color = ChatColor.GRAY;
            if (action == OverrideAction.SHOW &&
                    canSee == null) {
                canSee = Boolean.TRUE;
                color = ChatColor.GREEN;
            }
            debug.add(color + "Overriding Handler: \"" + entry.getKey() + "\": " + action);
        }
        for (Map.Entry<String, VisibilityHandler> entry : handlers.entrySet()) {
            VisibilityHandler handler = entry.getValue();
            VisibilityAction action = handler.getAction(target, viewer);
            ChatColor color = ChatColor.GRAY;
            if (action == VisibilityAction.HIDE &&
                    canSee == null) {
                canSee = Boolean.FALSE;
                color = ChatColor.GREEN;
            }
            debug.add(color + "Normal Handler: \"" + (String)entry.getKey() + "\": " + action);
        }
        if (canSee == null)
            canSee = Boolean.TRUE;
        debug.add(ChatColor.AQUA + "Result: " + viewer.getName() + " " + (canSee ? "can" : "cannot") + " see " + target.getName());
        return debug;
    }

}

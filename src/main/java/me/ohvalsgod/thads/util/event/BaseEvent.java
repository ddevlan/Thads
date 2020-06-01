package me.ohvalsgod.thads.util.event;

import me.ohvalsgod.thads.Thads;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BaseEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public void call() {
		Thads.get().getServer().getPluginManager().callEvent(this);
	}

}

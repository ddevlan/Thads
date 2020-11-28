package me.ohvalsgod.thads.util.event;

import org.bukkit.event.Cancellable;

public class CancellableEvent extends BaseEvent implements Cancellable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

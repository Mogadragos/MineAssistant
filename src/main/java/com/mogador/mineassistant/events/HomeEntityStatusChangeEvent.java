package com.mogador.mineassistant.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;

public class HomeEntityStatusChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final HomeEntity entity;
    private final HomeEntityStatus status;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public HomeEntityStatusChangeEvent(HomeEntity entity, HomeEntityStatus status) {
        this.entity = entity;
        this.status = status;
    }

    public HomeEntity getEntity() {
        return entity;
    }

    public HomeEntityStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "HomeEntityStatusChangeEvent{entity=" + entity.name() + ", status=" + status.name() + '}';
    }
}

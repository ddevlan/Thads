package me.ohvalsgod.thads.baller.gui.object;

import lombok.Getter;

public enum LOLFilterType {

    WEIGHT("Default"),
    ABC("→abc"),
    XYZ("xyz←"),
    VALUE_UP("$↑"),
    VALUE_DOWN("↓$"),
    ALL_ITEMS("⚔ Items"),
    ALL_ARMOR("✙ Armor");

    @Getter
    String display;

    LOLFilterType(String display) {
        this.display = display;
    }

    public static LOLFilterType getByDisplay(String display) {
        for (LOLFilterType type : values()) {
            if (type.getDisplay().equalsIgnoreCase(display)) {
                return type;
            }
        }
        return WEIGHT;
    }

}

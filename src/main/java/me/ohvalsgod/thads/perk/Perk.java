package me.ohvalsgod.thads.perk;

import lombok.Getter;

@Getter
public enum Perk {

    IRONMAN(PerkType.PERMISSION, "lolpvp.kits.ironman"),
    REGEN(PerkType.PERMISSION, "lolpvp.kits.regen"),
    REPAIR(PerkType.PERMISSION, "essentials.repair.*"),
    IGNORE(PerkType.PERMISSION, "essentials.ignore"),
    VIP(PerkType.RANK, "VIP"),
    VIP_PLUS(PerkType.RANK, "VIP+"),
    THAD(PerkType.RANK, "THAD"),
    THAD_PLUS(PerkType.RANK, "THAD+"),
    $$$$$(PerkType.RANK, "$$$$$");

    private PerkType type;
    private String source;

    Perk(PerkType type, String source) {
        this.type = type;
        this.source = source;
    }

    public boolean isRank() {
        return this.type == PerkType.RANK;
    }

    public boolean isPermission() {
        return this.type == PerkType.PERMISSION;
    }

}

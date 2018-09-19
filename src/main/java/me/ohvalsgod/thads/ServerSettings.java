package me.ohvalsgod.thads;

import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.thads.util.CC;

@Setter
@Getter
public class ServerSettings {

    //TODO: change this, just needed a place to use it for now

    public static String COOLDOWN = CC.AQUA + "{ITEM}" + CC.RED + " is on cooldown for {AMOUNT} seconds.";
    public static String ERROR_INVIS_RING = CC.RED + "You cannot use this while '" + CC.AQUA + "Invisibility Ring" + CC.RED + "' is active!";
    public static String ERROR_NOT_PVP = CC.RED + "You can only use this in PVP enabled areas!";

    public ServerSettings(Thads thads) {

    }

    public void save() {

    }

}

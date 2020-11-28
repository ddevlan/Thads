package me.ohvalsgod.thads.data;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private UUID uuid;
    private String lolTag;
    private long lastLolTag;
    private boolean debugMode;
//    private Set<Perk> perks;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        lolTag = "";
        debugMode = false;
    }

    //TODO: write receivers and givers

//    public Perk getRank() {
//        for (Perk perk : perks) {
//            if (perk.isRank()) {
//                return perk;
//            }
//        }
//        return null;
//    }
//
//    public Set<Perk> getPermissionPerks() {
//        Set<Perk> toReturn = new HashSet<>();
//
//        for (Perk perk : perks) {
//            if (perk.isPermission()) {
//                toReturn.add(perk);
//            }
//        }
//        return toReturn;
//    }
//
//    public boolean hasPerk(Perk perk) {
//        return perks.contains(perk);
//    }

}

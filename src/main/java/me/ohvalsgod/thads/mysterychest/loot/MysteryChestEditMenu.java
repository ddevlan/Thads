package me.ohvalsgod.thads.mysterychest.loot;

import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import me.ohvalsgod.thads.mysterychest.MysteryChest;
import me.ohvalsgod.thads.util.CC;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MysteryChestEditMenu extends Menu {

    private MysteryChest chest;
    private Set<MysteryChestLootEdit> edits;

    public MysteryChestEditMenu(MysteryChest chest) {
        this.chest = chest;
        this.edits = new HashSet<>();
    }

    @Override
    public String getTitle(Player player) {
        return CC.GOLD + "Editing: " + CC.AQUA + chest.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        //loop through items.

        //button logic
        //left click to change item
        //right click to remove.
        return null;
    }

    @Override
    public void onClose(Player player) {
        chest.getLootEditMap().computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
        edits.forEach(mysteryChestLootEdit -> chest.getLootEditMap().get(player.getUniqueId()).add(mysteryChestLootEdit));
    }
}

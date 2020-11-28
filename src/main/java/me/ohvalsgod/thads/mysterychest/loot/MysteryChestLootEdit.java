package me.ohvalsgod.thads.mysterychest.loot;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@Getter
public class MysteryChestLootEdit {

    public UUID editor;
    private Map<ItemStack, String> edits;
    private long editDate;

    public MysteryChestLootEdit(UUID editor, Map<ItemStack, String> edits, long editDate) {
        this.editor = editor;
        this.edits = edits;
        this.editDate = editDate;
    }

}

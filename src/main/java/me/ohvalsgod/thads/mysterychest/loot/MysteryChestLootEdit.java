package me.ohvalsgod.thads.mysterychest.loot;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class MysteryChestLootEdit {

    public UUID editor;
    private Map<String, String> edits;
    private long editDate;

    public MysteryChestLootEdit(UUID editor, Map<String, String> edits, long editDate) {
        this.editor = editor;
        this.edits = edits;
        this.editDate = editDate;
    }

}

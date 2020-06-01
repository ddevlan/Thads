package me.ohvalsgod.thads.baller.armor;

import lombok.Getter;
import me.ohvalsgod.thads.baller.object.AbstractBallerObject;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class AbstractBallerArmor extends AbstractBallerObject implements IBallerArmor {

    private ItemStack[] ballerArmor;

    public AbstractBallerArmor(String name) {
        super(name);
    }

    @Override
    public void setArmor(ItemStack[] items) {
        ballerArmor = items;
    }

}

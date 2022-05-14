package com.feywild.quest_giver.item;

import com.feywild.quest_giver.util.TooltipHelper;
import io.github.noeppi_noeppi.libx.base.ItemBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

public class TooltipItem extends ItemBase {
    private final Component[] itemTooltip;

    public TooltipItem(ModX mod, Properties properties, Component... itemTooltip) {
        super(mod, properties);
        this.itemTooltip = itemTooltip;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        TooltipHelper.addTooltip(tooltip, this.itemTooltip);
        super.appendHoverText(stack, level, tooltip, flag);
    }
}

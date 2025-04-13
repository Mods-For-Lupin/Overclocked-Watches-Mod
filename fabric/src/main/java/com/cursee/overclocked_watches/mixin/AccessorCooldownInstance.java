package com.cursee.overclocked_watches.mixin;

import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCooldowns.CooldownInstance.class)
public interface AccessorCooldownInstance {
    @Accessor
    void setStartTime(int var1);

    @Accessor
    void setEndTime(int var1);

    @Accessor
    int getStartTime();

    @Accessor
    int getEndTime();
}

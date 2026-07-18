package io.github.jason13official.overclocked_watches.mixin;

import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCooldowns.CooldownInstance.class)
public interface AccessorCooldownInstance {

  @Accessor
  int getStartTime();

  @Accessor
  void setStartTime(int var1);

  @Accessor
  int getEndTime();

  @Accessor
  void setEndTime(int var1);
}

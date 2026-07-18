package io.github.jason13official.overclocked_watches.mixin;

import io.github.jason13official.overclocked_watches.impl.common.util.CoolDownRecord;
import io.github.jason13official.overclocked_watches.impl.common.util.IItemCooldowns;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemCooldowns.class)
public abstract class ForgeItemCooldownsMixin implements IItemCooldowns {

  @Shadow
  @Final
  private Map<Item, ItemCooldowns.CooldownInstance> cooldowns;
  @Shadow
  private int tickCount;

  public ForgeItemCooldownsMixin() {
  }

  @Shadow
  public abstract void addCooldown(Item var1, int var2);

  public List<CoolDownRecord> persistcd$getCooldownTicks() {
    return this.cooldowns.entrySet().stream().map((e) -> {
      AccessorCooldownInstance instance = (AccessorCooldownInstance) e.getValue();
      return new CoolDownRecord(e.getKey(), instance.getEndTime() - this.tickCount, instance.getEndTime() - instance.getStartTime());
    }).toList();
  }

  public void persistcd$addCoolDown(CoolDownRecord cd) {
    this.addCooldown(cd.item(), 50000);
    AccessorCooldownInstance instance = (AccessorCooldownInstance) this.cooldowns.get(cd.item());
    int end = this.tickCount + cd.remain();
    int start = end - cd.total();
    instance.setStartTime(start);
    instance.setEndTime(end);
  }
}

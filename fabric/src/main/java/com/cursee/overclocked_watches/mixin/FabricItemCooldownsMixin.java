package com.cursee.overclocked_watches.mixin;

import com.cursee.overclocked_watches.core.util.CoolDownRecord;
import com.cursee.overclocked_watches.core.util.IItemCooldowns;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(ItemCooldowns.class)
public abstract class FabricItemCooldownsMixin implements IItemCooldowns {

    @Shadow
    @Final
    private Map<Item, ItemCooldowns.CooldownInstance> cooldowns;
    @Shadow
    private int tickCount;

    public FabricItemCooldownsMixin() {
    }

    @Shadow
    public abstract void addCooldown(Item var1, int var2);

    public List<CoolDownRecord> persistcd$getCooldownTicks() {
        return this.cooldowns.entrySet().stream().map((e) -> {
            AccessorCooldownInstance instance = (AccessorCooldownInstance)e.getValue();
            return new CoolDownRecord((Item)e.getKey(), instance.getEndTime() - this.tickCount, instance.getEndTime() - instance.getStartTime());
        }).toList();
    }

    public void persistcd$addCoolDown(CoolDownRecord cd) {
        this.addCooldown(cd.item(), 50000);
        AccessorCooldownInstance instance = (AccessorCooldownInstance)this.cooldowns.get(cd.item());
        int end = this.tickCount + cd.remain();
        int start = end - cd.total();
        instance.setStartTime(start);
        instance.setEndTime(end);
    }
}

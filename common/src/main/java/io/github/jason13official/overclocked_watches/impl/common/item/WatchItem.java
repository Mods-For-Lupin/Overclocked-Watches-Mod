package io.github.jason13official.overclocked_watches.impl.common.item;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.api.common.data.WatchItemData;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class WatchItem extends Item {

  public static final String CHARGES_TAG = "OverclockCharges";

  private final WatchTier tier;

  public WatchItem(WatchTier tier) {
    super(new Properties().durability(tier.getItemDurability()));
    this.tier = tier;
  }

  /// Helper method to reduce spamming similar lines, apply same cooldown for all watches
  public static void applyCooldowns(Player player, int lengthInTicks) {
    player.getCooldowns().addCooldown(ModItems.GOLDEN_WATCH, lengthInTicks);
    player.getCooldowns().addCooldown(ModItems.DIAMOND_WATCH, lengthInTicks);
    player.getCooldowns().addCooldown(ModItems.NETHERITE_WATCH, lengthInTicks);
  }

  @Override
  public ItemStack getDefaultInstance() {

    ItemStack itemStack = new ItemStack(this);

    return WatchItemData.initializeTag(this, itemStack);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {

    CompoundTag stackData = itemStack.getOrCreateTag();
    if (!stackData.contains(CHARGES_TAG, CompoundTag.TAG_INT)) {
      tooltip.add(Component.translatable("text.overclocked_watches.default_charges", this.getTier().getWatchCharges()));
    } else {
      tooltip.add(Component.translatable("text.overclocked_watches.charges", stackData.getInt(CHARGES_TAG)));
    }
  }

  @Override
  public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean slotSelected) {

    WatchItemData.initializeTag(this, itemStack);
  }

  // interaction when clicking in the air
  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

    ItemStack itemInHand = player.getItemInHand(hand);
    if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
      return InteractionResultHolder.pass(itemInHand);
    }

    // task: validate charges, update time, consume charge, apply cooldowns

    CompoundTag unvalidatedStackData = itemInHand.getOrCreateTag();
    if (!unvalidatedStackData.contains(CHARGES_TAG, CompoundTag.TAG_INT)) {
      itemInHand = WatchItemData.initializeTag(this, itemInHand);
    } else if (unvalidatedStackData.getInt(CHARGES_TAG) == 0) {
      return InteractionResultHolder.pass(itemInHand);
    }
    // we have validated charges to be either initialized or non-zero

    serverLevel.setDayTime((serverLevel.getDayTime() + this.getTier().getTimeAdvancementTicks()) % 24_000L);
    // we have advanced the server daytime

    CompoundTag validatedStackData = itemInHand.getOrCreateTag();
    final int newChargeCount = validatedStackData.getInt(CHARGES_TAG) - 1;
    // validatedStackData.remove(CHARGES_TAG); // shouldn't need this, putting should overwrite current value in backing map
    validatedStackData.putInt(CHARGES_TAG, newChargeCount);
    itemInHand.save(validatedStackData);
    OverclockedWatchesUtil.consumeCharge(itemInHand);
    // we have consumed a charge

    applyCooldowns(serverPlayer, 20 * 60 * this.getTier().getCooldownMinutes());
    // we have applied cooldowns

    return InteractionResultHolder.success(itemInHand);
  }

  public WatchTier getTier() {

    return tier;
  }
}

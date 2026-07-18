package io.github.jason13official.overclocked_watches.impl.common.item;

import io.github.jason13official.overclocked_watches.api.common.data.WatchItemData;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModDataComponents;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import java.util.List;
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

public class WatchItem extends Item {

  private final WatchTier tier;

  public WatchItem(WatchTier tier) {
    super(new Properties().durability(tier.getItemDurability()).component(ModDataComponents.CHARGES, tier.getWatchCharges()));
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

    return WatchItemData.initializeCharges(this, itemStack);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {

    if (!itemStack.has(ModDataComponents.CHARGES)) {
      tooltip.add(Component.translatable("text.overclocked_watches.default_charges", this.getTier().getWatchCharges()));
    } else {
      tooltip.add(Component.translatable("text.overclocked_watches.charges", itemStack.get(ModDataComponents.CHARGES)));
    }
  }

  @Override
  public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean slotSelected) {

    WatchItemData.initializeCharges(this, itemStack);
  }

  // interaction when clicking in the air
  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

    ItemStack itemInHand = player.getItemInHand(hand);
    if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
      return InteractionResultHolder.pass(itemInHand);
    }

    // task: validate charges, update time, consume charge, apply cooldowns

    if (!itemInHand.has(ModDataComponents.CHARGES)) {
      itemInHand = WatchItemData.initializeCharges(this, itemInHand);
    } else if (itemInHand.get(ModDataComponents.CHARGES) == 0) {
      return InteractionResultHolder.pass(itemInHand);
    }
    // we have validated charges to be either initialized or non-zero

    serverLevel.setDayTime((serverLevel.getDayTime() + this.getTier().getTimeAdvancementTicks()) % 24_000L);
    // we have advanced the server daytime

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

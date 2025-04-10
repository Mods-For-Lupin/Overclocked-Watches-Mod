package com.cursee.overclocked_watches.core.world.item;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.registry.ModItems;
import com.cursee.overclocked_watches.platform.Services;
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

import java.util.List;

public class WatchItem extends Item {

    public static final String CHARGES = "charges";
    private static final int GOLDEN_CHARGES = (int) CommonConfigValues.golden_watch_charges;
    private static final int DIAMOND_CHARGES = (int) CommonConfigValues.diamond_watch_charges;
    private static final int NETHERITE_CHARGES = (int) CommonConfigValues.netherite_watch_charges;

    private final Tier tier;

    public WatchItem(Tier tier) {
        super(new Properties().durability(tier.getItemDurability()));
        this.tier = tier;
    }

    @Override
    public ItemStack getDefaultInstance() {

        ItemStack itemStack = new ItemStack(this);
        CompoundTag stackData = itemStack.getOrCreateTag();
        if (stackData.contains(CHARGES, CompoundTag.TAG_INT)) return itemStack;

        switch (this.getTier()) {
            case GOLDEN: stackData.putInt(CHARGES, GOLDEN_CHARGES);
            case DIAMOND: stackData.putInt(CHARGES, DIAMOND_CHARGES);
            case NETHERITE: stackData.putInt(CHARGES, NETHERITE_CHARGES);
        }

        itemStack.save(stackData);

        return itemStack.copy();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {

        CompoundTag stackData = itemStack.getOrCreateTag();
        if (!stackData.contains(CHARGES, CompoundTag.TAG_INT)) {
            switch (((WatchItem) itemStack.getItem()).getTier()) {
                case GOLDEN: tooltip.add(Component.translatable("text.overclocked_watches.default_charges", GOLDEN_CHARGES)); break;
                case DIAMOND: tooltip.add(Component.translatable("text.overclocked_watches.default_charges", DIAMOND_CHARGES)); break;
                case NETHERITE: tooltip.add(Component.translatable("text.overclocked_watches.default_charges", NETHERITE_CHARGES)); break;
            }
        }
        else {
            tooltip.add(Component.translatable("text.overclocked_watches.charges", stackData.getInt(CHARGES)));
        }
    }

    // setup item if not initialized
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIndex, boolean slotSelected) {

        CompoundTag stackData = itemStack.getOrCreateTag();
        if (stackData.contains(CHARGES, CompoundTag.TAG_INT)) return;
        // returned early if tag already exists

        switch (((WatchItem) itemStack.getItem()).getTier()) {
            case GOLDEN: stackData.putInt(CHARGES, GOLDEN_CHARGES); break;
            case DIAMOND: stackData.putInt(CHARGES, DIAMOND_CHARGES); break;
            case NETHERITE: stackData.putInt(CHARGES, NETHERITE_CHARGES); break;
        }
        // initialized to new values
    }

    /**
     * Helper method to reduce spamming similar lines, apply same cooldown for all watches
     */
    public static void applyCooldowns(Player player, int lengthInTicks) {
        player.getCooldowns().addCooldown(ModItems.GOLDEN_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItems.DIAMOND_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItems.NETHERITE_WATCH, lengthInTicks);
    }

    public static ItemStack initializeTag(ItemStack itemStack) {

        CompoundTag stackData = itemStack.getOrCreateTag();
        if (stackData.contains(CHARGES, CompoundTag.TAG_INT)) return itemStack;
        // returned early if tag already exists

        switch (((WatchItem) itemStack.getItem()).getTier()) {
            case GOLDEN: stackData.putInt(CHARGES, GOLDEN_CHARGES); break;
            case DIAMOND: stackData.putInt(CHARGES, DIAMOND_CHARGES); break;
            case NETHERITE: stackData.putInt(CHARGES, NETHERITE_CHARGES); break;
        }

        itemStack.save(stackData);
        return itemStack.copy();
    }

    // interaction when clicking in the air
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemInHand = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) return InteractionResultHolder.pass(itemInHand);

        // todo: validate charges, update time, consume charge, apply cooldowns
        CompoundTag unvalidatedStackData = itemInHand.getOrCreateTag();

        if (!unvalidatedStackData.contains(CHARGES, CompoundTag.TAG_INT)) itemInHand = initializeTag(itemInHand);
        else if (unvalidatedStackData.getInt(CHARGES) == 0) return InteractionResultHolder.pass(itemInHand);
        // we have validated charges to be either initialized or non-zero

        switch (this.getTier()) {
            case GOLDEN: serverLevel.setDayTime((serverLevel.getDayTime() + CommonConfigValues.golden_time_advancement_ticks) % 24_000L); break;
            case DIAMOND: serverLevel.setDayTime((serverLevel.getDayTime() + CommonConfigValues.diamond_time_advancement_ticks) % 24_000L); break;
            case NETHERITE: serverLevel.setDayTime((serverLevel.getDayTime() + CommonConfigValues.netherite_time_advancement_ticks) % 24_000L); break;
        }

        // we have advanced the server daytime by half a day

        CompoundTag validatedStackData = itemInHand.getOrCreateTag();
        final int newChargeCount = validatedStackData.getInt(CHARGES) - 1;
        validatedStackData.remove(CHARGES);
        validatedStackData.putInt(CHARGES, newChargeCount);
        itemInHand.save(validatedStackData);
        // we have consumed a charge

        switch (this.getTier()) {
            case GOLDEN: applyCooldowns(serverPlayer, 20 * 60 * (int) CommonConfigValues.golden_watch_cooldown_minutes); break;
            case DIAMOND: applyCooldowns(serverPlayer, 20 * 60 * (int) CommonConfigValues.diamond_watch_cooldown_minutes); break;
            case NETHERITE: applyCooldowns(serverPlayer, 20 * 60 * (int) CommonConfigValues.netherite_watch_cooldown_minutes); break;
        }
        // we have applied cooldowns

        return InteractionResultHolder.success(itemInHand);
    }

    // WATCH TIER
    public Tier getTier() {
        return tier;
    }

    public enum Tier {

        GOLDEN(CommonConfigValues.golden_watch_durability),
        DIAMOND(CommonConfigValues.diamond_watch_durability),
        NETHERITE(CommonConfigValues.netherite_watch_durability);

        private final long itemDurability;

        Tier(long itemDurability) {
            this.itemDurability = itemDurability;
        }

        public int getItemDurability() {
            return (int) itemDurability;
        }
    }
}

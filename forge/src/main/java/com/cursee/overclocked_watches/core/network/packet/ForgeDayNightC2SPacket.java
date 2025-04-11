package com.cursee.overclocked_watches.core.network.packet;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.registry.ModItems;
import com.cursee.overclocked_watches.core.util.TimeManager;
import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeDayNightC2SPacket {

    public static final String CHARGES = "charges";

    public ForgeDayNightC2SPacket() {}

    public ForgeDayNightC2SPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            if (!CommonConfigValues.day_night_cycle_allowed) return;

            ServerPlayer player = context.getSender();
            if (player.getServer() == null) return;

            // player has watch variant equipped
            final boolean hasNetheriteWatch = Services.PLATFORM.playerHasNetheriteWatchEquipped(player);
            final boolean hasDiamondWatch = Services.PLATFORM.playerHasDiamondWatchEquipped(player);
            final boolean hasGoldenWatch = Services.PLATFORM.playerHasGoldenWatchEquipped(player);
            if (!(hasNetheriteWatch || hasDiamondWatch || hasGoldenWatch)) return;

            // player does not have any watch variant on cooldown
            final boolean WATCHES_ON_COOLDOWN = player.getCooldowns().isOnCooldown(ModItems.NETHERITE_WATCH) || player.getCooldowns().isOnCooldown(ModItems.DIAMOND_WATCH) || player.getCooldowns().isOnCooldown(ModItems.GOLDEN_WATCH);
            if (WATCHES_ON_COOLDOWN) return;

            if (hasNetheriteWatch) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedNetheriteWatch(player);

                CompoundTag validatedStackData = equippedWatch.getOrCreateTag();
                final int newChargeCount = validatedStackData.getInt(CHARGES) - 1;
                validatedStackData.remove(CHARGES);
                validatedStackData.putInt(CHARGES, newChargeCount);
                equippedWatch.save(validatedStackData);
                // we have consumed a charge

                applyCooldowns(player, 20 * 60 * (int) CommonConfigValues.netherite_watch_cooldown_minutes);
                addTime(player.getServer(), CommonConfigValues.netherite_time_advancement_ticks);
            }
            else if (hasDiamondWatch) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedDiamondWatch(player);

                CompoundTag validatedStackData = equippedWatch.getOrCreateTag();
                final int newChargeCount = validatedStackData.getInt(CHARGES) - 1;
                validatedStackData.remove(CHARGES);
                validatedStackData.putInt(CHARGES, newChargeCount);
                equippedWatch.save(validatedStackData);
                // we have consumed a charge

                applyCooldowns(player, 20 * 60 * (int) CommonConfigValues.diamond_watch_cooldown_minutes);
                addTime(player.getServer(), CommonConfigValues.diamond_time_advancement_ticks);
            }
            else if (hasGoldenWatch) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedGoldenWatch(player);

                CompoundTag validatedStackData = equippedWatch.getOrCreateTag();
                final int newChargeCount = validatedStackData.getInt(CHARGES) - 1;
                validatedStackData.remove(CHARGES);
                validatedStackData.putInt(CHARGES, newChargeCount);
                equippedWatch.save(validatedStackData);
                // we have consumed a charge

                applyCooldowns(player, 20 * 60 * (int) CommonConfigValues.golden_watch_cooldown_minutes);
                addTime(player.getServer(), CommonConfigValues.golden_time_advancement_ticks);
            }

            player.serverLevel().playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.BELL_RESONATE, SoundSource.AMBIENT, 0.5f, 0.5f, false);
            player.serverLevel().addParticle(ParticleTypes.END_ROD, player.position().x, player.position().y, player.position().z, 0, 0.005, 0);
            player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));
        });

        return true;
    }

    public static void applyCooldowns(Player player, int lengthInTicks) {
        player.getCooldowns().addCooldown(ModItems.NETHERITE_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItems.DIAMOND_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItems.GOLDEN_WATCH, lengthInTicks);
    }

    public static void addTime(MinecraftServer pSource, long pAmount) {
        for(ServerLevel level : pSource.getAllLevels()) {
            if (!CommonConfigValues.use_long_time_delta) level.setDayTime(level.getDayTime() + pAmount);
        }

        if (CommonConfigValues.use_long_time_delta) TimeManager.addToRemainingTime((int) pAmount);
    }
}

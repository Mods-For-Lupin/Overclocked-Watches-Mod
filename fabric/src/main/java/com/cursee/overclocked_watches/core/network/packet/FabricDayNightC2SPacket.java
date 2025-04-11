package com.cursee.overclocked_watches.core.network.packet;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.registry.ModItems;
import com.cursee.overclocked_watches.core.util.TimeManager;
import com.cursee.overclocked_watches.platform.Services;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;

public class FabricDayNightC2SPacket {

    public static final String CHARGES = "charges";

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf packetData, PacketSender sender) {

        if (!CommonConfigValues.day_night_cycle_allowed) return;

        boolean hasNetheriteWatch = Services.PLATFORM.playerHasNetheriteWatchEquipped(player);
        boolean hasDiamondWatch = Services.PLATFORM.playerHasDiamondWatchEquipped(player);
        boolean hasGoldenWatch = Services.PLATFORM.playerHasGoldenWatchEquipped(player);
        if (!(hasNetheriteWatch || hasDiamondWatch || hasGoldenWatch)) return;

        ItemCooldowns cooldowns = player.getCooldowns();
        boolean onCooldown = cooldowns.isOnCooldown(ModItems.NETHERITE_WATCH) || cooldowns.isOnCooldown(ModItems.DIAMOND_WATCH) || cooldowns.isOnCooldown(ModItems.GOLDEN_WATCH);
        if (onCooldown) return;

        if (hasNetheriteWatch) {
            ItemStack watch = Services.PLATFORM.getEquippedNetheriteWatch(player);
            CompoundTag data = watch.getOrCreateTag();
            if (data.getInt(CHARGES) == 0) return; // validate charges on watch

            // consumer charge from watch
            int newCharges = data.getInt(CHARGES) - 1;
            data.remove(CHARGES);
            data.putInt(CHARGES, newCharges);
            watch.save(data);

            addTime(server, CommonConfigValues.netherite_time_advancement_ticks);
            applyCooldowns(player, 20 * 60 * (int) CommonConfigValues.netherite_watch_cooldown_minutes);
        }
        else if (hasDiamondWatch) {
            ItemStack watch = Services.PLATFORM.getEquippedDiamondWatch(player);
            CompoundTag data = watch.getOrCreateTag();
            if (data.getInt(CHARGES) == 0) return; // validate charges on watch

            // consumer charge from watch
            int newCharges = data.getInt(CHARGES) - 1;
            data.remove(CHARGES);
            data.putInt(CHARGES, newCharges);
            watch.save(data);

            addTime(server, CommonConfigValues.diamond_time_advancement_ticks);
            applyCooldowns(player, 20 * 60 * (int) CommonConfigValues.diamond_watch_cooldown_minutes);
        }
        else if (hasGoldenWatch) {
            ItemStack watch = Services.PLATFORM.getEquippedGoldenWatch(player);
            CompoundTag data = watch.getOrCreateTag();
            if (data.getInt(CHARGES) == 0) return; // validate charges on watch

            // consumer charge from watch
            int newCharges = data.getInt(CHARGES) - 1;
            data.remove(CHARGES);
            data.putInt(CHARGES, newCharges);
            watch.save(data);

            addTime(server, CommonConfigValues.golden_time_advancement_ticks);
            applyCooldowns(player, 20 * 60 * (int) CommonConfigValues.golden_watch_cooldown_minutes);
        }

        player.serverLevel().playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.BELL_RESONATE, SoundSource.AMBIENT, 0.5f, 0.5f, false);
        player.serverLevel().addParticle(ParticleTypes.END_ROD, player.position().x, player.position().y, player.position().z, 0, 0.005, 0);
        player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));
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

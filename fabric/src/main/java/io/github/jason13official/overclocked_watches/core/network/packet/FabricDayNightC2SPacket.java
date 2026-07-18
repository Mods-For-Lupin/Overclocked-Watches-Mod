package io.github.jason13official.overclocked_watches.core.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;

public class FabricDayNightC2SPacket {

    public static final String CHARGES = "charges";

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf packetData, PacketSender sender) {

        System.out.println("handling packet");

        if (!ServerModConfig.dayNightCycleAllowed) return;

        boolean hasNetheriteWatch = Services.PLATFORM.playerHasNetheriteWatchEquipped(player);
        boolean hasDiamondWatch = Services.PLATFORM.playerHasDiamondWatchEquipped(player);
        boolean hasGoldenWatch = Services.PLATFORM.playerHasGoldenWatchEquipped(player);
        if (!(hasNetheriteWatch || hasDiamondWatch || hasGoldenWatch)) {
            System.out.println("returned early, no watch equipped");
            return;
        }

        ItemCooldowns cooldowns = player.getCooldowns();
        boolean onCooldown = cooldowns.isOnCooldown(ModItems.NETHERITE_WATCH) || cooldowns.isOnCooldown(ModItems.DIAMOND_WATCH) || cooldowns.isOnCooldown(ModItems.GOLDEN_WATCH);
        if (onCooldown) {
            System.out.println("returned early, watches on cooldown");
            return;
        }

        if (hasNetheriteWatch) {
            ItemStack watch = Services.PLATFORM.getEquippedNetheriteWatch(player);
            CompoundTag data = watch.getOrCreateTag();
            if (data.getInt(CHARGES) == 0) {
                System.out.println("returned early, no charges on watch");
                return; // validate charges on watch
            }

            // consumer charge from watch
            int newCharges = data.getInt(CHARGES) - 1;
            data.remove(CHARGES);
            data.putInt(CHARGES, newCharges);
            watch.save(data);

            addTime(server, ServerModConfig.netheriteTimeAdvancementTicks);
            applyCooldowns(player, 20 * 60 * (int) ServerModConfig.netheriteWatchCooldownMinutes);
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

            addTime(server, ServerModConfig.diamondTimeAdvancementTicks);
            applyCooldowns(player, 20 * 60 * (int) ServerModConfig.diamondWatchCooldownMinutes);
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

            addTime(server, ServerModConfig.goldenTimeAdvancementTicks);
            applyCooldowns(player, 20 * 60 * (int) ServerModConfig.goldenWatchCooldownMinutes);
        }
        player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));
    }

    public static void applyCooldowns(Player player, int lengthInTicks) {
        player.getCooldowns().addCooldown(ModItems.NETHERITE_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItems.DIAMOND_WATCH, lengthInTicks);
        player.getCooldowns().addCooldown(ModItems.GOLDEN_WATCH, lengthInTicks);
    }

    public static void addTime(MinecraftServer pSource, long pAmount) {
        for(ServerLevel level : pSource.getAllLevels()) {
            if (!ServerModConfig.useLongTimeDelta) level.setDayTime(level.getDayTime() + pAmount);
        }

        System.out.println("added to server time manager: " + pAmount);
        if (ServerModConfig.useLongTimeDelta) TimeManager.SERVER.addToRemainingTime((int) pAmount);
    }
}

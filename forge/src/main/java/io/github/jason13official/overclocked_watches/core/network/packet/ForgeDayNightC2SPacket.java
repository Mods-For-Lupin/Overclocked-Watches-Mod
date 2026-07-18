package io.github.jason13official.overclocked_watches.core.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.platform.Services;
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

            if (!ServerModConfig.dayNightCycleAllowed) return;

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

                applyCooldowns(player, 20 * 60 * (int) ServerModConfig.netheriteWatchCooldownMinutes);
                addTime(player.getServer(), ServerModConfig.netheriteTimeAdvancementTicks);
            }
            else if (hasDiamondWatch) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedDiamondWatch(player);

                CompoundTag validatedStackData = equippedWatch.getOrCreateTag();
                final int newChargeCount = validatedStackData.getInt(CHARGES) - 1;
                validatedStackData.remove(CHARGES);
                validatedStackData.putInt(CHARGES, newChargeCount);
                equippedWatch.save(validatedStackData);
                // we have consumed a charge

                applyCooldowns(player, 20 * 60 * (int) ServerModConfig.diamondWatchCooldownMinutes);
                addTime(player.getServer(), ServerModConfig.diamondTimeAdvancementTicks);
            }
            else if (hasGoldenWatch) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedGoldenWatch(player);

                CompoundTag validatedStackData = equippedWatch.getOrCreateTag();
                final int newChargeCount = validatedStackData.getInt(CHARGES) - 1;
                validatedStackData.remove(CHARGES);
                validatedStackData.putInt(CHARGES, newChargeCount);
                equippedWatch.save(validatedStackData);
                // we have consumed a charge

                applyCooldowns(player, 20 * 60 * (int) ServerModConfig.goldenWatchCooldownMinutes);
                addTime(player.getServer(), ServerModConfig.goldenTimeAdvancementTicks);
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
            if (!ServerModConfig.useLongTimeDelta) level.setDayTime(level.getDayTime() + pAmount);
        }

        if (ServerModConfig.useLongTimeDelta) TimeManager.SERVER.addToRemainingTime((int) pAmount);
    }
}

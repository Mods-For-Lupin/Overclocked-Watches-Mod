package com.cursee.overclocked_watches.mixin;

import com.cursee.overclocked_watches.core.util.OverclockedWatchesUtil;
import com.cursee.overclocked_watches.platform.Services;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ForgeServerPlayerMixin extends Player {

    public ForgeServerPlayerMixin(Level l, BlockPos p, float f, GameProfile g) {
        super(l, p, f, g);
    }

    @Inject(
            method = {"addAdditionalSaveData"},
            at = {@At("HEAD")}
    )
    private void inject$addAdditionalSaveData(CompoundTag tag0, CallbackInfo ci) {
        OverclockedWatchesUtil.saveCooldowns(this.getPersistentData(), this);
    }
}

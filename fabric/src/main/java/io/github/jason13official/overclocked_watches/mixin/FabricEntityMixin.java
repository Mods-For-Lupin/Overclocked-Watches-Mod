package io.github.jason13official.overclocked_watches.mixin;

import io.github.jason13official.overclocked_watches.api.common.data.IEntityDataSaver;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class FabricEntityMixin implements IEntityDataSaver {

  @Unique
  private CompoundTag persistentData;

  @Override
  public CompoundTag overclocked_watches$getPersistentData() {
    if (this.persistentData == null) {
      this.persistentData = new CompoundTag();
    }
    return persistentData;
  }

  @Inject(method = "saveWithoutId", at = @At("HEAD"))
  protected void injectWriteMethod(CompoundTag nbt, CallbackInfoReturnable info) {
    if (persistentData != null) {
      nbt.put(OverclockedWatchesUtil.PERSISTENT_DATA_TAG, persistentData);
    }
  }

  @Inject(method = "load", at = @At("HEAD"))
  protected void injectReadMethod(CompoundTag nbt, CallbackInfo info) {
    if (nbt.contains(OverclockedWatchesUtil.PERSISTENT_DATA_TAG, Tag.TAG_COMPOUND)) {
      persistentData = nbt.getCompound(OverclockedWatchesUtil.PERSISTENT_DATA_TAG);
    }
  }
}

package io.github.jason13official.overclocked_watches.mixin;

import io.github.jason13official.overclocked_watches.impl.common.util.IEntityDataSaver;
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
  public CompoundTag getPersistentData() {
    if (this.persistentData == null) {
      this.persistentData = new CompoundTag();
    }
    return persistentData;
  }

  @Inject(method = "saveWithoutId", at = @At("HEAD"))
  protected void injectWriteMethod(CompoundTag nbt, CallbackInfoReturnable info) {
    if (persistentData != null) {
      nbt.put("ocw.watch_data", persistentData);
    }
  }

  @Inject(method = "load", at = @At("HEAD"))
  protected void injectReadMethod(CompoundTag nbt, CallbackInfo info) {
    if (nbt.contains("ocw.watch_data", Tag.TAG_COMPOUND)) {
      persistentData = nbt.getCompound("ocw.watch_data");
    }
  }
}

package io.github.jason13official.overclocked_watches.impl.common.registry;

import com.mojang.serialization.Codec;
import io.github.jason13official.overclocked_watches.OverclockedWatches;
import java.util.function.BiConsumer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

public class ModDataComponents {

  public static DataComponentType<Integer> CHARGES;

  public static void register(BiConsumer<DataComponentType<?>, ResourceLocation> consumer) {

    CHARGES = DataComponentType.<Integer>builder()
        .persistent(Codec.INT)
        .networkSynchronized(ByteBufCodecs.VAR_INT)
        .build();

    consumer.accept(CHARGES, OverclockedWatches.identifier("charges"));
  }
}

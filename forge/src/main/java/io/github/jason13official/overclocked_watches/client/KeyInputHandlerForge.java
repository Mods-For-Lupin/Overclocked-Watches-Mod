package io.github.jason13official.overclocked_watches.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jason13official.overclocked_watches.Constants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandlerForge {

  public static final KeyMapping dayNightKey = new KeyMapping(
      Constants.KEY_DAY_NIGHT,
      KeyConflictContext.IN_GAME,
      InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_O,
      Constants.KEY_CATEGORY_DAY_NIGHT
  );
}

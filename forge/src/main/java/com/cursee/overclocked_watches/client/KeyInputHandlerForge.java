package com.cursee.overclocked_watches.client;

import com.cursee.overclocked_watches.Constants;
import com.mojang.blaze3d.platform.InputConstants;
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

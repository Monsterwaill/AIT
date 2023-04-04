package io.mdt.ait.util;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

public class Util {

    public static void sendMessage(Entity entity, ITextComponent text) {
        entity.sendMessage(text, UUID.randomUUID());
    }
}

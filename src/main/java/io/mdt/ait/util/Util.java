package io.mdt.ait.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

import java.util.UUID;

public class Util {

    public static void sendMessage(Entity entity, ITextComponent text) {
        entity.sendMessage(text, UUID.randomUUID());
    }
}

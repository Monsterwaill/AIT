package com.mdt.ait.network.packets;

import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IPacket {

    void encode(PacketBuffer buffer);

    boolean handle(Supplier<NetworkEvent.Context> ctx);
}

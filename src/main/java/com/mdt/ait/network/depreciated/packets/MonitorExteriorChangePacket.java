package com.mdt.ait.network.depreciated.packets;

import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MonitorExteriorChangePacket {

    private int list = 0;

    public MonitorExteriorChangePacket(int list) {
        this.list = list;
    }

    public static void encode(MonitorExteriorChangePacket msg, PacketBuffer packet) {
        packet.writeInt(msg.list);
    }

    public static <MSG> void handle(MSG msg, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {});
    }

    // public static <MSG> MSG decode(PacketBuffer packetBuffer) {
    //    packetBuffer;
    // }
}

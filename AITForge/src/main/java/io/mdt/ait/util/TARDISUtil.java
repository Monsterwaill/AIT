package io.mdt.ait.util;

import com.mdt.ait.AIT;
import com.mdt.ait.core.init.AITDimensions;
import io.mdt.ait.config.TARDISConfig;
import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.TARDISManager;
import io.mdt.ait.tardis.interior.TARDISInteriorSchema;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class TARDISUtil {

    public static ServerWorld getTARDISWorld() {
        return ServerLifecycleHooks.getCurrentServer().getLevel(AITDimensions.TARDIS_DIMENSION);
    }

    public static int getIndexByPos(BlockPos pos) {
        return (int) Math.ceil(
                        ((double) TARDISConfig.TARDIS_DIMENSION_START - pos.getX()) / (double) TARDISConfig.TARDIS_AREA)
                - 1;
    }

    public static boolean canLand(Block landing) {
        boolean canLand = true;
        for (Block block : TARDISConfig.CANT_LAND_ON) {
            if (block.is(landing)) {
                canLand = false;
                break;
            }
        }

        return canLand;
    }

    public static String getRandomName(Random random) {
        return TARDISConfig.TARDIS_NAMES[random.nextInt(TARDISConfig.TARDIS_NAMES.length - 1)];
    }

    public static String composeName(int iterations, Random random) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            builder.append(TARDISUtil.getRandomName(random));
        }

        return builder.toString();
    }

    public static BlockPos getTARDISCenter(int i) {
        int xz = TARDISConfig.TARDIS_DIMENSION_START
                - (TARDISConfig.TARDIS_AREA * i)
                + ((TARDISConfig.TARDIS_AREA - 1) / 2)
                + 1;
        return new BlockPos(xz, TARDISConfig.TARDIS_CENTER_Y, xz);
    }

    public static World getExteriorLevel(TARDIS tardis) {
        return TARDISUtil.getWorld(tardis.getPosition().getDimension());
    }

    public static World getWorld(RegistryKey<World> dimension) {
        return AIT.server.getLevel(dimension);
    }

    public static BlockPos getInteriorPos(TARDISInteriorSchema interior) {
        return TARDISUtil.getTARDISCenter(TARDISManager.getInstance().getLastIndex())
                .offset(
                        -interior.getCenter().getX(),
                        -interior.getCenter().getY(),
                        -interior.getCenter().getZ());
    }
}

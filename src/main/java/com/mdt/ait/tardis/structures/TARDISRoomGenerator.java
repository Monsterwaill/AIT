package com.mdt.ait.tardis.structures;

import com.mdt.ait.AIT;
import com.mdt.ait.core.init.AITBlocks;
import java.util.*;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;

public class TARDISRoomGenerator {
    private final ServerWorld tardisWorld;
    private final String name;
    private final String fileName;
    private final List<ResourceLocation> structureList = new ArrayList<>();
    //    public ArrayList<String> structureNameList = new ArrayList<>();
    public static String[] structureNameList = {
        "ars_tree_room",
        "downstairs_corridor",
        "gardening_room",
        "left_corridor_bend",
        "long_corridor",
        "medium_corridor",
        "right_corridor_bend",
        "short_corridor",
        "upstairs_corridor"
    }; // TO ADD A NEW STRUCTURE, PUT ITS FILE NAME HERE PLEASE
    private final Block[] blockIgnoreList = {AITBlocks.ARS_GENERATE_BLOCK.get(), AITBlocks.ARS_CENTRE_BLOCK.get()
    }; // blocks that will be ignored if found in the check

    private final String filePrefix = "rooms/";
    private final Template structureTemplate;
    private final String fallbackStructure = "short_corridor";

    /*
    ** READ ME **
    To add new structures to this file, its simple.
    BUT remember to read *ALL* the comments that regard advice on building first, as otherwise it may not function properly
    Simply drag and drop your .nbt file into resources/data/ait/structures/rooms
    Then, add its file name to the end of the structureNameList variable
    Example:
    * if my room was called "example_building.nbt"
    * and the variable was {"aaasa","sadsad"} before
    * i would add "example_building" to the variable and it would become
    * {"aasa","sadsad","example_building}
    GOOD LUCK
     */

    // Building Comments :

    // ENCASE STRUCTURE IN STRUCTURE VOID BLOCKS

    // @TODO WARNING - THE EMPTY WALL WHICH YOU ENTER THE ROOM FROM MUST BE THE NORTHERMOST POINT OF
    // THE STRUCTURE, SEE EXAMPLE BELOW.
    /*
    key: ----- = EMPTY 5*5 WALL
       N
     -----
    E     W

       S
     */

    // PLACE THE BLOCK "ars_centre" WHERE YOU WANT THE ENTRANCE OF YOUR STRUCTURE TO BE
    // THEN, IN LINE WITH THE CENTRE BLOCK, PLACE A "ars_corner" BLOCK IN THE CORNER ON THE RIGHT

    public TARDISRoomGenerator(ServerWorld tardisWorld, String structureName) {
        this.tardisWorld = tardisWorld;
        this.name = structureName;
        this.fileName = filePrefix + structureName;
        // addFilesToStructureNameList(directoryToFiles);
        populateStructureList();
        structureTemplate = tardisWorld.getStructureManager().getOrCreate(getStructureLocation(structureName));
    }

    public void placeStructure(
            ServerWorld destinationWorld,
            BlockPos destinationBlockPos,
            Direction destinationDirection,
            PlayerEntity player) {
        BlockPos placePos = getPlacementPos(destinationBlockPos, destinationDirection);
        if (placePos == null
                || !safeToPlace(
                        placePos,
                        destinationDirection,
                        destinationWorld)) { // checks if the blockpos is broken or if the safe to place check
            // returned false
            sendPlaceChat(false, player, null);
            return;
        }

        structureTemplate.placeInWorld(
                destinationWorld,
                placePos,
                new PlacementSettings().setRotation(directionToRotation(destinationDirection)),
                destinationWorld.getRandom());
        destinationWorld.destroyBlock(
                findTargetBlockPos(destinationBlockPos, destinationDirection, AITBlocks.ARS_CENTRE_BLOCK.get()), false);
        destinationWorld.destroyBlock(
                findTargetBlockPos(destinationBlockPos, destinationDirection, AITBlocks.ARS_CORNER_BLOCK.get()), false);
        sendPlaceChat(true, player, null);
    }

    // @TODO FINISH THIS
    public void deleteStructure(ServerWorld world, BlockPos pos, Direction direction, PlayerEntity player) {
        // So, the player needs to place this ARS Remover where the ARS Generator used to be.
        // Then we get the position of where the corner block is, so we know where to begin iterating
        // from
        BlockPos cornerBlockPos = findTargetBlockPos(pos, direction, AITBlocks.ARS_CORNER_BLOCK.get());
        int x = cornerBlockPos.getX();
        int y = cornerBlockPos.getY();
        int z = cornerBlockPos.getZ();

        // Get the x,y,z lengths of the structure so we know how long to iterate for
        BlockPos structure_size = structureTemplate.getSize(directionToRotation(direction));
        int size_x = structure_size.getX();
        int size_y = structure_size.getY();
        int size_z = structure_size.getZ();

        // Begin iterating, checking if the block was there in the template, and then deleting it if it
        // was, and ignoring it if it wasnt.
        // *mostly copy pasted from the safe to place method*
        BlockPos checkPos;

        if (direction == Direction.NORTH) {
            for (x = x; x >= pos.getX() - size_x; x--) {
                y = pos.getY();
                for (y = y; y <= pos.getY() + size_y; y++) {
                    z = pos.getZ() - 2;
                    for (z = z; z >= pos.getZ() - size_z; z--) {
                        checkPos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (!(block instanceof AirBlock)) {
                            if (!isInIgnoreBlockList(block)) {
                                world.destroyBlock(checkPos, false);
                            }
                        }
                    }
                }
            }
        } else if (direction == Direction.SOUTH) {
            for (x = x; x <= pos.getX() + size_x; x++) {
                y = pos.getY();
                for (y = y; y <= pos.getY() + size_y; y++) {
                    z = pos.getZ() + 2;
                    for (z = z; z <= pos.getZ() + size_z; z++) {
                        checkPos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (!(block instanceof AirBlock)) {
                            if (!isInIgnoreBlockList(block)) {
                                world.destroyBlock(checkPos, false);
                            }
                        }
                    }
                }
            }
        } else if (direction == Direction.EAST) {
            x = pos.getX() + 2;
            for (x = x; x <= pos.getX() + size_x; x++) {
                y = pos.getY();
                for (y = y; y <= pos.getY() + size_y; y++) {
                    z = pos.getZ() - 2;
                    for (z = z; z >= pos.getZ() - size_z; z--) {
                        checkPos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (!(block instanceof AirBlock)) {
                            if (!isInIgnoreBlockList(block)) {
                                world.destroyBlock(checkPos, false);
                            }
                        }
                    }
                }
            }
        } else if (direction == Direction.WEST) {
            x = pos.getX() - 2;
            for (x = x; x >= pos.getX() - size_x; x--) {
                y = pos.getY();
                for (y = y; y <= pos.getY() + size_y; y++) {
                    z = pos.getZ() - 2;
                    for (z = z; z <= pos.getZ() + size_z; z++) {
                        checkPos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (!(block instanceof AirBlock)) {
                            if (!isInIgnoreBlockList(block)) {
                                world.destroyBlock(checkPos, false);
                            }
                        }
                    }
                }
            }
        }
    }

    public Rotation directionToRotation(Direction direction) {
        switch (direction) {
            default:
                return Rotation.NONE;
            case NORTH:
                return Rotation.CLOCKWISE_180;
            case SOUTH:
                return Rotation.NONE;
            case EAST:
                return Rotation.COUNTERCLOCKWISE_90;
            case WEST:
                return Rotation.CLOCKWISE_90;
        }
    }

    /**
     *
     * @param pos
     * @param direction
     * @return Returns the {@link BlockPos} where this structure should be placed.
     */
    private BlockPos getPlacementPos(BlockPos pos, Direction direction) {
        BlockPos centreBlockPos = findTargetBlockPos(pos, direction, AITBlocks.ARS_CENTRE_BLOCK.get());
        BlockPos cornerBlockPos = findTargetBlockPos(pos, direction, AITBlocks.ARS_CORNER_BLOCK.get());

        // Work out the difference between these two positions
        BlockPos remainderBlockPos = new BlockPos(
                centreBlockPos.getX() - cornerBlockPos.getX(),
                centreBlockPos.getY() - cornerBlockPos.getY(),
                centreBlockPos.getZ() - cornerBlockPos.getZ());
        // Now work out where to place the structure
        BlockPos placementBlockPos = new BlockPos(
                pos.getX() - remainderBlockPos.getX(),
                pos.getY() - remainderBlockPos.getY(),
                pos.getZ() - remainderBlockPos.getZ());

        // set variables again
        int placement_block_x = placementBlockPos.getX();
        int placement_block_y = placementBlockPos.getY();
        int placement_block_z = placementBlockPos.getZ();

        placementBlockPos = new BlockPos(placement_block_x, placement_block_y, placement_block_z);
        return placementBlockPos;
    }

    private BlockPos findTargetBlockPos(BlockPos pos, Direction direction, Block targetBlock) {
        List<Template.BlockInfo> list = structureTemplate.filterBlocks(
                pos, new PlacementSettings().setRotation(directionToRotation(direction)), targetBlock);
        return list.get(0).pos;
    }

    private boolean safeToPlace(BlockPos pos, Direction direction, World world) {
        BlockPos structure_size = structureTemplate.getSize(directionToRotation(direction));
        int size_x = structure_size.getX();
        int size_y = structure_size.getY();
        int size_z = structure_size.getZ();
        int x = pos.getX();
        int z = pos.getZ();
        int y = pos.getY();
        BlockPos checkPos = pos;

        // Long, lengthy check for directions as each direction needs to check a different way.
        // only way i know of doing this currently, if you know a better one PLEASE, let me know! :)
        if (direction == Direction.NORTH) {
            for (x = x; x >= pos.getX() - size_x; x--) {
                y = pos.getY();
                for (y = y; y <= pos.getY() + size_y; y++) {
                    z = pos.getZ() - 2;
                    for (z = z; z >= pos.getZ() - size_z; z--) {
                        checkPos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (!(block instanceof AirBlock)) {
                            if (!isInIgnoreBlockList(block)) {
                                System.out.println("CHECK FAILED, FOUND BLOCK : "
                                        + world.getBlockState(checkPos).getBlock()
                                        + " AT POSITION: "
                                        + checkPos);
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (direction == Direction.SOUTH) {
            for (x = x; x <= pos.getX() + size_x; x++) {
                y = pos.getY();
                for (y = y; y <= pos.getY() + size_y; y++) {
                    z = pos.getZ() + 2;
                    for (z = z; z <= pos.getZ() + size_z; z++) {
                        checkPos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (!(block instanceof AirBlock)) {
                            if (!isInIgnoreBlockList(block)) {
                                System.out.println("CHECK FAILED, FOUND BLOCK : "
                                        + world.getBlockState(checkPos).getBlock()
                                        + " AT POSITION: "
                                        + checkPos);
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (direction == Direction.EAST) {
            x = pos.getX() + 2;
            for (x = x; x <= pos.getX() + size_x; x++) {
                y = pos.getY();
                for (y = y; y <= pos.getY() + size_y; y++) {
                    z = pos.getZ() - 2;
                    for (z = z; z >= pos.getZ() - size_z; z--) {
                        checkPos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (!(block instanceof AirBlock)) {
                            if (!isInIgnoreBlockList(block)) {
                                System.out.println("CHECK FAILED, FOUND BLOCK : "
                                        + world.getBlockState(checkPos).getBlock()
                                        + " AT POSITION: "
                                        + checkPos);
                                return false;
                            }
                        }
                    }
                }
            }
        } else if (direction == Direction.WEST) {
            x = pos.getX() - 2;
            for (x = x; x >= pos.getX() - size_x; x--) {
                y = pos.getY();
                for (y = y; y <= pos.getY() + size_y; y++) {
                    z = pos.getZ() - 2;
                    for (z = z; z <= pos.getZ() + size_z; z++) {
                        checkPos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (!(block instanceof AirBlock)) {
                            if (!isInIgnoreBlockList(block)) {
                                System.out.println("CHECK FAILED, FOUND BLOCK : "
                                        + world.getBlockState(checkPos).getBlock()
                                        + " AT POSITION: "
                                        + checkPos);
                                return false;
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean isInIgnoreBlockList(Block block) {
        for (Block checkBlock : blockIgnoreList) {
            if (checkBlock == block) {
                return true;
            }
        }
        return false;
    }

    private ResourceLocation getStructureLocation(String name) {
        if (name == null) {
            name = fallbackStructure;
        }
        name = "ait:" + filePrefix + name;
        for (ResourceLocation i : structureList) {
            if (i.toString().equals(name)) {
                return i;
            }
        }
        return null;
    }

    public String getNextStructureName(String name) {
        name = filePrefix + name.toLowerCase();
        int nameLocation = 0;
        for (int i = 0; i <= structureNameList.length - 1; i++) {
            if (structureNameList[i] == name) {
                nameLocation = i;
                break;
            }
        }

        return structureNameList[nameLocation + 1];
    }

    public String getPreviousStructureName(String name) {
        name = filePrefix + name.toLowerCase();
        int nameLocation = 0;
        for (int i = 0; i <= structureNameList.length - 1; i++) {
            if (structureNameList[i] == name) {
                nameLocation = i;
                break;
            }
        }

        return structureNameList[nameLocation - 1];
    }

    /*private void addFilesToStructureNameList(String directory) {
        URL resource = AIT.class.getResource("/rooms");
        File fileDirectory;
        try {
            fileDirectory = new File(new File(resource.toURI()).getAbsolutePath());
        } catch (Exception e) {
            System.out.println("EXCEPTION WITH ADDING FILES TO BaseStructure: " + e);
            return;
        }
        System.out.println(fileDirectory.listFiles());
        File filesList[] = fileDirectory.listFiles();
        for (File file : filesList) {
            System.out.println(file.getName().substring(0,file.getName().length()-4));
            structureNameList.add(file.getName().substring(0,file.getName().length()-4));
        }
    }*/

    private void populateStructureList() {
        for (String i : structureNameList) {
            i = filePrefix + i;
            structureList.add(new ResourceLocation(AIT.MOD_ID, i));
        }
    }

    /**
     * Sends a message to a player in chat about whether the structure was placed successfully.
     * @param isSuccess True for success message - False for failed message
     * @param player The player to send the chat to
     * @param extraInformation Any other information that needs to be sent - will be appended to message
     */
    private void sendPlaceChat(boolean isSuccess, PlayerEntity player, String extraInformation) {
        if (player != null) {
            if (!isSuccess) {
                player.sendMessage(
                        new TranslationTextComponent("FAILED to place structure: " + toStructureName(name))
                                .setStyle(Style.EMPTY
                                        .withColor(TextFormatting.RED)
                                        .withItalic(true)),
                        UUID.randomUUID());
                if (extraInformation != null) {
                    player.sendMessage(
                            new TranslationTextComponent("Information: " + extraInformation)
                                    .setStyle(Style.EMPTY
                                            .withColor(TextFormatting.YELLOW)
                                            .withItalic(true)),
                            UUID.randomUUID());
                }
            }
            if (isSuccess) {
                player.sendMessage(
                        new TranslationTextComponent("Successfully placed structure: " + toStructureName(name))
                                .setStyle(Style.EMPTY
                                        .withColor(TextFormatting.GREEN)
                                        .withItalic(true)),
                        UUID.randomUUID());
                if (extraInformation != null) {
                    player.sendMessage(
                            new TranslationTextComponent("Information: " + extraInformation)
                                    .setStyle(Style.EMPTY
                                            .withColor(TextFormatting.YELLOW)
                                            .withItalic(true)),
                            UUID.randomUUID());
                }
            }
        }
    }

    public Template getTemplate() {
        return structureTemplate;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toFileName() {
        return fileName;
    }

    /**
     * Removes Underscores from the NBT file name
     * Capitalises the first letter of every word
     * Returns the new, more understandable, string.
     */
    public static String toStructureName(String name) {
        String spaced = name.replace("_", " ");
        String[] words = spaced.split(" ");
        StringBuilder output = new StringBuilder();
        for (String word : words) {
            output.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return output.toString();
    }
}

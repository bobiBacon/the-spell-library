package net.bobbacon.utils;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.mixin.StructureTemplateAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorldUtils {
    public static boolean assertStructure(BlockPos origin, ServerWorld world, Identifier structureId) {
        Optional<StructureTemplate> optional = world.getServer()
                .getStructureTemplateManager()
                .getTemplate(structureId);

        if (optional.isEmpty()) return false;

        StructureTemplate template = optional.get();

        for (BlockRotation rotation : BlockRotation.values()) {
            if (matches(world, origin, template, rotation)) {
                return true;
            }
        }

        return false;
    }
    private static boolean matches(ServerWorld world,
                                   BlockPos origin,
                                   StructureTemplate template,
                                   BlockRotation rotation) {

        List<StructureTemplate.StructureBlockInfo> allBlocks = new ArrayList<>();

        for (StructureTemplate.PalettedBlockInfoList blocks :
                ((StructureTemplateAccessor) template).getBlockInfoLists()) {

            allBlocks.addAll(blocks.getAll());
        }

        List<BlockPos> transformedPositions = new ArrayList<>();

        for (StructureTemplate.StructureBlockInfo info : allBlocks) {

            BlockPos transformed = StructureTemplate.transformAround(
                    info.pos(),
                    BlockMirror.NONE,
                    rotation,
                    BlockPos.ORIGIN
            );

            transformedPositions.add(transformed);
        }

        int minX = transformedPositions.stream().mapToInt(BlockPos::getX).min().orElse(0);
        int minY = transformedPositions.stream().mapToInt(BlockPos::getY).min().orElse(0);
        int minZ = transformedPositions.stream().mapToInt(BlockPos::getZ).min().orElse(0);

        for (int i = 0; i < allBlocks.size(); i++) {

            StructureTemplate.StructureBlockInfo info = allBlocks.get(i);

            BlockPos transformed = transformedPositions.get(i)
                    .subtract(new BlockPos(minX, minY, minZ));

            BlockPos worldPos = origin.add(transformed);

            BlockState expected = info.state().rotate(rotation);
            BlockState actual = world.getBlockState(worldPos);

            if (expected.isAir()) continue;

            if (!actual.isOf(expected.getBlock())) {

                TheSpellLibrary.LOGGER.info(
                        "Mismatch at {} expected {} got {}",
                        worldPos,
                        expected.getBlock(),
                        actual.getBlock()
                );

                return false;
            }
        }

        return true;
    }
}

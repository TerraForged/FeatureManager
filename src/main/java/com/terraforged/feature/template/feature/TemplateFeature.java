/*
 *
 * MIT License
 *
 * Copyright (c) 2020 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.terraforged.feature.template.feature;

import com.terraforged.feature.util.BlockReader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TemplateFeature extends Feature<TemplateFeatureConfig> {

    private final List<BlockInfo> blocks;
    private final BlockReader reader = new BlockReader();

    private TemplateFeature(List<BlockInfo> blocks) {
        super(TemplateFeatureConfig::deserialize);
        this.blocks = blocks;
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<?> generator, Random rand, BlockPos origin, TemplateFeatureConfig config) {
        Mirror mirror = getMirror(rand);
        Rotation rotation = getRotation(rand);

        boolean placed = false;
        for (BlockInfo block : blocks) {
            BlockState state = block.state.rotate(rotation).mirror(mirror);
            if (isAir(state) && !config.pasteAir) {
                continue;
            }

            BlockPos pos = Template.getTransformedPos(block.pos, mirror, rotation, BlockPos.ZERO).add(origin);
            if (block.pos.getY() <= 0 && block.state.isNormalCube(reader.setState(block.state), BlockPos.ZERO)) {
                placeBase(world, pos, state, config.baseDepth);
            }

            if (!config.replaceSolid) {
                BlockState current = world.getBlockState(pos);
                if (current.isSolid()) {
                    continue;
                }
            }

            placed = true;
            world.setBlockState(pos, state, 2);
        }

        return placed;
    }

    private void placeBase(IWorld world, BlockPos pos, BlockState state, int depth) {
        for (int dy = 0; dy < depth; dy++) {
            pos = pos.down();
            world.setBlockState(pos, state, 2);
        }
    }

    private static boolean isAir(BlockState state) {
        return state.getBlock() == Blocks.AIR;
    }

    private static Mirror getMirror(Random random) {
        return Mirror.values()[random.nextInt(Mirror.values().length)];
    }

    private static Rotation getRotation(Random random) {
        return Rotation.values()[random.nextInt(Rotation.values().length)];
    }

    public static class BlockInfo {

        private final BlockPos pos;
        private final BlockState state;

        public BlockInfo(BlockPos pos, BlockState state) {
            this.pos = pos;
            this.state = state;
        }

        public BlockPos getPos() {
            return pos;
        }

        public BlockState getState() {
            return state;
        }

        @Override
        public String toString() {
            return state.toString();
        }
    }

    public static Optional<TemplateFeature> load(InputStream data) {
        try {
            CompoundNBT root = CompressedStreamTools.readCompressed(data);
            if (!root.contains("palette") || !root.contains("blocks")) {
                return Optional.empty();
            }
            BlockState[] palette = readPalette(root.getList("palette", Constants.NBT.TAG_COMPOUND));
            BlockInfo[] blockInfos = readBlocks(root.getList("blocks", Constants.NBT.TAG_COMPOUND), palette);
            List<BlockInfo> blocks = relativize(blockInfos);
            return Optional.of(new TemplateFeature(blocks));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private static BlockState[] readPalette(ListNBT list) {
        BlockState[] palette = new BlockState[list.size()];
        for (int i = 0; i < list.size(); i++) {
            try {
                palette[i] = NBTUtil.readBlockState(list.getCompound(i));
            } catch (Throwable t) {
                palette[i] = Blocks.AIR.getDefaultState();
            }
        }
        return palette;
    }

    private static BlockInfo[] readBlocks(ListNBT list, BlockState[] palette) {
        BlockInfo[] blocks = new BlockInfo[list.size()];
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT compound = list.getCompound(i);
            BlockState state = palette[compound.getInt("state")];
            BlockPos pos = readPos(compound.getList("pos", Constants.NBT.TAG_INT));
            blocks[i] = new BlockInfo(pos, state);
        }
        return blocks;
    }

    private static List<BlockInfo> relativize(BlockInfo[] blocks) {
        // find the size
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (BlockInfo block : blocks) {
            minX = Math.min(minX, block.pos.getX());
            maxX = Math.max(maxX, block.pos.getX());
            minZ = Math.min(minZ, block.pos.getZ());
            maxZ = Math.max(maxZ, block.pos.getZ());
        }
        int width = maxX - minX;
        int length = maxZ - minZ;

        // find the lowest, most-central block (the origin)
        int centerX = width / 2;
        int centerZ = length / 2;
        BlockPos origin = null;
        int lowestSolid = Integer.MAX_VALUE;
        int closestDist2 = Integer.MAX_VALUE;

        for (BlockInfo block : blocks) {
            if (!block.state.isSolid()) {
                continue;
            }

            if (origin == null) {
                origin = block.pos;
                lowestSolid = block.pos.getY();
                closestDist2 = dist2(centerX, centerZ, block.pos.getX(), block.pos.getZ());
            } else if (block.pos.getY() < lowestSolid) {
                origin = block.pos;
                lowestSolid = block.pos.getY();
                closestDist2 = dist2(centerX, centerZ, block.pos.getX(), block.pos.getZ());
            } else if (block.pos.getY() == lowestSolid) {
                int dist2 = dist2(centerX, centerZ, block.pos.getX(), block.pos.getZ());
                if (dist2 < closestDist2) {
                    origin = block.pos;
                    closestDist2 = dist2;
                    lowestSolid = block.pos.getY();
                }
            }
        }

        if (origin == null) {
            return Arrays.asList(blocks);
        }

        // relativize all blocks to the origin
        List<BlockInfo> list = new ArrayList<>(blocks.length);
        for (BlockInfo in : blocks) {
            BlockPos pos = in.pos.subtract(origin);
            BlockInfo out = new BlockInfo(pos, in.state);
            list.add(out);
        }

        return list;
    }

    private static int dist2(int x1, int z1, int x2, int z2) {
        int dx = x1 - x2;
        int dz = z1 - z2;
        return dx * dx + dz * dz;
    }

    private static BlockPos readPos(ListNBT list) {
        int x = list.getInt(0);
        int y = list.getInt(1);
        int z = list.getInt(2);
        return new BlockPos(x, y, z);
    }
}

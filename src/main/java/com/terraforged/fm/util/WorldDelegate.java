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

package com.terraforged.fm.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ITickList;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WorldDelegate implements IWorld {

    private IWorld delegate;

    public WorldDelegate(IWorld delegate) {
        this.delegate = delegate;
    }

    public void setDelegate(IWorld delegate) {
        this.delegate = delegate;
    }

    public IWorld getDelegate() {
        return delegate;
    }

    @Override
    public long getSeed() {
        return delegate.getSeed();
    }

    @Override
    public float getCurrentMoonPhaseFactor() {
        return delegate.getCurrentMoonPhaseFactor();
    }

    @Override
    public float getCelestialAngle(float partialTicks) {
        return delegate.getCelestialAngle(partialTicks);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getMoonPhase() {
        return delegate.getMoonPhase();
    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return delegate.getPendingBlockTicks();
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return delegate.getPendingFluidTicks();
    }

    @Override
    public World getWorld() {
        return delegate.getWorld();
    }

    @Override
    public WorldInfo getWorldInfo() {
        return delegate.getWorldInfo();
    }

    @Override
    public DifficultyInstance getDifficultyForLocation(BlockPos pos) {
        return delegate.getDifficultyForLocation(pos);
    }

    @Override
    public Difficulty getDifficulty() {
        return delegate.getDifficulty();
    }

    @Override
    public AbstractChunkProvider getChunkProvider() {
        return delegate.getChunkProvider();
    }

    @Override
    public boolean chunkExists(int chunkX, int chunkZ) {
        return delegate.chunkExists(chunkX, chunkZ);
    }

    @Override
    public Random getRandom() {
        return delegate.getRandom();
    }

    @Override
    public void notifyNeighbors(BlockPos pos, Block blockIn) {
        delegate.notifyNeighbors(pos, blockIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockPos getSpawnPoint() {
        return delegate.getSpawnPoint();
    }

    @Override
    public void playSound(@Nullable PlayerEntity player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
        delegate.playSound(player, pos, soundIn, category, volume, pitch);
    }

    @Override
    public void addParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        delegate.addParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void playEvent(@Nullable PlayerEntity player, int type, BlockPos pos, int data) {
        delegate.playEvent(player, type, pos, data);
    }

    @Override
    public void playEvent(int type, BlockPos pos, int data) {
        delegate.playEvent(type, pos, data);
    }

    @Override
    public Stream<VoxelShape> getEmptyCollisionShapes(@Nullable Entity entityIn, AxisAlignedBB aabb, Set<Entity> entitiesToIgnore) {
        return delegate.getEmptyCollisionShapes(entityIn, aabb, entitiesToIgnore);
    }

    @Override
    public boolean checkNoEntityCollision(@Nullable Entity entityIn, VoxelShape shape) {
        return delegate.checkNoEntityCollision(entityIn, shape);
    }

    @Override
    public BlockPos getHeight(Heightmap.Type heightmapType, BlockPos pos) {
        return delegate.getHeight(heightmapType, pos);
    }

    @Override
    public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
        return delegate.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb, @Nullable Predicate<? super T> filter) {
        return delegate.getEntitiesWithinAABB(clazz, aabb, filter);
    }

    @Override
    public <T extends Entity> List<T> func_225316_b(Class<? extends T> p_225316_1_, AxisAlignedBB p_225316_2_, @Nullable Predicate<? super T> p_225316_3_) {
        return delegate.func_225316_b(p_225316_1_, p_225316_2_, p_225316_3_);
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        return delegate.getPlayers();
    }

    @Override
    public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity entityIn, AxisAlignedBB bb) {
        return delegate.getEntitiesWithinAABBExcludingEntity(entityIn, bb);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> p_217357_1_, AxisAlignedBB p_217357_2_) {
        return delegate.getEntitiesWithinAABB(p_217357_1_, p_217357_2_);
    }

    @Override
    public <T extends Entity> List<T> func_225317_b(Class<? extends T> p_225317_1_, AxisAlignedBB p_225317_2_) {
        return delegate.func_225317_b(p_225317_1_, p_225317_2_);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(double x, double y, double z, double distance, @Nullable Predicate<Entity> predicate) {
        return delegate.getClosestPlayer(x, y, z, distance, predicate);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(Entity entityIn, double distance) {
        return delegate.getClosestPlayer(entityIn, distance);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(double x, double y, double z, double distance, boolean creativePlayers) {
        return delegate.getClosestPlayer(x, y, z, distance, creativePlayers);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(double x, double y, double z) {
        return delegate.getClosestPlayer(x, y, z);
    }

    @Override
    public boolean isPlayerWithin(double x, double y, double z, double distance) {
        return delegate.isPlayerWithin(x, y, z, distance);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, LivingEntity target) {
        return delegate.getClosestPlayer(predicate, target);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, LivingEntity target, double p_217372_3_, double p_217372_5_, double p_217372_7_) {
        return delegate.getClosestPlayer(predicate, target, p_217372_3_, p_217372_5_, p_217372_7_);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, double x, double y, double z) {
        return delegate.getClosestPlayer(predicate, x, y, z);
    }

    @Override
    @Nullable
    public <T extends LivingEntity> T getClosestEntityWithinAABB(Class<? extends T> entityClazz, EntityPredicate p_217360_2_, @Nullable LivingEntity target, double x, double y, double z, AxisAlignedBB boundingBox) {
        return delegate.getClosestEntityWithinAABB(entityClazz, p_217360_2_, target, x, y, z, boundingBox);
    }

    @Override
    @Nullable
    public <T extends LivingEntity> T func_225318_b(Class<? extends T> p_225318_1_, EntityPredicate p_225318_2_, @Nullable LivingEntity p_225318_3_, double p_225318_4_, double p_225318_6_, double p_225318_8_, AxisAlignedBB p_225318_10_) {
        return delegate.func_225318_b(p_225318_1_, p_225318_2_, p_225318_3_, p_225318_4_, p_225318_6_, p_225318_8_, p_225318_10_);
    }

    @Override
    @Nullable
    public <T extends LivingEntity> T getClosestEntity(List<? extends T> entities, EntityPredicate predicate, @Nullable LivingEntity target, double x, double y, double z) {
        return delegate.getClosestEntity(entities, predicate, target, x, y, z);
    }

    @Override
    public List<PlayerEntity> getTargettablePlayersWithinAABB(EntityPredicate predicate, LivingEntity target, AxisAlignedBB box) {
        return delegate.getTargettablePlayersWithinAABB(predicate, target, box);
    }

    @Override
    public <T extends LivingEntity> List<T> getTargettableEntitiesWithinAABB(Class<? extends T> p_217374_1_, EntityPredicate p_217374_2_, LivingEntity p_217374_3_, AxisAlignedBB p_217374_4_) {
        return delegate.getTargettableEntitiesWithinAABB(p_217374_1_, p_217374_2_, p_217374_3_, p_217374_4_);
    }

    @Override
    @Nullable
    public PlayerEntity getPlayerByUuid(UUID uniqueIdIn) {
        return delegate.getPlayerByUuid(uniqueIdIn);
    }

    @Override
    @Nullable
    public IChunk getChunk(int x, int z, ChunkStatus requiredStatus, boolean nonnull) {
        return delegate.getChunk(x, z, requiredStatus, nonnull);
    }

    @Override
    public int getHeight(Heightmap.Type heightmapType, int x, int z) {
        return delegate.getHeight(heightmapType, x, z);
    }

    @Override
    public int getSkylightSubtracted() {
        return delegate.getSkylightSubtracted();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return delegate.getBiomeManager();
    }

    @Override
    public Biome getBiome(BlockPos p_226691_1_) {
        return delegate.getBiome(p_226691_1_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getBlockColor(BlockPos blockPosIn, ColorResolver colorResolverIn) {
        return delegate.getBlockColor(blockPosIn, colorResolverIn);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return delegate.getNoiseBiome(x, y, z);
    }

    @Override
    public Biome getNoiseBiomeRaw(int x, int y, int z) {
        return delegate.getNoiseBiomeRaw(x, y, z);
    }

    @Override
    public boolean isRemote() {
        return delegate.isRemote();
    }

    @Override
    public int getSeaLevel() {
        return delegate.getSeaLevel();
    }

    @Override
    public Dimension getDimension() {
        return delegate.getDimension();
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return delegate.isAirBlock(pos);
    }

    @Override
    public boolean canBlockSeeSky(BlockPos pos) {
        return delegate.canBlockSeeSky(pos);
    }

    @Override
    @Deprecated
    public float getBrightness(BlockPos pos) {
        return delegate.getBrightness(pos);
    }

    @Override
    public int getStrongPower(BlockPos pos, Direction direction) {
        return delegate.getStrongPower(pos, direction);
    }

    @Override
    public IChunk getChunk(BlockPos pos) {
        return delegate.getChunk(pos);
    }

    @Override
    public IChunk getChunk(int chunkX, int chunkZ) {
        return delegate.getChunk(chunkX, chunkZ);
    }

    @Override
    public IChunk getChunk(int chunkX, int chunkZ, ChunkStatus requiredStatus) {
        return delegate.getChunk(chunkX, chunkZ, requiredStatus);
    }

    @Override
    @Nullable
    public IBlockReader getBlockReader(int p_225522_1_, int p_225522_2_) {
        return delegate.getBlockReader(p_225522_1_, p_225522_2_);
    }

    @Override
    public boolean hasWater(BlockPos pos) {
        return delegate.hasWater(pos);
    }

    @Override
    public boolean containsAnyLiquid(AxisAlignedBB bb) {
        return delegate.containsAnyLiquid(bb);
    }

    @Override
    public int getLight(BlockPos pos) {
        return delegate.getLight(pos);
    }

    @Override
    public int getNeighborAwareLightSubtracted(BlockPos pos, int amount) {
        return delegate.getNeighborAwareLightSubtracted(pos, amount);
    }

    @Override
    @Deprecated
    public boolean isBlockLoaded(BlockPos pos) {
        return delegate.isBlockLoaded(pos);
    }

    @Override
    public boolean isAreaLoaded(BlockPos center, int range) {
        return delegate.isAreaLoaded(center, range);
    }

    @Override
    @Deprecated
    public boolean isAreaLoaded(BlockPos from, BlockPos to) {
        return delegate.isAreaLoaded(from, to);
    }

    @Override
    @Deprecated
    public boolean isAreaLoaded(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        return delegate.isAreaLoaded(fromX, fromY, fromZ, toX, toY, toZ);
    }

    @Override
    public WorldLightManager getLightManager() {
        return delegate.getLightManager();
    }

    @Override
    public int getLightFor(LightType lightTypeIn, BlockPos blockPosIn) {
        return delegate.getLightFor(lightTypeIn, blockPosIn);
    }

    @Override
    public int getLightSubtracted(BlockPos blockPosIn, int amount) {
        return delegate.getLightSubtracted(blockPosIn, amount);
    }

    @Override
    public boolean canSeeSky(BlockPos blockPosIn) {
        return delegate.canSeeSky(blockPosIn);
    }

    @Override
    @Nullable
    public TileEntity getTileEntity(BlockPos pos) {
        return delegate.getTileEntity(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return delegate.getBlockState(pos);
    }

    @Override
    public IFluidState getFluidState(BlockPos pos) {
        return delegate.getFluidState(pos);
    }

    @Override
    public int getLightValue(BlockPos pos) {
        return delegate.getLightValue(pos);
    }

    @Override
    public int getMaxLightLevel() {
        return delegate.getMaxLightLevel();
    }

    @Override
    public int getHeight() {
        return delegate.getHeight();
    }

    @Override
    public BlockRayTraceResult rayTraceBlocks(RayTraceContext context) {
        return delegate.rayTraceBlocks(context);
    }

    @Override
    @Nullable
    public BlockRayTraceResult rayTraceBlocks(Vec3d p_217296_1_, Vec3d p_217296_2_, BlockPos p_217296_3_, VoxelShape p_217296_4_, BlockState p_217296_5_) {
        return delegate.rayTraceBlocks(p_217296_1_, p_217296_2_, p_217296_3_, p_217296_4_, p_217296_5_);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return delegate.getWorldBorder();
    }

    @Override
    public boolean func_226663_a_(BlockState p_226663_1_, BlockPos p_226663_2_, ISelectionContext p_226663_3_) {
        return delegate.func_226663_a_(p_226663_1_, p_226663_2_, p_226663_3_);
    }

    @Override
    public boolean func_226668_i_(Entity p_226668_1_) {
        return delegate.func_226668_i_(p_226668_1_);
    }

    @Override
    public boolean func_226664_a_(AxisAlignedBB p_226664_1_) {
        return delegate.func_226664_a_(p_226664_1_);
    }

    @Override
    public boolean func_226669_j_(Entity p_226669_1_) {
        return delegate.func_226669_j_(p_226669_1_);
    }

    @Override
    public boolean func_226665_a__(Entity p_226665_1_, AxisAlignedBB p_226665_2_) {
        return delegate.func_226665_a__(p_226665_1_, p_226665_2_);
    }

    @Override
    public boolean func_226662_a_(@Nullable Entity p_226662_1_, AxisAlignedBB p_226662_2_, Set<Entity> p_226662_3_) {
        return delegate.func_226662_a_(p_226662_1_, p_226662_2_, p_226662_3_);
    }

    @Override
    public Stream<VoxelShape> func_226667_c_(@Nullable Entity p_226667_1_, AxisAlignedBB p_226667_2_, Set<Entity> p_226667_3_) {
        return delegate.func_226667_c_(p_226667_1_, p_226667_2_, p_226667_3_);
    }

    @Override
    public Stream<VoxelShape> func_226666_b_(@Nullable Entity p_226666_1_, AxisAlignedBB p_226666_2_) {
        return delegate.func_226666_b_(p_226666_1_, p_226666_2_);
    }

    @Override
    public boolean hasBlockState(BlockPos p_217375_1_, Predicate<BlockState> p_217375_2_) {
        return delegate.hasBlockState(p_217375_1_, p_217375_2_);
    }

    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState newState, int flags) {
        return delegate.setBlockState(pos, newState, flags);
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean isMoving) {
        return delegate.removeBlock(pos, isMoving);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
        return delegate.destroyBlock(pos, dropBlock);
    }

    @Override
    public boolean func_225521_a_(BlockPos p_225521_1_, boolean p_225521_2_, @Nullable Entity p_225521_3_) {
        return delegate.func_225521_a_(p_225521_1_, p_225521_2_, p_225521_3_);
    }

    @Override
    public boolean addEntity(Entity entityIn) {
        return delegate.addEntity(entityIn);
    }
}

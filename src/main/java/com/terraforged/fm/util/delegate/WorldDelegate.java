package com.terraforged.fm.util.delegate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.DimensionType;
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
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WorldDelegate<T extends IWorld> implements IWorld {
    
    private T world;

    public WorldDelegate(T world) {
        this.world = world;
    }

    public T getDelegate() {
        return world;
    }

    public void setDelegate(T world) {
        this.world = world;
    }

    @Override
    public float getCurrentMoonPhaseFactor() {
        return world.getCurrentMoonPhaseFactor();
    }

    @Override
    public float getCelestialAngle(float partialTicks) {
        return world.getCelestialAngle(partialTicks);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getMoonPhase() {
        return world.getMoonPhase();
    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return world.getPendingBlockTicks();
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return world.getPendingFluidTicks();
    }

    @Override
    public World getWorld() {
        return world.getWorld();
    }

    @Override
    public IWorldInfo getWorldInfo() {
        return world.getWorldInfo();
    }

    @Override
    public DifficultyInstance getDifficultyForLocation(BlockPos pos) {
        return world.getDifficultyForLocation(pos);
    }

    @Override
    public Difficulty getDifficulty() {
        return world.getDifficulty();
    }

    @Override
    public AbstractChunkProvider getChunkProvider() {
        return world.getChunkProvider();
    }

    @Override
    public boolean chunkExists(int chunkX, int chunkZ) {
        return world.chunkExists(chunkX, chunkZ);
    }

    @Override
    public Random getRandom() {
        return world.getRandom();
    }

    @Override
    public void func_230547_a_(BlockPos p_230547_1_, Block p_230547_2_) {
        world.func_230547_a_(p_230547_1_, p_230547_2_);
    }

    @Override
    public void playSound(PlayerEntity player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
        world.playSound(player, pos, soundIn, category, volume, pitch);
    }

    @Override
    public void addParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        world.addParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void playEvent(PlayerEntity player, int type, BlockPos pos, int data) {
        world.playEvent(player, type, pos, data);
    }

    @Override
    public int func_234938_ad_() {
        return world.func_234938_ad_();
    }

    @Override
    public void playEvent(int type, BlockPos pos, int data) {
        world.playEvent(type, pos, data);
    }

    @Override
    public Stream<VoxelShape> func_230318_c_(Entity p_230318_1_, AxisAlignedBB p_230318_2_, Predicate<Entity> p_230318_3_) {
        return world.func_230318_c_(p_230318_1_, p_230318_2_, p_230318_3_);
    }

    @Override
    public boolean checkNoEntityCollision(Entity entityIn, VoxelShape shape) {
        return world.checkNoEntityCollision(entityIn, shape);
    }

    @Override
    public BlockPos getHeight(Heightmap.Type heightmapType, BlockPos pos) {
        return world.getHeight(heightmapType, pos);
    }

    @Override
    public List<Entity> getEntitiesInAABBexcluding(Entity entityIn, AxisAlignedBB boundingBox, Predicate<? super Entity> predicate) {
        return world.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb, Predicate<? super T> filter) {
        return world.getEntitiesWithinAABB(clazz, aabb, filter);
    }

    @Override
    public <T extends Entity> List<T> getLoadedEntitiesWithinAABB(Class<? extends T> p_225316_1_, AxisAlignedBB p_225316_2_, Predicate<? super T> p_225316_3_) {
        return world.getLoadedEntitiesWithinAABB(p_225316_1_, p_225316_2_, p_225316_3_);
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        return world.getPlayers();
    }

    @Override
    public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entityIn, AxisAlignedBB bb) {
        return world.getEntitiesWithinAABBExcludingEntity(entityIn, bb);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> p_217357_1_, AxisAlignedBB p_217357_2_) {
        return world.getEntitiesWithinAABB(p_217357_1_, p_217357_2_);
    }

    @Override
    public <T extends Entity> List<T> getLoadedEntitiesWithinAABB(Class<? extends T> p_225317_1_, AxisAlignedBB p_225317_2_) {
        return world.getLoadedEntitiesWithinAABB(p_225317_1_, p_225317_2_);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(double x, double y, double z, double distance, Predicate<Entity> predicate) {
        return world.getClosestPlayer(x, y, z, distance, predicate);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(Entity entityIn, double distance) {
        return world.getClosestPlayer(entityIn, distance);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(double x, double y, double z, double distance, boolean creativePlayers) {
        return world.getClosestPlayer(x, y, z, distance, creativePlayers);
    }

    @Override
    public boolean isPlayerWithin(double x, double y, double z, double distance) {
        return world.isPlayerWithin(x, y, z, distance);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, LivingEntity target) {
        return world.getClosestPlayer(predicate, target);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, LivingEntity target, double p_217372_3_, double p_217372_5_, double p_217372_7_) {
        return world.getClosestPlayer(predicate, target, p_217372_3_, p_217372_5_, p_217372_7_);
    }

    @Override
    @Nullable
    public PlayerEntity getClosestPlayer(EntityPredicate predicate, double x, double y, double z) {
        return world.getClosestPlayer(predicate, x, y, z);
    }

    @Override
    @Nullable
    public <T extends LivingEntity> T getClosestEntityWithinAABB(Class<? extends T> entityClazz, EntityPredicate p_217360_2_, LivingEntity target, double x, double y, double z, AxisAlignedBB boundingBox) {
        return world.getClosestEntityWithinAABB(entityClazz, p_217360_2_, target, x, y, z, boundingBox);
    }

    @Override
    @Nullable
    public <T extends LivingEntity> T func_225318_b(Class<? extends T> p_225318_1_, EntityPredicate p_225318_2_, LivingEntity p_225318_3_, double p_225318_4_, double p_225318_6_, double p_225318_8_, AxisAlignedBB p_225318_10_) {
        return world.func_225318_b(p_225318_1_, p_225318_2_, p_225318_3_, p_225318_4_, p_225318_6_, p_225318_8_, p_225318_10_);
    }

    @Override
    @Nullable
    public <T extends LivingEntity> T getClosestEntity(List<? extends T> entities, EntityPredicate predicate, LivingEntity target, double x, double y, double z) {
        return world.getClosestEntity(entities, predicate, target, x, y, z);
    }

    @Override
    public List<PlayerEntity> getTargettablePlayersWithinAABB(EntityPredicate predicate, LivingEntity target, AxisAlignedBB box) {
        return world.getTargettablePlayersWithinAABB(predicate, target, box);
    }

    @Override
    public <T extends LivingEntity> List<T> getTargettableEntitiesWithinAABB(Class<? extends T> p_217374_1_, EntityPredicate p_217374_2_, LivingEntity p_217374_3_, AxisAlignedBB p_217374_4_) {
        return world.getTargettableEntitiesWithinAABB(p_217374_1_, p_217374_2_, p_217374_3_, p_217374_4_);
    }

    @Override
    @Nullable
    public PlayerEntity getPlayerByUuid(UUID uniqueIdIn) {
        return world.getPlayerByUuid(uniqueIdIn);
    }

    @Override
    @Nullable
    public IChunk getChunk(int x, int z, ChunkStatus requiredStatus, boolean nonnull) {
        return world.getChunk(x, z, requiredStatus, nonnull);
    }

    @Override
    public int getHeight(Heightmap.Type heightmapType, int x, int z) {
        return world.getHeight(heightmapType, x, z);
    }

    @Override
    public int getSkylightSubtracted() {
        return world.getSkylightSubtracted();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return world.getBiomeManager();
    }

    @Override
    public Biome getBiome(BlockPos p_226691_1_) {
        return world.getBiome(p_226691_1_);
    }

    @Override
    public Stream<BlockState> func_234939_c_(AxisAlignedBB p_234939_1_) {
        return world.func_234939_c_(p_234939_1_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getBlockColor(BlockPos blockPosIn, ColorResolver colorResolverIn) {
        return world.getBlockColor(blockPosIn, colorResolverIn);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return world.getNoiseBiome(x, y, z);
    }

    @Override
    public Biome getNoiseBiomeRaw(int x, int y, int z) {
        return world.getNoiseBiomeRaw(x, y, z);
    }

    @Override
    public boolean isRemote() {
        return world.isRemote();
    }

    @Override
    @Deprecated
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public DimensionType func_230315_m_() {
        return world.func_230315_m_();
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return world.isAirBlock(pos);
    }

    @Override
    public boolean canBlockSeeSky(BlockPos pos) {
        return world.canBlockSeeSky(pos);
    }

    @Override
    @Deprecated
    public float getBrightness(BlockPos pos) {
        return world.getBrightness(pos);
    }

    @Override
    public int getStrongPower(BlockPos pos, Direction direction) {
        return world.getStrongPower(pos, direction);
    }

    @Override
    public IChunk getChunk(BlockPos pos) {
        return world.getChunk(pos);
    }

    @Override
    public IChunk getChunk(int chunkX, int chunkZ) {
        return world.getChunk(chunkX, chunkZ);
    }

    @Override
    public IChunk getChunk(int chunkX, int chunkZ, ChunkStatus requiredStatus) {
        return world.getChunk(chunkX, chunkZ, requiredStatus);
    }

    @Override
    @Nullable
    public IBlockReader getBlockReader(int chunkX, int chunkZ) {
        return world.getBlockReader(chunkX, chunkZ);
    }

    @Override
    public boolean hasWater(BlockPos pos) {
        return world.hasWater(pos);
    }

    @Override
    public boolean containsAnyLiquid(AxisAlignedBB bb) {
        return world.containsAnyLiquid(bb);
    }

    @Override
    public int getLight(BlockPos pos) {
        return world.getLight(pos);
    }

    @Override
    public int getNeighborAwareLightSubtracted(BlockPos pos, int amount) {
        return world.getNeighborAwareLightSubtracted(pos, amount);
    }

    @Override
    @Deprecated
    public boolean isBlockLoaded(BlockPos pos) {
        return world.isBlockLoaded(pos);
    }

    @Override
    public boolean isAreaLoaded(BlockPos center, int range) {
        return world.isAreaLoaded(center, range);
    }

    @Override
    @Deprecated
    public boolean isAreaLoaded(BlockPos from, BlockPos to) {
        return world.isAreaLoaded(from, to);
    }

    @Override
    @Deprecated
    public boolean isAreaLoaded(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        return world.isAreaLoaded(fromX, fromY, fromZ, toX, toY, toZ);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float func_230487_a_(Direction p_230487_1_, boolean p_230487_2_) {
        return world.func_230487_a_(p_230487_1_, p_230487_2_);
    }

    @Override
    public WorldLightManager getLightManager() {
        return world.getLightManager();
    }

    @Override
    public int getLightFor(LightType lightTypeIn, BlockPos blockPosIn) {
        return world.getLightFor(lightTypeIn, blockPosIn);
    }

    @Override
    public int getLightSubtracted(BlockPos blockPosIn, int amount) {
        return world.getLightSubtracted(blockPosIn, amount);
    }

    @Override
    public boolean canSeeSky(BlockPos blockPosIn) {
        return world.canSeeSky(blockPosIn);
    }

    @Override
    @Nullable
    public TileEntity getTileEntity(BlockPos pos) {
        return world.getTileEntity(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return world.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return world.getFluidState(pos);
    }

    @Override
    public int getLightValue(BlockPos pos) {
        return world.getLightValue(pos);
    }

    @Override
    public int getMaxLightLevel() {
        return world.getMaxLightLevel();
    }

    @Override
    public int getHeight() {
        return world.getHeight();
    }

    @Override
    public Stream<BlockState> func_234853_a_(AxisAlignedBB p_234853_1_) {
        return world.func_234853_a_(p_234853_1_);
    }

    @Override
    public BlockRayTraceResult rayTraceBlocks(RayTraceContext context) {
        return world.rayTraceBlocks(context);
    }

    @Override
    @Nullable
    public BlockRayTraceResult rayTraceBlocks(Vector3d startVec, Vector3d endVec, BlockPos pos, VoxelShape shape, BlockState state) {
        return world.rayTraceBlocks(startVec, endVec, pos, shape, state);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return world.getWorldBorder();
    }

    @Override
    public boolean func_226663_a_(BlockState p_226663_1_, BlockPos p_226663_2_, ISelectionContext p_226663_3_) {
        return world.func_226663_a_(p_226663_1_, p_226663_2_, p_226663_3_);
    }

    @Override
    public boolean checkNoEntityCollision(Entity p_226668_1_) {
        return world.checkNoEntityCollision(p_226668_1_);
    }

    @Override
    public boolean hasNoCollisions(AxisAlignedBB p_226664_1_) {
        return world.hasNoCollisions(p_226664_1_);
    }

    @Override
    public boolean hasNoCollisions(Entity p_226669_1_) {
        return world.hasNoCollisions(p_226669_1_);
    }

    @Override
    public boolean hasNoCollisions(Entity p_226665_1_, AxisAlignedBB p_226665_2_) {
        return world.hasNoCollisions(p_226665_1_, p_226665_2_);
    }

    @Override
    public boolean func_234865_b_(Entity p_234865_1_, AxisAlignedBB p_234865_2_, Predicate<Entity> p_234865_3_) {
        return world.func_234865_b_(p_234865_1_, p_234865_2_, p_234865_3_);
    }

    @Override
    public Stream<VoxelShape> func_234867_d_(Entity p_234867_1_, AxisAlignedBB p_234867_2_, Predicate<Entity> p_234867_3_) {
        return world.func_234867_d_(p_234867_1_, p_234867_2_, p_234867_3_);
    }

    @Override
    public Stream<VoxelShape> getCollisionShapes(Entity p_226666_1_, AxisAlignedBB p_226666_2_) {
        return world.getCollisionShapes(p_226666_1_, p_226666_2_);
    }

    @Override
    public Stream<VoxelShape> func_241457_a_(Entity p_241457_1_, AxisAlignedBB p_241457_2_, BiPredicate<BlockState, BlockPos> p_241457_3_) {
        return world.func_241457_a_(p_241457_1_, p_241457_2_, p_241457_3_);
    }

    @Override
    public boolean hasBlockState(BlockPos pos, Predicate<BlockState> state) {
        return world.hasBlockState(pos, state);
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags, int recursionLeft) {
        return world.setBlockState(pos, state, flags, recursionLeft);
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState newState, int flags) {
        return world.setBlockState(pos, newState, flags);
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean isMoving) {
        return world.removeBlock(pos, isMoving);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
        return world.destroyBlock(pos, dropBlock);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, Entity entity) {
        return world.destroyBlock(pos, dropBlock, entity);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, Entity entity, int recursionLeft) {
        return world.destroyBlock(pos, dropBlock, entity, recursionLeft);
    }

    @Override
    public boolean addEntity(Entity entityIn) {
        return world.addEntity(entityIn);
    }
}

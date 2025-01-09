package net.satisfy.meadow.core.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.meadow.core.registry.EntityTypeRegistry;
import net.satisfy.meadow.core.registry.ObjectRegistry;
import net.satisfy.meadow.core.util.MeadowIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WoolyCowEntity extends Animal implements Shearable, VariantHolder<WoolyCowVar> {
    private static final EntityDataAccessor<Boolean> IS_SHEARED = SynchedEntityData.defineId(WoolyCowEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(WoolyCowEntity.class, EntityDataSerializers.INT);

    private static final ResourceLocation COW_LOOT_TABLE = new ResourceLocation("entities/cow");

    private int eatGrassTimer;
    private EatBlockGoal eatGrassGoal;

    public WoolyCowEntity(EntityType<WoolyCowEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected @NotNull ResourceLocation getDefaultLootTable() {
        if (isSheared()) return COW_LOOT_TABLE;

        ResourceLocation location = BuiltInRegistries.ITEM.getKey(getVariant().getWool());
        String s = location.getPath().replace("_wool", "");

        return new MeadowIdentifier("entities/wooly_cow/" + s);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS)) {
            if (!this.level().isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);
                itemStack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        } else if (itemStack.is(ObjectRegistry.WOODEN_BUCKET.get()) && !this.isBaby()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, getVariant().getBucket().getDefaultInstance());
            player.setItemInHand(hand, itemStack2);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(@NotNull SoundSource shearedSoundCategory) {
        this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, shearedSoundCategory, 1.0f, 1.0f);
        this.setSheared(true);
        int i = 1 + this.random.nextInt(3);
        for (int j = 0; j < i; ++j) {
            ItemEntity itemEntity = this.spawnAtLocation(getVariant().getWool(), 1);
            if (itemEntity == null) continue;
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Sheared", this.isSheared());
        nbt.putInt("Variant", getTypeVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setSheared(nbt.getBoolean("Sheared"));
        setTypeVariant(nbt.getInt("Variant"));
    }

    @Override
    public void ate() {
        super.ate();
        this.setSheared(false);
        if (this.isBaby()) {
            this.ageUp(60);
        }
    }

    @Override
    protected void customServerAiStep() {
        this.eatGrassTimer = this.eatGrassGoal.getEatAnimationTick();
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
        }
        super.aiStep();
    }

    public void setSheared(boolean sheared) {
        this.entityData.set(IS_SHEARED, sheared);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_SHEARED, false);
        entityData.define(DATA_ID_TYPE_VARIANT, 0);
    }

    public boolean isSheared() {
        return entityData.get(IS_SHEARED);
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.EAT_GRASS) {
            this.eatGrassTimer = 40;
        } else {
            super.handleEntityEvent(status);
        }
    }

    public float getNeckAngle(float delta) {
        if (this.eatGrassTimer <= 0) {
            return 0.0f;
        }
        if (this.eatGrassTimer >= 4 && this.eatGrassTimer <= 36) {
            return 1.0f;
        }
        if (this.eatGrassTimer < 4) {
            return ((float) this.eatGrassTimer - delta) / 4.0f;
        }
        return -((float) (this.eatGrassTimer - 40) - delta) / 4.0f;
    }

    public float getHeadAngle(float delta) {
        if (this.eatGrassTimer > 4 && this.eatGrassTimer <= 36) {
            float f = ((float) (this.eatGrassTimer - 4) - delta) / 32.0f;
            return 0.62831855f + 0.21991149f * Mth.sin(f * 28.7f);
        }
        if (this.eatGrassTimer > 0) {
            return 0.62831855f;
        }
        return this.getXRot() * ((float) Math.PI / 180);
    }

    @Nullable
    @Override
    public WoolyCowEntity getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        WoolyCowEntity cow = EntityTypeRegistry.WOOLY_COW.get().create(serverLevel);
        if (cow == null) return null;

        RandomSource random = serverLevel.getRandom();
        WoolyCowVar var = this.getVariant();
        if (random.nextBoolean() && ageableMob instanceof WoolyCowEntity varCow) {
            var = varCow.getVariant();
        }
        cow.setVariant(var);
        return cow;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor serverLevelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {

        WoolyCowVar variant;
        if (spawnGroupData instanceof ShearableVarCowGroupData data) {
            variant = data.variant;
        } else {
            variant = WoolyCowVar.getRandomVariant(serverLevelAccessor, blockPosition(), mobSpawnType.equals(MobSpawnType.SPAWN_EGG));
            spawnGroupData = new ShearableVarCowGroupData(variant);
        }

        setVariant(variant);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    public void setVariant(WoolyCowVar variant) {
        setTypeVariant(variant.getId() & 255 | this.getTypeVariant() & -256);
    }

    @Override
    public @NotNull WoolyCowVar getVariant() {
        return WoolyCowVar.byId(getTypeVariant() & 255);
    }

    private void setTypeVariant(int i) {
        entityData.set(DATA_ID_TYPE_VARIANT, i);
    }

    private int getTypeVariant() {
        return entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public static class ShearableVarCowGroupData extends AgeableMob.AgeableMobGroupData {
        public final WoolyCowVar variant;

        public ShearableVarCowGroupData(WoolyCowVar variant) {
            super(true);
            this.variant = variant;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.eatGrassGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(5, this.eatGrassGoal);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions entityDimensions) {
        return this.isBaby() ? entityDimensions.height * 0.95F : 1.3F;
    }
}

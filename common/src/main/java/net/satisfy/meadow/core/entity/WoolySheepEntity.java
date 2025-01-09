package net.satisfy.meadow.core.entity;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.meadow.core.registry.EntityTypeRegistry;
import net.satisfy.meadow.core.registry.ObjectRegistry;
import net.satisfy.meadow.core.util.MeadowIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class WoolySheepEntity extends Animal implements Shearable {
    private static final EntityDataAccessor<Byte> DATA_WOOL_TYPE = SynchedEntityData.defineId(WoolySheepEntity.class, EntityDataSerializers.BYTE);
    private EatBlockGoal eatBlockGoal;
    private int eatAnimationTick;

    public enum SheepTexture {
        FLECKED, PATCHED, ROCKY, INKY, FUZZY, LONG_NOSED
    }

    public WoolySheepEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.setWoolType((byte) this.random.nextInt(6));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513);
    }

    protected void registerGoals() {
        this.eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, this.eatBlockGoal);
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public void customServerAiStep() {
        if (this.eatBlockGoal == null) {
            this.eatBlockGoal = new EatBlockGoal(this);
        }
        super.customServerAiStep();
    }

    @Override
    public void ate() {
        super.ate();
        if (this.isSheared()) {
            this.setSheared(false);
        }
    }


    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }
        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(b);
        }
    }

    public float getHeadEatPositionScale(float f) {
        if (this.eatAnimationTick <= 0) {
            return 0.0F;
        } else if (this.eatAnimationTick >= 4 && this.eatAnimationTick <= 36) {
            return 1.0F;
        } else {
            return this.eatAnimationTick < 4 ? ((float) this.eatAnimationTick - f) / 4.0F : -((float) (this.eatAnimationTick - 40) - f) / 4.0F;
        }
    }

    public float getHeadEatAngleScale(float f) {
        if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
            float g = ((float) (this.eatAnimationTick - 4) - f) / 32.0F;
            return 0.62831855F + 0.21991149F * Mth.sin(g * 28.7F);
        } else {
            return this.eatAnimationTick > 0 ? 0.62831855F : this.getXRot() * 0.017453292F;
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.SHEARS)) {
            if (!this.level().isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(SoundSource source) {
        if (!this.level().isClientSide && this.readyForShearing()) {
            this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
            this.setSheared(true);
            int count = 1 + this.random.nextInt(3);
            Block woolBlock = switch (this.getSheepTexture()) {
                case PATCHED -> ObjectRegistry.PATCHED_WOOL.get();
                case ROCKY -> ObjectRegistry.ROCKY_WOOL.get();
                case INKY -> ObjectRegistry.INKY_WOOL.get();
                case FUZZY -> Blocks.WHITE_WOOL;
                case LONG_NOSED -> ObjectRegistry.HIGHLAND_WOOL.get();
                default -> ObjectRegistry.FLECKED_WOOL.get();
            };
            for (int i = 0; i < count; i++) {
                Item woolItem = woolBlock.asItem();
                ItemStack woolStack = new ItemStack(woolItem);
                ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY() + 1.0D, this.getZ(), woolStack);
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F));
                this.level().addFreshEntity(itemEntity);
            }
        }
    }

    @Override
    public @NotNull ResourceLocation getDefaultLootTable() {
        return switch (this.getTextureColor()) {
            case LONG_NOSED_WOOL -> new MeadowIdentifier("entities/sheep/long_nosed_sheep");
            case FUZZY_WOOL -> new MeadowIdentifier("entities/sheep/fuzzy_sheep");
            case FLECKED_WOOL -> new MeadowIdentifier("entities/sheep/flecked_sheep");
            case PATCHED_WOOL -> new MeadowIdentifier("entities/sheep/patched_sheep");
            case ROCKY_WOOL -> new MeadowIdentifier("entities/sheep/rocky_sheep");
            case INKY_WOOL -> new MeadowIdentifier("entities/sheep/inky_sheep");
        };
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WOOL_TYPE, (byte) 0);
    }

    public void setWoolType(byte woolType) {
        this.entityData.set(DATA_WOOL_TYPE, woolType);
    }

    public byte getWoolType() {
        return (byte) (this.entityData.get(DATA_WOOL_TYPE) & 0x0F);
    }

    @Nullable
    @Override
    public WoolySheepEntity getBreedOffspring(ServerLevel level, AgeableMob mob) {
        WoolySheepEntity sheep = EntityTypeRegistry.WOOLY_SHEEP.get().create(level);
        if (sheep != null) {
            sheep.setWoolType((byte) this.random.nextInt(6));
        }
        return sheep;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor s, DifficultyInstance d, MobSpawnType m, @Nullable SpawnGroupData g, @Nullable CompoundTag c) {
        this.setWoolType((byte) this.random.nextInt(6));
        return Objects.requireNonNull(super.finalizeSpawn(s, d, m, g, c));
    }

    public SheepTexture getSheepTexture() {
        return switch (this.getWoolType()) {
            case 1 -> SheepTexture.PATCHED;
            case 2 -> SheepTexture.ROCKY;
            case 3 -> SheepTexture.INKY;
            case 4 -> SheepTexture.LONG_NOSED;
            case 5 -> SheepTexture.FUZZY;
            default -> SheepTexture.FLECKED;
        };
    }

    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    public boolean isSheared() {
        return (this.entityData.get(DATA_WOOL_TYPE) & 16) != 0;
    }

    public void setSheared(boolean bl) {
        byte b = this.entityData.get(DATA_WOOL_TYPE);
        if (bl) {
            this.entityData.set(DATA_WOOL_TYPE, (byte) (b | 16));
        } else {
            this.entityData.set(DATA_WOOL_TYPE, (byte) (b & -17));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag c) {
        super.addAdditionalSaveData(c);
        c.putBoolean("Sheared", this.isSheared());
        c.putByte("WoolType", this.getWoolType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag c) {
        super.readAdditionalSaveData(c);
        this.setSheared(c.getBoolean("Sheared"));
        this.setWoolType(c.getByte("WoolType"));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    public enum SheepColor {
        FLECKED_WOOL(new float[]{1.0F, 1.0F, 1.0F, 0.0F}),
        PATCHED_WOOL(new float[]{1.0F, 1.0F, 1.0F, 0.0F}),
        ROCKY_WOOL(new float[]{1.0F, 1.0F, 1.0F, 0.0F}),
        INKY_WOOL(new float[]{1.0F, 1.0F, 1.0F, 0.0F}),
        FUZZY_WOOL(new float[]{1.0F, 1.0F, 1.0F, 0.0F}),
        LONG_NOSED_WOOL(new float[]{1.0F, 1.0F, 1.0F, 0.0F});

        private final float[] textureDiffuseColors;
        SheepColor(float[] colors) {
            this.textureDiffuseColors = colors;
        }
        public float[] getTextureDiffuseColors() {
            return this.textureDiffuseColors;
        }
    }

    public SheepColor getTextureColor() {
        return switch (this.getWoolType()) {
            case 1 -> SheepColor.PATCHED_WOOL;
            case 2 -> SheepColor.ROCKY_WOOL;
            case 3 -> SheepColor.INKY_WOOL;
            case 4 -> SheepColor.LONG_NOSED_WOOL;
            case 5 -> SheepColor.FUZZY_WOOL;
            default -> SheepColor.FLECKED_WOOL;
        };
    }

    public void setSheepTexture(SheepTexture texture) {
        setWoolType(switch (texture) {
            case PATCHED -> (byte)1;
            case ROCKY -> (byte)2;
            case INKY -> (byte)3;
            case LONG_NOSED -> (byte)4;
            case FUZZY -> (byte)5;
            default -> (byte)0;
        });
    }
}

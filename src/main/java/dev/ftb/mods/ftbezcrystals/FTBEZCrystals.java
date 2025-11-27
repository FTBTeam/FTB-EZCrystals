package dev.ftb.mods.ftbezcrystals;

import dev.ftb.mods.ftbezcrystals.datamap.EZCrystalsDataMap;
import dev.ftb.mods.ftbezcrystals.datamap.HarvestableCrystal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(FTBEZCrystals.MOD_ID)
public class FTBEZCrystals {
    public static final String MOD_ID = "ftbezcrystals";

    public static final Logger LOGGER = LoggerFactory.getLogger(FTBEZCrystals.class);

    public FTBEZCrystals() {
        MinecraftForge.EVENT_BUS.addListener(this::clusterInteract);
        MinecraftForge.EVENT_BUS.addListener(this::registerReloadListeners);
    }

    private void clusterInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() == InteractionHand.OFF_HAND)
            return;

        harvestCrystal(event.getLevel(), event.getPos(), event.getItemStack(), event.getEntity());
    }

    public static void harvestCrystal(Level level, BlockPos pos, ItemStack heldStack, Player player) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        HarvestableCrystal harvestableCrystal = EZCrystalsDataMap.INSTANCE.get(block);

        if (harvestableCrystal != null && state.hasProperty(BlockStateProperties.FACING)) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            BlockState attachedState = level.getBlockState(pos.relative(direction.getOpposite()));

            if (harvestableCrystal.buddingBlocks().stream().anyMatch(attachedState::is)) {
                BlockState budState = harvestableCrystal.bud().defaultBlockState()
                        .setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING));
                Block.dropResources(state, level, pos, null, player, heldStack);

                SoundType soundtype = state.getSoundType(level, pos, player);
                level.playSound(null, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS,
                        (soundtype.getVolume() + 1.0F) / 2.0F,
                        soundtype.getPitch() * 0.8F);

                level.setBlock(pos, budState, Block.UPDATE_ALL);
                spawnParticles(level, pos, 10, 1.0, 1.0, true, ParticleTypes.HAPPY_VILLAGER);
                player.swing(InteractionHand.MAIN_HAND, true);
            }
        }
    }

    private void registerReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new EZCrystalsDataMap.ReloadListener(event.getRegistryAccess()));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void spawnParticles(LevelAccessor level, BlockPos pos, int count, double xzSpread, double ySpread, boolean allowInAir, ParticleOptions particle) {
        RandomSource randomsource = level.getRandom();

        for (int i = 0; i < count; ++i) {
            double d0 = randomsource.nextGaussian() * 0.02;
            double d1 = randomsource.nextGaussian() * 0.02;
            double d2 = randomsource.nextGaussian() * 0.02;
            double d3 = 0.5 - xzSpread;
            double d4 = (double) pos.getX() + d3 + randomsource.nextDouble() * xzSpread * 2.0;
            double d5 = (double) pos.getY() + randomsource.nextDouble() * ySpread;
            double d6 = (double) pos.getZ() + d3 + randomsource.nextDouble() * xzSpread * 2.0;
            if (allowInAir || !level.getBlockState(BlockPos.containing(d4, d5, d6).below()).isAir()) {
                level.addParticle(particle, d4, d5, d6, d0, d1, d2);
            }
        }
    }
}

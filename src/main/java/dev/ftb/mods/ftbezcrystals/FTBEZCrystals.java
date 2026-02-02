package dev.ftb.mods.ftbezcrystals;

import dev.ftb.mods.ftbezcrystals.datamap.EZDataMaps;
import dev.ftb.mods.ftbezcrystals.datamap.HarvestableCrystal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(FTBEZCrystals.MOD_ID)
public class FTBEZCrystals {
    public static final String MOD_ID = "ftbezcrystals";

    private static final Logger LOGGER = LoggerFactory.getLogger(FTBEZCrystals.class);

    public FTBEZCrystals(IEventBus eventBus, ModContainer container) {
        eventBus.addListener(EZDataMaps::register);

        NeoForge.EVENT_BUS.addListener(this::clusterInteract);
    }

    private void clusterInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() == InteractionHand.OFF_HAND || event.getEntity().isCrouching())
            return;

        if (harvestCrystal(event.getLevel(), event.getPos(), event.getItemStack(), event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean harvestCrystal(Level level, BlockPos pos, ItemStack heldStack, Player player) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        HarvestableCrystal harvestableCrystal = block.builtInRegistryHolder().getData(EZDataMaps.HARVESTABLE_CRYSTALS);
        if (harvestableCrystal != null && state.hasProperty(BlockStateProperties.FACING)) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            BlockState attachedState = level.getBlockState(pos.relative(direction.getOpposite()));

            if (harvestableCrystal.buddingBlocks().stream().anyMatch(attachedState::is)) {
                BlockState budState = harvestableCrystal.bud().defaultBlockState();
                budState = budState.setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING));
                Block.dropResources(state, level, pos, null, player, heldStack);

                SoundType soundtype = state.getSoundType(level, pos, player);
                level.playSound(null, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS,
                        (soundtype.getVolume() + 1.0F) / 2.0F,
                        soundtype.getPitch() * 0.8F);

                level.setBlock(pos, budState, 3);
                ParticleUtils.spawnParticles(level, pos, 10, 1.0, 1.0, true, ParticleTypes.HAPPY_VILLAGER);
                player.swing(InteractionHand.MAIN_HAND, true);

                return true;
            }
        }
        return false;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

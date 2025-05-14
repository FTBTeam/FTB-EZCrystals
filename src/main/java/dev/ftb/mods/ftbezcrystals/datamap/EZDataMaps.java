package dev.ftb.mods.ftbezcrystals.datamap;

import dev.ftb.mods.ftbezcrystals.FTBEZCrystals;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class EZDataMaps {
    public static final DataMapType<Block, HarvestableCrystal> HARVESTABLE_CRYSTALS = DataMapType.builder(
            FTBEZCrystals.id("harvestable_crystals"), Registries.BLOCK, HarvestableCrystal.CODEC).synced(HarvestableCrystal.CODEC, false).build();

    public static void register(final RegisterDataMapTypesEvent event) {
        event.register(HARVESTABLE_CRYSTALS);
    }
}

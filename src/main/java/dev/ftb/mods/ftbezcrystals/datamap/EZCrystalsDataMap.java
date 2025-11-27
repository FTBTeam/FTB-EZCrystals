package dev.ftb.mods.ftbezcrystals.datamap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.ftb.mods.ftbezcrystals.FTBEZCrystals;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public enum EZCrystalsDataMap {
    INSTANCE;

    private final Map<ResourceLocation, HarvestableCrystal> map = new HashMap<>();

    public void clear() {
        map.clear();
    }

    public void add(ResourceLocation blockid, HarvestableCrystal rec) {
        map.put(blockid, rec);
    }

    public HarvestableCrystal get(Block block) {
        return map.get(ForgeRegistries.BLOCKS.getKey(block));
    }

    public static class ReloadListener extends SimpleJsonResourceReloadListener {
        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        private final RegistryAccess registryAccess;

        public ReloadListener(RegistryAccess registryAccess) {
            super(GSON, "harvestable_crystals");

            this.registryAccess = registryAccess;
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
            INSTANCE.clear();

            map.values().forEach(json -> json.getAsJsonObject().entrySet().forEach(entry -> {
               ResourceLocation blockId = ResourceLocation.tryParse(entry.getKey());
               if (blockId != null && ForgeRegistries.BLOCKS.containsKey(blockId)) {
                   HarvestableCrystal.fromJson(entry.getValue(), registryAccess)
                           .ifPresent(rec -> INSTANCE.add(blockId, rec));
               } else {
                   FTBEZCrystals.LOGGER.error("ignoring unknown block '{}'", entry.getKey());
               }
            }));
        }
    }
}

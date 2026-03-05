package net.sweningteam.fabric.datagen.assets;

import com.google.common.collect.ImmutableList;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.registry.ModBlocks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModBlockModelDefinationsProvider implements DataProvider {
    private static ImmutableList<Block> DONT_CREATE_ITEM_MODEL;

    public ModBlockModelDefinationsProvider(PackOutput output){
        this.itemsPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK,"models/item");
    }
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput.PathProvider itemsPath;
    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<ResourceLocation,JsonObject> toWrite = new LinkedHashMap<>();
        for(var sup : ModBlocks.BLOCKS){
            Block block = sup.get();
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if(!DONT_CREATE_ITEM_MODEL.contains(block)){
                toWrite.put(id,blockItemModelHelper(id));
            }

        }
        CompletableFuture<?>[] futures = toWrite.entrySet().stream()
                .map(e->writeJsonAsync(output,e.getKey(),e.getValue())).toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }
    private CompletableFuture<?> writeJsonAsync(CachedOutput cachedOutput,ResourceLocation id,JsonObject json){
        Path path = itemsPath.json(id);
        byte[] bytes = (GSON.toJson(json)+"\n").getBytes(StandardCharsets.UTF_8);
        var hash = Hashing.sha1().hashBytes(bytes);

        return CompletableFuture.runAsync(() -> {
            try {
                cachedOutput.writeIfNeeded(path, bytes, hash);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String getName() {
        return NotEnoughDragons.MOD_ID +" Block Model Definitions";
    }
    private static String blockPath(ResourceLocation blockID){
        return NotEnoughDragons.MOD_ID+":block/"+blockID.getPath();
    }
    private static JsonObject blockItemModelHelper(ResourceLocation blockID){
        JsonObject root = new JsonObject();
        root.addProperty("parent",blockPath(blockID));
        return root;
    }
    static {
        DONT_CREATE_ITEM_MODEL = ImmutableList.of(ModBlocks.NIGHT_FURY_SCALES.get());
    }
}

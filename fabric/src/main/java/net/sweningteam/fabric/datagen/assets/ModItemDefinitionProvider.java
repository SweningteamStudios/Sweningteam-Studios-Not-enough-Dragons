package net.sweningteam.fabric.datagen.assets;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.registry.ModItems;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModItemDefinitionProvider implements DataProvider {
    public ModItemDefinitionProvider(PackOutput output){
        this.itemsPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK,"items");
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput.PathProvider itemsPath;

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<ResourceLocation, JsonObject> toWrite = new LinkedHashMap<>();

        for(var sup : ModItems.ITEMS){
            Item item = sup.get();
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            toWrite.put(id,itemHelper(id));
        }
        CompletableFuture<?>[] futures = toWrite.entrySet().stream()
                .map(e->writeJsonAsync(output,e.getKey(),e.getValue())).toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }

    private CompletableFuture<?> writeJsonAsync(CachedOutput cachedOutput, ResourceLocation id, JsonObject json) {
        Path path = itemsPath.json(id); // assets/<ns>/items/<path>.json
        byte[] bytes = (GSON.toJson(json) + "\n").getBytes(StandardCharsets.UTF_8);
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
        return NotEnoughDragons.MOD_ID +" Item Definitions";
    }

    private static String ItemPath(ResourceLocation itemID){
        return NotEnoughDragons.MOD_ID+":item/"+ itemID.getPath();
    }

    private static JsonObject itemHelper(ResourceLocation itemID){
        JsonObject root = new JsonObject();
        JsonObject model = new JsonObject();

        model.addProperty("type","minecraft:model");
        model.addProperty("model",ItemPath(itemID));
        root.add("model",model);
        return root;
    }
}

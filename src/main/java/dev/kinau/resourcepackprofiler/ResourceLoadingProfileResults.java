package dev.kinau.resourcepackprofiler;

import dev.kinau.resourcepackprofiler.expander.ReloadStateExpander;
import net.minecraft.client.PeriodicNotificationManager;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.GpuWarnlistManager;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ResultField;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ResourceLoadingProfileResults implements ProfileResults {

    private final List<ReloadStateExpander> states;
    private final long totalTime;
    private final List<ResultField> results;

    public ResourceLoadingProfileResults(List<ReloadStateExpander> states) {
        this.states = states;
        this.totalTime = getTotalTime();
        this.results = states.stream().map(this::createResultField).sorted(Comparator.<ResultField>comparingDouble(value -> value.percentage).reversed()).collect(Collectors.toList());
    }

    private long getTotalTime() {
        return states.stream().mapToLong(value -> TimeUnit.NANOSECONDS.toMillis(value.preparationNanos().get()) + TimeUnit.NANOSECONDS.toMillis(value.reloadNanos().get())).sum();
    }

    private ResultField createResultField(ReloadStateExpander reloadState) {
        long prepMillis = TimeUnit.NANOSECONDS.toMillis(reloadState.preparationNanos().get());
        long reloadMillis = TimeUnit.NANOSECONDS.toMillis(reloadState.reloadNanos().get());
        long sumMillis = prepMillis + reloadMillis;
        String name = reloadState.name();
        Class<?> clazz = getClass(GameRenderer.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(LanguageManager.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(LanguageManager.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(TextureManager.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(SoundManager.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(SplashManager.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(FontManager.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(ModelManager.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(EntityModelSet.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(BlockEntityRenderDispatcher.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(ItemRenderer.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(ParticleEngine.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(BlockRenderDispatcher.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(GuiSpriteManager.class.getPackageName() + "." + name);
        if (clazz == null) clazz = getClass(PeriodicNotificationManager.class.getPackageName() + "." + name);

        double percent = (sumMillis / (double)totalTime) * 100.0;
        return new ResultField(getHumanReadableName(clazz, name), percent, percent, sumMillis);
    }

    private Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignore) {}
        return null;
    }

    private String getHumanReadableName(Class<?> clazz, String fallback) {
        if (clazz == LanguageManager.class)
            return "Language Manager";
        else if (clazz == TextureManager.class)
            return "Texture Manager";
        else if (clazz == SoundManager.class)
            return "Sound Manager";
        else if (clazz == SplashManager.class)
            return "Splash Manager";
        else if (clazz == FontManager.class)
            return "Font Manager";
        else if (clazz == GrassColorReloadListener.class)
            return "Grass Color Reload Listener";
        else if (clazz == FoliageColorReloadListener.class)
            return "Foliage Color Reload Listener";
        else if (clazz == ModelManager.class)
            return "Model Manager";
        else if (clazz == EntityModelSet.class)
            return "Entity Model Set";
        else if (clazz == BlockEntityRenderDispatcher.class)
            return "Block Entity Renderer";
        else if (clazz == MapDecorationTextureManager.class)
            return "Map Decoration Texture Manager";
        else if (clazz == ItemRenderer.class)
            return "Item Renderer";
        else if (clazz == BlockRenderDispatcher.class)
            return "Block Renderer";
        else if (clazz == EntityRenderDispatcher.class)
            return "Entity Renderer";
        else if (clazz == LevelRenderer.class)
            return "Glowing/Transparency Shader Loader";
        else if (clazz == ParticleEngine.class)
            return "Particle Engine";
        else if (clazz == PaintingTextureManager.class)
            return "Painting Texture Manager";
        else if (clazz == MobEffectTextureManager.class)
            return "Mob Effect Texture Manager";
        else if (clazz == GuiSpriteManager.class)
            return "Gui Sprite Manager";
        else if (clazz == GpuWarnlistManager.class)
            return "Gpu Warnlist Manager";
        else if (clazz == PeriodicNotificationManager.class)
            return "Periodic Notification Manager";
        else if (clazz == EquipmentAssetManager.class)
            return "Equipment Asset Manager";
        else if (clazz == CloudRenderer.class)
            return "Cloud Renderer";

        else if (clazz != null)
            return clazz.getName();
        return fallback;
    }

    @Override
    public List<ResultField> getTimes(String string) {
        return results;
    }

    @Override
    public boolean saveResults(Path path) {
        return false;
    }

    @Override
    public long getStartTimeNano() {
        return 0;
    }

    @Override
    public int getStartTimeTicks() {
        return 0;
    }

    @Override
    public long getEndTimeNano() {
        return 0;
    }

    @Override
    public int getEndTimeTicks() {
        return 0;
    }

    @Override
    public String getProfilerResults() {
        return null;
    }
}

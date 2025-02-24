package dev.kinau.resourcepackprofiler.mixin;

import dev.kinau.resourcepackprofiler.expander.ReloadStateExpander;
import net.minecraft.server.packs.resources.ProfiledReloadInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.atomic.AtomicLong;

@Mixin(ProfiledReloadInstance.State.class)
public class ReloadStateMixin implements ReloadStateExpander {

    @Shadow @Final String name;
    @Shadow @Final AtomicLong preparationNanos;
    @Shadow @Final AtomicLong reloadNanos;

    @Override
    public String name() {
        return name;
    }

    @Override
    public AtomicLong preparationNanos() {
        return preparationNanos;
    }

    @Override
    public AtomicLong reloadNanos() {
        return reloadNanos;
    }
}

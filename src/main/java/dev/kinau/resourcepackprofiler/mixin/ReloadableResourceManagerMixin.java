package dev.kinau.resourcepackprofiler.mixin;

import dev.kinau.resourcepackprofiler.ResourcePackProfilerMod;
import dev.kinau.resourcepackprofiler.config.RPPConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {

    @Shadow private CloseableResourceManager resources;

    @Shadow @Final private List<PreparableReloadListener> listeners;

    @Inject(method = "createReload", at = @At("RETURN"), cancellable = true)
    private void createReloadReturn(Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture, List<PackResources> list, CallbackInfoReturnable<ReloadInstance> cir) {
        RPPConfig config = AutoConfig.getConfigHolder(RPPConfig.class).get();
        if (config == null) return;
        boolean enabled = config.enabled();

        if (enabled && ResourcePackProfilerMod.started) {
            // use ProfiledReloadInstance
            cir.setReturnValue(SimpleReloadInstance.create(resources, listeners, executor, executor2, completableFuture, true));
        }
    }
}

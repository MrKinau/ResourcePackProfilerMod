package dev.kinau.resourcepackprofiler.mixin;

import dev.kinau.resourcepackprofiler.ResourceLoadingProfileResults;
import dev.kinau.resourcepackprofiler.expander.ReloadStateExpander;
import dev.kinau.resourcepackprofiler.screen.PieChartScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ProfiledReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ProfiledReloadInstance.class)
public class ProfiledResourceReloadMixin {

    @Inject(method = "finish", at = @At("HEAD"))
    private void finish(List<ProfiledReloadInstance.State> summaries, CallbackInfoReturnable<List<ProfiledReloadInstance.State>> cir) {
        List<ReloadStateExpander> results = new ArrayList<>();

        for (ProfiledReloadInstance.State summary : summaries) {
            results.add((ReloadStateExpander)(Object)summary);
        }
        if (Minecraft.getInstance().screen == null) {
            Minecraft.getInstance().setScreen(new PieChartScreen(new ResourceLoadingProfileResults(results)));
        }
    }
}

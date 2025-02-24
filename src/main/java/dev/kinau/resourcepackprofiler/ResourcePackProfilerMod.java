package dev.kinau.resourcepackprofiler;

import dev.kinau.resourcepackprofiler.config.RPPConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class ResourcePackProfilerMod implements ModInitializer {
	public static boolean started = false;

	@Override
	public void onInitialize() {
		AutoConfig.register(RPPConfig.class, GsonConfigSerializer::new);

		ClientLifecycleEvents.CLIENT_STARTED.register(minecraft -> {
			started = true;
		});
	}
}
package dev.kinau.resourcepackprofiler.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "resourcepackprofiler")
@Accessors(fluent = true)
@Getter
public class RPPConfig implements ConfigData {
    private boolean enabled = true;
}

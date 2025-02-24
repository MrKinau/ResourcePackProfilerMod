package dev.kinau.resourcepackprofiler.expander;

import java.util.concurrent.atomic.AtomicLong;

public interface ReloadStateExpander {
    String name();
    AtomicLong preparationNanos();
    AtomicLong reloadNanos();
}

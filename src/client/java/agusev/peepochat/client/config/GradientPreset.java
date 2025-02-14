package agusev.peepochat.client.config;

import net.minecraft.item.Item;

import java.awt.*;

public enum GradientPreset {
    SUNSET("Sunset"),
    OCEAN("Ocean"),
    FOREST("Forest");

    public final String name;

    GradientPreset(String name) {
        this.name = name;
    }
}

package lol.hub.mapcolors;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.material.MapColor;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {

        var colors = Arrays.stream(MapColor.MATERIAL_COLORS)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(value -> value.id))
                .toList();

        StringBuilder csv = new StringBuilder();
        for (MapColor color : colors) {
            Color c = new Color(color.col);
            csv
                    .append(color.id)
                    .append(",")
                    .append(c.getRed())
                    .append(",")
                    .append(c.getGreen())
                    .append(",")
                    .append(c.getBlue())
                    .append(",")
                    .append(color.id > 0 ? 255 : 0)
                    .append(",")
                    .append(String.format("%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue()))
                    .append("\n");
        }
        csv.append("\n");

        try {
            Files.write(Path.of("colors.csv"), csv.toString().getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.exit(0);
    }
}

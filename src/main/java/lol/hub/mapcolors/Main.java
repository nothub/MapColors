package lol.hub.mapcolors;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.material.MapColor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {

        var table = Arrays.stream(MapColor.MATERIAL_COLORS)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(value -> value.id))
                .toList();

        StringBuilder csv = new StringBuilder();
        for (MapColor color : table) {
            var rgba = 0xff000000 | color.col;
            var r = (rgba >> 16) & 0xFF;
            var g = (rgba >> 8) & 0xFF;
            var b = rgba & 0xFF;
            var a = color.id > 0 ? 255 : 0;
            csv
                    .append(color.id)
                    .append(",")
                    .append(r)
                    .append(",")
                    .append(g)
                    .append(",")
                    .append(b)
                    .append(",")
                    .append(a)
                    .append(",")
                    .append(String.format("%02X%02X%02X", r, g, b))
                    .append("\n");
        }

        try {
            Files.write(Path.of("colors.csv"), csv.toString().getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.err.println("We done saving the data, shutting down...");
        System.exit(0);
    }
}

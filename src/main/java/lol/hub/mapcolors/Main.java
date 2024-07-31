package lol.hub.mapcolors;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.material.MapColor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {

        var bases = Arrays.stream(MapColor.MATERIAL_COLORS)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(value -> value.id))
                .toList();

        var table = new ArrayList<String>();

        for (MapColor base : bases) {
            for (MapColor.Brightness shade : MapColor.Brightness.VALUES) {
                int id = base.id * 4 + shade.id;
                var rgba = base.calculateRGBColor(shade);
                int r = rgba & 0xFF;
                int g = (rgba >> 8) & 0xFF;
                int b = (rgba >> 16) & 0xFF;
                int a = base.id > 3 ? 255 : 0;
                String line = String.format("%s,%s,%s,%s,%s,%02X%02X%02X", id, r, g, b, a, r, g, b);
                table.add(line);
            }
        }

        try {
            Files.write(Path.of("colors.csv"),
                    String.join("\n", table).getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.err.println("We done saving the data, shutting down...");
        System.exit(0);
    }
}

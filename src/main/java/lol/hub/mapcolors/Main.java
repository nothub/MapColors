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

    private static final String htmlTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
                <link rel="icon" href="data:,">
                <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes">
                <style>
                table, th, td {
                  border: 1px solid;
                }
                td {
                  padding: 0.15em 0.75em 0.15em 0.75em;
                  text-align: center;
                }
                p {
                  margin: 0;
                }
                </style>
            </head>
            <body>
              <table>
                <thead>
                  <tr>
                    <th>id</th>
                    <th>r</th>
                    <th>g</th>
                    <th>b</th>
                    <th>a</th>
                    <th>hex</th>
                  </tr>
                </thead>
                <tbody>%s
                </tbody>
              </table>
            </body>
            </html>
            """;

    @Override
    public void onInitialize() {

        var bases = Arrays.stream(MapColor.MATERIAL_COLORS)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(value -> value.id))
                .toList();

        var csvEntries = new ArrayList<String>();
        var htmlEntries = new ArrayList<String>();

        for (MapColor base : bases) {
            for (MapColor.Brightness shade : MapColor.Brightness.VALUES) {
                int id = base.id * 4 + shade.id;
                var rgba = base.calculateARGBColor(shade);
                int r = rgba & 0xFF;
                int g = (rgba >> 8) & 0xFF;
                int b = (rgba >> 16) & 0xFF;
                int a = base.id == 0 ? 0 : 255;
                var hex = String.format("%02X%02X%02X", r, g, b);
                csvEntries.add(String.format("%s,%s,%s,%s,%s,%s", id, r, g, b, a, hex));
                htmlEntries.add(String.format("""
            <tr>
              <td>%s</td>
              <td>%s</td>
              <td>%s</td>
              <td>%s</td>
              <td>%s</td>
              <td style="background-color: #%s"><p style="color: #%s; filter: invert(1);">#%s</p></td>
            </tr>""", id, r, g, b, a, hex, hex, hex));
            }
        }

        try {
            Files.write(Path.of("colors.csv"),
                    (String.join("\n", csvEntries) + "\n").getBytes());
            Files.write(Path.of("colors.html"),
                    (String.join("\n", String.format(htmlTemplate, String.join("\n", htmlEntries))) + "\n").getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.err.println("We done saving the data, shutting down...");
        System.exit(0);
    }
}

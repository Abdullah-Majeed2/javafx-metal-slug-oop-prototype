import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;

public final class UITheme {
    private static String family = "Arial"; // Fallback if custom font not found

    private UITheme() {}

    public static void init() {
        String[] candidates = new String[] {
                "/Assets/Fonts/etal-slug.ttf",
                "/Assets/Fonts/ETAL-SLUG.ttf",
                "/Assets/Fonts/metal-slug.ttf",
                "/resources/Assets/Fonts/etal-slug.ttf",
                "/resources/Assets/Fonts/ETAL-SLUG.ttf",
                "/resources/Assets/Fonts/metal-slug.ttf"
        };

        for (String path : candidates) {
            try (InputStream is = UITheme.class.getResourceAsStream(path)) {
                if (is == null) continue;
                Font loaded = Font.loadFont(is, 12);
                if (loaded != null) {
                    family = loaded.getFamily();
                    break;
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static Font font(double size) {
        return Font.font(family, size);
    }

    public static Font boldFont(double size) {
        return Font.font(family, FontWeight.BOLD, size);
    }
}

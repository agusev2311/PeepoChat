package agusev.peepochat.client.config;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class GradientTextExample {
    public static Text getGradientText(String text, int startColor, int endColor) {
        MutableText builder = Text.empty();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            int color = interpolateColor(startColor, endColor, (float) i / (length - 1));
            builder.append(Text.literal(String.valueOf(text.charAt(i)))
                    .setStyle(Style.EMPTY.withColor(color)));
        }

        return builder;
    }

    private static int interpolateColor(int start, int end, float ratio) {
        int sr = (start >> 16) & 0xFF, sg = (start >> 8) & 0xFF, sb = start & 0xFF;
        int er = (end >> 16) & 0xFF, eg = (end >> 8) & 0xFF, eb = end & 0xFF;

        int r = (int) (sr + (er - sr) * ratio);
        int g = (int) (sg + (eg - sg) * ratio);
        int b = (int) (sb + (eb - sb) * ratio);

        return (r << 16) | (g << 8) | b;
    }
}

package xyz.amymialee.mialeemisc.util;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import xyz.amymialee.mialeemisc.MialeeMisc;

import java.util.List;

public class MialeeText {
    public Text withColor(Text text, int color) {
        Style style = text.getStyle().withColor(color);
        List<Text> styled = text.getWithStyle(style);
        if (styled.size() > 0) {
            return styled.get(0);
        }
        MialeeMisc.LOGGER.error("Failed to set color of text: " + text.getString() + " to color: " + color);
        return text;
    }
}
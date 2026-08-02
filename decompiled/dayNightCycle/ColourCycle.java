/*
 * Decompiled with CFR 0.152.
 */
package dayNightCycle;

import java.util.LinkedHashMap;
import java.util.Map;
import toolbox.Colour;

public class ColourCycle {
    private final Colour nightColour;
    private final Colour dayColour;
    private final Colour dawnColour;
    private final Colour duskColour;
    private final float midnightEnd;
    private final float middayStart;
    private final float middayEnd;
    private final Map<Float, Colour> colours;

    public ColourCycle(Colour nightColour, Colour dayColour, Colour dawnColour, Colour duskColour, float midnightEnd, float middayStart, float middayEnd) {
        this.nightColour = nightColour;
        this.dayColour = dayColour;
        this.dawnColour = dawnColour;
        this.duskColour = duskColour;
        this.midnightEnd = midnightEnd / 24.0f;
        this.middayStart = middayStart / 24.0f;
        this.middayEnd = middayEnd / 24.0f;
        this.colours = this.create();
    }

    public Colour getColour(float time) {
        Map.Entry<Float, Colour> prev = null;
        Map.Entry<Float, Colour> next = null;
        for (Map.Entry<Float, Colour> frame : this.colours.entrySet()) {
            if (frame.getKey().floatValue() > time) {
                next = frame;
                break;
            }
            prev = frame;
        }
        float blend = (time - ((Float)prev.getKey()).floatValue()) / (((Float)next.getKey()).floatValue() - ((Float)prev.getKey()).floatValue());
        return Colour.interpolateColours(prev.getValue(), next.getValue(), blend, null);
    }

    private Map<Float, Colour> create() {
        LinkedHashMap<Float, Colour> map = new LinkedHashMap<Float, Colour>();
        map.put(Float.valueOf(0.0f), this.nightColour);
        map.put(Float.valueOf(this.midnightEnd), this.nightColour);
        map.put(Float.valueOf(this.midnightEnd + (this.middayStart - this.midnightEnd) * 0.5f), this.dawnColour);
        map.put(Float.valueOf(this.middayStart), this.dayColour);
        map.put(Float.valueOf(this.middayEnd), this.dayColour);
        map.put(Float.valueOf(this.middayEnd + (1.0f - this.middayEnd) * 0.5f), this.duskColour);
        map.put(Float.valueOf(1.0f), this.nightColour);
        return map;
    }
}


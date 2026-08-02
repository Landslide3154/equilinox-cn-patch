/*
 * Decompiled with CFR 0.152.
 */
package environmentWarning;

import environment.EnviroFactor;
import environmentWarning.EnvironmentProbe;
import environmentWarning.PopUpUi;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector3f;

public class EnviroTipUi
extends PopUpUi {
    private static final int OFFSET_X = 75;
    private static final String ENVIRO = GameText.getText(920);
    private EnvironmentProbe probe;

    public EnviroTipUi(EnvironmentProbe probe) {
        super(EnviroTipUi.getHeaders(probe));
        super.setPixelOffset(75, 10);
        this.probe = probe;
    }

    public void updateTerrainPoint(Vector3f terrainPoint) {
        if (this.probe != null) {
            this.probe.recalculate(terrainPoint);
        }
    }

    private static String[] getHeaders(EnvironmentProbe probe) {
        if (probe == null) {
            return new String[0];
        }
        List<EnviroFactor> factors = probe.getFactors();
        String[] headers = new String[factors.size() + 1];
        int i = 0;
        headers[i++] = ENVIRO;
        for (EnviroFactor factor : factors) {
            headers[i++] = factor.getName();
        }
        return headers;
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        return false;
    }

    @Override
    public List<PopUpUi.StatData> getData() {
        ArrayList<PopUpUi.StatData> data = new ArrayList<PopUpUi.StatData>();
        data.add(new PopUpUi.StatData(String.valueOf(Math.round(this.probe.getSatisfaction() * 100.0f)) + "%", ColourPalette.BEIGE));
        for (EnviroFactor factor : this.probe.getFactors()) {
            data.add(new PopUpUi.StatData(factor.getValue(), factor.getColour()));
        }
        return data;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package environment;

import entityInfoGui.BarMouseOverGui;
import environment.EnviroComponent;
import environment.EnviroFactor;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;

public class EnviroPopUp
extends BarMouseOverGui {
    private static final String ENVIRO = GameText.getText(920);
    private EnviroComponent enviroComponent;

    public EnviroPopUp(EnviroComponent enviroComponent) {
        super(EnviroPopUp.getHeaders(enviroComponent));
        this.enviroComponent = enviroComponent;
    }

    private static String[] getHeaders(EnviroComponent enviroComponent) {
        List<EnviroFactor> factors = enviroComponent.getFactors();
        String[] headers = new String[factors.size() + 1];
        int i = 0;
        headers[i++] = ENVIRO;
        for (EnviroFactor factor : factors) {
            headers[i++] = factor.getName();
        }
        return headers;
    }

    @Override
    public List<BarMouseOverGui.StatData> getData() {
        ArrayList<BarMouseOverGui.StatData> data = new ArrayList<BarMouseOverGui.StatData>();
        data.add(new BarMouseOverGui.StatData(String.valueOf(Math.round(this.enviroComponent.getEnvironmentSatisfaction() * 100.0f)) + "%", ColourPalette.BEIGE));
        for (EnviroFactor factor : this.enviroComponent.getFactors()) {
            data.add(new BarMouseOverGui.StatData(factor.getValue(), factor.getColour()));
        }
        return data;
    }
}


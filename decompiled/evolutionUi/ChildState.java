/*
 * Decompiled with CFR 0.152.
 */
package evolutionUi;

import components.InformationComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import toolbox.Colour;
import toolbox.Maths;
import userInterfaces.Tab2ButtonUi;

public enum ChildState {
    NORMAL(ColourPalette.WHITE, ColourPalette.BEIGE, ColourPalette.BEIGE, ColourPalette.LIGHT_GREY, null){

        @Override
        public String getStatusString(InformationComponent.InformationCompBlueprint info, int percent) {
            return String.valueOf(Maths.formatNumber(info.getPrice())) + " dp";
        }

        @Override
        public void updateTab(Tab2ButtonUi tab) {
            tab.block(false);
        }

        @Override
        public Colour getNameColour(boolean reqsMet) {
            return reqsMet ? ColourPalette.GOLD : this.nameColour;
        }
    }
    ,
    BLOCKED(ColourPalette.MIDDLE_GREY, ColourPalette.MIDDLE_GREY, ColourPalette.LIGHT_GREY, ColourPalette.MIDDLE_GREY, ColourPalette.WHITE){

        @Override
        public String getStatusString(InformationComponent.InformationCompBlueprint info, int percent) {
            return String.valueOf(Maths.formatNumber(info.getPrice())) + " dp";
        }

        @Override
        public void updateTab(Tab2ButtonUi tab) {
            tab.block(true);
        }

        @Override
        public Colour getNameColour(boolean reqsMet) {
            return this.nameColour;
        }
    }
    ,
    IN_PROGRESS(ColourPalette.BASE_BLUE, ColourPalette.WHITE, ColourPalette.BASE_BLUE, ColourPalette.LIGHT_GREY, null){

        @Override
        public String getStatusString(InformationComponent.InformationCompBlueprint info, int percent) {
            return GameText.getText(688);
        }

        @Override
        public void updateTab(Tab2ButtonUi tab) {
            tab.block(true);
        }

        @Override
        public Colour getNameColour(boolean reqsMet) {
            return this.nameColour;
        }
    }
    ,
    PAUSED(ColourPalette.GOLD, ColourPalette.BEIGE, ColourPalette.GOLD, ColourPalette.LIGHT_GREY, null){

        @Override
        public String getStatusString(InformationComponent.InformationCompBlueprint info, int percent) {
            return String.valueOf(GameText.getText(1041)) + " (" + percent + "%)";
        }

        @Override
        public void updateTab(Tab2ButtonUi tab) {
            tab.block(false);
        }

        @Override
        public Colour getNameColour(boolean reqsMet) {
            return reqsMet ? ColourPalette.GOLD : this.nameColour;
        }
    }
    ,
    ELSEWHERE(ColourPalette.BASE_BLUE, ColourPalette.WHITE, ColourPalette.BASE_BLUE, ColourPalette.LIGHT_GREY, null){

        @Override
        public String getStatusString(InformationComponent.InformationCompBlueprint info, int percent) {
            return String.valueOf(GameText.getText(688)) + " (" + percent + "%)";
        }

        @Override
        public void updateTab(Tab2ButtonUi tab) {
            tab.block(false);
        }

        @Override
        public Colour getNameColour(boolean reqsMet) {
            return this.nameColour;
        }
    }
    ,
    UNLOCKED(ColourPalette.DARKER_GREEN, ColourPalette.LIGHT_GREY, ColourPalette.LIGHT_GREY, ColourPalette.DARKER_GREEN, ColourPalette.WHITE){

        @Override
        public String getStatusString(InformationComponent.InformationCompBlueprint info, int percent) {
            return GameText.getText(689);
        }

        @Override
        public void updateTab(Tab2ButtonUi tab) {
        }

        @Override
        public Colour getNameColour(boolean reqsMet) {
            return this.nameColour;
        }
    };

    public final Colour statusColour;
    public final Colour nameColour;
    public final Colour iconBorder;
    public final Colour iconBackground;
    public final Colour iconColour;

    private ChildState(Colour statusColour, Colour nameColour, Colour iconBorder, Colour iconBackground, Colour iconColour) {
        this.statusColour = statusColour;
        this.nameColour = nameColour;
        this.iconColour = iconColour;
        this.iconBackground = iconBackground;
        this.iconBorder = iconBorder;
    }

    public abstract String getStatusString(InformationComponent.InformationCompBlueprint var1, int var2);

    public abstract void updateTab(Tab2ButtonUi var1);

    public abstract Colour getNameColour(boolean var1);

    /* synthetic */ ChildState(String string, int n, Colour colour, Colour colour2, Colour colour3, Colour colour4, Colour colour5, ChildState childState) {
        this(colour, colour2, colour3, colour4, colour5);
    }
}


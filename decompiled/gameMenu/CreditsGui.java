/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import gameMenu.CreditsInfo;
import gameMenu.CreditsPanelGui;
import gameMenu.GameMenuGui;
import gameMenu.SecondPanelUi;
import languages.GameText;
import mainGuis.ColourPalette;
import userInterfaces.GuiScrollPanel;

public class CreditsGui
extends SecondPanelUi {
    private static final String PROGRAMMING = GameText.getText(52);
    private static final String SOUND_TRACK = GameText.getText(53);
    private static final String EXTRA_MUSIC = GameText.getText(54);
    private static final String WEBSITE = GameText.getText(55);
    private static final String MAIN_MENU = GameText.getText(56);
    private static final String SOUND_EFFECTS = GameText.getText(57);
    private static final String THANKS = GameText.getText(58);
    private static final String SOUNDS2 = GameText.getText(857);
    private static final float SCROLL_SPEED = 0.1f;
    private static final float Y_SIZE = 0.9f;
    private static final float X_SIZE = 0.6f;

    protected CreditsGui(GameMenuGui mainMenu) {
        super(mainMenu);
    }

    @Override
    protected void init() {
        super.init();
        CreditsInfo info = this.createInfo();
        final CreditsPanelGui panel = new CreditsPanelGui(info);
        GuiScrollPanel scrollPanel = new GuiScrollPanel(ColourPalette.DARK_GREY, 0.75f, 0.1f){

            @Override
            protected void init() {
                super.init();
                this.setContents(panel, panel.getTotalYSize());
            }
        };
        super.addComponent(scrollPanel, 0.19999999f, 0.050000012f, 0.6f, 0.9f);
    }

    private CreditsInfo createInfo() {
        CreditsInfo creditsInfo = new CreditsInfo();
        creditsInfo.addInfo(PROGRAMMING, "ThinMatrix");
        creditsInfo.addInfo(SOUND_TRACK, "Jamal Green Music");
        creditsInfo.addInfo(EXTRA_MUSIC, "Dannek Studio");
        creditsInfo.addInfo(SOUND_EFFECTS, "Dannek Studio");
        creditsInfo.addInfo(SOUNDS2, "SoundSnap", "RCPTones");
        creditsInfo.addInfo(WEBSITE, "Constantin Ross", "Josh Speight");
        creditsInfo.addInfo(MAIN_MENU, "Syndesi");
        creditsInfo.addInfo(THANKS, "Alberto Spina", "Alexander Ch\u00e1vez", "Alvin Daley", "Amean Abdelfattah", "andrew wilson", "Andrew Witt", "animo", "Anton Weise", "Austin Adamson", "Beard or Die", "BeJammin", "ben humphries", "Benjamin Fuller", "Bill Brownell", "BluNova", "caffeinecoder", "Casper Studios", "Charlie Quigley", "Claudiu Dumitru", "Claudiu Mihail Danciu", "Clayton Scarcella", "Cole Foderaro", "comaralex", "CrazyRedd", "Dakota Berry", "Dan A", "Daniel Thian", "Danny D", "Dilink", "Diogo", "Dylan Thompson", "F Meyer", "Galathil Feanor", "George Angelopoulos", "Harold Pe\u00f1a", "Harry Stanley", "i330733", "Ivan Izhbirdeev", "James McGregor", "Jason Pack", "Jeffrey Goudzwaard", "Jerry bradshaw", "Josh Gill", "Joshua Hannaford", "Justin", "Klowded Dreamz", "Leandro Dipietro", "Lucas Moiseyev (Crazeeruskee)", "Marek Mikolajczyk", "Matdu", "Micah Weightman", "Michal Merc", "Michiel de Smet", "Miguel A. Diaz-Rivas", "Milan", "MrMattyJ", "nasser-sh", "Nathan", "nmann", "Noah Korth", "Petter Kaspersen", "Radioactive Bullfrog", "Redd", "Rene Damm", "Sean McCrory", "Sithis", "Stefan", "Steven Kimpe", "The5thBluesky", "Timothy Gibbons", "Wolfgang Karl", "Zacharias Adamsson");
        return creditsInfo;
    }
}


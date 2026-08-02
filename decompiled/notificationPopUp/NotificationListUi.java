/*
 * Decompiled with CFR 0.152.
 */
package notificationPopUp;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import notificationPopUp.Notification;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiImage;
import userInterfaces.IconButtonUi;
import userInterfaces.Listener;

public class NotificationListUi
extends GuiComponent {
    private static final String MESSAGE = GameText.getText(1117);
    private static final int NOTIFICATION_PIXELS = 65;
    private static final float ICON_SECTION_WIDTH = 0.16f;
    private static final float BUTTON_SECTION_WIDTH = 0.13f;
    private static final float BUTTON_POS_X = 0.91f;
    private static final int BUTTON_OFF_Y = 14;
    private static final float TEXT_SECTION_WIDTH = 0.71000004f;
    private static final float ICON_SIZE = 0.1f;
    private static final int ICON_OFF_Y = 3;
    private static final int TOP_PAD = 10;
    private static final int LINE_THICK = 1;
    private static final float LINE_WIDTH = 0.8f;
    private static final int LINE_Y_POS = 52;
    private int totalPixels;
    private List<Notification> notifications = EquilinoxGuis.getNotificationLog().getNotifications();

    public NotificationListUi() {
        this.totalPixels = this.notifications.isEmpty() ? 20 : 65 * this.notifications.size();
    }

    @Override
    protected void init() {
        super.init();
        float yPos = super.pixelsToRelativeY(10.0f);
        if (this.notifications.size() == 0) {
            this.addMessage(yPos);
            return;
        }
        int i = this.notifications.size() - 1;
        while (i >= 0) {
            Notification notification = this.notifications.get(i);
            this.addNotification(notification, yPos, i == 0);
            yPos += super.pixelsToRelativeY(65.0f);
            --i;
        }
    }

    public int getTotalPixels() {
        return this.totalPixels;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addMessage(float yPos) {
        Text text = Text.newText(MESSAGE).center().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.BRIGHT_GREY);
        super.addText(text, 0.0f, super.pixelsToRelativeY(10.0f), 1.0f);
    }

    private void addNotification(Notification notification, float yPos, boolean last) {
        this.addIcon(notification.icon, yPos);
        this.addText(notification.description, yPos);
        this.addButton(yPos, notification.listener);
        if (!last) {
            this.addLine(yPos);
        }
    }

    private void addIcon(Texture icon, float yPos) {
        GuiImage image = new GuiImage(icon);
        float ySize = super.getRelativeHeightCoords(0.1f);
        float xPos = 0.029999997f;
        float yPosition = yPos + super.pixelsToRelativeY(3.0f);
        image.getTexture().setOverrideColour(ColourPalette.WHITE);
        super.addComponent(image, xPos, yPosition, 0.1f, ySize);
    }

    private void addText(String textString, float yPos) {
        Text text = Text.newText(textString).justify().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.BRIGHT_GREY);
        super.addText(text, 0.16f, yPos, 0.71000004f);
    }

    private void addLine(float yPosition) {
        GuiImage line = new GuiImage(GuiRepository.BLOCK);
        line.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        float xPos = 0.099999994f;
        float yPos = yPosition + super.pixelsToRelativeY(52.0f);
        float height = super.pixelsToRelativeY(1.0f);
        super.addComponent(line, xPos, yPos, 0.8f, height);
    }

    private void addButton(float yPos, final Listener listener) {
        if (listener == null) {
            return;
        }
        IconButtonUi button = new IconButtonUi(GuiRepository.ARROW_OFF_2);
        button.setPreferredPixelSize(18);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    listener.eventOccurred(true);
                }
            }
        });
        super.addPixelComp(button, 0.91f, yPos + super.pixelsToRelativeY(14.0f));
    }
}


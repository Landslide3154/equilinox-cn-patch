/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import basics.DisplayManager;
import entityInfoGui.InfoType;
import entityInfoGui.PopUpInfoGui;
import guis.GuiComponent;
import userInterfaces.GuiProgressBar;

public abstract class ProgressInfo
extends PopUpInfoGui {
    private static final float WAIT_TIME = 0.15f;
    private GuiProgressBar bar;
    private GuiComponent popUp = null;
    private boolean mousedOver = false;
    private float popUpCountDown = 0.15f;

    public ProgressInfo(String name, float font, boolean health) {
        super(name, health ? InfoType.HEALTH : InfoType.PROGRESS_BAR, font);
    }

    @Override
    protected void initValueGui() {
        this.bar = new GuiProgressBar(this.getValue());
        this.bar.setPreferredAspectRatio(8.0f);
        super.addCenteredComponentY(this.bar, 0.5f, 0.54f, 0.42f);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.bar.setProgress(this.getValue());
        if (this.bar.isMouseOver() && !this.mousedOver) {
            this.mousedOver = true;
        } else if (!this.bar.isMouseOver() && this.mousedOver) {
            this.deleteMouseOver();
            this.mousedOver = false;
            this.popUpCountDown = 0.15f;
        }
        if (this.mousedOver && this.popUp == null) {
            this.popUpCountDown -= DisplayManager.getDeltaSeconds();
            if (this.popUpCountDown <= 0.0f) {
                this.popUp = this.addMouseOver();
            }
        }
    }

    @Override
    public void delete() {
        super.delete();
        this.deleteMouseOver();
    }

    protected abstract GuiComponent addMouseOver();

    protected abstract float getValue();

    private void deleteMouseOver() {
        if (this.popUp != null) {
            this.popUp.remove();
            this.popUp = null;
        }
    }
}


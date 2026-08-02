/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import basics.DisplayManager;
import biomes.Biome;
import gameManaging.GameManager;
import gameManaging.GameState;
import guis.GuiMaster;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;
import terrains.TerrainVertex;
import toolbar.BiomePickerGui;
import toolbox.MyMouse;
import userInterfaces.TabButtonUi;

public class BiomePicker {
    private static final float MOVE_THRESHOLD = 0.25f;
    protected static final int MAX_BIOMES = 3;
    private BiomePickerGui gui;
    private boolean active = false;
    private TabButtonUi biomeButton;
    private float buttonTime = 0.0f;

    public BiomePicker(TabButtonUi biomeButton) {
        this.biomeButton = biomeButton;
        this.gui = new BiomePickerGui();
    }

    public void activate(boolean active) {
        if (this.active != active) {
            this.active = active;
            if (active) {
                GameManager.gameState.setState(GameState.BIOME_PICKING);
            } else {
                this.gui.show(false);
                GameManager.gameState.endState(GameState.BIOME_PICKING);
            }
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void update() {
        if (!this.active) {
            return;
        }
        Vector3f terrainPos = GameManager.getTerrainPicker().getCurrentTerrainPoint();
        if (terrainPos != null) {
            this.gui.show(true);
            TerrainVertex vertex = GameManager.getWorld().getTerrainVertex(terrainPos.x, terrainPos.z);
            if (vertex == null) {
                this.gui.show(false);
            } else {
                Map<Biome, Integer> biomeInfo = vertex.getBiomeAmounts();
                float meters = GameManager.getWorld().getAltitude(terrainPos.x, terrainPos.z);
                this.gui.displayInfo(this.getStringInfo(biomeInfo, vertex.getTotalWeights()), (int)meters, meters < 0.0f, terrainPos);
            }
        } else {
            this.gui.show(false);
        }
        this.checkClear();
    }

    private Map<Biome, Integer> getStringInfo(Map<Biome, Integer> biomeInfo, int total) {
        int totalWeight = Math.max(100, total);
        List<Map.Entry<Biome, Integer>> sortedBiomes = this.createSortedList(biomeInfo);
        Map<Biome, Integer> weightData = this.getWeightData(sortedBiomes, totalWeight);
        return weightData;
    }

    private List<Map.Entry<Biome, Integer>> createSortedList(Map<Biome, Integer> biomeInfo) {
        ArrayList<Map.Entry<Biome, Integer>> sortedBiomes = new ArrayList<Map.Entry<Biome, Integer>>();
        Iterator<Map.Entry<Biome, Integer>> iterator = biomeInfo.entrySet().iterator();
        while (iterator.hasNext()) {
            this.sortIntoList(sortedBiomes, iterator.next());
        }
        return sortedBiomes;
    }

    private void sortIntoList(List<Map.Entry<Biome, Integer>> sortedBiomes, Map.Entry<Biome, Integer> entry) {
        int i = 0;
        while (i < sortedBiomes.size()) {
            if (entry.getValue() > sortedBiomes.get(i).getValue()) {
                sortedBiomes.add(i, entry);
                return;
            }
            ++i;
        }
        sortedBiomes.add(entry);
    }

    private Map<Biome, Integer> getWeightData(List<Map.Entry<Biome, Integer>> sortedBiomes, int totalWeight) {
        LinkedHashMap<Biome, Integer> weightData = new LinkedHashMap<Biome, Integer>();
        int count = 0;
        for (Map.Entry<Biome, Integer> entry : sortedBiomes) {
            int percentage = entry.getValue() * 100 / totalWeight;
            Biome biome = entry.getKey();
            weightData.put(biome, Math.min(100, percentage));
            if (++count != 3) continue;
            return weightData;
        }
        return weightData;
    }

    private void checkClear() {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (this.active && !GuiMaster.isMouseInGui() && mouse.isLeftClick()) {
            this.biomeButton.toggle();
            return;
        }
        if (mouse.isRightClickRelease() && this.buttonTime < 0.25f) {
            this.biomeButton.toggle();
        }
        this.buttonTime = mouse.isRightButtonDown() ? (this.buttonTime += DisplayManager.getDeltaSeconds() + (float)(Math.abs(mouse.getDX()) + Math.abs(mouse.getDY())) * 0.001f) : 0.0f;
    }
}


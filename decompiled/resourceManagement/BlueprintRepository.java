/*
 * Decompiled with CFR 0.152.
 */
package resourceManagement;

import blueprints.Blueprint;
import components.NamesLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.FileUtils;
import utils.MyFile;

public class BlueprintRepository {
    private static final MyFile ENTITY_FOLDER = new MyFile(FileUtils.RES_FOLDER, "entities");
    private static final String FILE_EXT = ".txt";
    private static Map<Integer, Blueprint> blueprints = new HashMap<Integer, Blueprint>();

    public static Blueprint getBlueprint(int id) {
        return blueprints.get(id);
    }

    public static void loadAllBlueprints(boolean backgroundLoad) {
        NamesLoader.loadUpNames();
        BlueprintRepository.loadNewBlueprint("1_Sheep", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("2_Oak", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("3_BerryBush", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("4_JuniperTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("5_Acer", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("6_Rocks", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("7_Heather", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("8_Chicken", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("9_Pebbles", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("10_Fern", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("11_Wheat", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("12_Tortoise", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("13_Kelp", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("14_Trout", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("15_Herring", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("16_Lilly", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("17_Seaweed", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("18_Cactus", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("19_PricklyPear", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("20_Grass", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("21_Yucca", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("23_Pike", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("25_BirchTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("26_PinkTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("27_PalmTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("28_TallTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("30_CherryTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("31_Mushroom", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("32_AppleTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("33_Apple", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("35_JungleRocks", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("36_JunglePlant", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("37_VineTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("38_Frog", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("39_JungleMushroom", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("40_Coconut", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("41_Rabbit", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("42_RedTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("43_BananaTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("44_Banana", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("45_Carrot", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("46_UmbrellaTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("47_OrangeTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("48_Orange", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("49_Squirrel", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("50_Boar", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("51_PotatoPlant", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("52_GuineaPig", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("53_Potato", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("54_LargeTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("55_Butterfly", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("56_Bee", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("57_Hive", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("58_SwampTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("59_RedMushroom", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("60_LongGrass", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("62_TomatoPlant", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("63_Toucan", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("64_Sparrow", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("65_Duck", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("66_Eucalyptus", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("67_SpiralTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("68_Nest", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("69_SpindleTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("70_Bamboo", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("71_BlueberryBush", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("72_Wolf", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("73_DesertHare", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("74_Toad", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("75_Lizard", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("76_ClownFish", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("78_Bear", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("79_Warthog", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("80_Tulip", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("81_ForestTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("83_TallFir", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("84_Fox", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("85_Camel", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("89_Beaver", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("92_Goat", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("94_MangoTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("99_Deer", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("100_Twig", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("101_Bark", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("102_Den", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("103_SnapDragon", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("104_Meat", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("105_WildMint", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("106_Sage", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("107_Oregano", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("108_Rosemary", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("109_FlowerTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("110_Willow", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("111_BigFlower", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("112_ElmTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("113_BirchTree3", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("114_Daisy", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("115_Buttercup", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("116_Poppy", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("117_TropicalFlower", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("118_Bluebell", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("119_ButtonMushroom", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("120_SmallCactus", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("121_GiantCactus", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("122_DesertTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("123_JungleGrass", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("124_SmallJungleTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("125_TallJungleTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("126_SpecialMushroom", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("127_LushFlower", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("128_FloweryGrass", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("129_TropicalPlant", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("130_LeafyPlant", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("131_PineTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("132_FirTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("133_HollyBush", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("135_RedMaple", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("136_TropicalSeaweed", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("137_NutTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("138_Nut", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("139_BirdMeat", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("140_SmallMeat", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("141_Mango", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("142_Turnip", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("143_Bullrush", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("144_SwampFlower", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("145_Peacock", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("146_DeadTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("148_Barley", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("149_LushGrass", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("150_PerfectTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("151_Rose", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("152_Lily", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("153_SunFlower", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("154_Pansies", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("155_FloppyTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("156_NewTree", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("157_Healbloom", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("158_Seed", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("159_WitchWood", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("160_WitchWoodFruit", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("161_Dove", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("162_Stones", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("163_Boulders", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("164_Spit", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("165_Coral", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("166_Shell", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("167_Salmon", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("168_AngelFish", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("169_NeonFish", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("170_RoyalGramma", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("171_Eagle", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("172_JellyFish", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("173_MoonBush", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("174_EagleNest", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("175_Fly", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("176_CarnivorePlant", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("177_Tongue", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("178_Meerkat", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("179_Burrow", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("180_DesertGrass", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("181_DesertRock", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("182_Marigolds", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("183_Dolphin", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("134_SnowRocks", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("1000", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("1001", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("1002", backgroundLoad);
        BlueprintRepository.loadNewBlueprint("1003", backgroundLoad);
    }

    public static List<Blueprint> getAllBlueprints() {
        ArrayList<Blueprint> list = new ArrayList<Blueprint>();
        for (Blueprint b : blueprints.values()) {
            list.add(b);
        }
        return list;
    }

    private static void loadNewBlueprint(String name, boolean loadInBackground) {
        int id = Integer.parseInt(name.split("_")[0]);
        MyFile blueprintFile = new MyFile(ENTITY_FOLDER, String.valueOf(name) + FILE_EXT);
        Blueprint blueprint = Blueprint.load(id, blueprintFile, loadInBackground);
        blueprints.put(id, blueprint);
    }
}


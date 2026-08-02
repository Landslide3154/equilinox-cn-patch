/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import classification.Classifier;
import gameManaging.GameManager;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import main.EquilinoxMusic;
import materials.PresetColour;
import resourceManagement.BlueprintRepository;
import tasks.BaaReq;
import tasks.BloomingReq;
import tasks.BreedReq;
import tasks.BuildReq;
import tasks.ButterflyCatchReq;
import tasks.CashReward;
import tasks.ColourEntityReq;
import tasks.EagleCatchReq;
import tasks.EarningReq;
import tasks.EatReq;
import tasks.EntityCountReq;
import tasks.EvolutionReq;
import tasks.FlyCatchReq;
import tasks.FullGrownCountReq;
import tasks.HalfGrownOakReq;
import tasks.HappyEntitiesReq;
import tasks.HoleDigReq;
import tasks.HoneyHarvesterReq;
import tasks.HoneyReq;
import tasks.HuntingReq;
import tasks.MusicUnlockReward;
import tasks.Reward;
import tasks.ShopItemUnlockReward;
import tasks.SpittingRequirement;
import tasks.Task;
import tasks.TaskCompletorRequirement;
import tasks.TaskRequirement;
import tasks.TaskUnlockReward;
import tasks.TreeCutReq;

public class TaskCreator {
    protected static List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        tasks.add(TaskCreator.createGrassTask(1));
        tasks.add(TaskCreator.createFlowerPower(2));
        tasks.add(TaskCreator.createTreeTask(3));
        tasks.add(TaskCreator.createMoreGrassTask(4));
        tasks.add(TaskCreator.createSheepTask(5));
        tasks.add(TaskCreator.createBreedWheatTask(6));
        tasks.add(TaskCreator.createGenModTask(7));
        tasks.add(TaskCreator.createChickenTask(10));
        tasks.add(TaskCreator.createBoarTask(11));
        tasks.add(TaskCreator.createUnderwaterTask(12));
        tasks.add(TaskCreator.createFishingTask(13));
        tasks.add(TaskCreator.createDiversifyTask(14));
        tasks.add(TaskCreator.createAppleDayTask(15));
        tasks.add(TaskCreator.createAppleEveryTask(16));
        tasks.add(TaskCreator.createCompletionTask(17));
        tasks.add(TaskCreator.createBirdTask(18));
        tasks.add(TaskCreator.createDuckTask(19));
        tasks.add(TaskCreator.createGrassyTask(20));
        tasks.add(TaskCreator.createMerryBerryTask(21));
        tasks.add(TaskCreator.createDesertTask(22));
        tasks.add(TaskCreator.createWoodlandTask(23));
        tasks.add(TaskCreator.createCarrotTask(24));
        tasks.add(TaskCreator.createGoldCarrotTask(25));
        tasks.add(TaskCreator.createForestFaunaTask(26));
        tasks.add(TaskCreator.createMountaineeringTask(27));
        tasks.add(TaskCreator.createNutsTask(28));
        tasks.add(TaskCreator.createDeerTask(29));
        tasks.add(TaskCreator.createTropicalTask(30));
        tasks.add(TaskCreator.createCoconutsTask(31));
        tasks.add(TaskCreator.createTropicalWatersTask(32));
        tasks.add(TaskCreator.createBaaTask(33));
        tasks.add(TaskCreator.createMarshTask(34));
        tasks.add(TaskCreator.createPotatoTask(35));
        tasks.add(TaskCreator.createJungleTask(36));
        tasks.add(TaskCreator.createMightyJungleTask(37));
        tasks.add(TaskCreator.createButterflyCatchTask(38));
        tasks.add(TaskCreator.createTurnipTask(39));
        tasks.add(TaskCreator.createBeaverTask(40));
        tasks.add(TaskCreator.createButterflyTask(41));
        tasks.add(TaskCreator.createHoneyTask(42));
        tasks.add(TaskCreator.createHungeryTurtleTask(43));
        tasks.add(TaskCreator.createMushroomTask(44));
        tasks.add(TaskCreator.createLushTask(45));
        tasks.add(TaskCreator.createPikeTask(46));
        tasks.add(TaskCreator.createTreeCuttingTask(47));
        tasks.add(TaskCreator.createHungryFishTask(48));
        tasks.add(TaskCreator.createButterflyHunterTask(49));
        tasks.add(TaskCreator.createBananaFarmerTask(50));
        tasks.add(TaskCreator.createHoneyHarvesterTask(51));
        tasks.add(TaskCreator.createDiggyHoleTask(52));
        tasks.add(TaskCreator.createHuntingTask(53));
        tasks.add(TaskCreator.createWolfHuntingTask(54));
        tasks.add(TaskCreator.createEarningTask(55));
        tasks.add(TaskCreator.createPigPotatoTask(56));
        tasks.add(TaskCreator.createCamelTask(57));
        tasks.add(TaskCreator.createFishMasteryTask(58));
        tasks.add(TaskCreator.createBloomingTask(59));
        tasks.add(TaskCreator.createFlyCatchingTask(60));
        tasks.add(TaskCreator.createDesertEagleTask(61));
        tasks.add(TaskCreator.createHoleInOneTask(62));
        return tasks;
    }

    private static Task createGrassTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new FullGrownCountReq(Classifier.getClassification("png20"), 1));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(150));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(114)));
        rewards.add(new TaskUnlockReward(new int[]{2}));
        Task task = new Task(id, GameText.getText(111), GameText.getText(112), reqs, rewards);
        task.setLinkedHelpTab(0);
        return task;
    }

    private static Task createBaaTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new BaaReq());
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(300));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(119)));
        rewards.add(new TaskUnlockReward(new int[]{44}));
        return new Task(id, GameText.getText(1065), GameText.getText(1066), reqs, rewards);
    }

    private static Task createButterflyCatchTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new ButterflyCatchReq(15));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(40000));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(26)));
        rewards.add(new TaskUnlockReward(new int[]{45, 49}));
        return new Task(id, GameText.getText(611), GameText.getText(612), reqs, rewards);
    }

    private static Task createButterflyHunterTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new ButterflyCatchReq(25));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(18000));
        return new Task(id, true, GameText.getText(670), GameText.getText(671), reqs, rewards);
    }

    private static Task createFlowerPower(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new FullGrownCountReq(Classifier.getClassification("png20"), 6));
        reqs.add(new FullGrownCountReq(Classifier.getClassification("pnf114"), 3));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(650));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(2)));
        rewards.add(new TaskUnlockReward(new int[]{3}));
        Task task = new Task(id, GameText.getText(211), GameText.getText(212), reqs, rewards);
        task.setLinkedHelpTab(1);
        return task;
    }

    private static Task createTropicalWatersTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("pnw136"), 5));
        reqs.add(new EntityCountReq(Classifier.getClassification("afs76"), 7));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(20000));
        rewards.add(new TaskUnlockReward(new int[]{46}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(166)));
        rewards.add(new MusicUnlockReward(EquilinoxMusic.getTrack(19)));
        return new Task(id, GameText.getText(597), GameText.getText(598), reqs, rewards);
    }

    private static Task createTropicalTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("png128"), 18));
        reqs.add(new EntityCountReq(Classifier.getClassification("pbl130"), 6));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(18500));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(27)));
        rewards.add(new TaskUnlockReward(new int[]{31}));
        return new Task(id, GameText.getText(592), GameText.getText(593), reqs, rewards);
    }

    private static Task createTreeTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HalfGrownOakReq());
        reqs.add(new HappyEntitiesReq(Classifier.getClassification("ptw2"), 1, 70, false));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{5, 4, 7, 17}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(1)));
        rewards.add(new CashReward(1500));
        Task task = new Task(id, GameText.getText(219), GameText.getText(220), reqs, rewards);
        task.setLinkedHelpTab(4);
        return task;
    }

    private static Task createAppleDayTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("ef33"), Classifier.getClassification("ahm1"), 1));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{16, 11}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(28)));
        rewards.add(new CashReward(8500));
        return new Task(id, GameText.getText(549), GameText.getText(550), reqs, rewards);
    }

    private static Task createPikeTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("afs14"), Classifier.getClassification("afb23"), 10));
        reqs.add(new EatReq(Classifier.getClassification("afs15"), Classifier.getClassification("afb23"), 10));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{48, 58}));
        rewards.add(new CashReward(30000));
        return new Task(id, GameText.getText(638), GameText.getText(639), reqs, rewards);
    }

    private static Task createHungryFishTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("afs14"), Classifier.getClassification("afb23"), 5));
        reqs.add(new EatReq(Classifier.getClassification("afs15"), Classifier.getClassification("afb23"), 5));
        reqs.add(new EatReq(Classifier.getClassification("afs76"), Classifier.getClassification("afb23"), 5));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(10000));
        return new Task(id, true, GameText.getText(668), GameText.getText(669), reqs, rewards);
    }

    private static Task createJungleTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("png123"), 20));
        reqs.add(new EntityCountReq(Classifier.getClassification("pbl36"), 5));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{37}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(37)));
        rewards.add(new CashReward(27300));
        return new Task(id, GameText.getText(604), GameText.getText(605), reqs, rewards);
    }

    private static Task createLushTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("pnf127"), 10));
        reqs.add(new EntityCountReq(Classifier.getClassification("png149"), 10));
        reqs.add(new EntityCountReq(Classifier.getClassification("ptl26"), 4));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(78)));
        rewards.add(new CashReward(54000));
        return new Task(id, GameText.getText(636), GameText.getText(637), reqs, rewards);
    }

    private static Task createMightyJungleTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("ptj124"), 5));
        reqs.add(new EntityCountReq(Classifier.getClassification("pnf111"), 5));
        reqs.add(new EntityCountReq(Classifier.getClassification("pnm39"), 5));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{38, 41, 59}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(55)));
        rewards.add(new CashReward(40000));
        return new Task(id, GameText.getText(606), GameText.getText(607), reqs, rewards);
    }

    private static Task createCoconutsTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new BreedReq(Classifier.getClassification("ef40"), 50));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{43, 50}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(75)));
        rewards.add(new CashReward(12000));
        return new Task(id, GameText.getText(594), GameText.getText(595), reqs, rewards);
    }

    private static Task createMerryBerryTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("pbf3"), Classifier.getClassification("ahm1"), 5));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{22}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(21)));
        rewards.add(new CashReward(7500));
        return new Task(id, GameText.getText(571), GameText.getText(572), reqs, rewards);
    }

    private static Task createCarrotTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("pnv45"), Classifier.getClassification("ahs41"), 10));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{25, 28}));
        rewards.add(new MusicUnlockReward(EquilinoxMusic.getTrack(21)));
        rewards.add(new CashReward(15000));
        return new Task(id, GameText.getText(577), GameText.getText(578), reqs, rewards);
    }

    private static Task createTurnipTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("pnv142"), Classifier.getClassification("ahm79"), 20));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(149)));
        rewards.add(new CashReward(50000));
        rewards.add(new MusicUnlockReward(EquilinoxMusic.getTrack(18)));
        return new Task(id, GameText.getText(613), GameText.getText(614), reqs, rewards);
    }

    private static Task createPotatoTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("ev53"), Classifier.getClassification("ahm50"), 10));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{52}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(134)));
        rewards.add(new CashReward(28000));
        return new Task(id, GameText.getText(601), GameText.getText(602), reqs, rewards);
    }

    private static Task createGoldCarrotTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("pnv45"), Classifier.getClassification("ahs41"), 24, PresetColour.GOLD));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(15000));
        return new Task(id, true, GameText.getText(579), GameText.getText(580), reqs, rewards);
    }

    private static Task createHungeryTurtleTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("ef40"), Classifier.getClassification("ar12"), 5));
        reqs.add(new EatReq(Classifier.getClassification("pnw136"), Classifier.getClassification("ar12"), 5));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(58000));
        return new Task(id, GameText.getText(632), GameText.getText(633), reqs, rewards);
    }

    private static Task createMountaineeringTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("pbf133"), Classifier.getClassification("ahm92"), 20));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{53}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(84)));
        rewards.add(new CashReward(35000));
        rewards.add(new MusicUnlockReward(EquilinoxMusic.getTrack(20)));
        return new Task(id, GameText.getText(585), GameText.getText(586), reqs, rewards);
    }

    private static Task createDiggyHoleTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("pnv51"), Classifier.getClassification("ahm50"), 10));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(17500));
        return new Task(id, true, GameText.getText(681), GameText.getText(682), reqs, rewards);
    }

    private static Task createNutsTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("en138"), Classifier.getClassification("ahs49"), 50));
        reqs.add(new EntityCountReq(Classifier.getClassification("ptw137"), 3));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{30, 40}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(128)));
        rewards.add(new CashReward(27500));
        return new Task(id, GameText.getText(587), GameText.getText(588), reqs, rewards);
    }

    private static Task createAppleEveryTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new BreedReq(GameText.getText(557), GameText.getComplexText(558), Classifier.getClassification("ef33"), 250));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(5000));
        return new Task(id, true, GameText.getText(553), GameText.getText(554), reqs, rewards);
    }

    private static Task createBananaFarmerTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new BreedReq(GameText.getText(674), GameText.getComplexText(675), Classifier.getClassification("ef44"), 250));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(30000));
        return new Task(id, true, GameText.getText(672), GameText.getText(673), reqs, rewards);
    }

    private static Task createCompletionTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new TaskCompletorRequirement());
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(10000000));
        return new Task(id, GameText.getText(1067), GameText.getText(1068), reqs, rewards);
    }

    private static Task createDiversifyTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EvolutionReq(Classifier.getClassification("pnf115"), 1));
        reqs.add(new EvolutionReq(Classifier.getClassification("p"), 3));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(2500));
        rewards.add(new TaskUnlockReward(new int[]{15, 23}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(108)));
        return new Task(id, GameText.getText(545), GameText.getText(546), reqs, rewards);
    }

    private static Task createForestFaunaTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EvolutionReq(Classifier.getClassification("ptf"), 2));
        reqs.add(new EvolutionReq(Classifier.getClassification("pnf7"), 1));
        reqs.add(new EvolutionReq(Classifier.getClassification("pnx10"), 1));
        reqs.add(new EvolutionReq(Classifier.getClassification("pnh106"), 1));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(17000));
        rewards.add(new TaskUnlockReward(new int[]{27, 35}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(132)));
        return new Task(id, GameText.getText(583), GameText.getText(584), reqs, rewards);
    }

    private static Task createEarningTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EarningReq(500));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(2000));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(9)));
        Task task = new Task(id, GameText.getText(982), GameText.getText(983), reqs, rewards);
        task.setLinkedHelpTab(3);
        return task;
    }

    private static Task createMushroomTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EvolutionReq(Classifier.getClassification("pnm31"), 1));
        reqs.add(new EvolutionReq(Classifier.getClassification("pnm39"), 1));
        reqs.add(new EvolutionReq(Classifier.getClassification("pnm59"), 1));
        reqs.add(new EvolutionReq(Classifier.getClassification("pnm126"), 1));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(48000));
        return new Task(id, GameText.getText(634), GameText.getText(635), reqs, rewards);
    }

    private static Task createDeerTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HappyEntitiesReq(Classifier.getClassification("ahm99"), 3, 80, false));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(60)));
        rewards.add(new TaskUnlockReward(new int[]{34}));
        rewards.add(new CashReward(20000));
        return new Task(id, GameText.getText(590), GameText.getText(591), reqs, rewards);
    }

    private static Task createTreeCuttingTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new TreeCutReq(20));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(50000));
        return new Task(id, true, GameText.getText(666), GameText.getText(667), reqs, rewards);
    }

    private static Task createSheepTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HappyEntitiesReq(Classifier.getClassification("ahm1"), 3, 75, false));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(25)));
        rewards.add(new TaskUnlockReward(new int[]{20, 33, 55}));
        rewards.add(new CashReward(2000));
        Task task = new Task(id, GameText.getText(117), GameText.getText(118), reqs, rewards);
        task.setLinkedHelpTab(1);
        return task;
    }

    private static Task createGrassyTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("ptg135"), 3));
        reqs.add(new EntityCountReq(Classifier.getClassification("ptg113"), 3));
        reqs.add(new EntityCountReq(Classifier.getClassification("pnh105"), 4));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(62)));
        rewards.add(new TaskUnlockReward(new int[]{21}));
        rewards.add(new CashReward(16000));
        return new Task(id, GameText.getText(569), GameText.getText(570), reqs, rewards);
    }

    private static Task createWoodlandTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("ptw112"), 3));
        reqs.add(new EntityCountReq(Classifier.getClassification("ptw69"), 3));
        reqs.add(new EntityCountReq(Classifier.getClassification("pnh107"), 4));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(52)));
        rewards.add(new TaskUnlockReward(new int[]{24, 29, 56}));
        rewards.add(new CashReward(18000));
        return new Task(id, GameText.getText(575), GameText.getText(576), reqs, rewards);
    }

    private static Task createPigPotatoTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EatReq(Classifier.getClassification("ev53"), Classifier.getClassification("ahs52"), 30));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(10000));
        return new Task(id, true, GameText.getText(1077), GameText.getText(1078), reqs, rewards);
    }

    private static Task createMarshTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("png60"), 8));
        reqs.add(new EntityCountReq(Classifier.getClassification("pnm59"), 8));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(110)));
        rewards.add(new TaskUnlockReward(new int[]{39, 60}));
        rewards.add(new CashReward(20000));
        return new Task(id, GameText.getText(599), GameText.getText(600), reqs, rewards);
    }

    private static Task createDesertTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("pc120"), 4));
        reqs.add(new EntityCountReq(Classifier.getClassification("pc19"), 4));
        reqs.add(new EntityCountReq(Classifier.getClassification("pc21"), 4));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(122)));
        rewards.add(new TaskUnlockReward(new int[]{57}));
        rewards.add(new CashReward(7000));
        return new Task(id, GameText.getText(573), GameText.getText(574), reqs, rewards);
    }

    private static Task createCamelTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new SpittingRequirement());
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(50000));
        return new Task(id, true, GameText.getText(1109), GameText.getText(1110), reqs, rewards);
    }

    private static Task createHuntingTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HuntingReq(BlueprintRepository.getBlueprint(84), Classifier.getClassification("abs8"), 15));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(44000));
        rewards.add(new TaskUnlockReward(new int[]{54}));
        return new Task(id, GameText.getText(866), GameText.getText(867), reqs, rewards);
    }

    private static Task createWolfHuntingTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HuntingReq(BlueprintRepository.getBlueprint(72), Classifier.getClassification("ahm1"), 10));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(80000));
        return new Task(id, GameText.getText(868), GameText.getText(869), reqs, rewards);
    }

    private static Task createBirdTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new BuildReq(Classifier.getClassification("es68"), GameText.getText(565), GameText.getText(566)));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(11800));
        rewards.add(new TaskUnlockReward(new int[]{36, 61}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(123)));
        return new Task(id, GameText.getText(563), GameText.getText(564), reqs, rewards);
    }

    private static Task createBeaverTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new BuildReq(Classifier.getClassification("es102"), GameText.getText(626), GameText.getText(627)));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(60000));
        rewards.add(new TaskUnlockReward(new int[]{47}));
        return new Task(id, GameText.getText(624), GameText.getText(625), reqs, rewards);
    }

    private static Task createBoarTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HappyEntitiesReq(Classifier.getClassification("ahm50"), 6, 75, false));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(15000));
        rewards.add(new TaskUnlockReward(new int[]{18, 26}));
        return new Task(id, GameText.getText(270), GameText.getText(271), reqs, rewards);
    }

    private static Task createButterflyTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("ai55"), 12));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{42}));
        rewards.add(new CashReward(45000));
        return new Task(id, GameText.getText(628), GameText.getText(629), reqs, rewards);
    }

    private static Task createChickenTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HappyEntitiesReq(Classifier.getClassification("abs8"), 3, 70, false));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(2000));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(163)));
        return new Task(id, GameText.getText(266), GameText.getText(267), reqs, rewards);
    }

    private static Task createDuckTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HappyEntitiesReq(Classifier.getClassification("abs65"), 2, 75, false));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(6000));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(35)));
        return new Task(id, GameText.getText(567), GameText.getText(568), reqs, rewards);
    }

    private static Task createHoneyTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HoneyReq());
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{51}));
        rewards.add(new CashReward(75000));
        return new Task(id, GameText.getText(630), GameText.getText(631), reqs, rewards);
    }

    private static Task createHoneyHarvesterTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HoneyHarvesterReq());
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(8000));
        return new Task(id, true, GameText.getText(677), GameText.getText(678), reqs, rewards);
    }

    private static Task createUnderwaterTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("pnw17"), 10));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(1200));
        rewards.add(new TaskUnlockReward(new int[]{13}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(14)));
        return new Task(id, GameText.getText(541), GameText.getText(542), reqs, rewards);
    }

    private static Task createFishingTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("afs14"), 4));
        reqs.add(new EntityCountReq(Classifier.getClassification("afs15"), 4));
        reqs.add(new EntityCountReq(Classifier.getClassification("pnw13"), 4));
        reqs.add(new EntityCountReq(Classifier.getClassification("pnw16"), 4));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new TaskUnlockReward(new int[]{19, 32}));
        rewards.add(new CashReward(12500));
        return new Task(id, GameText.getText(543), GameText.getText(544), reqs, rewards);
    }

    private static Task createMoreGrassTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("png20"), 50));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(750));
        rewards.add(new TaskUnlockReward(new int[]{6}));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(162)));
        Task task = new Task(id, GameText.getText(119), GameText.getText(120), reqs, rewards);
        task.setLinkedHelpTab(2);
        return task;
    }

    private static Task createGenModTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new ColourEntityReq(Classifier.getClassification("pnf114"), 3, PresetColour.LIGHT_BLUE));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(1000));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(6)));
        rewards.add(new MusicUnlockReward(EquilinoxMusic.getTrack(17)));
        Task task = new Task(id, GameText.getText(230), GameText.getText(231), reqs, rewards);
        task.setLinkedHelpTab(5);
        return task;
    }

    private static Task createBreedWheatTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("png11"), 3));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(8)));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(17)));
        rewards.add(new TaskUnlockReward(new int[]{10, 12, 14}));
        rewards.add(new CashReward(450));
        Task task = new Task(id, GameText.getText(226), GameText.getText(227), reqs, rewards);
        task.setLinkedHelpTab(6);
        return task;
    }

    private static Task createFishMasteryTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EntityCountReq(Classifier.getClassification("afs170"), 20));
        reqs.add(new EntityCountReq(Classifier.getClassification("afs169"), 20));
        reqs.add(new EntityCountReq(Classifier.getClassification("afs76"), 20));
        reqs.add(new EntityCountReq(Classifier.getClassification("afb23"), 10));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(200000));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(172)));
        Task task = new Task(id, GameText.getText(1140), GameText.getText(1141), reqs, rewards);
        return task;
    }

    private static Task createBloomingTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new BloomingReq());
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(100000));
        Task task = new Task(id, GameText.getText(1161), GameText.getText(1162), reqs, rewards);
        return task;
    }

    private static Task createDesertEagleTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new EagleCatchReq());
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(35000));
        rewards.add(new ShopItemUnlockReward(GameManager.getShops().getItem(181)));
        rewards.add(new TaskUnlockReward(new int[]{62}));
        Task task = new Task(id, GameText.getText(1183), GameText.getText(1184), reqs, rewards);
        return task;
    }

    private static Task createHoleInOneTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new HoleDigReq());
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(80000));
        Task task = new Task(id, GameText.getText(1185), GameText.getText(1186), reqs, rewards);
        return task;
    }

    private static Task createFlyCatchingTask(int id) {
        ArrayList<TaskRequirement> reqs = new ArrayList<TaskRequirement>();
        reqs.add(new FlyCatchReq(40));
        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new CashReward(40000));
        Task task = new Task(id, true, GameText.getText(1171), GameText.getText(1172), reqs, rewards);
        return task;
    }
}


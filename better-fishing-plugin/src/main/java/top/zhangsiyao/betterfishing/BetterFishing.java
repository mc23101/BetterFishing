package top.zhangsiyao.betterfishing;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import top.zhangsiyao.betterfishing.command.Command;
import top.zhangsiyao.betterfishing.command.GiveCommand;
import top.zhangsiyao.betterfishing.config.*;

import top.zhangsiyao.betterfishing.event.FishEatEvent;
import top.zhangsiyao.betterfishing.event.FishInteractEvent;
import top.zhangsiyao.betterfishing.event.RodSetBaitEvent;
import top.zhangsiyao.betterfishing.fishing.FishingBaitProcessor;
import top.zhangsiyao.betterfishing.fishing.FishingTitleEvent;
import top.zhangsiyao.betterfishing.item.BRarity;
import top.zhangsiyao.betterfishing.item.BaitItem;
import top.zhangsiyao.betterfishing.item.FishItem;

import top.zhangsiyao.betterfishing.fishing.FishingNoneBaitProcessor;
import top.zhangsiyao.betterfishing.item.Rod;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BetterFishing extends JavaPlugin {
    private Random random = new Random();


    // "鱼"配置文件(钓鱼的掉落物)
    public static FishFile fishFile;

    // 稀有度配置文件
    public static RaritiesFile raritiesFile;

    //诱饵配置文件
    public static BaitFile baitFile;

    // 鱼竿配置文件
    public static RodFile rodFile;


    public static MainConfig mainConfig;

    public static MessageConfig messageConfig;

    /**
     *
     * */
    public static Map<String, FishItem> globalFishes;

    public static Map<String,FishItem> extraFishes;

    public static Map<String,FishItem> allFishes;

    public static Map<BRarity,List<FishItem>> globalRarityFishes;

    public static Map<String,Map<BRarity,List<FishItem>>> extraRarityFishes;

    public static Map<String, BRarity> rarityMap;

    public static Map<String, Rod> rodMap;

    public static Map<String, BaitItem> baitMap;

    public static List<String> competitionWorlds;

    public static Permission permission = null;

    public static Economy econ = null;

    public static Logger logger;
    public static PluginManager pluginManager;

    public static boolean usingPAPI;

    public static WorldGuardPlugin wgPlugin;
    public static String guardPL;
    
    public static boolean papi;
    private static BetterFishing instance;

    public static BetterFishing getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        pluginManager = getServer().getPluginManager();
        setupPermissions();
        initCollections();
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        loadAllConfig();


        listeners();
        commands();

        logger.log(Level.INFO, "BetterFishing 插件启动成功！");
    }


    @Override
    public void onDisable() {
        terminateSellGUIS();
        logger.log(Level.INFO, "BetterFishing 插件关闭成功！");
    }


    /**
     * 注册基本监听器
     * */
    private void listeners() {

        getServer().getPluginManager().registerEvents(new FishingNoneBaitProcessor(), this);
        getServer().getPluginManager().registerEvents(new FishingBaitProcessor(),this);
        getServer().getPluginManager().registerEvents(new FishInteractEvent(),this);
        getServer().getPluginManager().registerEvents(new FishingTitleEvent(),this);
        getServer().getPluginManager().registerEvents(new RodSetBaitEvent(),this);
        getServer().getPluginManager().registerEvents(new FishEatEvent(),this);
        getServer().getPluginManager().registerEvents(new SkullSaver(), this);

        optionalListeners();
    }


    /**
     * 根据配置文件，注册额外监听器
     * */
    private void optionalListeners() {
//        if (checkingEatEvent) {
//            getServer().getPluginManager().registerEvents(FishEatEvent.getInstance(), this);
//        }
//
//        if (checkingIntEvent) {
//            getServer().getPluginManager().registerEvents(FishInteractEvent.getInstance(), this);
//        }
//
//        if (mainConfig.blockCrafting()) {
//            getServer().getPluginManager().registerEvents(new AntiCraft(), this);
//        }
//
//        if (Bukkit.getPluginManager().getPlugin("mcMMO") != null) {
//            usingMcMMO = true;
//            if (mainConfig.disableMcMMOTreasure()) {
//                getServer().getPluginManager().registerEvents(McMMOTreasureEvent.getInstance(), this);
//            }
//        }
//
//        if (Bukkit.getPluginManager().getPlugin("HeadDatabase") != null) {
//            usingHeadsDB = true;
//            getServer().getPluginManager().registerEvents(new HeadDBIntegration(), this);
//        }
//
//        if (Bukkit.getPluginManager().getPlugin("AureliumSkills") != null) {
//            if (mainConfig.disableAureliumSkills()) {
//                getServer().getPluginManager().registerEvents(AureliumSkillsFishingEvent.getInstance(), this);
//            }
//        }
    }

    /**
     * 读取插件配置文件
     * */
    private void loadAllConfig(){
        //加载配置文件
        mainConfig = new MainConfig(this);
        raritiesFile = new RaritiesFile(this);
        fishFile = new FishFile(this);
        baitFile = new BaitFile(this);
        rodFile=new RodFile(this);
        messageConfig=new MessageConfig(this);
    }

    /**
     * 注册服务器指令
     * */
    private void commands() {
        getCommand("betterfishing").setExecutor(new Command(this));
    }


    /**
     * 服务器关闭或者重载插件时，关闭玩家所有shop的GUI
     * */
    private void terminateSellGUIS() {
//        getServer().getOnlinePlayers().forEach(player -> {
//            if (player.getOpenInventory().getTopInventory().getHolder() instanceof SellGUI) {
//                player.closeInventory();
//            }
//        });
    }


    /**
     * 初始化插件所需要的容器
     * */
    private void initCollections(){

        globalFishes=new HashMap<>();
        extraFishes=new HashMap<>();
        rarityMap=new HashMap<>();
        rodMap=new HashMap<>();
        extraRarityFishes=new HashMap<>();
        globalRarityFishes=new HashMap<>();
        allFishes=new HashMap<>();
        baitMap=new HashMap<>();

        competitionWorlds=new ArrayList<>();


    }


    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

    /**
     * 重载插件
     * */
    public void reload() {
        terminateSellGUIS();
        initCollections();
        reloadConfig();
        saveDefaultConfig();
        loadAllConfig();
    }


    public Random getRandom() {
        return random;
    }
}

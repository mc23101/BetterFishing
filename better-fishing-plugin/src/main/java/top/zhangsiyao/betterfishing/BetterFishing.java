package top.zhangsiyao.betterfishing;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import top.zhangsiyao.betterfishing.command.Command;
import top.zhangsiyao.betterfishing.config.*;
import top.zhangsiyao.betterfishing.event.*;
import top.zhangsiyao.betterfishing.fishing.FishingProcessor;
import top.zhangsiyao.betterfishing.fishing.FishingTitleEvent;
import top.zhangsiyao.betterfishing.gui.*;
import top.zhangsiyao.betterfishing.item.*;

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

    public static AttachmentFile attachmentFile;

    public static AttachmentSlotFile attachmentSlotFile;

    public static ConfigFile configFile;

    public static MessageFile messageConfig;

    public static Map<String, AttachmentSlot> attachmentSlots;

    public static Map<String,Attachment> attachments;

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
        loadAllConfig();

        listeners();
        commands();

        new PlaceholderReceiver(this).register();

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
        getServer().getPluginManager().registerEvents(new RodAttachmentGUI(),this);
        getServer().getPluginManager().registerEvents(new RodSetAttachmentEvent(),this);
        getServer().getPluginManager().registerEvents(new FishItemsGui(),this);
        getServer().getPluginManager().registerEvents(new RodPrepareEnchantEvent(),this);
        getServer().getPluginManager().registerEvents(new BaitItemCraftEvent(),this);
        getServer().getPluginManager().registerEvents(new RodGui(),this);
        getServer().getPluginManager().registerEvents(new FishingProcessor(),this);
        getServer().getPluginManager().registerEvents(new FishInteractEvent(),this);
        getServer().getPluginManager().registerEvents(new FishGui(),this);
        getServer().getPluginManager().registerEvents(new FishingTitleEvent(),this);
        getServer().getPluginManager().registerEvents(new FishBlockPlaceEvent(),this);
        getServer().getPluginManager().registerEvents(new BaitGui(),this);
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
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();
        //加载配置文件
        configFile = new ConfigFile(this);
        raritiesFile = new RaritiesFile(this);
        fishFile = new FishFile(this);
        baitFile = new BaitFile(this);
        rodFile=new RodFile(this);
        messageConfig=new MessageFile(this);
        attachmentSlotFile=new AttachmentSlotFile(this);
        attachmentFile=new AttachmentFile(this);
        logger.log(Level.INFO,"加载鱼"+allFishes.size()+"个");
        logger.log(Level.INFO,"加载稀有度"+rarityMap.size()+"个");
        logger.log(Level.INFO,"加载鱼饵"+baitMap.size()+"个");
        logger.log(Level.INFO,"加载配件槽"+attachmentSlots.size()+"个");
        logger.log(Level.INFO,"加载配件"+attachments.size()+"个");
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
        attachmentSlots=new HashMap<>();
        attachments=new HashMap<>();
        competitionWorlds=new ArrayList<>();


    }


    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            permission = rsp.getProvider();
        }
    }

    /**
     * 重载插件
     * */
    public void reload() {
        terminateSellGUIS();
        //初始化插件容器
        initCollections();
        loadAllConfig();
    }


    public Random getRandom() {
        return random;
    }
}

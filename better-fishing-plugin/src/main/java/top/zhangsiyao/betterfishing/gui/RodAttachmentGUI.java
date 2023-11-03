package top.zhangsiyao.betterfishing.gui;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import top.zhangsiyao.betterfishing.BetterFishing;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.Attachment;
import top.zhangsiyao.betterfishing.item.AttachmentSlot;
import top.zhangsiyao.betterfishing.item.Rod;
import top.zhangsiyao.betterfishing.utils.FishUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RodAttachmentGUI implements InventoryHolder,Listener{

    private Inventory inventory;

    @Getter
    private Player player;

    private final int[] slots= new int[]{22,10,11,19,20,28,29,15,16,24,25,33,34};

    private Map<Integer, String> slotMap;

    public RodAttachmentGUI(){
        initSlotMap();
    }



    public RodAttachmentGUI(Player player) {
        inventory= Bukkit.createInventory(this,45,"鱼竿配件");
        slotMap=new HashMap<>();
        this.player=player;
        init();
        initSlotMap();
        setSlots();
    }

    private void init(){
        ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = white.getItemMeta();
        meta.setDisplayName("-");
        white.setItemMeta(meta);
        ItemStack yellow = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        yellow.setItemMeta(meta);
        for(int i=0;i<=8;i++){
            inventory.setItem(i,white);
        }
        for(int i=36;i<=44;i++){
            inventory.setItem(i,white);
        }
        for(int i=9;i<=44;i+=9){
            inventory.setItem(i,white);
            inventory.setItem(i+8,white);
        }
        for(int i=12;i<=30;i+=9){
            inventory.setItem(i,yellow);
            inventory.setItem(i+2,yellow);
        }
        inventory.setItem(13,yellow);
        inventory.setItem(31,yellow);
        for(int i:slots){
            ItemStack cancel=new ItemStack(Material.BARRIER);
            inventory.setItem(i,cancel);
        }
    }

    private void initSlotMap(){
        slotMap=new HashMap<>();
        int index=1;
        for (AttachmentSlot value : BetterFishing.attachmentSlots.values()) {
            slotMap.put(slots[index],value.getName());
            index++;
        }
    }

    private void setSlots(){
        inventory.setItem(slots[0],player.getInventory().getItemInMainHand());
        for(int slotIndex:slotMap.keySet()){
            ItemStack mainHand=player.getInventory().getItemInMainHand();
            if(FishUtils.constantAttachment(mainHand,slotMap.get(slotIndex))){
                inventory.setItem(slotIndex,FishUtils.getAttachmentBySlot(mainHand,slotMap.get(slotIndex)).give(player,-1));
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlotClick(InventoryClickEvent event){
        if(!(event.getInventory().getHolder() instanceof RodAttachmentGUI)){
            return;
        }
        event.setCancelled(true);
        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }
        Inventory clickInventory=event.getClickedInventory();
        if(clickInventory==null){
            return;
        }
        Player clicker= (Player) event.getWhoClicked();
        String slotName=slotMap.get(event.getSlot());
        ItemStack mainHand=clicker.getInventory().getItemInMainHand();
        if((clickInventory.getHolder() instanceof RodAttachmentGUI) &&slotMap.containsKey(event.getSlot())){
            if(FishUtils.constantAttachment(mainHand,slotName)){
                Attachment used = FishUtils.getAttachmentBySlot(mainHand, slotName);
                FishUtils.giveItems(Collections.singletonList(used.give(clicker, -1)),clicker);
                NBTItem nbtItem=new NBTItem(mainHand,true);
                NBTCompound attachmentCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND).getOrCreateCompound(NbtConstant.BF_ATTACHMENTS);
                attachmentCompound.removeKey(slotName);
                FishUtils.refreshRodLore(mainHand);
            }
        }else if(clickInventory.getHolder()!=null&&clickInventory.getHolder().getClass().getSimpleName().equals("CraftPlayer")){
            ItemStack attachmentItem=clickInventory.getItem(event.getSlot());
            Rod rod= FishUtils.getRod(mainHand);
            Attachment attachment=FishUtils.getAttachment(attachmentItem);
            if(rod!=null&&attachment!=null){
                NBTItem nbtItem=new NBTItem(mainHand,true);

                if(FishUtils.constantAttachment(mainHand,attachment.getSlot())){
                    Attachment used = FishUtils.getAttachmentBySlot(mainHand, attachment.getSlot());
                    Player player = (Player) event.getWhoClicked();
                    FishUtils.giveItems(Collections.singletonList(used.give(player, -1)),player);
                }

                // 添加配件
                NBTCompound attachmentCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND).getOrCreateCompound(NbtConstant.BF_ATTACHMENTS);
                attachmentCompound.setString(attachment.getSlot(), attachment.getName());
                attachmentItem=clickInventory.getItem(event.getSlot());
                if(attachmentItem!=null){
                    attachmentItem.setAmount(attachmentItem.getAmount()-1);
                }
                FishUtils.refreshRodLore(mainHand);
            }
        }

        clicker.closeInventory();
        clicker.openInventory(new RodAttachmentGUI(clicker).getInventory());
    }

//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onMainHandItemChange(){
//
//    }

}

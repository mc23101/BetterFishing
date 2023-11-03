package top.zhangsiyao.betterfishing.event;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import top.zhangsiyao.betterfishing.constant.NbtConstant;
import top.zhangsiyao.betterfishing.item.Attachment;
import top.zhangsiyao.betterfishing.item.Rod;
import top.zhangsiyao.betterfishing.utils.FishUtils;

import java.util.Collections;

public class RodSetAttachmentEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void process(InventoryClickEvent event){
        if(event.getAction()== InventoryAction.SWAP_WITH_CURSOR){
            ItemStack attachmentItem=event.getCursor();
            ItemStack rodItem=event.getCurrentItem();
            Rod rod= FishUtils.getRod(rodItem);
            Attachment attachment=FishUtils.getAttachment(attachmentItem);
            if(rod!=null&&attachment!=null){
                NBTItem nbtItem=new NBTItem(rodItem);
                int amount=attachmentItem.getAmount();
                if(FishUtils.constantAttachment(rodItem,attachment.getSlot())){
                    Attachment used = FishUtils.getAttachmentBySlot(rodItem, attachment.getSlot());
                    Player player = (Player) event.getWhoClicked();
                    FishUtils.giveItems(Collections.singletonList(used.give(player, -1)),player);
                }

                // 添加配件
                NBTCompound attachmentCompound = nbtItem.getOrCreateCompound(NbtConstant.BF_COMPOUND).getOrCreateCompound(NbtConstant.BF_ATTACHMENTS);
                attachmentCompound.setString(attachment.getSlot(), attachment.getName());
                if(attachmentItem.getAmount()>0){
                    attachmentItem.setAmount(amount-1);
                }
                ItemStack stack=nbtItem.getItem();
                FishUtils.refreshRodLore(stack);
                event.setCurrentItem(stack);
                event.setCancelled(true);
            }
        }
    }

}

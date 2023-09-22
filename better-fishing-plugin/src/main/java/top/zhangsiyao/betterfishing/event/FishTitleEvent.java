package top.zhangsiyao.betterfishing.event;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
public class FishTitleEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private ItemStack fish;

    private PlayerFishEvent.State state;

    private Player player;

    public FishTitleEvent(ItemStack fish, PlayerFishEvent.State state,Player player) {
        this.fish=fish;
        this.state=state;
        this.player=player;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}

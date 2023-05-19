package xyz.larkyy.inventorylibrary.api.ui.history;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.inventorylibrary.api.InventoryHandler;
import xyz.larkyy.inventorylibrary.api.ui.rendered.RenderedMenu;

import java.util.EmptyStackException;
import java.util.Stack;

public class MenuHistory {

    private final Stack<RenderedMenu> history = new Stack<>();
    private final Player player;

    public MenuHistory(Player player) {
        this.player = player;
    }

    public void add(RenderedMenu renderedMenu) {
        history.push(renderedMenu);
    }

    public void openPrevious() {
        JavaPlugin plugin = InventoryHandler.getInstance().getPlugin();

        plugin.getServer().getScheduler().runTask(plugin, () -> {

            try {
                if (this.history.isEmpty()) {
                    player.closeInventory();
                    return;
                }

                var menu = history.pop();
                if (menu == null) {
                    player.closeInventory();
                    return;
                }
                menu.open(player, true);
            } catch (EmptyStackException ignored) {
                if (this.history.isEmpty()) {
                    player.closeInventory();
                    return;
                }

                var menu = history.peek();
                if (menu != null) {
                    menu.close(player);
                    return;
                }

                player.closeInventory();
            }
        });
    }

    public boolean hasPreviousMenu() {
        return !history.empty();
    }

    public void clear() {
        history.clear();
    }

}

package notzStorage.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static notzStorage.Main.started;
import static notzStorage.utils.managers.PlayerManager.addPlayer;
import static notzStorage.utils.managers.PlayerManager.leavePlayer;

public class JoinLeaveEv implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!started)
            return;

        addPlayer(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (!started)
            return;

        leavePlayer(e.getPlayer());
    }
}


package me.deinname;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;

public class HuskHomesBlocker extends JavaPlugin implements Listener {

    private final HashSet<UUID> blockedPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("HuskHomesBlocker aktiviert!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cBenutzung: /" + label + " <Spieler>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("§cSpieler nicht gefunden.");
            return true;
        }
        if (label.equalsIgnoreCase("huskhomesblock")) {
            blockedPlayers.add(target.getUniqueId());
            sender.sendMessage("§a" + target.getName() + " wurde blockiert.");
        } else if (label.equalsIgnoreCase("huskhomesunblock")) {
            blockedPlayers.remove(target.getUniqueId());
            sender.sendMessage("§a" + target.getName() + " wurde freigegeben.");
        }
        return true;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        if (blockedPlayers.contains(player.getUniqueId()) &&
            (message.startsWith("/home") || message.startsWith("/warp") || message.startsWith("/tp") || message.startsWith("/tpa"))) {
            event.setCancelled(true);
            player.sendMessage("§cDu darfst HuskHomes derzeit nicht verwenden.");
        }
    }
}

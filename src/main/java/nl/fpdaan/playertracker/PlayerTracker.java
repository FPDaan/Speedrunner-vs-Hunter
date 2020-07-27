package nl.fpdaan.playertracker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTracker extends JavaPlugin implements Listener, CommandExecutor {
    private Player speedRunner;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("speedrunner").setExecutor(this);

        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()){
                    if (speedRunner != null && player != speedRunner){
                        if (!player.getInventory().contains(Material.COMPASS) && player.getInventory().firstEmpty() != -1){
                            player.getInventory().addItem(new ItemStack(Material.COMPASS));
                        }
                        if (player.getWorld().equals(speedRunner.getWorld())){
                            player.setCompassTarget(speedRunner.getLocation());
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if (event.getPlayer().equals(speedRunner)){
            speedRunner = null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("speedrunner")){
            Player player = (Player) sender;
            if (this.speedRunner == null){
                this.speedRunner = player;
                player.sendMessage("Jij bent de speedrunner");
                player.getInventory().clear();
            } else {
                player.sendMessage("Iemand anders is het al");
            }
        }
        return true;
    }
}

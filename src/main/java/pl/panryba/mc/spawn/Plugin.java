/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.panryba.mc.spawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author PanRyba.pl
 */
public class Plugin extends JavaPlugin {
    
    private Location spawnLocation;

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();
        
        World world = getServer().getWorld(config.getString("world"));
        double x = config.getDouble("x");
        double y = config.getDouble("y");
        double z = config.getDouble("z");
        float yaw = (float)config.getDouble("yaw");
        float pitch = (float)config.getDouble("pitch");
        this.spawnLocation = new Location(world, x, y, z, yaw, pitch);
        
        getCommand("fishspawn").setExecutor(new SpawnCommand(this));
    }

    void teleportToSpawn(Player player) {
        if(!player.hasPermission("fishspawn.free")) {
            PlayerInventory inv = player.getInventory();
        
            int spongeSlot = inv.first(Material.SPONGE);
            if(spongeSlot == -1) {
                player.sendMessage(ChatColor.GRAY +
                        "Musisz posiadac gabke aby teleportowac sie na spawn");
                return;
            }
        
            ItemStack sponge = inv.getItem(spongeSlot);
            if(sponge.getAmount() > 1) {
                sponge.setAmount(sponge.getAmount() - 1);
            } else {
                inv.remove(sponge);
            }
            
            player.sendMessage(ChatColor.BLUE + "Teleportowanie na spawn.. (zuzyles jedna gabke)");
        } else {
            player.sendMessage(ChatColor.BLUE + "Teleportowanie na spawn..");
        }
        
        player.teleport(this.spawnLocation, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
    
}

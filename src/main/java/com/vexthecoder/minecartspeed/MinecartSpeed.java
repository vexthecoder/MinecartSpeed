package com.vexthecoder.minecartspeed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecartSpeed extends JavaPlugin implements Listener, CommandExecutor {

    private double minecartSpeed = 1.0;

    @Override
    public void onEnable() {
        getLogger().info("MinecartSpeed plugin enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("minecartspeed").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MinecartSpeed plugin disabled!");
    }

    @EventHandler
    public void onVehicleUpdate(VehicleUpdateEvent event) {
        Entity entity = event.getVehicle();
        if (entity instanceof Minecart) {
            Minecart minecart = (Minecart) entity;
            if (minecart.getVelocity().length() > 0) {
                minecart.setVelocity(minecart.getVelocity().normalize().multiply(minecartSpeed / 20));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /minecartspeed [bps]");
            return true;
        }

        try {
            double newSpeed = Double.parseDouble(args[0]);
            if (newSpeed <= 0) {
                sender.sendMessage(ChatColor.RED + "Speed must be greater than 0.");
                return true;
            }

            minecartSpeed = newSpeed;
            sender.sendMessage(ChatColor.GREEN + "Minecart speed set to " + minecartSpeed + " blocks-per-second.");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid number format.");
        }

        return true;
    }
}

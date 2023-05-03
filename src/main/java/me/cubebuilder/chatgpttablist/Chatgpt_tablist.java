package me.cubebuilder.chatgpttablist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.List;
public class Chatgpt_tablist extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();
        this.saveDefaultConfig();
        this.config = this.getConfig();

        // Set tablist header and footer for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            setTablistHeaderFooter(player);
        }

        // Register the reload command
        getCommand("tablistreload").setExecutor(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        // Set tablist header and footer back to default for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setPlayerListHeaderFooter(ChatColor.BOLD + "Server Name", "");
        }
    }

    public void reloadConfig() {
        super.reloadConfig();
        this.config = this.getConfig();
        for (Player player : Bukkit.getOnlinePlayers()) {
            setTablistHeaderFooter(player);
        }
    }

    public void setTablistHeaderFooter(Player player) {
        String header = ChatColor.translateAlternateColorCodes('&', config.getString("header"));
        String footer = ChatColor.translateAlternateColorCodes('&', config.getString("footer"));

        // Set tablist header and footer for the player
        player.setPlayerListHeaderFooter(header, footer);
    }

    public static class ReloadCommand implements org.bukkit.command.CommandExecutor {

        private Chatgpt_tablist plugin;

        public ReloadCommand(Chatgpt_tablist plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (sender instanceof Player && !sender.hasPermission("tablist.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Tablist configuration reloaded!");
            return true;
        }
    }
}
package me.jabberjerry.motion.util;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.wrappers.CommandResult;
import me.jabberjerry.motion.commands.OwnerCMD;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class PersistentData {
    public static void loadAnyCommands() throws IOException {
        File myData = getFile("anyCommand");
        Properties properties = new Properties();
        properties.load(new FileInputStream(myData));

        for (String key : properties.stringPropertyNames()) {
            new CommandAPICommand(key)
                    .withPermission(CommandPermission.NONE)
                    .executesPlayer((sender,args) -> {
                        Command executeCMD = Bukkit.getCommandMap().getCommand("execute");
                        CommandResult command = new CommandResult(executeCMD,("as " + sender.getUniqueId() + " at @s run " + properties.get(key)).split(" "));
                        command.execute(Bukkit.createCommandSender(k -> {}));
                    })
                    .register();
        }
    }

    public static HashMap<String,String> getOwnerMap() throws IOException {
        HashMap<String,String> ownerMap = new HashMap<>();
        File myData = getFile("owner");

        Properties properties = new Properties();
        properties.load(new FileInputStream(myData));
        for (String key : properties.stringPropertyNames()) {
            ownerMap.put(key, (String) properties.get(key));
        }
        return ownerMap;
    }

    public static void saveOwnerMap(HashMap<String,String> ownerMap) throws IOException {
        File myData = getFile("owner");
        Properties properties = new Properties();
        properties.putAll(ownerMap);
        properties.store(new FileOutputStream(myData), null);
    }

    private static File getFile(String name) throws IOException {
        new File("plugins/AdvancedCommands").mkdirs();
        File myDisc = new File("plugins/AdvancedCommands/" + name);
        myDisc.createNewFile();
        return myDisc;
    }
}

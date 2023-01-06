package me.jabberjerry.motion;
import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import me.jabberjerry.motion.commands.*;
import me.jabberjerry.motion.listeners.*;
import me.jabberjerry.motion.util.PersistentData;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;

public final class Motion extends JavaPlugin {
    MiniMessage mm;

    @Override
    public void onEnable() {
        registerMiniMessage();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig()
                .initializeNBTAPI(NBTContainer.class, NBTContainer::new)
        );
    }

    @Override
    public void onDisable() {
        try {
            PersistentData.saveOwnerMap(OwnerCMD.ownerMap);
        } catch (IOException e) {
            getLogger().warning("Failed to load data");
        }

        InventoryCMD.onDisable();
        EventCMD.onDisable();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ClickableNBT(), this);;
    }
    private void registerCommands() {
        try {
            PersistentData.loadAnyCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            OwnerCMD.ownerMap = PersistentData.getOwnerMap();
        } catch (IOException e) {
            getLogger().warning("Owner map failed to load");
        }

        new RemoveCMD();
        new MathCMD();
        new DistanceCMD();
        new MotionCMD();
        new ModifyPlayerCMD();
        new DelayCMD(this);
        new RaycastCMD(this);
        getServer().getPluginManager().registerEvents(new OwnerCMD(), this);
        getServer().getPluginManager().registerEvents(new InventoryCMD(mm), this);
        getServer().getPluginManager().registerEvents(new EventCMD(mm), this);
    }
    private void registerMiniMessage() {
        mm = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.clickEvent())
                        .resolver(StandardTags.font())
                        .build()
                )
                .build();
    }
}

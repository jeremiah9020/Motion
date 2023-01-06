package me.jabberjerry.motion.commands;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTEntity;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.HashMap;
import java.util.Map;

public class OwnerCMD implements Listener {
    public static HashMap<String,String> ownerMap = null;
    //                 Entity,Owner
    public OwnerCMD() {
        new CommandTree("owner")
                .withPermission(CommandPermission.OP)
                .then(new LiteralArgument("find")
                        .then(new EntitySelectorArgument.OneEntity("owner")
                                .executes(this::findOwner)
                        )

                )
                .then(new EntitySelectorArgument.OneEntity("entity")
                        .then(new LiteralArgument("set")
                                .then(new EntitySelectorArgument.OneEntity("owner")
                                        .executes(this::setOwner)
                                )
                        )
                        .then(new LiteralArgument("remove")
                                .executes(this::removeOwner)
                        )
                        .then(new LiteralArgument("get")
                                .executes(this::getOwner)
                        )
                ).register();

        new CommandTree("summon")
                .then(new EntityTypeArgument("entity")
                        .then(new LocationArgument("position")
                                .then(new NBTCompoundArgument<NBTContainer>("nbt")
                                        .then(new LiteralArgument("owner")
                                                .then(new EntitySelectorArgument.OneEntity("owner")
                                                        .executes(this::summonOwner)
                                                )
                                        )
                                )
                        )
                ).register();
    }

    private void setOwner(CommandSender sender, Object[] args) {
        Entity entity = (Entity) args[0];
        if (entity instanceof Player) {
            if (sender instanceof Player) sender.sendMessage(ChatColor.RED + "Player can not be owned");
            return;
        }
        Entity owner = (Entity) args[1];
        ownerMap.put(entity.getUniqueId().toString(),owner.getUniqueId().toString());

        sender.sendMessage(owner.getName() + " now owns " + entity.getName());
    }

    private void removeOwner(CommandSender sender, Object[] args) {
        Entity entity = (Entity) args[0];
        if (entity instanceof Player) return;
        String result = ownerMap.remove(entity.getUniqueId().toString());

        if (sender instanceof  Player) {
            if (result == null) sender.sendMessage(entity.getName() + " was not owned");
            else sender.sendMessage(entity.getName() + " was freed");
        }
    }

    private void getOwner(CommandSender sender, Object[] args) {
        Entity entity = (Entity) args[0];
        if (entity instanceof Player) {
            if (sender instanceof Player) sender.sendMessage(ChatColor.RED + "Player can not be owned");
            return;
        }
        String owner = ownerMap.get(entity.getUniqueId().toString());
        if (owner == null) {
            if (sender instanceof Player) {
                sender.sendMessage(entity.getName() + " is not owned");
            }
            return;
        }

        for(World w: Bukkit.getWorlds()) {
            for(Entity e: w.getEntities()){
                if (e.getUniqueId().toString().equals(owner)) {
                    if (sender instanceof Player) {
                        sender.sendMessage(entity.getName() + " is owned by " + e.getName() + " (given tag owner.result)");
                    }
                    e.addScoreboardTag("owner.result");
                    return;
                }
            }
        }
    }

    private void findOwner(CommandSender sender, Object[] args) {
        int count = 0;
        Entity owner = (Entity) args[0];
        for (Map.Entry<String,String> e : ownerMap.entrySet())
        {
            if (e.getValue().equals(owner.getUniqueId().toString())) {
                for(World w: Bukkit.getWorlds()) {
                    for(Entity entity: w.getEntities()){
                        if (entity.getUniqueId().toString().equals(e.getKey())) {
                            entity.addScoreboardTag("owner.result");
                            break;
                        }
                    }
                }
                count++;
            }
        }
        if (sender instanceof Player) {
            if (count == 0) sender.sendMessage("Found no entities belonging to " + owner.getName());
            else if (count == 1) sender.sendMessage("Found " + count + " entity (given tag owner.result) belonging to "+ owner.getName());
            else sender.sendMessage("Found " + count + " entities (given tag owner.result) belonging to "+ owner.getName());
        }
    }

    private void summonOwner(CommandSender sender, Object[] args) {
        EntityType entityType = (EntityType) args[0];
        Location location = (Location) args[1];
        NBTContainer nbt = (NBTContainer) args[2];
        Entity owner = (Entity) args[3];

        Entity newEntity = location.getWorld().spawnEntity(location,entityType);
        NBTEntity nbtEntity = new NBTEntity(newEntity);
        nbtEntity.mergeCompound(nbt);

        ownerMap.put(newEntity.getUniqueId().toString(),owner.getUniqueId().toString());

        if (sender instanceof Player) {
            sender.sendMessage("Summoned new " + newEntity.getName() + " owned by " + owner.getName());
        }
    }

    @EventHandler
    private void onEntityRemoval(EntityRemoveFromWorldEvent event) {
        ownerMap.remove(event.getEntity().getUniqueId().toString());
    }
}

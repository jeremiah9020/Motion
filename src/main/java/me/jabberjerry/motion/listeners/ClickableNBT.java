package me.jabberjerry.motion.listeners;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import dev.jorel.commandapi.wrappers.CommandResult;
import me.jabberjerry.motion.util.Pair;
import me.jabberjerry.motion.util.Quadruple;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClickableNBT implements Listener {
    @EventHandler
    private void onClick(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (player.isFrozen()) executeCommand(getClickCommands(item,"Frozen"),event,player);
        if (player.isFlying()) executeCommand(getClickCommands(item,"Flying"),event,player);
        if (player.isGliding()) executeCommand(getClickCommands(item,"Gliding"),event,player);
        if (player.isClimbing()) executeCommand(getClickCommands(item,"Climbing"),event,player);
        if (player.isSwimming()) executeCommand(getClickCommands(item,"Swimming"),event,player);
        if (player.isSneaking()) executeCommand(getClickCommands(item,"Sneaking"),event,player);
        if (player.isSprinting()) executeCommand(getClickCommands(item,"Sprinting"),event,player);
        if (player.isUnderWater()) executeCommand(getClickCommands(item,"UnderWater"),event,player);
        if (player.isInRain()) executeCommand(getClickCommands(item,"InRain"),event,player);
        if (player.isInLava()) executeCommand(getClickCommands(item,"InLava"),event,player);
        if (player.isInWater()) executeCommand(getClickCommands(item,"InWater"),event,player);
        if (player.isInPowderedSnow()) executeCommand(getClickCommands(item,"InPoweredSnow"),event,player);
        if (player.isInBubbleColumn()) executeCommand(getClickCommands(item,"InBubbleColumn"),event,player);
        executeCommand(getClickCommands(item,"Default"),event,player);

    }
    private void executeCommand(Pair<String,String> commands,PlayerInteractEvent event, Player player) {
        if (commands == null) return;

        final Command executeCMD = Bukkit.getCommandMap().getCommand("execute");
        CommandResult right = new CommandResult(executeCMD,("as " + player.getUniqueId() + " at @s run " + commands.first).split(" "));
        CommandResult left = new CommandResult(executeCMD,("as " + player.getUniqueId() + " at @s run " + commands.second).split(" "));

        if(event.getAction() == Action.RIGHT_CLICK_AIR){
            if (!commands.first.equals("")) right.execute(Bukkit.createCommandSender(k -> {}));
        } else if(event.getAction() == Action.LEFT_CLICK_AIR && event.getHand() == EquipmentSlot.HAND){
            if (!commands.second.equals("")) left.execute(Bukkit.createCommandSender(k -> {}));
        } else if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.OFF_HAND){
            if (!commands.first.equals("")) right.execute(Bukkit.createCommandSender(k -> {}));
        } else if(event.getAction() == Action.LEFT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND){
            if (!commands.second.equals("")) left.execute(Bukkit.createCommandSender(k -> {}));
        }
    }

    private Pair<String,String> getClickCommands(ItemStack item, String type) {
        if (item == null) return null;
        NBTItem nbti = new NBTItem(item);
        NBTCompound clickable = nbti.getCompound("Clickable");
        if (clickable == null) return null;

        NBTCompound typed = clickable.getCompound(type);
        if (typed == null) return null;

        String right = typed.getString("R");
        String left = typed.getString("L");
        return new Pair<>(right,left);
    }
}


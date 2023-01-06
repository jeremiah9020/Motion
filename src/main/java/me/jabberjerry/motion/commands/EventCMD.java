package me.jabberjerry.motion.commands;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.CommandResult;
import me.jabberjerry.motion.util.Pad;
import me.jabberjerry.motion.util.Quadruple;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.*;
import java.util.*;

public class EventCMD implements Listener {
    private enum EventTypes {
            RIGHT_CLICK_AIR,
            LEFT_CLICK_AIR,
            RIGHT_CLICK_BLOCK,
            LEFT_CLICK_BLOCK,
            RIGHT_CLICK_ENTITY,
            LEFT_CLICK_ENTITY,
            START_SNEAK,
            START_SPRINT,
            START_FLY,
            END_SNEAK,
            END_SPRINT,
            END_FLY,
            JUMP,
            JOIN,
            LEAVE
    }

    MiniMessage mm;
    public EventCMD(MiniMessage mm) {
        this.mm = mm;

        //Get Event types
        ArrayList<String> pEventTypes = new ArrayList<>();
        for (EventTypes e : EventTypes.values()) {
            pEventTypes.add(e.toString());
        }
        String[] events = pEventTypes.toArray(new String[pEventTypes.size()]);

        new CommandTree("event")
                .withPermission(CommandPermission.OP)
                .then(new LiteralArgument("list")
                        .executes(this::list)
                ).then(new LiteralArgument("remove")
                        .then(new NamespacedKeyArgument("name")
                                .executes(this::remove)
                        )
                ).then(new LiteralArgument("add")
                        .then(new NamespacedKeyArgument("name")
                                .then(new StringArgument("type").replaceSuggestions(ArgumentSuggestions.strings(events))
                                        .then(new CommandArgument("command")
                                                .executes(this::add)
                                        )
                                        .then(new MultiLiteralArgument("cancelling")
                                                .then(new CommandArgument("command")
                                                        .executes(this::add)
                                                )
                                        )
                                )
                        )
                ).register();
    }
    // EventList = {minecraft:[{name:"",type:"",reward:""}]}
    private final HashMap<String, ArrayList<Quadruple<String,EventTypes,CommandResult,Boolean>>> EventList = new HashMap<>();
    // TypeList = {type:[{namespace:"",name:"",reward:""}]}
    private static final HashMap<EventTypes, ArrayList<Quadruple<String,String,CommandResult,Boolean>>> TypeList = new HashMap<>();

    private void list(CommandSender sender, Object[] args) {
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "\nALL EVENTS");
        EventList.forEach((k,v) -> {
            String namespace = ChatColor.GOLD + "Namespace: " + ChatColor.YELLOW + k;
            sender.sendMessage(namespace);

            for (Quadruple<String,EventTypes,CommandResult,Boolean> s : v) {
                String listIcon = (v.indexOf(s) != v.size() - 1) ? "╠⇒" : "╚⇒";
                String cancelling = (s.fourth) ? " <red>cancelling" : " <yellow>non-cancelling";

                String frs = Pad.right(s.first,' ',20);
                String sec = Pad.right(s.second.toString(),' ',20);
                String command = "\"/event remove " + k + ":" + s.first + "\"";

                sender.sendMessage(mm.deserialize("<font:minecraft:uniform><click:run_command:" + command + "><red>[X]</click><gold> " + listIcon + "<dark_green> Name: <green> " + frs + " <dark_green> Type: <green>" + sec + cancelling));
            }
            sender.sendMessage("\n");
        });
    }
    private void remove(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        if (nk == null) return;

        String namespace = nk.namespace();
        String name = nk.getKey();

        ArrayList<Quadruple<String, EventTypes, CommandResult,Boolean>> namespaceArray = EventList.get(namespace);
        if (namespaceArray == null) return;

        for (Quadruple<String, EventTypes, CommandResult,Boolean> s: namespaceArray) {
            if (name.equals(s.first)) {
                EventTypes type = s.second;

                ArrayList<Quadruple<String, String, CommandResult,Boolean>> typeArray = TypeList.get(type);
                typeArray.removeIf(t -> name.equals(t.second) && namespace.equals(t.first));
                if (typeArray.size() == 0) TypeList.remove(type);

                namespaceArray.remove(s);
                if (namespaceArray.size() == 0) EventList.remove(namespace);

                break;
            }
        }

        Player player = (Player) sender;
        player.performCommand("event list");
    }
    private void add(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        EventTypes eventType = EventTypes.valueOf((String) args[1]);

        Boolean cancel;
        CommandResult cmd;
        if (args[2] instanceof CommandResult) {
            cancel = false;
            cmd = (CommandResult) args[2];
        }
        else {
            cancel = true;
            cmd = (CommandResult) args[3];
        }

        String namespace = nk.namespace();
        String name = nk.getKey();

        EventList.computeIfAbsent(namespace, k -> new ArrayList<>());
        ArrayList<Quadruple<String, EventTypes, CommandResult, Boolean>> newNamespace = EventList.get(namespace);

        for (Quadruple<String, EventTypes, CommandResult,Boolean> k : newNamespace) {
            if (name.equals(k.first)) {
                sender.sendMessage(ChatColor.RED + "Event under that namespace and name already registered.");
                return;
            }
        }

        newNamespace.add(new Quadruple<>(name,eventType,cmd,cancel));

        TypeList.computeIfAbsent(eventType, k -> new ArrayList<>());
        TypeList.get(eventType).add(new Quadruple<>(namespace,name,cmd,cancel));
    }
    private static void execute(Player player, CommandResult cmd, Entity target) {
        final Command executeCMD = Bukkit.getCommandMap().getCommand("execute");
        final String newCmd = cmd.command().getLabel();
        final String[] newArgs = cmd.args();
        if (target != null) target.addScoreboardTag("event.result");

        List<String> list = new ArrayList<>();
        list.add("as");
        list.add(player.getUniqueId().toString());
        list.add("at");
        list.add("@s");
        list.add("run");
        list.add(newCmd);
        list.addAll(Arrays.asList(newArgs));
        String[] cmdArgs = list.toArray(new String[0]);
        CommandResult toRun = new CommandResult(executeCMD,cmdArgs);
        toRun.execute(Bukkit.createCommandSender(k -> {}));

        //player.sendMessage(toRun.args());

        if (target != null) target.removeScoreboardTag("event.result");
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR){
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.RIGHT_CLICK_AIR);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        }
        else if(event.getAction() == Action.LEFT_CLICK_AIR && event.getHand() == EquipmentSlot.HAND){
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.LEFT_CLICK_AIR);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        }
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.OFF_HAND){
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.RIGHT_CLICK_BLOCK);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        }
        else if(event.getAction() == Action.LEFT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND){
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.LEFT_CLICK_BLOCK);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        }
    }
    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND){
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.RIGHT_CLICK_ENTITY);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,event.getRightClicked());
                if (k.fourth) event.setCancelled(true);
            });
        }
    }
    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player player){
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.LEFT_CLICK_ENTITY);
            if (handlers != null) handlers.forEach((k) -> {
                execute(player,k.third,event.getEntity());
                if (k.fourth) event.setCancelled(true);
            });
        }
    }
    @EventHandler
    private void onPlayerJump(PlayerJumpEvent event) {
        ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.JUMP);
        if (handlers != null) handlers.forEach((k) -> {
            execute(event.getPlayer(),k.third,null);
            if (k.fourth) event.setCancelled(true);
        });
    }
    @EventHandler
    private void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if (event.isFlying()) {
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.START_FLY);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        } else {
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.END_FLY);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        }
    }
    @EventHandler
    private void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.START_SNEAK);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        } else {
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.END_SNEAK);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        }
    }
    @EventHandler
    private void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        if (event.isSprinting()) {
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.START_SPRINT);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        } else {
            ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.END_SPRINT);
            if (handlers != null) handlers.forEach((k) -> {
                execute(event.getPlayer(),k.third,null);
                if (k.fourth) event.setCancelled(true);
            });
        }
    }

    @EventHandler
    private void OnPlayerJoin(PlayerJoinEvent event) {
        ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.JOIN);
        if (handlers != null) handlers.forEach((k) -> {
            execute(event.getPlayer(),k.third,null);
        });
    }

    @EventHandler
    private void OnPlayerQuitEvent(PlayerQuitEvent event) {
        ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.LEAVE);
        if (handlers != null) handlers.forEach((k) -> {
            execute(event.getPlayer(),k.third,null);
        });
    }

    @EventHandler
    private void OnPlayerKick(PlayerKickEvent event) {
        ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.LEAVE);
        if (handlers != null) handlers.forEach((k) -> {
            execute(event.getPlayer(),k.third,null);
        });
    }


    public static void onDisable() {
        Bukkit.getWorlds().forEach(world -> {
            world.getPlayers().forEach(player -> {
                ArrayList<Quadruple<String, String, CommandResult,Boolean>> handlers = TypeList.get(EventTypes.LEAVE);
                if (handlers != null) handlers.forEach((k) -> {
                    execute(player,k.third,null);
                });
            });
        });
    }

}

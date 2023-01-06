package me.jabberjerry.motion.commands;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.CommandResult;
import me.jabberjerry.motion.util.Pad;
import me.jabberjerry.motion.util.Quadruple;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryCMD implements Listener {
    private final MiniMessage mm;

    Inventory test;

    public InventoryCMD(MiniMessage mm) {
        this.mm = mm;

        test = Bukkit.createInventory(null,18);
        ItemStack item = new ItemStack(Material.BARRIER);
        test.setItem(4,item);

        new CommandTree("inventory")
                .withPermission(CommandPermission.OP)
                .then(new LiteralArgument("list")
                        .executes(this::list)
                )
                .then(new LiteralArgument("create")
                        .then(new NamespacedKeyArgument("name")
                                .then(new IntegerArgument("rows",1,6)
                                        .then(new ChatComponentArgument("displayName")
                                                .executes(this::create)
                                        )
                                )
                        )
                )
                .then(new LiteralArgument("copy")
                        .then(new NamespacedKeyArgument("source")
                                .then(new NamespacedKeyArgument("destination")
                                        .executes(this::copy)
                                )
                        )
                )
                .then(new LiteralArgument("open")
                        .then(new NamespacedKeyArgument("name")
                                .then(new EntitySelectorArgument.OnePlayer("player")
                                        .executes(this::open)
                                )
                        )
                )
                .then(new LiteralArgument("apply")
                        .then(new NamespacedKeyArgument("name")
                                .then(new EntitySelectorArgument.OnePlayer("player")
                                        .executes(this::apply)
                                )
                        )
                )
                .then(new LiteralArgument("reset")
                        .then(new EntitySelectorArgument.OnePlayer("player")
                                .executes(this::reset)
                        )
                )
                .then(new LiteralArgument("remove")
                        .then(new NamespacedKeyArgument("name")
                                .executes(this::remove)
                        )
                )
                .then(new LiteralArgument("modify")
                        .then(new NamespacedKeyArgument("name")
                                .then(new LiteralArgument("edit")
                                        .executes(this::modifyEdit)
                                )
                                .then(new LiteralArgument("size")
                                        .then(new IntegerArgument("rows",1,6)
                                                .executes(this::modifySize)
                                        )
                                )
                                .then(new LiteralArgument("displayName")
                                        .then(new ChatComponentArgument("displayName")
                                                .executes(this::modifyName)
                                        )
                                )
                                .then(new LiteralArgument("slot")
                                        .then(new IntegerArgument("slot",0,53)
                                                .then(new LiteralArgument("with")
                                                        .then(new ItemStackArgument("item")
                                                                .executes(this::modifySlotWith)
                                                        )
                                                )
                                                .then(new LiteralArgument("from")
                                                        .then(new EntitySelectorArgument.OnePlayer("player")
                                                                .then(new MultiLiteralArgument("mainhand","offhand")
                                                                        .executes(this::modifySlotFrom)
                                                                )
                                                        )
                                                )
                                                .then(new LiteralArgument("command")
                                                        .then(new CommandArgument("command")
                                                                .executes(this::modifySlotCommand)
                                                        )
                                                )
                                                .then(new LiteralArgument("clear")
                                                        .then(new LiteralArgument("command")
                                                                .executes(this::modifySlotClearCommand)
                                                        )
                                                        .executes(this::modifySlotClear)
                                                )
                                        )
                                )
                        )
                ).register();
    }


    private static final HashMap<UUID,HashMap<Integer, CommandResult>> appliedPlayers = new HashMap<>();
    private final HashMap<String, ArrayList<Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>>>> Inventories = new HashMap<>();

    private void apply(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        Player player = (Player) args[1];

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> listItem = getInventoriesListItem(nk);
        if (listItem == null) return;

        int size = listItem.third.getSize();
        if (size > 36) return;

        player.getInventory().setContents(listItem.third.getContents());
        appliedPlayers.put(player.getUniqueId(),listItem.fourth);
    }
    private void reset(CommandSender sender, Object[] args) {
        Player player = (Player) args[0];
        appliedPlayers.remove(player.getUniqueId());
        player.getInventory().clear();
    }
    private void list(CommandSender sender, Object[] args) {
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "\nALL Inventories");
        Inventories.forEach((k,v) -> {
            String namespace = ChatColor.GOLD + "Namespace: " + ChatColor.YELLOW + k;
            sender.sendMessage(namespace);

            for (Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> s : v) {
                String listIcon = (v.indexOf(s) != v.size() - 1) ? "╠⇒" : "╚⇒";

                String frs = Pad.right(s.first,' ',20);
                String sec = Pad.right(getPlainText(s.second),' ',20);
                String last = "" + s.third.getSize()/9;

                sender.sendMessage(mm.deserialize("<font:minecraft:uniform><gold> " + listIcon + "<dark_green> Name: <green> " + frs + " <dark_green> Display: <green>" + sec + " <dark_green> Rows: <green>" + last));
            }
            sender.sendMessage("\n");
        });
    }
    private void remove(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> item = getInventoriesListItem(nk);
        if (item == null) return;

        Inventories.get(nk.namespace()).remove(item);
        if (Inventories.get(nk.namespace()).size() == 0) Inventories.remove(nk.namespace());
    }
    private void create(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        int size = (int) args[1];
        BaseComponent[] json = (BaseComponent[]) args[2];

        String displayName = getLegacyText(json);

        String namespace = nk.namespace();
        String name = nk.getKey();

        Inventories.computeIfAbsent(namespace, k -> new ArrayList<>());

        ArrayList<Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>>> newNamespace = Inventories.get(namespace);

        for (Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> k : newNamespace) {
            if (name.equals(k.first)) {
                sender.sendMessage(ChatColor.RED + "Inventory under that namespace and name already created.");
                return;
            }
        }

        Inventory newInventory = Bukkit.createInventory(null,9*size, displayName);
        newNamespace.add(new Quadruple<>(name,json,newInventory,new HashMap<>()));
    }

    private void copy(CommandSender sender, Object[] args) {
        NamespacedKey srcNK = (NamespacedKey) args[0];
        NamespacedKey dstNK = (NamespacedKey) args[1];

        ArrayList<Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>>> namespace = Inventories.get(srcNK.namespace());
        for (Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> k : namespace) {
            if (srcNK.getKey().equals(k.first)) {
                Inventories.computeIfAbsent(dstNK.getNamespace(), n -> new ArrayList<>());
                ArrayList<Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>>> newNamespace = Inventories.get(dstNK.getNamespace());

                for (Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> s : newNamespace) {
                    if (dstNK.getKey().equals(k.first)) {
                        sender.sendMessage(ChatColor.RED + "Inventory under that namespace and name already exists.");
                        return;
                    }
                }

                newNamespace.add(new Quadruple<>(dstNK.getKey(),k.second,k.third,k.fourth));
                return;
            }
        }
    }

    private void open(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        Player player = (Player) args[1];

        if (getInventoriesListItem(nk) == null) return;
        player.openInventory(getInventoriesListItem(nk).third);
    }
    private void modifyEdit(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> item = getInventoriesListItem(nk);
        if (item == null) return;

        Player player = (Player) sender;
        player.addScoreboardTag("inventory.edit");
        player.openInventory(item.third);

    }
    private void modifySize(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        int rows = (int) args[1];

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> item = getInventoriesListItem(nk);
        if (item == null) return;

        Inventory newInventory = Bukkit.createInventory(null,rows*9,getLegacyText(item.second));

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> newItem = new Quadruple<>(item.first,item.second,newInventory,new HashMap<>());

        Inventories.get(nk.namespace()).remove(item);
        Inventories.get(nk.namespace()).add(newItem);
    }
    private void modifyName(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        BaseComponent[] json = (BaseComponent[]) args[1];

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> item = getInventoriesListItem(nk);
        if (item == null) return;

        Inventory newInventory = Bukkit.createInventory(null,item.third.getSize(),getLegacyText(json));

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> newItem = new Quadruple<>(item.first,json,newInventory,new HashMap<>());

        Inventories.get(nk.namespace()).remove(item);
        Inventories.get(nk.namespace()).add(newItem);
    }

    private void modifySlotWith(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        int slot = (int) args[1];
        ItemStack item = (ItemStack) args[2];


        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> listItem = getInventoriesListItem(nk);
        if (listItem == null) return;

        int size = listItem.third.getSize();
        if (slot > size - 1) return;

        listItem.third.setItem(slot,item);
    }
    private void modifySlotFrom(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        int slot = (int) args[1];
        Player player = (Player) args[2];
        String from = (String) args[3];


        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> listItem = getInventoriesListItem(nk);
        if (listItem == null) return;

        int size = listItem.third.getSize();
        if (slot > size - 1) return;

        EquipmentSlot hand = (from.equals("mainhand")) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
        listItem.third.setItem(slot,player.getInventory().getItem(hand));
    }
    private void modifySlotCommand(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        int slot = (int) args[1];
        CommandResult cmd = (CommandResult) args[2];

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> listItem = getInventoriesListItem(nk);
        if (listItem == null) return;

        int size = listItem.third.getSize();
        if (slot > size - 1) return;

        listItem.fourth.put(slot,cmd);
    }
    private void modifySlotClearCommand(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        int slot = (int) args[1];

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> listItem = getInventoriesListItem(nk);
        if (listItem == null) return;

        int size = listItem.third.getSize();
        if (slot > size - 1) return;

        listItem.fourth.remove(slot);
    }
    private void modifySlotClear(CommandSender sender, Object[] args) {
        NamespacedKey nk = (NamespacedKey) args[0];
        int slot = (int) args[1];

        Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> listItem = getInventoriesListItem(nk);
        if (listItem == null) return;

        int size = listItem.third.getSize();
        if (slot > size - 1) return;

        listItem.third.setItem(slot,new ItemStack(Material.AIR));
        listItem.fourth.remove(slot);
    }

    private Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> getInventoriesListItem(NamespacedKey nk) {
        String namespace = nk.namespace();
        String name = nk.getKey();

        ArrayList<Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>>> newNamespace = Inventories.get(namespace);
        if (newNamespace == null) return null;

        for (Quadruple<String, BaseComponent[], Inventory, HashMap<Integer, CommandResult>> k : newNamespace) {
            if (name.equals(k.first)) {
                return k;
            }
        }
        return null;
    }
    private String getLegacyText(BaseComponent[] component) {
        StringBuilder displayName = new StringBuilder();
        for (BaseComponent s : component) {
            displayName.append(s.toLegacyText());
        }
        return displayName.toString();
    }
    private String getPlainText(BaseComponent[] component) {
        StringBuilder displayName = new StringBuilder();
        for (BaseComponent s : component) {
            displayName.append(s.toPlainText());
        }
        return displayName.toString();
    }

    private void execute(Player player, CommandResult cmd) {
        final Command executeCMD = Bukkit.getCommandMap().getCommand("execute");
        final String newCmd = cmd.command().getLabel();
        final String[] newArgs = cmd.args();

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
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (appliedPlayers.containsKey(event.getWhoClicked().getUniqueId())) {
            if (event.getClickedInventory() == event.getWhoClicked().getInventory()) {
                event.setCancelled(true);

                int slot = event.getSlot();
                HashMap<Integer, CommandResult> item = appliedPlayers.get(event.getWhoClicked().getUniqueId());

                CommandResult cmd = item.get(slot);
                if (cmd == null) return;

                execute((Player) event.getWhoClicked(),cmd);
            }
        }

        if (event.getWhoClicked().getScoreboardTags().contains("inventory.edit")) return;
        Inventory toTest = event.getInventory();
        Inventories.forEach((k,v)-> {
            v.forEach(q -> {
                if (q.third == toTest) {
                    if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        event.setCancelled(true);
                    }

                    if (event.getClickedInventory() == toTest) {
                        event.setCancelled(true);
                        int slot = event.getSlot();

                        CommandResult cmd = q.fourth.get(slot);
                        if (event.getInventory().getItem(slot) != null) {
                            event.getWhoClicked().closeInventory();
                            if (cmd != null) {
                                execute((Player) event.getWhoClicked(),cmd);
                            }
                        }
                    }
                }
            });
        });
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked().getScoreboardTags().contains("inventory.edit")) return;

        Inventory toTest = event.getInventory();
        Inventories.forEach((k,v)-> {
            v.forEach(q -> {
                if (q.third == toTest) {
                    event.setCancelled(true);
                }
            });
        });
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        event.getPlayer().removeScoreboardTag("inventory.edit");
    }
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (appliedPlayers.containsKey(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (appliedPlayers.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            player.updateInventory();

        }
    }
    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (appliedPlayers.containsKey(player.getUniqueId())) {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
    }
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (appliedPlayers.containsKey(player.getUniqueId())) {
            appliedPlayers.remove(player.getUniqueId());
            player.getInventory().clear();
        }
    }

    public static void onDisable() {
        Bukkit.getWorlds().forEach(world -> {
            world.getPlayers().forEach(player -> {
                if (appliedPlayers.containsKey(player.getUniqueId())) {
                    appliedPlayers.remove(player.getUniqueId());
                    player.getInventory().clear();
                }
            });
        });
    }
}


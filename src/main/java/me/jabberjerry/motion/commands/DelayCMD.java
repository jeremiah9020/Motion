package me.jabberjerry.motion.commands;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.CommandArgument;
import dev.jorel.commandapi.arguments.TimeArgument;
import dev.jorel.commandapi.wrappers.CommandResult;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DelayCMD {
    private final JavaPlugin plugin;
    public DelayCMD(JavaPlugin plugin) {
        this.plugin = plugin;

        new CommandTree("delay")
                .withPermission(CommandPermission.OP).then(
                    new TimeArgument("time").then(
                            new CommandArgument("command").executesNative((sender,args)->{
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        Location location = sender.getLocation();
                                        double pitch = Math.toRadians(location.getPitch());
                                        double yaw = Math.toRadians(location.getYaw());

                                        double x = -Math.cos(yaw) * Math.sin(pitch);
                                        double y = -Math.sin(yaw);
                                        double z = Math.cos(yaw) * Math.cos(pitch);




                                        CommandResult command = (CommandResult) args[1];

                                        LivingEntity target = getTarget(sender);
                                        String UUID = target == null? null: target.getUniqueId().toString();
                                        Command newCommand = Bukkit.getCommandMap().getCommand("execute");
                                        String newCmd = command.command().getLabel();
                                        final String[] newArgs = command.args();

                                        List<String> list = new ArrayList<String>();
                                        if (UUID != null) {
                                            list.add("as");
                                            list.add(UUID);
                                        }
                                        list.add("positioned");
                                        list.add(String.valueOf(location.getX()));
                                        list.add(String.valueOf(location.getY()));
                                        list.add(String.valueOf(location.getZ()));

                                        list.add("facing");
                                        list.add(String.valueOf(location.getX() + x));
                                        list.add(String.valueOf(location.getY() + y));
                                        list.add(String.valueOf(location.getZ() + z));


                                        list.add("run");
                                        list.add(newCmd);
                                        list.addAll(Arrays.asList(newArgs));
                                        String[] cmdArgs = list.toArray(new String[0]);
                                        CommandResult toRun = new CommandResult(newCommand,cmdArgs);
                                        toRun.execute(Bukkit.createCommandSender(k -> {}));
                                    }
                                }.runTaskLater(this.plugin, (long) (int) args[0]);
                            })
                    )
                )
                .register();
    }

    private LivingEntity getTarget(NativeProxyCommandSender sender) {
        if (!(sender.getCallee() instanceof LivingEntity entity)) return null;
        return entity;
    }
}

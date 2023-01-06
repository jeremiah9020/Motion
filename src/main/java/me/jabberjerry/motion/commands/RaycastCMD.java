package me.jabberjerry.motion.commands;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.CommandResult;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Predicate;

public class RaycastCMD {
   public final JavaPlugin plugin;
    public RaycastCMD(JavaPlugin plugin) {
        this.plugin = plugin;
        new CommandTree("raycast")
                .withPermission(CommandPermission.OP)
                .then(new DoubleArgument("distance",1,512)
                        .then(new LiteralArgument("during")
                                .then(new CommandArgument("command").executesNative(this::handlerInDuring)))
                        .then(new LiteralArgument("after")
                                .then(new CommandArgument("command").executesNative(this::handlerInAfter))
                        )
                        .then(new DoubleArgument("precision",0.01,5)
                                .then(new LiteralArgument("during")
                                        .then(new CommandArgument("command").executesNative(this::handlerInDuring)))
                                .then(new LiteralArgument("after")
                                        .then(new CommandArgument("command").executesNative(this::handlerInAfter))
                                )
                                .then(new DoubleArgument("modifier",0.1,2)
                                        .then(new LiteralArgument("during")
                                                .then(new CommandArgument("command").executesNative(this::handlerInDuring)))
                                        .then(new LiteralArgument("after")
                                                .then(new CommandArgument("command").executesNative(this::handlerInAfter))
                                        )
                                        .then(new LongArgument("delay",0,60)
                                                .then(new LiteralArgument("during")
                                                        .then(new CommandArgument("command").executesNative(this::handlerInDuring)))
                                                .then(new LiteralArgument("after")
                                                        .then(new CommandArgument("command").executesNative(this::handlerInAfter))
                                                )
                                                .then(new LiteralArgument("in")
                                                    .then(new LiteralArgument("during")
                                                            .then(new CommandArgument("command").executesNative(this::handlerInDuring)))
                                                    .then(new LiteralArgument("after")
                                                            .then(new CommandArgument("command").executesNative(this::handlerInAfter))
                                                    )
                                                    .then(new BlockPredicateArgument("allowable")
                                                            .then(new LiteralArgument("during")
                                                                    .then(new CommandArgument("command").executesNative(this::handlerInDuring)))
                                                            .then(new LiteralArgument("after")
                                                                    .then(new CommandArgument("command").executesNative(this::handlerInAfter))
                                                            )
                                                            .then(new EntitySelectorArgument.ManyEntities("targeted")
                                                                    .then(new LiteralArgument("during")
                                                                            .then(new CommandArgument("command").executesNative(this::handlerInDuring))
                                                                    )
                                                                    .then(new LiteralArgument("after")
                                                                            .then(new CommandArgument("command").executesNative(this::handlerInAfter))
                                                                    ))))
                                                .then(new LiteralArgument("on")
                                                    .then(new LiteralArgument("during")
                                                            .then(new CommandArgument("command").executesNative(this::handlerOnDuring)))
                                                    .then(new LiteralArgument("after")
                                                            .then(new CommandArgument("command").executesNative(this::handlerOnAfter))
                                                    )
                                                    .then(new BlockPredicateArgument("allowable")
                                                            .then(new LiteralArgument("during")
                                                                    .then(new CommandArgument("command").executesNative(this::handlerOnDuring)))
                                                            .then(new LiteralArgument("after")
                                                                    .then(new CommandArgument("command").executesNative(this::handlerOnAfter))
                                                            )
                                                            .then(new EntitySelectorArgument.ManyEntities("targeted")
                                                                    .then(new LiteralArgument("during")
                                                                            .then(new CommandArgument("command").executesNative(this::handlerOnDuring)))
                                                                    .then(new LiteralArgument("after")
                                                                            .then(new CommandArgument("command").executesNative(this::handlerOnAfter))
                                                                    )
                                                            )
                                                    )
                                                )
                                    )
                            )
                        )
                )
                .register();
    }
    private void handlerInAfter(NativeProxyCommandSender sender, Object[] args) {
        LivingEntity target = getTarget(sender);
        if (target == null) return;

        boolean in = true;
        boolean after = true;
        Map<String,Object> parameters = getParameters(args);
        raycast(sender,target,parameters,in,after);
    }
    private void handlerInDuring(NativeProxyCommandSender sender, Object[] args) {
        LivingEntity target = getTarget(sender);
        if (target == null) return;

        boolean in = true;
        boolean after = false;
        Map<String,Object> parameters = getParameters(args);
        raycast(sender,target,parameters,in,after);
    }
    private void handlerOnAfter(NativeProxyCommandSender sender, Object[] args) {
        LivingEntity target = getTarget(sender);
        if (target == null) return;

        boolean in = false;
        boolean after = true;
        Map<String,Object> parameters = getParameters(args);
        raycast(sender,target,parameters,in,after);
    }
    private void handlerOnDuring(NativeProxyCommandSender sender, Object[] args) {
        LivingEntity target = getTarget(sender);
        if (target == null) return;

        boolean in = false;
        boolean after = false;
        Map<String,Object> parameters = getParameters(args);
        raycast(sender,target,parameters,in,after);
    }

    private LivingEntity getTarget(NativeProxyCommandSender sender) {
        if (!(sender.getCallee() instanceof LivingEntity entity)) return null;
       return entity;
    }
    private Map<String,Object> getParameters(Object[] args) {
        Map<String,Object> parameters = new HashMap<>();

        parameters.put("distance", args[0]);
        parameters.put("precision",(args.length > 2)? args[1]: 0.125);
        parameters.put("modifier",(args.length > 3)? args[2]: 1.0);
        parameters.put("delay",(args.length > 4)? args[3]: 0L);
        parameters.put("allowable",(args.length > 5)? args[4]: null);
        parameters.put("targeted",(args.length > 6)? args[5]: null);
        parameters.put("command", args[args.length-1]);

        return parameters;
    }

    private void raycast(NativeProxyCommandSender sender, LivingEntity target, Map<String,Object> parameters, boolean in, boolean after) {
        Location ray = sender.getLocation();

        double pitch = Math.toRadians(ray.getPitch());
        double yaw = Math.toRadians(ray.getYaw());

        double x = -Math.cos(yaw) * Math.sin(pitch);
        double y = -Math.sin(yaw);
        double z = Math.cos(yaw) * Math.cos(pitch);

        Vector unit = new Vector(x,y,z);

        CommandResult command = (CommandResult) parameters.get("command");
        double precision = (double) parameters.get("precision");
        double distance = (double) parameters.get("distance");
        double modifier = (double) parameters.get("modifier");
        long delay = (long) parameters.get("delay");

        @SuppressWarnings("unchecked")
        Predicate<Block> allowable = (Predicate<Block>) parameters.get("allowable");
        if (allowable == null) allowable = block -> {
            boolean success = false;
            if (block.getType() == Material.AIR) success = true;
            else if (block.getType() == Material.CAVE_AIR) success = true;
            else if (block.getType() == Material.VOID_AIR) success = true;
            else if (block.getType() == Material.BARRIER) success = true;
            return success;
        };

        Predicate<Block> allow = allowable;
        @SuppressWarnings("unchecked")
        Collection<Entity> targeted = (Collection<Entity>) parameters.get("targeted");

       BukkitRunnable task = new BukkitRunnable()
       {
           private Entity hit;
           private final CommandMap map = Bukkit.getCommandMap();
           private final Command newCommand = map.getCommand("execute");
           final String newCmd = command.command().getLabel();
           final String[] newArgs = command.args();
           private double currentDistance = 0.0;
           private double deltaX = unit.getX() * precision;
           private double deltaY = unit.getY() * precision;
           private double deltaZ = unit.getZ() * precision;

           private boolean finish = false;

           public boolean checkForNearbyEntities() {
               Collection<Entity> nearby = ray.getWorld().getNearbyEntities(ray,8,8,8);
               nearby.retainAll(targeted);

               for (Entity e: nearby) {
                   BoundingBox bb = e.getBoundingBox();

                   RayTraceResult rr = bb.rayTrace(ray.toVector(),unit,precision);
                   if (rr != null) {
                      hit = e;
                      return true;
                   }
               }
               return false;
           }

           public void run() {
               if (!after) {
                   this.runCommand();
               }

               ray.add(deltaX,deltaY,deltaZ);
               currentDistance += precision;

               Block currentBlock = ray.getBlock();
               if (!allow.test(currentBlock)) finish = true;
               if (currentDistance >= distance) finish = true;
               if (Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2) + Math.pow(deltaZ,2)) < 0.01) finish = true;
               if (targeted != null && this.checkForNearbyEntities()) finish = true;

               deltaX *= modifier;
               deltaY *= modifier;
               deltaZ *= modifier;

               if (finish) this.end();
               if (!finish && delay == 0) this.run();
           }
           private void runCommand() {
               if (hit != null) hit.addScoreboardTag("raycast.result");
               target.addScoreboardTag("raycast.sender");

               String x = String.valueOf(ray.getX());
               String y = String.valueOf(ray.getY());
               String z = String.valueOf(ray.getZ());

               String xForward = String.valueOf(unit.getX() + ray.getX());
               String yForward = String.valueOf(unit.getY() + ray.getY());
               String zForward = String.valueOf(unit.getZ() + ray.getZ());

               List<String> list = new ArrayList<>();
               list.add("as");
               list.add(target.getUniqueId().toString());
               list.add("positioned");
               list.add(x);
               list.add(y);
               list.add(z);
               list.add("facing");
               list.add(xForward);
               list.add(yForward);
               list.add(zForward);
               list.add("run");
               list.add(newCmd);
               list.addAll(Arrays.asList(newArgs));
               String[] cmdArgs = list.toArray(new String[0]);
               CommandResult toRun = new CommandResult(newCommand,cmdArgs);
               toRun.execute(Bukkit.createCommandSender(k -> {}));

               if (hit != null) hit.removeScoreboardTag("raycast.result");
               target.removeScoreboardTag("raycast.sender");

           }
           private void end() {

               if (after) {
                   if (!in) ray.subtract(deltaX, deltaY, deltaZ);
                   this.runCommand();
               }
               //ray.getBlock().setType(Material.STONE);
               if (delay > 0 ) this.cancel();
           }
       };
       if (delay > 0)
           task.runTaskTimer(plugin,0,delay);
       else task.run();
    }
}

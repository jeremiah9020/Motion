package me.jabberjerry.motion.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.ExecutorType;
import me.jabberjerry.motion.util.Pair;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import static java.lang.Math.max;
import static java.lang.Math.min;
public class MotionCMD {
     private enum commandTypes {
         BLOCK,
         ENTITY,
         SCORE,
         VALUE
     }
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    public MotionCMD() {
        new CommandTree("motion")
                .withPermission(CommandPermission.OP)
                .then(new EntitySelectorArgument.OneEntity("target")
                        .then(new AxisArgument("axis")
                                .then(new MultiLiteralArgument("add","set")
                                        .then(new LiteralArgument("towards")
                                                .then(new LiteralArgument("block")
                                                        .then(new LocationArgument("location")
                                                                .then(new DoubleArgument("scale")
                                                                        .executes((sender, args) -> {
                                                                            this.commandParser(sender,args,commandTypes.BLOCK);
                                                                        })
                                                                )
                                                                .then(new MultiLiteralArgument("scaledByScore")
                                                                        .then(new ScoreHolderArgument.Single("scalar name")
                                                                                .then(new ObjectiveArgument("scalar objective")
                                                                                        .then(new DoubleArgument("scale")
                                                                                                .executes((sender, args) -> {
                                                                                                    this.commandParser(sender,args,commandTypes.BLOCK);
                                                                                                })
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                                .then(new LiteralArgument("entity")
                                                        .then(new EntitySelectorArgument.OneEntity("entity")
                                                                .then(new DoubleArgument("scale")
                                                                        .executes((sender, args) -> {
                                                                            this.commandParser(sender,args,commandTypes.ENTITY);
                                                                        })
                                                                )
                                                                .then(new MultiLiteralArgument("scaledByScore")
                                                                        .then(new ScoreHolderArgument.Single("scalar name")
                                                                                .then(new ObjectiveArgument("scalar objective")
                                                                                        .then(new DoubleArgument("scale")
                                                                                                .executes((sender, args) -> {
                                                                                                    this.commandParser(sender,args,commandTypes.ENTITY);
                                                                                                })
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                        .then(new LiteralArgument("from")
                                                .then(new LiteralArgument("score")
                                                        .then(new ScoreHolderArgument.Single("name")
                                                                .then(new ObjectiveArgument("objective")
                                                                        .then(new DoubleArgument("scale")
                                                                                .executes((sender, args) -> {
                                                                                    this.commandParser(sender,args,commandTypes.SCORE);
                                                                                })
                                                                        )
                                                                        .then(new MultiLiteralArgument("scaledByScore")
                                                                                .then(new ScoreHolderArgument.Single("scalar name")
                                                                                        .then(new ObjectiveArgument("scalar objective")
                                                                                                .then(new DoubleArgument("scale")
                                                                                                        .executes((sender, args) -> {
                                                                                                            this.commandParser(sender,args,commandTypes.SCORE);
                                                                                                        })
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                        .then(new ScoreHolderArgument.Single("name 2")
                                                                                .then(new ObjectiveArgument("objective 2")
                                                                                        .then(new DoubleArgument("scale")
                                                                                                .executes((sender, args) -> {
                                                                                                    this.commandParser(sender,args,commandTypes.SCORE);
                                                                                                })
                                                                                        )
                                                                                        .then(new MultiLiteralArgument("scaledByScore")
                                                                                                .then(new ScoreHolderArgument.Single("scalar name")
                                                                                                        .then(new ObjectiveArgument("scalar objective")
                                                                                                                .then(new DoubleArgument("scale")
                                                                                                                        .executes((sender, args) -> {
                                                                                                                            this.commandParser(sender,args,commandTypes.SCORE);
                                                                                                                        })
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                        .then(new ScoreHolderArgument.Single("name 3")
                                                                                                .then(new ObjectiveArgument("objective 3")
                                                                                                        .then(new DoubleArgument("scale")
                                                                                                                .executes((sender, args) -> {
                                                                                                                    this.commandParser(sender,args,commandTypes.SCORE);
                                                                                                                })
                                                                                                        )
                                                                                                        .then(new MultiLiteralArgument("scaledByScore")
                                                                                                                .then(new ScoreHolderArgument.Single("scalar name")
                                                                                                                        .then(new ObjectiveArgument("scalar objective")
                                                                                                                                .then(new DoubleArgument("scale")
                                                                                                                                        .executes((sender, args) -> {
                                                                                                                                            this.commandParser(sender,args,commandTypes.SCORE);
                                                                                                                                        })
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                        .then(new LiteralArgument("value")
                                                .then(new DoubleArgument("first")
                                                        .executes((sender, args) -> {
                                                            this.commandParser(sender,args,commandTypes.VALUE);
                                                        })
                                                        .then(new MultiLiteralArgument("scaledByScore")
                                                                .then(new ScoreHolderArgument.Single("scalar name")
                                                                        .then(new ObjectiveArgument("scalar objective")
                                                                                .then(new DoubleArgument("scale")
                                                                                        .executes((sender, args) -> {
                                                                                            this.commandParser(sender,args,commandTypes.VALUE);
                                                                                        })
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                        .then(new DoubleArgument("second")
                                                                .executes((sender, args) -> {
                                                                    this.commandParser(sender,args,commandTypes.VALUE);
                                                                })
                                                                .then(new MultiLiteralArgument("scaledByScore")
                                                                        .then(new ScoreHolderArgument.Single("scalar name")
                                                                                .then(new ObjectiveArgument("scalar objective")
                                                                                        .then(new DoubleArgument("scale")
                                                                                                .executes((sender, args) -> {
                                                                                                    this.commandParser(sender,args,commandTypes.VALUE);
                                                                                                })
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                                .then(new DoubleArgument("third")
                                                                        .executes((sender, args) -> {
                                                                            this.commandParser(sender,args,commandTypes.VALUE);
                                                                        })
                                                                        .then(new MultiLiteralArgument("scaledByScore")
                                                                                .then(new ScoreHolderArgument.Single("scalar name")
                                                                                        .then(new ObjectiveArgument("scalar objective")
                                                                                                .then(new DoubleArgument("scale")
                                                                                                        .executes((sender, args) -> {
                                                                                                            this.commandParser(sender,args,commandTypes.VALUE);
                                                                                                        })
                                                                                                )
                                                                                        )
                                                                                )
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
    private void commandParser(CommandSender logger, Object[] args, commandTypes type) {
        if (!(args[0] instanceof LivingEntity target)) return;
        @SuppressWarnings("unchecked")
        EnumSet<Axis> axis = (EnumSet<Axis>) args[1];
        String mode = (String) args[2];

        Pair<Integer,Double> scaledBy = getScaledBy(args);
        int lastValidIndex = (int) scaledBy.first;
        double scale = (double) scaledBy.second;

        switch (type) {
            case BLOCK -> {
                Location towards = (Location) args[3];
                Vector direction = getDirectionalVector(scale,target.getLocation(),towards);

                if (mode.equals("add"))
                    addVelocity(target,direction,axis);
                else
                    setVelocity(target,direction,axis);
            }
            case SCORE -> {
                Objective objective;
                Score score;

                double x = 0;
                double y = 0;
                double z = 0;

                if (lastValidIndex >= 4) {
                    objective = scoreboard.getObjective((String) args[4]);
                    score = objective.getScore((String) args[3]);

                    x = scale * score.getScore();;
                    y = scale * score.getScore();;
                    z = scale * score.getScore();;
                }
                if (lastValidIndex >= 6) {
                    objective = scoreboard.getObjective((String) args[6]);
                    score = objective.getScore((String) args[5]);

                    y = scale * score.getScore();
                    z = scale * score.getScore();
                }
                if (lastValidIndex >= 8) {
                    objective = scoreboard.getObjective((String) args[8]);
                    score = objective.getScore((String) args[7]);

                    z = scale * score.getScore();
                }

                Vector direction = new Vector(x,y,z);

                if (mode.equals("add"))
                    addVelocity(target,direction, axis);
                else
                    setVelocity(target,direction, axis);
            }
            case ENTITY -> {
                Location towards = ((Entity) args[3]).getLocation();
                Vector direction = getDirectionalVector(scale,target.getLocation(),towards);

                if (mode.equals("add"))
                    addVelocity(target,direction,axis);
                else
                    setVelocity(target,direction,axis);
            }
            case VALUE -> {
                double x = 0;
                double y = 0;
                double z = 0;
                if (lastValidIndex == args.length - 1) scale = 1;

                if (lastValidIndex >= 3) {
                    x = scale * (double) args[3];
                    y = scale * (double) args[3];
                    z = scale * (double) args[3];
                }
                if (lastValidIndex >= 4) {
                    y = scale * (double) args[4];
                    z = scale * (double) args[4];

                }
                if (lastValidIndex >= 5) {
                   z = scale * (double) args[5];
                }

                Vector direction = new Vector(x,y,z);

                if (mode.equals("add"))
                    addVelocity(target,direction,axis);
                else
                    setVelocity(target,direction,axis);
            }
        }
    }
    private Pair<Integer,Double> getScaledBy(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String name) {
                if (name.equals("scaledByScore")) {
                    Objective objective = scoreboard.getObjective((String) args[i+2]);
                    Score score = objective.getScore((String) args[i+1]);
                    return new Pair<>(i - 1,(double) args[i+3] * score.getScore());
                }
            }
        }
        return new Pair<>(args.length - 1,(double) args[args.length - 1]);
    }
    private static Vector getDirectionalVector(double scale, Location from, Location to) {
        double x = scale * (to.getX() - from.getX());
        double y = scale * (to.getY() - from.getY());
        double z = scale * (to.getZ() - from.getZ());
        return new Vector(x,y,z);
    }
    private static void setVelocity(LivingEntity target, Vector direction, EnumSet<Axis> axis){
        double totalPower = direction.length();
        double launchingPower = totalPower > 5 ? 5 : totalPower;
        Vector normalizedDirection = direction.normalize();
        Vector scaledDirection = normalizedDirection.multiply(launchingPower);
        target.setVelocity(new Vector(axis.contains(Axis.X)? scaledDirection.getX() : 0, axis.contains(Axis.Y)? scaledDirection.getY() : 0, axis.contains(Axis.Z)? scaledDirection.getZ() : 0));
    }
    private static void addVelocity(LivingEntity target, Vector direction, EnumSet<Axis> axis){
        double xToAdd = axis.contains(Axis.X) ? direction.getX() : 0;
        double yToAdd = axis.contains(Axis.Y) ? direction.getY() : 0;
        double zToAdd = axis.contains(Axis.Z) ? direction.getZ() : 0;
        Vector newDirection = new Vector(xToAdd,yToAdd,zToAdd);
        Vector addedDirection = target.getVelocity().add(newDirection);
        double totalPower = addedDirection.length();
        double launchingPower = totalPower > 5 ? 5 : totalPower;
        Vector normalizedDirection = addedDirection.normalize();
        Vector scaledDirection = normalizedDirection.multiply(launchingPower);
        target.setVelocity(scaledDirection);
    }
}
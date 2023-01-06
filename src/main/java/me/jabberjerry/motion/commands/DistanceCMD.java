package me.jabberjerry.motion.commands;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class DistanceCMD {
    public DistanceCMD() {
        new CommandTree("distance")
                .withPermission(CommandPermission.OP)
                .then(new LocationArgument("start block")
                        .then(new LocationArgument("end block")
                                .then(new DoubleArgument("scale")
                                        .executes((sender, args) -> {
                                            Location start = (Location) args[0];
                                            Location end = (Location) args[1];
                                            double scale = (double) args[2];

                                            int distance = (int) Math.round(scale * getDistance(start,end));
                                            return distance;
                                        })
                                )
                        )
                        .then(new EntitySelectorArgument.OneEntity("end entity")
                                .then(new DoubleArgument("scale")
                                        .executes((sender, args) -> {
                                            Location start = (Location) args[0];
                                            Location end = ((Entity) args[1]).getLocation();
                                            double scale = (double) args[2];

                                            int distance = (int) Math.round(scale * getDistance(start,end));
                                            return distance;
                                        })
                                )
                        )
                )
                .then(new EntitySelectorArgument.OneEntity("start entity")
                        .then(new LocationArgument("end block")
                                .then(new DoubleArgument("scale")
                                        .executes((sender, args) -> {
                                            Location start = ((Entity) args[0]).getLocation();
                                            Location end = (Location) args[1];
                                            double scale = (double) args[2];

                                            int distance = (int) Math.round(scale * getDistance(start,end));
                                            return distance;
                                        })
                                )
                        )
                        .then(new EntitySelectorArgument.OneEntity("end entity")
                                .then(new DoubleArgument("scale")
                                        .executes((sender, args) -> {
                                            Location start = ((Entity) args[0]).getLocation();
                                            Location end = ((Entity) args[1]).getLocation();
                                            double scale = (double) args[2];


                                            int distance = (int) Math.round(scale * getDistance(start,end));
                                            return distance;
                                        })
                                )
                        )
                )
                .register();
    }

    public double getDistance(Location to, Location from) {
        double x = to.getX() - from.getX();
        double y = to.getY() - from.getY();
        double z = to.getZ() - from.getZ();
        return Math.sqrt(Math.pow(x,2.0) + Math.pow(y,2) + Math.pow(z,2));
    }
}

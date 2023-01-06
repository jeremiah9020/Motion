package me.jabberjerry.motion.commands;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;



public class ModifyPlayerCMD {

    public ModifyPlayerCMD() {
        new CommandTree("modifyplayer")
                .withPermission(CommandPermission.OP)
                .then(new EntitySelectorArgument.OnePlayer("player")
                        .then(new LiteralArgument("bedSpawnLocation").then(new LocationArgument("value").executes((sender, args) -> {((Player) args[0]).setBedSpawnLocation((Location) args[1]);})))
                        .then(new LiteralArgument("compassTarget").then(new LocationArgument("value").executes((sender, args) -> {((Player) args[0]).setCompassTarget((Location) args[1]);})))
                        .then(new LiteralArgument("Exp").then(new FloatArgument("value",0,1).executes((sender, args) -> {((Player) args[0]).setExp((float) args[1]);})))
                        .then(new LiteralArgument("Flying").then(new BooleanArgument("value").executes((sender, args) -> {((Player) args[0]).setFlying((boolean) args[1]);})))
                        .then(new LiteralArgument("FlySpeed").then(new FloatArgument("value").executes((sender, args) -> {((Player) args[0]).setFlySpeed((float) args[1]);})))
                        .then(new LiteralArgument("HealthScale").then(new DoubleArgument("value").executes((sender, args) -> {((Player) args[0]).setHealthScale((double) args[1]);})))
                        .then(new LiteralArgument("Level").then(new IntegerArgument("value").executes((sender, args) -> {((Player) args[0]).setLevel((int) args[1]);})))
                        .then(new LiteralArgument("Sneaking").then(new BooleanArgument("value").executes((sender, args) -> {((Player) args[0]).setSneaking((boolean) args[1]);})))
                        .then(new LiteralArgument("Sprinting").then(new BooleanArgument("value").executes((sender, args) -> {((Player) args[0]).setSprinting((boolean) args[1]);})))
                        .then(new LiteralArgument("TotalExperience").then(new IntegerArgument("value").executes((sender, args) -> {((Player) args[0]).setTotalExperience((int) args[1]);})))
                        .then(new LiteralArgument("WalkSpeed").then(new FloatArgument("value",-1,1).executes((sender, args) -> {((Player) args[0]).setWalkSpeed((float) args[1]);})))
                        .then(new LiteralArgument("allowFlight").then(new BooleanArgument("value").executes((sender, args) -> {((Player) args[0]).setAllowFlight((boolean) args[1]);})))
                )
                .register();

    }
}

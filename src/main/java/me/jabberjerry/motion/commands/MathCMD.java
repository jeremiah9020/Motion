package me.jabberjerry.motion.commands;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.ObjectiveArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class MathCMD {
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    public MathCMD() {
        new CommandTree("math")
                .withPermission(CommandPermission.OP)
                .then(new MultiLiteralArgument("sqrt","sin","cos","tan","!")
                        .then(new ScoreHolderArgument.Single("name")
                                .then(new ObjectiveArgument("objective")
                                        .executes(this::applyOperation)
                                )
                        )
                ).register();
    }

    private void applyOperation(CommandSender sender, Object[] args) {
        String operation = (String) args[0];
        String name = (String) args[1];
        String obj = (String) args[2];

        Objective objective = scoreboard.getObjective(obj);
        Score score = objective.getScore(name);

        int output = 0, input = score.getScore();

        switch (operation) {
            case "sqrt" -> {
                input *= 100;
                int modifier = (input < 0) ? -1 : 1;
                input *= modifier;
                output = (int) Math.round((Math.sqrt(input)) / 10);
                output *= modifier;
            }
            case "sin" -> {
               output = (int) Math.round(Math.sin(Math.toRadians(input)));
            }
            case "cos" -> {
                output = (int) Math.round(Math.cos(Math.toRadians(input)));
            }
            case "tan" -> {
                output = (int) Math.round(Math.tan(Math.toRadians(input)));
            }
            case "!" -> {
                output = (int) input * -1;
            }
        }
        score.setScore(output);
    }
}

package me.jabberjerry.motion.commands;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class RemoveCMD {
    public RemoveCMD() {
        new CommandTree("remove")
                .withPermission(CommandPermission.OP)
                .then(new EntitySelectorArgument.ManyEntities("entities")
                        .executes(this::remove)
                ).register();
    }

    private void remove(CommandSender sender, Object[] args) {
        @SuppressWarnings("unchecked")
        Collection<Entity> entities = (Collection<Entity>) args[0];

        entities.forEach(entity -> {
            if (!(entity instanceof Player)) entity.remove();
        });
    }
}

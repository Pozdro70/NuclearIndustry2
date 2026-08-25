package com.pozdro.nuclearindustry.command;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandNI2 extends CommandBase {

    @Override
    public String getName() {
        return "ni2";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/ni2 <rr>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;   //dziala dla kazdego chyhba
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 0 && args[0].equalsIgnoreCase("rr")) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft.getMinecraft().refreshResources();
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "NI2: Reloaded JSONs and Textures!"));
            });
        } else { //help command (/ni2)
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN +
                    "List of command from Nuclear Industry:\n " +
                    "/ni2 rr - resource reload (Same as F3+T)"));
        }
    }
}
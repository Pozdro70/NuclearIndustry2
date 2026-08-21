package com.pozdro.nuclearindustry.command;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandReloadResources extends CommandBase {

    @Override
    public String getName() {
        return "rr";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/rr";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Minecraft.getMinecraft().refreshResources();
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Reloaded JSONs and Textures!"));
        });
    }
}
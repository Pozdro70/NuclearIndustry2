package com.pozdro.nuclearindustry.command;

import com.pozdro.nuclearindustry.NuclearIndustry;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

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
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("rr")) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    FMLClientHandler.instance().refreshResources();
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "NI2: Reloaded JSONs and textures!"));
                    NuclearIndustry.LOGGER.info("NI2: Reloaded JSONs and textures!");
                });
            } else if (args[0].equalsIgnoreCase("nbt")) {
                if (!(sender instanceof EntityPlayer)) {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Only players can use this command."));
                    return;
                }
                EntityPlayer player = (EntityPlayer) sender;
                double reach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
                RayTraceResult ray = player.rayTrace(reach, 1.0F);

                if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
                    TileEntity te = player.world.getTileEntity(ray.getBlockPos());

                    if (te != null) {
                        NBTTagCompound nbt = te.writeToNBT(new NBTTagCompound());

                        player.sendMessage(new TextComponentString(
                                TextFormatting.GREEN + "NBT for " + te.getBlockType().getLocalizedName() + " at " + ray.getBlockPos() + ":"
                        ));
                        player.sendMessage(new TextComponentString(TextFormatting.AQUA + nbt.toString()));
                    } else {
                        player.sendMessage(new TextComponentString(TextFormatting.RED + "This block is not a TileEntity (No NBT data)."));
                    }
                } else {
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "You are not looking at a block!"));
                }
                return;
            }
        }
        //help command (/ni2)
        sender.sendMessage(new TextComponentString(TextFormatting.GREEN +
                "List of command from Nuclear Industry:\n " +
                "/ni2 rr - resource reload (Same as F3+T)\n" +
                "/ni2 nbt - dump nbt of a tileEntity"));

    }
}
package com.pozdro.nuclearindustry.block.tile;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.block.custom.BasicMachineBlock;
import com.pozdro.nuclearindustry.recipe.FluidIngredient;
import com.pozdro.nuclearindustry.recipe.FluidTankMachineRecipe;
import com.pozdro.nuclearindustry.recipe.ItemIngredient;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.upgrade.IUpgradeItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class LeacherTileEntity extends TileEntity implements ITickable, IHasInventory, ISettableTank, IHasProgressAndEnergy {

    private IItemHandler getItemHandlerForSide(@Nullable EnumFacing facing) {
        return new IItemHandler() {
            @Override
            public int getSlots() {
                return inventory.getSlots();
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return inventory.getStackInSlot(slot);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (facing == EnumFacing.UP) {
                    return inventory.insertItem(2, stack, simulate); //2-input slot
                }
                return stack;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (facing != EnumFacing.UP && facing != null) {
                    ItemStack fromSlot3 = inventory.extractItem(3, amount, simulate);
                    if (!fromSlot3.isEmpty()) {
                        return fromSlot3;
                    }
                    return inventory.extractItem(4, amount, simulate); //3,4 output slots
                }
                return ItemStack.EMPTY;
            }

            @Override
            public int getSlotLimit(int slot) {
                return inventory.getSlotLimit(slot);
            }
        };
    }

    private IFluidHandler getFluidHandlerForSide(@Nullable EnumFacing facing) {
        return new IFluidHandler() {
            @Override
            public IFluidTankProperties[] getTankProperties() {
                // combine both tanks' properties so pipes can see both when querying
                IFluidTankProperties[] inProps = tankIn.getTankProperties();
                IFluidTankProperties[] outProps = tankOut.getTankProperties();
                IFluidTankProperties[] combined = new IFluidTankProperties[inProps.length + outProps.length];
                System.arraycopy(inProps, 0, combined, 0, inProps.length);
                System.arraycopy(outProps, 0, combined, inProps.length, outProps.length);
                return combined;
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if (facing == null || facing != EnumFacing.DOWN) {
                    return tankIn.fill(resource, doFill);
                }
                return 0;
            }

            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {
                if (facing == EnumFacing.DOWN) {
                    return tankOut.drain(resource, doDrain);
                }
                return null;
            }

            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                if (facing == EnumFacing.DOWN) {
                    return tankIn.drain(maxDrain, doDrain);
                }
                return null;
            }
        };
    }


    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandlerForSide(facing));
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidHandlerForSide(facing));
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        if(capability== CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;

        return super.hasCapability(capability,facing);
    }

    public static final List<FluidTankMachineRecipe> RECIPES = new ArrayList<>();

    public static final int INVENTORY_SIZE = 11;

    public final ItemStackHandler inventory = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

            if (slot == 0) {
                IFluidHandlerItem handler =
                        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (handler == null) {
                    return false;
                }

                FluidStack fluid = handler.getTankProperties()[0].getContents();
                return fluid != null && fluid.amount > 0;
            }
            if(slot==1||slot==3||slot==4||slot==6){
                return false;
            }
            if(slot==2){

                //TODO: nie działa
                boolean anyRecipeMatch=false;

                for (FluidTankMachineRecipe r : RECIPES) {
                    for (ItemIngredient ing : r.inputs()) {
                        if (ing.stack().isItemEqual(stack)) {
                            anyRecipeMatch = true;
                            break;
                        }
                    }

                    if (anyRecipeMatch) {
                        break;
                    }
                }

            }
            if(slot==5){
                IFluidHandlerItem handler =
                        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

                return handler == null
                        || handler.getTankProperties()[0].getContents() == null
                        || handler.getTankProperties()[0].getContents().amount <= 0;
            }
            if(slot==7||slot==8||slot==9||slot==10){
                return stack.getItem() instanceof IUpgradeItem;
            }


            return true;
        }
    };

    private boolean isRunning = false;


    public final FluidTank tankIn = new FluidTank(10000) { //ID = 0
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    public final FluidTank tankOut = new FluidTank(10000) { //ID = 1
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    private static final Map<Integer, Map.Entry<Integer, Integer>> guiSlots = new HashMap<>();
    static {
        guiSlots.put(0, new AbstractMap.SimpleEntry<>(22, 18));
        guiSlots.put(1, new AbstractMap.SimpleEntry<>(22, 50));
        guiSlots.put(2, new AbstractMap.SimpleEntry<>(74, 21));
        guiSlots.put(3, new AbstractMap.SimpleEntry<>(105, 72));
        guiSlots.put(4, new AbstractMap.SimpleEntry<>(124, 72));
        guiSlots.put(5, new AbstractMap.SimpleEntry<>(129, 18));
        guiSlots.put(6, new AbstractMap.SimpleEntry<>(129, 50));

        //upgrade slots
        guiSlots.put(7, new AbstractMap.SimpleEntry<>(152, 21));
        guiSlots.put(8, new AbstractMap.SimpleEntry<>(152, 39));
        guiSlots.put(9, new AbstractMap.SimpleEntry<>(152, 57));
        guiSlots.put(10, new AbstractMap.SimpleEntry<>(152, 75));
    }

    private static final Map<Integer, Map.Entry<Integer, Integer>> tankSize = new HashMap<>();
    static {
        tankSize.put(0, new AbstractMap.SimpleEntry<>(48, 16));
        tankSize.put(1, new AbstractMap.SimpleEntry<>(48, 16));
    }

    private static final Map<Integer, Map.Entry<Integer, Integer>> tankPos = new HashMap<>();
    static {
        tankPos.put(0, new AbstractMap.SimpleEntry<>(47, 18));
        tankPos.put(1, new AbstractMap.SimpleEntry<>(105, 18));
    }


    public static void renderGUI(){//FMLInit
        NetworkRegistry.INSTANCE.registerGuiHandler(NuclearIndustry.instance, new BasicMachineGuiHandler<>(
                guiSlots,
                new ResourceLocation(NuclearIndustry.MODID, "textures/gui/leachergui.png"),
                LeacherTileEntity.class,
                "Leacher",
                true,
                26,
                37,
                65,39,
                176,0,
                176,
                181,
                15,
                tankSize,
                tankPos,
                9 ,25,
                56
        ));
        GameRegistry.registerTileEntity(LeacherTileEntity.class, new ResourceLocation(NuclearIndustry.MODID, "leacher"));
    }


    private int progress = 0;
    private int maxProgress = 100;

    @Override
    public void dropInventory() {
        InventoryHelper.dropInventoryItems(world, pos, new InventoryHandlerWrapper(
                inventory,
                "container.leacher")
        );
    }

    @Override
    public ItemStackHandler getInventoryHandler() {
        return inventory;
    }

    public final BasicSink energy = new BasicSink(this,10000,1);
    private static int clientEnergy;

    @Override
    public BasicSink getEnergySink() {
        return energy;
    }

    @Override
    public void setClientEnergy(int cenergy) {
        clientEnergy=cenergy;
        if(clientEnergy>energy.getCapacity()){
            clientEnergy= (int) energy.getCapacity();
        }
    }

    @Override
    public int getClientEnergy() {
        return clientEnergy;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if(!world.isRemote){energy.onLoad();}
    }

    @Override
    public void invalidate() {
        if(!world.isRemote){energy.invalidate();}

        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if(!world.isRemote){energy.onChunkUnload();}

        super.onChunkUnload();
    }

    private final Random random = new Random();

    @Override
    public void update() {
        if (world.isRemote) {
            return;
        }


        ItemStack inputStack = inventory.getStackInSlot(0);

        if (!inputStack.isEmpty()) {

            ItemStack singleInput = inputStack.copy();
            singleInput.setCount(1);

            IFluidHandlerItem inputHandler =
                    singleInput.getCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                            null
                    );

            if (inputHandler != null) {

                FluidStack inputFluid =
                        inputHandler.drain(Integer.MAX_VALUE, false);

                if (inputFluid != null && inputFluid.amount > 0) {

                    boolean compatible =
                            tankIn.getFluid() == null ||
                                    inputFluid.isFluidEqual(tankIn.getFluid());

                    if (compatible) {

                        int accepted =
                                tankIn.fill(inputFluid, false);

                        if (accepted > 0) {

                            FluidStack drained =
                                    inputHandler.drain(accepted, false);

                            if (drained != null && drained.amount > 0) {

                                ItemStack containerCopy = singleInput.copy();

                                IFluidHandlerItem realInputHandler =
                                        containerCopy.getCapability(
                                                CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                                                null
                                        );

                                FluidStack actualDrained =
                                        realInputHandler.drain(
                                                drained.amount,
                                                true
                                        );

                                if (actualDrained != null &&
                                        actualDrained.amount > 0) {

                                    ItemStack emptyContainer =
                                            realInputHandler.getContainer();

                                    ItemStack remainder =
                                            inventory.insertItem(
                                                    1,
                                                    emptyContainer,
                                                    true
                                            );

                                    if (remainder.isEmpty()) {

                                        tankIn.fill(
                                                actualDrained,
                                                true
                                        );

                                        inventory.extractItem(
                                                0,
                                                1,
                                                false
                                        );

                                        inventory.insertItem(
                                                1,
                                                emptyContainer,
                                                false
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        ItemStack outputStack = inventory.getStackInSlot(5);

        if (!outputStack.isEmpty() &&
                tankOut.getFluid() != null &&
                tankOut.getFluidAmount() > 0) {

            ItemStack singleOutput = outputStack.copy();
            singleOutput.setCount(1);

            IFluidHandlerItem outputHandler =
                    singleOutput.getCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                            null
                    );

            if (outputHandler != null) {

                FluidStack tankFluid =
                        tankOut.getFluid().copy();

                int accepted =
                        outputHandler.fill(
                                tankFluid,
                                false
                        );

                accepted = Math.min(
                        accepted,
                        tankOut.getFluidAmount()
                );

                if (accepted > 0) {

                    FluidStack simulatedFluid =
                            tankFluid.copy();

                    simulatedFluid.amount = accepted;

                    outputHandler.fill(
                            simulatedFluid,
                            true
                    );

                    ItemStack filledContainer =
                            outputHandler.getContainer();


                    ItemStack remainder =
                            inventory.insertItem(
                                    6,
                                    filledContainer,
                                    true
                            );

                    if (remainder.isEmpty()) {
                        inventory.extractItem(
                                5,
                                1,
                                false
                        );

                        inventory.insertItem(
                                6,
                                filledContainer,
                                false
                        );

                        tankOut.drain(
                                accepted,
                                true
                        );
                    }
                }
            }
        }


        //RECIPE CHECKING
        boolean wasRunning = isRunning;
        isRunning = false;

        boolean anyRecipeMatch = false;

        for (FluidTankMachineRecipe recipe : RECIPES) {

            boolean recipeMatches = true;

            //Check item inputs
            if (recipe.inputs() != null) {
                for (ItemIngredient ingredient : recipe.inputs()) {

                    if (ingredient == null) continue;

                    ItemStack stack = inventory.getStackInSlot(ingredient.slot());

                    if (stack.isEmpty()
                            || !stack.isItemEqual(ingredient.stack())
                            || stack.getCount() < ingredient.stack().getCount()) {

                        recipeMatches = false;
                        break;
                    }
                }
            }


            //Check fluid inputs
            if (recipeMatches && recipe.fluidInputs() != null) {
                for (FluidIngredient ingredient : recipe.fluidInputs()) {

                    if (ingredient == null) continue;

                    FluidTank tank = getTank(ingredient.tankID());

                    if (tank == null
                            || tank.getFluid() == null
                            || !tank.getFluid().isFluidEqual(ingredient.stack())
                            || tank.getFluidAmount() < ingredient.stack().amount) {

                        recipeMatches = false;
                        break;
                    }
                }
            }


            //Check item outputs
            if (recipeMatches && recipe.outputs() != null) {
                for (ItemIngredient ingredient : recipe.outputs()) {

                    if (ingredient == null) continue;

                    ItemStack current = inventory.getStackInSlot(ingredient.slot());

                    if (!current.isEmpty()
                            && (!current.isItemEqual(ingredient.stack())
                            || current.getCount() + ingredient.stack().getCount() > current.getMaxStackSize())) {

                        recipeMatches = false;
                        break;
                    }
                }
            }


            //Check fluid outputs
            if (recipeMatches && recipe.fluidOutputs() != null) {
                for (FluidIngredient ingredient : recipe.fluidOutputs()) {

                    if (ingredient == null) continue;

                    FluidTank tank = getTank(ingredient.tankID());

                    if (tank == null) {
                        recipeMatches = false;
                        break;
                    }

                    FluidStack current = tank.getFluid();

                    if (current != null
                            && (!current.isFluidEqual(ingredient.stack())
                            || tank.getFluidAmount() + ingredient.stack().amount > tank.getCapacity())) {

                        recipeMatches = false;
                        break;
                    }
                }
            }


            //Check energy
            if (recipeMatches && energy.getEnergyStored() < recipe.energyNeeded()) {
                recipeMatches = false;
            }


            if (recipeMatches) {
                isRunning = true;
                anyRecipeMatch = true;

                maxProgress = recipe.processingTime();

                progress++;

                energy.useEnergy(recipe.energyNeeded() / maxProgress);


                if (progress >= maxProgress) {


                    //Remove item inputs
                    if (recipe.inputs() != null) {
                        for (ItemIngredient ingredient : recipe.inputs()) {

                            if (ingredient == null) continue;

                            inventory.extractItem(
                                    ingredient.slot(),
                                    ingredient.stack().getCount(),
                                    false
                            );
                        }
                    }


                    //Remove fluid inputs
                    if (recipe.fluidInputs() != null) {
                        for (FluidIngredient ingredient : recipe.fluidInputs()) {

                            if (ingredient == null) continue;

                            FluidTank tank = getTank(ingredient.tankID());

                            if (tank != null) {
                                tank.drain(
                                        ingredient.stack().amount,
                                        true
                                );
                            }
                        }
                    }


                    //Produce item outputs
                    if (recipe.outputs() != null) {
                        for (ItemIngredient ingredient : recipe.outputs()) {

                            if (ingredient == null) continue;

                            if (random.nextInt(100) < ingredient.probability()) {

                                inventory.insertItem(
                                        ingredient.slot(),
                                        ingredient.stack().copy(),
                                        false
                                );
                            }
                        }
                    }


                    //Produce fluid outputs
                    if (recipe.fluidOutputs() != null) {
                        for (FluidIngredient ingredient : recipe.fluidOutputs()) {

                            if (ingredient == null) continue;

                            if (random.nextInt(100) < ingredient.probability()) {

                                FluidTank tank = getTank(ingredient.tankID());

                                if (tank != null) {
                                    tank.fill(
                                            ingredient.stack().copy(),
                                            true
                                    );
                                }
                            }
                        }
                    }


                    progress = 0;
                }

                //Only run one recipe per tick
                break;
            }
        }


        if (!anyRecipeMatch) {
            progress = 0;
        }

        if (wasRunning != isRunning) {

            IBlockState state = world.getBlockState(pos);

            world.setBlockState(
                    pos,
                    world.getBlockState(pos)
                            .withProperty(BasicMachineBlock.LIT, isRunning),
                    2
            );
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("Inventory", inventory.serializeNBT());

        NBTTagCompound tankInTag = new NBTTagCompound();
        tankIn.writeToNBT(tankInTag);
        compound.setTag("TankIn", tankInTag);

        NBTTagCompound tankOutTag = new NBTTagCompound();
        tankOut.writeToNBT(tankOutTag);
        compound.setTag("TankOut", tankOutTag);

        compound.setInteger("Progress", progress);

        energy.writeToNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory.deserializeNBT(compound.getCompoundTag("Inventory"));

        tankIn.readFromNBT(compound.getCompoundTag("TankIn"));

        tankOut.readFromNBT(compound.getCompoundTag("TankOut"));

        progress = compound.getInteger("Progress");

        energy.readFromNBT(compound);
    }

    @Override
    public int getProgress() { return progress; }

    @Override
    public int getMaxProgress() { return maxProgress; }

    @Override
    public void setProgress(int p) { progress = p; }

    @Override
    public void setMaxProgress(int data) {
        maxProgress=data;
    }


    public FluidTank getTank(int tankID) {
        switch (tankID){
            case 0:
                return tankIn;
            case 1:
                return tankOut;
            default:
                return null;
        }

    }


    @Override
    public void setFluidInTank(FluidStack fluidStack, int tankID) {
        switch (tankID){
            case 0:
                tankIn.setFluid(fluidStack);break;
            case 1:
                tankOut.setFluid(fluidStack);break;

        }
    }

    @Override
    public Map<Integer, FluidTank> getFluidTanks() {
        Map<Integer, FluidTank> fluidTanks = new HashMap<>();

        fluidTanks.put(0,tankIn);
        fluidTanks.put(1,tankOut);

        return fluidTanks;
    }

}

package com.pozdro.nuclearindustry.tile.tiles;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.block.custom.BasicMachineBlock;
import com.pozdro.nuclearindustry.recipe.FluidIngredient;
import com.pozdro.nuclearindustry.recipe.ItemIngredient;
import com.pozdro.nuclearindustry.recipe.TankMachineRecipe;
import com.pozdro.nuclearindustry.tile.basicte.SlotType;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import com.pozdro.nuclearindustry.tile.tankte.TankMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.tankte.TankTileEntity;
import com.pozdro.nuclearindustry.tile.tankte.TankType;
import com.pozdro.nuclearindustry.tile.tankte.TileTank;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class LeacherTileEntity extends TankTileEntity implements ITickable {

    private final BasicSink sink = new BasicSink(this,10000,1);

    private int progress = 0;
    private int maxProgress = 100;

    ItemStackHandler inventory;
    private boolean isRunning=false;
    private static final List<TankMachineRecipe> RECIPES = new ArrayList<>();


    private static final List<TileSlot> guiSlots = Arrays.asList(
            new TileSlot(0, SlotType.FLUID_HANDLER_SLOT,EnumFacing.DOWN,22,18),
            new TileSlot(1, SlotType.FLUID_HANDLER_SLOT,EnumFacing.DOWN,22,50),
            new TileSlot(2, SlotType.INPUT_SLOT,EnumFacing.DOWN,74,21),
            new TileSlot(3, SlotType.INPUT_SLOT,EnumFacing.DOWN,105,72),
            new TileSlot(4, SlotType.INPUT_SLOT,EnumFacing.DOWN,124,72),
            new TileSlot(5, SlotType.FLUID_HANDLER_SLOT,EnumFacing.DOWN,129,18),
            new TileSlot(6, SlotType.FLUID_HANDLER_SLOT,EnumFacing.DOWN,129,50),

            //upgrade slots
            new TileSlot(7, SlotType.UPGRADE_SLOT,EnumFacing.DOWN,152,21),
            new TileSlot(8, SlotType.UPGRADE_SLOT,EnumFacing.DOWN,152,39),
            new TileSlot(9, SlotType.UPGRADE_SLOT,EnumFacing.DOWN,152,57),
            new TileSlot(10, SlotType.UPGRADE_SLOT,EnumFacing.DOWN,152,75)
    );

    public LeacherTileEntity() {
        super(guiSlots.toArray().length, guiSlots, "leacher", 0);
        inventory=getInventoryHandler();
        setRecipes(new ArrayList<>(RECIPES));
        tanks= Arrays.asList(
                new TileTank(0, TankType.INPUT_TANK, EnumFacing.UP,47,18,16,48,new FluidTank(10000){
                    @Override
                    protected void onContentsChanged() {markDirty();}
                }),

                new TileTank(1, TankType.OUTPUT_TANK,EnumFacing.DOWN,105,18,16,48,new FluidTank(10000){
                    @Override
                    protected void onContentsChanged() {markDirty();}
                })
        );
    }

    @Override
    public TankMachineGuiHandler<? extends TankTileEntity> getTankGuiHandler() {
        return new TankMachineGuiHandler<>(
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
                9 ,25,
                56,
                0,
                tanks
        );
    }


    @Override
    public BasicSink getSink() {
        return sink;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress=progress;
    }

    @Override
    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public void setMaxProgress(int data) {
        this.maxProgress=data;
    }

    public static void addRecipe(TankMachineRecipe tankMachineRecipe){
        RECIPES.add(tankMachineRecipe);
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
                            tanks.get(0).getTank().getFluid() == null ||
                                    inputFluid.isFluidEqual(tanks.get(0).getTank().getFluid());

                    if (compatible) {

                        int accepted =
                                tanks.get(0).getTank().fill(inputFluid, false);

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

                                        tanks.get(0).getTank().fill(
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
                tanks.get(1).getTank().getFluid() != null &&
                tanks.get(1).getTank().getFluidAmount() > 0) {

            ItemStack singleOutput = outputStack.copy();
            singleOutput.setCount(1);

            IFluidHandlerItem outputHandler =
                    singleOutput.getCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
                            null
                    );

            if (outputHandler != null) {

                FluidStack tankFluid =
                        tanks.get(1).getTank().getFluid().copy();

                int accepted =
                        outputHandler.fill(
                                tankFluid,
                                false
                        );

                accepted = Math.min(
                        accepted,
                        tanks.get(1).getTank().getFluidAmount()
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

                        tanks.get(1).getTank().drain(
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

        for (TankMachineRecipe recipe : RECIPES) {

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

                    FluidTank tank = tanks.get(ingredient.tankID()).getTank();

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

                    FluidTank tank = tanks.get(ingredient.tankID()).getTank();

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
            if (recipeMatches && sink.getEnergyStored() < recipe.energyNeeded()) {
                recipeMatches = false;
            }


            if (recipeMatches) {
                isRunning = true;
                anyRecipeMatch = true;

                maxProgress = recipe.processingTime();

                progress++;

                sink.useEnergy(recipe.energyNeeded() / maxProgress);


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

                            FluidTank tank = tanks.get(ingredient.tankID()).getTank();

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

                                FluidTank tank = tanks.get(ingredient.tankID()).getTank();

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
}

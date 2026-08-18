package com.pozdro.nuclearindustry.tile.tiles;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.recipe.ItemIngredient;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.basicte.BasicTileEntity;
import com.pozdro.nuclearindustry.tile.basicte.SlotType;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class GrinderTileEntity extends BasicTileEntity implements ITickable {

    private final BasicSink sink = new BasicSink(this,10000,1);
    private static final List<BasicMachineRecipe> RECIPES = new ArrayList<>();


    static List<TileSlot> guiSlots= Arrays.asList(
        new TileSlot(0, SlotType.INPUT_SLOT, EnumFacing.UP,27,28),
        new TileSlot(1,SlotType.INPUT_SLOT,EnumFacing.UP,47,28),
        new TileSlot(2,SlotType.INPUT_SLOT,EnumFacing.UP,67,28),
        new TileSlot(3,SlotType.OUTPUT_SLOT,EnumFacing.DOWN,104,47),
        new TileSlot(4,SlotType.OUTPUT_SLOT,EnumFacing.DOWN,124,47),
        new TileSlot(5,SlotType.UPGRADE_SLOT,152,21),
        new TileSlot(6,SlotType.UPGRADE_SLOT,152,39),
        new TileSlot(7,SlotType.UPGRADE_SLOT,152,57),
        new TileSlot(8,SlotType.UPGRADE_SLOT,152,75)

    );

    private int progress = 0;
    private int maxProgress = 100;

    private final ItemStackHandler inventory;

    public GrinderTileEntity(){
        super(9, guiSlots, "grinder", 1);
        inventory=getInventoryHandler();

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

    @Override
    public BasicMachineGuiHandler<? extends BasicTileEntity> getGuiHandler() {
        return new BasicMachineGuiHandler<>(
                guiSlots,
                new ResourceLocation(NuclearIndustry.MODID, "textures/gui/grindergui.png"),
                GrinderTileEntity.class,
                "Grinder",
                true,
                29,
                71,
                30,46,
                176,0,
                176,
                181,
                15,
                9,25,
                56,
                1
        );
    }

    @Override
    protected List<BasicMachineRecipe> getRecipeList() {
        return RECIPES;
    }

    public static void addRecipe(BasicMachineRecipe basicMachineRecipe){
        RECIPES.add(basicMachineRecipe);
    }

    private final Random random = new Random();

    @Override
    public void update() {
        if (world.isRemote) {
            return;
        }

        boolean running = false;

        for (BasicMachineRecipe recipe : getRecipeList()) {

            if (recipe.inputs().isEmpty() || recipe.outputs().isEmpty()) {
                continue;
            }

            ItemIngredient input = recipe.inputs().get(0);
            ItemIngredient output = recipe.outputs().get(0);

            // The grinder accepts inputs in slots 0, 1 and 2
            for (int inputSlot = 0; inputSlot < 3; inputSlot++) {

                ItemStack inputStack = inventory.getStackInSlot(inputSlot);

                // No input
                if (inputStack.isEmpty()) {
                    continue;
                }

                // Wrong input
                if (!inputStack.isItemEqual(input.stack())) {
                    continue;
                }

                // Not enough input
                if (inputStack.getCount() < input.stack().getCount()) {
                    continue;
                }

                // Not enough energy
                if (sink.getEnergyStored() < recipe.energyNeeded()) {
                    continue;
                }

                // Check first output
                ItemStack outputStack =
                        inventory.getStackInSlot(output.slot());

                if (!outputStack.isEmpty() &&
                        !outputStack.isItemEqual(output.stack())) {
                    continue;
                }

                if (!outputStack.isEmpty() &&
                        outputStack.getCount() + output.stack().getCount()
                                > inventory.getSlotLimit(output.slot())) {
                    continue;
                }

                // Check second output only if it actually exists
                if (recipe.outputs().size() > 1) {

                    ItemIngredient output2 = recipe.outputs().get(1);

                    ItemStack outputStack2 =
                            inventory.getStackInSlot(output2.slot());

                    if (!outputStack2.isEmpty() &&
                            !outputStack2.isItemEqual(output2.stack())) {
                        continue;
                    }

                    if (!outputStack2.isEmpty() &&
                            outputStack2.getCount() + output2.stack().getCount()
                                    > inventory.getSlotLimit(output2.slot())) {
                        continue;
                    }
                }

                /*
                 * Valid recipe found.
                 */
                running = true;

                // Make GUI maximum match this recipe
                setMaxProgress(recipe.processingTime());

                /*
                 * Advance progress.
                 */
                progress++;

                /*
                 * Recipe finished.
                 */
                if (progress >= recipe.processingTime()) {

                    // Reset BEFORE processing so it can never run above max.
                    progress = 0;

                    // Consume energy
                    sink.useEnergy(recipe.energyNeeded());

                    // Consume input
                    if (random.nextInt(100) < input.probability()) {

                        inventory.extractItem(
                                inputSlot,
                                input.stack().getCount(),
                                false
                        );
                    }

                    // First output
                    if (random.nextInt(100) < output.probability()) {

                        inventory.insertItem(
                                output.slot(),
                                output.stack().copy(),
                                false
                        );
                    }

                    // Second output
                    if (recipe.outputs().size() > 1) {

                        ItemIngredient output2 = recipe.outputs().get(1);

                        if (random.nextInt(100) < output2.probability()) {

                            inventory.insertItem(
                                    output2.slot(),
                                    output2.stack().copy(),
                                    false
                            );
                        }
                    }
                }

                // Only process one recipe per tick
                break;
            }

            if (running) {
                break;
            }
        }

        setActiveBlockstate(running);

        if (!running) {
            progress = 0;
            setMaxProgress(100);
        }
    }


}

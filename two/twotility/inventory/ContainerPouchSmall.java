/*
 */
package two.twotility.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import two.twotility.TwoTility;
import two.twotility.gui.SlotWithValidation;

/**
 * @author Two
 */
public class ContainerPouchSmall extends ContainerBase {

  protected final static int UPDATEID_STORED_OPERATIONS = 0;
  protected final static int UPDATEID_SMELTTIME_REMAINING = UPDATEID_STORED_OPERATIONS + 1;
  protected final ItemStack stackPouchSmall;
  protected final IInventory pouchInventory;
  protected int lastStoredFuel, lastSmeltTime;

  public ContainerPouchSmall(final InventoryPlayer inventoryPlayer, final ItemStack stackPouchSmall) {
    super(inventoryPlayer, 4, 119, 4, 64);
    this.stackPouchSmall = stackPouchSmall;
    pouchInventory = InventoryPouchSmall.fromItemStack(stackPouchSmall, inventoryPlayer.player);
  }

  @Override
  public ContainerBase layout() {
    super.layout();

    int slotCount = 0;
    // pouch slots
    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 3; ++x) {
        this.addSlotToContainer(createSlot(pouchInventory, slotCount++, 58 + x * 18, 4 + y * 18));
      }
    }

    if (slotCount != pouchInventory.getSizeInventory()) {
      throw new RuntimeException("Mismatch between container slot-size{" + slotCount + "} and " + pouchInventory.getClass().getName() + " slot-size{" + pouchInventory.getSizeInventory() + "}");
    }
    
    return this;
  }

  @Override
  protected Slot createSlot(final IInventory inventory, final int slotIndex, final int x, final int y) {
    final ItemStack itemStackInSlot = inventory.getStackInSlot(slotIndex);
    final Item itemInSlot = itemStackInSlot != null ? itemStackInSlot.getItem() : null;
    if ((itemInSlot != null) && (itemInSlot.itemID == TwoTility.proxy.itemPouchSmall.itemID)) {
      return new SlotWithValidation(inventory, slotIndex, x, y, false);
    } else {
      return super.createSlot(inventory, slotIndex, x, y);
    }
  }

  @Override
  public boolean canInteractWith(final EntityPlayer entityplayer) {
    return pouchInventory.isUseableByPlayer(entityplayer);
  }

  @Override
  public ItemStack transferStackInSlot(final EntityPlayer player, final int slotId) {
    final Slot slot = getSlot(slotId);
    if ((slot != null) && (slot.getHasStack())) {
      final ItemStack itemStack = slot.getStack();
      final ItemStack result = itemStack.copy();

      if (slotId >= 36) {
        if (!mergeItemStack(result, 0, 36)) { // transfer to player's inventory by first match
          return null;
        }
      } else if (!mergeItemStack(result, 36 + InventoryPouchSmall.INVENTORY_START, 36 + InventoryPouchSmall.INVENTORY_START + InventoryPouchSmall.INVENTORY_SIZE)) {
        return null;
      }

      final int itemsMoved = itemStack.stackSize - result.stackSize;
      slot.decrStackSize(itemsMoved);
      slot.onPickupFromSlot(player, itemStack);
      return result;
    }

    return null;
  }
}

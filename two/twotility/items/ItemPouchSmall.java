/*
 */
package two.twotility.items;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import two.twotility.TwoTility;
import two.twotility.inventory.ContainerPouchSmall;
import two.twotility.gui.GUICallback;
import two.twotility.gui.GUIPouchSmall;
import two.twotility.tiles.TilePouchSmall;
import two.util.InvalidTileEntityException;
import two.util.Logging;

/**
 * @author Two
 */
public class ItemPouchSmall extends ItemBase implements GUICallback {

  public static final String NAME = "PouchSmall";
  protected final int guiId;
  protected final TilePouchSmall tilePouchSmall;

  public ItemPouchSmall() {
    super(TwoTility.config.getItemID(ItemPouchSmall.class));
    GameRegistry.registerItem(this, TwoTility.getItemName(NAME));
    guiId = TwoTility.guiHandler.registerGui(this);
    tilePouchSmall = new TilePouchSmall();
  }

  @Override
  public void initialize() {
    setUnlocalizedName(NAME);
    setMaxStackSize(1);
    setTextureName(TwoTility.getTextureName(NAME));
    setCreativeTab(TwoTility.creativeTab);

    LanguageRegistry.addName(this, "Small Pouch");

    if (TwoTility.config.isCraftingEnabled(NAME)) {
      CraftingManager.getInstance().addRecipe(new ItemStack(this),
              " S ",
              "L L",
              " L ",
              'S', Item.silk,
              'L', Item.leather);
    }

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public ItemStack onItemRightClick(final ItemStack item, final World world, final EntityPlayer player) {
    Logging.logMethodEntry("ItemPouchSmall", "onItemRightClick", item.getDisplayName(), world.getWorldInfo().getWorldName(), player.getEntityName());
    if (world.isRemote == false) {
      FMLNetworkHandler.openGui(player, TwoTility.instance, guiId, world, (int) (player.posX + 0.5), (int) (player.posY + 0.5), (int) (player.posZ + 0.5));
    }
    return item;
  }

  @Override
  public boolean onBlockDestroyed(final ItemStack itemStack, final World world, final int x, final int y, final int z, final int metadata, final EntityLivingBase entity) {
    Logging.logMethodEntry("ItemPouchSmall", "onBlockDestroyed", itemStack.getDisplayName(), world.getWorldInfo().getWorldName(), entity.getEntityName());
    return super.onBlockDestroyed(itemStack, world, x, y, z, metadata, entity); // TODO
  }

  @Override
  public void onCreated(final ItemStack itemStack, final World world, final EntityPlayer player) {
    Logging.logMethodEntry("ItemPouchSmall", "onCreated", itemStack.getDisplayName(), world.getWorldInfo().getWorldName(), player.getEntityName());
    super.onCreated(itemStack, world, player); // TODO
  }

  @ForgeSubscribe
  public void onItemExpired(final ItemExpireEvent event) {
    Logging.logMethodEntry("ItemPouchSmall", "onItemExpired", "event");
  }

  @Override
  public Container createContainer(final EntityPlayer player, final World world, final int x, final int y, final int z) throws InvalidTileEntityException {
    final ItemStack heldItem = player.getHeldItem();
    if (heldItem.getItem().itemID == this.itemID) {
      return (new ContainerPouchSmall(player.inventory, heldItem)).layout();
    } else {
      throw new IllegalStateException("Container of " + this.getClass().getSimpleName() + " requested, but for a different item (" + heldItem.getDisplayName() + ")!");
    }
  }

  @Override
  public Gui createGUI(final EntityPlayer player, final World world, final int x, final int y, final int z) throws InvalidTileEntityException {
    final ItemStack heldItem = player.getHeldItem();
    if (heldItem.getItem().itemID == this.itemID) {
      return new GUIPouchSmall(player.inventory, heldItem);
    } else {
      throw new IllegalStateException("Container of " + this.getClass().getSimpleName() + " requested, but for a different item (" + heldItem.getDisplayName() + ")!");
    }
  }
}
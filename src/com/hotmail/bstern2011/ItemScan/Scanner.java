package com.hotmail.bstern2011.ItemScan;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Lever;
import org.bukkit.material.Redstone;

public class Scanner
{
  private Location loc;
  private int amount;
  private int delay;
  private int pulse;
  private int tick;
  private OfflinePlayer owner;
  private boolean keep;
  private ArrayList<BlockState> redstone;
  private HashMap<Location, Material> blockHold = new HashMap<Location, Material>();

  public Scanner(Location location, int a, int d, int p, boolean o, OfflinePlayer pl)
  {
    if (d == 0)
      d = 10;
    if (p == 0)
      p = 5;
    this.loc = location;
    setAmount(a);
    setDelay(d);
    setPulse(p);
    setKeep(o);
    tick = getPulse() + getDelay();
    this.owner = pl;
    populateRedStone();
  }

  public void collectNear() {
    Block block = this.loc.getBlock();
    Chest chest = (Chest)block.getState();
    populateRedStone();
    for (Entity item : this.loc.getChunk().getEntities())
      if ((item.getLocation().distance(this.loc) < 3.0D) && 
        ((item instanceof Item)))
      {
        if ((chest.getBlockInventory().contains(
          ((Item)item).getItemStack().getType())) && 
          (((Item)item).getItemStack().getAmount() >= this.amount)) {
          for (BlockState stone : this.redstone) {
        	  Block c = stone.getBlock().getRelative(0, -1, 0);
	    	  if(!c.getType().equals(Material.REDSTONE_BLOCK))
	    		  blockHold.put(c.getLocation(), c.getType());
          }
          ItemStack change = null;
          if (this.keep) {
            change = (ItemStack)chest.getInventory().addItem(new ItemStack[] { ((Item)item).getItemStack() }).get(Integer.valueOf(0));
          }
          if ((change != null) && ((this.owner instanceof Player))) {
            Player pl = (Player)this.owner;
            pl.sendMessage(ItemScan.prefix + "Your chest at location " + this.loc.toString() + 
              " is full");
          }
          item.remove();
          setTick(0);
        }
      }
  }

  public void stoneOff()
  {
    if (this.redstone.size() != 0)
      for (BlockState stone : this.redstone) {
    	  if(stone.getData() instanceof Lever){
    		  ((Lever)stone.getData()).setPowered(false);
    		  stone.update();
    	  }else{
        	  Block c = stone.getBlock().getRelative(0, -1, 0);
        	  boolean x = blockHold.containsKey(c.getLocation());
        	  if(x)
        		  c.setType(blockHold.get(c.getLocation()));
        	  else
        		  c.setType(Material.STONE);
    	  }
      }
  }

  public void stoneOn()
  {
    if (this.redstone.size() != 0)
      for (BlockState stone : this.redstone) {
    	  if(stone.getData() instanceof Lever){
    		  ((Lever)stone.getData()).setPowered(true);
    		  stone.update();
    	  }else{
        	  Block c = stone.getBlock().getRelative(0, -1, 0);
        	  c.setType(Material.REDSTONE_BLOCK);
    	  }
      }
  }

  public boolean equals(Object o)
  {
    return this.loc.equals(((Scanner)o).getLocation());
  }

  private void populateRedStone() {
    World world = this.loc.getWorld();
    double locX = this.loc.getX();
    double locZ = this.loc.getZ();
    Location temploc = new Location(this.loc.getWorld(), locX, this.loc.getY(), locZ);
    ArrayList<BlockState> sides = new ArrayList<BlockState>();
    temploc.setX(locX + 1.0D);
    BlockState a = world.getBlockAt(temploc).getState();
    temploc.setX(locX - 1.0D);
    BlockState b = world.getBlockAt(temploc).getState();
    temploc.setX(locX);
    temploc.setZ(locZ + 1.0D);
    BlockState c = world.getBlockAt(temploc).getState();
    temploc.setZ(locZ - 1.0D);
    BlockState d = world.getBlockAt(temploc).getState();
    if ((a.getData() instanceof Redstone) || (a.getData() instanceof Lever)){
        sides.add(a);
    }
    if ((b.getData() instanceof Redstone) || (a.getData() instanceof Lever)){
      sides.add(b);
    }
    if ((c.getData() instanceof Redstone) || (a.getData() instanceof Lever)){
      sides.add(c);
    }
    if ((d.getData() instanceof Redstone) || (a.getData() instanceof Lever)){
      sides.add(d);
    }
    this.redstone = sides;
  }

  public int getPulse() {
    return this.pulse;
  }

  public void setPulse(int pulse) {
    this.pulse = pulse;
  }

  public int getDelay() {
    return this.delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public int getTick() {
    return this.tick;
  }

  public void setTick(int tick) {
    this.tick = tick;
  }

  public Location getLocation() {
    return this.loc;
  }

  public String getOwner() {
    return this.owner.getName();
  }

  public void setAmount(int parameter) {
    this.amount = parameter;
  }
  
  public int getAmount(){
	  return amount;
  }

  public boolean keep()
  {
    return this.keep;
  }

  public void setKeep(boolean keep) {
    this.keep = keep;
  }
}
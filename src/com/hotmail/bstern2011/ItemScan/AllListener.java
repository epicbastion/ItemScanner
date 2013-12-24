package com.hotmail.bstern2011.ItemScan;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AllListener
  implements Listener
{
  ItemScan plugin;

  public AllListener(ItemScan itemScan)
  {
    this.plugin = itemScan;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    if ((this.plugin.checkLoc(block.getLocation(), Boolean.valueOf(false))) && 
      (this.plugin.getScanner(block.getLocation()) != null))
      event.setCancelled(true);
  }

  @EventHandler
  public void onPistonPush(BlockPistonExtendEvent event)
  {
    List<Block> blocks = event.getBlocks();
    for (Block block : blocks)
      if ((this.plugin.checkLoc(block.getLocation(), Boolean.valueOf(false))) && 
        (this.plugin.getScanner(block.getLocation()) != null))
        event.setCancelled(true);
  }

  @EventHandler
  public void onPistonPull(BlockPistonRetractEvent event)
  {
    Block block = event.getBlock();
    if ((this.plugin.checkLoc(block.getLocation(), Boolean.valueOf(false))) && 
      (this.plugin.getScanner(block.getLocation()) != null))
      event.setCancelled(true);
  }
  
  @EventHandler
  public void onInteract(PlayerInteractEvent e){
	  if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.CHEST)){
		  Block b = e.getClickedBlock();
		  Player p = e.getPlayer();
		  Scanner s = plugin.getScanner(b.getLocation());
		  if(s != null){
			  if(!(p.hasPermission("scanner.admin") || p.getName().equalsIgnoreCase(s.getOwner()))){
				  e.setCancelled(true);
				  p.sendMessage(ItemScan.prefix + "You do not have access to this Scanner!");
			  }
		  }
	  }
  }
}
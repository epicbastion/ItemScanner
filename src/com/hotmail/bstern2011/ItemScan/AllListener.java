/*  1:   */ package com.hotmail.bstern2011.ItemScan;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.bukkit.block.Block;
/*  5:   */ import org.bukkit.event.EventHandler;
/*  6:   */ import org.bukkit.event.EventPriority;
/*  7:   */ import org.bukkit.event.Listener;
/*  8:   */ import org.bukkit.event.block.BlockBreakEvent;
/*  9:   */ import org.bukkit.event.block.BlockPistonExtendEvent;
/* 10:   */ import org.bukkit.event.block.BlockPistonRetractEvent;
/* 11:   */ 
/* 12:   */ public class AllListener
/* 13:   */   implements Listener
/* 14:   */ {
/* 15:   */   ItemScan plugin;
/* 16:   */   
/* 17:   */   public AllListener(ItemScan itemScan)
/* 18:   */   {
/* 19:17 */     this.plugin = itemScan;
/* 20:   */   }
/* 21:   */   
/* 22:   */   @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
/* 23:   */   public void onBlockBreak(BlockBreakEvent event)
/* 24:   */   {
/* 25:22 */     Block block = event.getBlock();
/* 26:23 */     if ((this.plugin.checkLoc(block.getLocation(), Boolean.valueOf(false))) && 
/* 27:24 */       (this.plugin.getScanner(block.getLocation()) != null)) {
/* 28:25 */       event.setCancelled(true);
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
/* 33:   */   public void onPistonPush(BlockPistonExtendEvent event)
/* 34:   */   {
/* 35:31 */     List<Block> blocks = event.getBlocks();
/* 36:32 */     for (Block block : blocks) {
/* 37:33 */       if ((this.plugin.checkLoc(block.getLocation(), Boolean.valueOf(false))) && 
/* 38:34 */         (this.plugin.getScanner(block.getLocation()) != null)) {
/* 39:35 */         event.setCancelled(true);
/* 40:   */       }
/* 41:   */     }
/* 42:   */   }
/* 43:   */   
/* 44:   */   @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
/* 45:   */   public void onPistonPull(BlockPistonRetractEvent event)
/* 46:   */   {
/* 47:43 */     Block block = event.getBlock();
/* 48:44 */     if ((this.plugin.checkLoc(block.getLocation(), Boolean.valueOf(false))) && 
/* 49:45 */       (this.plugin.getScanner(block.getLocation()) != null)) {
/* 50:46 */       event.setCancelled(true);
/* 51:   */     }
/* 52:   */   }
/* 53:   */ }


/* Location:           C:\Users\Regan\Downloads\ItemScan.1.1.jar
 * Qualified Name:     com.hotmail.bstern2011.ItemScan.AllListener
 * JD-Core Version:    0.7.0.1
 */
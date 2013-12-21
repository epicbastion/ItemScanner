/*  1:   */ package com.hotmail.bstern2011.ItemScan;
/*  2:   */ 
/*  3:   */ public class Ticker
/*  4:   */   implements Runnable
/*  5:   */ {
/*  6:   */   private ItemScan scanner;
/*  7:   */   
/*  8:   */   public Ticker(ItemScan i)
/*  9:   */   {
/* 10: 6 */     this.scanner = i;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public void run()
/* 14:   */   {
/* 15:11 */     if (this.scanner.instances != null) {
/* 16:12 */       for (Scanner scan : this.scanner.instances)
/* 17:   */       {
/* 18:13 */         int ticks = scan.getTick();
/* 19:14 */         int delay = scan.getDelay();
/* 20:15 */         int pulse = scan.getPulse();
/* 21:16 */         if (ticks <= delay + pulse)
/* 22:   */         {
/* 23:17 */           if ((ticks >= pulse + delay) || (ticks <= delay)) {
/* 24:18 */             scan.leversOff();
/* 25:   */           } else {
/* 26:20 */             scan.leversOn();
/* 27:   */           }
/* 28:21 */           scan.setTick(ticks + 1);
/* 29:   */         }
/* 30:   */         else
/* 31:   */         {
/* 32:24 */           scan.collectNear();
/* 33:   */         }
/* 34:   */       }
/* 35:   */     }
/* 36:   */   }
/* 37:   */ }


/* Location:           C:\Users\Regan\Downloads\ItemScan.1.1.jar
 * Qualified Name:     com.hotmail.bstern2011.ItemScan.Ticker
 * JD-Core Version:    0.7.0.1
 */
/*  1:   */ package com.hotmail.bstern2011.ItemScan;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.logging.Logger;
/*  5:   */ 
/*  6:   */ public class Message
/*  7:   */   implements Runnable
/*  8:   */ {
/*  9:   */   private ArrayList<String> messages;
/* 10:   */   private Logger logger;
/* 11:   */   
/* 12:   */   public Message(ArrayList<String> broke, Logger s)
/* 13:   */   {
/* 14:11 */     this.messages = broke;
/* 15:12 */     this.logger = s;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void run()
/* 19:   */   {
/* 20:18 */     for (String message : this.messages)
/* 21:   */     {
/* 22:19 */       String[] m = message.split(",", 4);
/* 23:20 */       this.logger.severe("An invalid chest was detected at " + m[0] + "," + m[1] + "," + m[2]);
/* 24:   */     }
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\Regan\Downloads\ItemScan.1.1.jar
 * Qualified Name:     com.hotmail.bstern2011.ItemScan.Message
 * JD-Core Version:    0.7.0.1
 */
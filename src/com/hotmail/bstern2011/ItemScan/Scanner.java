/*   1:    */ package com.hotmail.bstern2011.ItemScan;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import org.bukkit.Chunk;
/*   6:    */ import org.bukkit.Location;
/*   7:    */ import org.bukkit.OfflinePlayer;
/*   8:    */ import org.bukkit.World;
/*   9:    */ import org.bukkit.block.Block;
/*  10:    */ import org.bukkit.block.BlockState;
/*  11:    */ import org.bukkit.block.Chest;
/*  12:    */ import org.bukkit.entity.Entity;
/*  13:    */ import org.bukkit.entity.Item;
/*  14:    */ import org.bukkit.entity.Player;
/*  15:    */ import org.bukkit.inventory.Inventory;
/*  16:    */ import org.bukkit.inventory.ItemStack;
/*  17:    */ import org.bukkit.material.Lever;
/*  18:    */ 
/*  19:    */ public class Scanner
/*  20:    */ {
/*  21:    */   private Location loc;
/*  22:    */   private int amount;
/*  23:    */   private int delay;
/*  24:    */   private int pulse;
/*  25: 22 */   private int tick = 0;
/*  26:    */   private OfflinePlayer owner;
/*  27:    */   private boolean keep;
/*  28:    */   private ArrayList<BlockState> levers;
/*  29:    */   
/*  30:    */   public Scanner(Location location, int a, int d, int p, boolean o, OfflinePlayer pl)
/*  31:    */   {
/*  32: 29 */     if (d == 0) {
/*  33: 30 */       d = 10;
/*  34:    */     }
/*  35: 31 */     if (p == 0) {
/*  36: 32 */       p = 5;
/*  37:    */     }
/*  38: 33 */     this.loc = location;
/*  39: 34 */     setAmount(a);
/*  40: 35 */     setDelay(d);
/*  41: 36 */     setPulse(p);
/*  42: 37 */     setKeep(o);
/*  43: 38 */     this.owner = pl;
/*  44: 39 */     populateLevers();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void collectNear()
/*  48:    */   {
/*  49: 43 */     Block block = this.loc.getBlock();
/*  50: 44 */     Chest chest = (Chest)block.getState();
/*  51: 45 */     populateLevers();
/*  52: 46 */     for (Entity item : this.loc.getChunk().getEntities()) {
/*  53: 47 */       if ((item.getLocation().distance(this.loc) < 3.0D) && 
/*  54: 48 */         ((item instanceof Item))) {
/*  55: 50 */         if ((chest.getBlockInventory().contains(((Item)item).getItemStack().getType())) && 
/*  56: 51 */           (((Item)item).getItemStack().getAmount() >= this.amount))
/*  57:    */         {
/*  58: 52 */           ItemStack change = null;
/*  59: 53 */           if (this.keep) {
/*  60: 54 */             change = (ItemStack)chest.getInventory().addItem(new ItemStack[] { ((Item)item).getItemStack() }).get(Integer.valueOf(0));
/*  61:    */           }
/*  62: 56 */           if ((change != null) && ((this.owner instanceof Player)))
/*  63:    */           {
/*  64: 57 */             Player pl = (Player)this.owner;
/*  65: 58 */             pl.sendMessage("Your chest at location " + this.loc.toString() + 
/*  66: 59 */               " is full");
/*  67:    */           }
/*  68: 61 */           item.remove();
/*  69: 62 */           setTick(0);
/*  70:    */         }
/*  71:    */       }
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void leversOff()
/*  76:    */   {
/*  77: 69 */     if (this.levers.size() != 0) {
/*  78: 70 */       for (BlockState lever : this.levers)
/*  79:    */       {
/*  80: 71 */         ((Lever)lever.getData()).setPowered(false);
/*  81: 72 */         lever.update();
/*  82:    */       }
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void leversOn()
/*  87:    */   {
/*  88: 78 */     if (this.levers.size() != 0) {
/*  89: 79 */       for (BlockState lever : this.levers)
/*  90:    */       {
/*  91: 80 */         ((Lever)lever.getData()).setPowered(true);
/*  92: 81 */         lever.update();
/*  93:    */       }
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public boolean equals(Object o)
/*  98:    */   {
/*  99: 87 */     return this.loc.equals(((Scanner)o).getLocation());
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void populateLevers()
/* 103:    */   {
/* 104: 91 */     World world = this.loc.getWorld();
/* 105: 92 */     double locX = this.loc.getX();
/* 106: 93 */     double locZ = this.loc.getZ();
/* 107: 94 */     Location temploc = new Location(this.loc.getWorld(), locX, this.loc.getY(), locZ);
/* 108: 95 */     ArrayList<BlockState> sides = new ArrayList();
/* 109: 96 */     temploc.setX(locX + 1.0D);
/* 110: 97 */     BlockState a = world.getBlockAt(temploc).getState();
/* 111: 98 */     temploc.setX(locX - 1.0D);
/* 112: 99 */     BlockState b = world.getBlockAt(temploc).getState();
/* 113:100 */     temploc.setX(locX);
/* 114:101 */     temploc.setZ(locZ + 1.0D);
/* 115:102 */     BlockState c = world.getBlockAt(temploc).getState();
/* 116:103 */     temploc.setZ(locZ - 1.0D);
/* 117:104 */     BlockState d = world.getBlockAt(temploc).getState();
/* 118:105 */     if ((a.getData() instanceof Lever)) {
/* 119:106 */       sides.add(a);
/* 120:    */     }
/* 121:107 */     if ((b.getData() instanceof Lever)) {
/* 122:108 */       sides.add(b);
/* 123:    */     }
/* 124:109 */     if ((c.getData() instanceof Lever)) {
/* 125:110 */       sides.add(c);
/* 126:    */     }
/* 127:111 */     if ((d.getData() instanceof Lever)) {
/* 128:112 */       sides.add(d);
/* 129:    */     }
/* 130:113 */     this.levers = sides;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String generateSaveString()
/* 134:    */   {
/* 135:    */     String name;
/* 136:    */     String name;
/* 137:118 */     if (this.owner != null) {
/* 138:119 */       name = this.owner.getName();
/* 139:    */     } else {
/* 140:121 */       name = "nullplaceholder";
/* 141:    */     }
/* 142:122 */     return 
/* 143:    */     
/* 144:124 */       this.loc.getX() + "," + this.loc.getY() + "," + this.loc.getZ() + "," + this.loc.getWorld().getName() + "," + this.amount + "," + this.delay + "," + this.pulse + "," + this.keep + "," + name;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public int getPulse()
/* 148:    */   {
/* 149:128 */     return this.pulse;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void setPulse(int pulse)
/* 153:    */   {
/* 154:132 */     this.pulse = pulse;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public int getDelay()
/* 158:    */   {
/* 159:136 */     return this.delay;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setDelay(int delay)
/* 163:    */   {
/* 164:140 */     this.delay = delay;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public int getTick()
/* 168:    */   {
/* 169:144 */     return this.tick;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setTick(int tick)
/* 173:    */   {
/* 174:148 */     this.tick = tick;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public Location getLocation()
/* 178:    */   {
/* 179:152 */     return this.loc;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public String getOwner()
/* 183:    */   {
/* 184:156 */     return this.owner.getName();
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setAmount(int parameter)
/* 188:    */   {
/* 189:160 */     this.amount = parameter;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public boolean keep()
/* 193:    */   {
/* 194:165 */     return this.keep;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setKeep(boolean keep)
/* 198:    */   {
/* 199:169 */     this.keep = keep;
/* 200:    */   }
/* 201:    */ }


/* Location:           C:\Users\Regan\Downloads\ItemScan.1.1.jar
 * Qualified Name:     com.hotmail.bstern2011.ItemScan.Scanner
 * JD-Core Version:    0.7.0.1
 */
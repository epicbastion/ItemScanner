/*   1:    */ package com.hotmail.bstern2011.ItemScan;
/*   2:    */ 
/*   3:    */ import com.google.common.io.Files;
/*   4:    */ import java.io.BufferedReader;
/*   5:    */ import java.io.DataInputStream;
/*   6:    */ import java.io.File;
/*   7:    */ import java.io.FileInputStream;
/*   8:    */ import java.io.FileNotFoundException;
/*   9:    */ import java.io.FileWriter;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.InputStreamReader;
/*  12:    */ import java.io.PrintWriter;
/*  13:    */ import java.util.ArrayList;
/*  14:    */ import java.util.logging.Logger;
/*  15:    */ import org.bukkit.Location;
/*  16:    */ import org.bukkit.Material;
/*  17:    */ import org.bukkit.OfflinePlayer;
/*  18:    */ import org.bukkit.Server;
/*  19:    */ import org.bukkit.World;
/*  20:    */ import org.bukkit.block.Block;
/*  21:    */ import org.bukkit.command.Command;
/*  22:    */ import org.bukkit.command.CommandSender;
/*  23:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  24:    */ import org.bukkit.configuration.file.FileConfigurationOptions;
/*  25:    */ import org.bukkit.entity.Player;
/*  26:    */ import org.bukkit.plugin.PluginManager;
/*  27:    */ import org.bukkit.plugin.java.JavaPlugin;
/*  28:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  29:    */ 
/*  30:    */ public class ItemScan
/*  31:    */   extends JavaPlugin
/*  32:    */ {
/*  33: 26 */   AllListener pl = new AllListener(this);
/*  34: 27 */   ArrayList<Scanner> instances = new ArrayList();
/*  35: 28 */   private ArrayList<String> broke = new ArrayList();
/*  36:    */   
/*  37:    */   public void onEnable()
/*  38:    */   {
/*  39: 31 */     getConfig().options().copyDefaults(true);
/*  40: 32 */     saveDefaultConfig();
/*  41: 33 */     convertOld();
/*  42: 34 */     readDat();
/*  43: 35 */     getServer().getPluginManager().registerEvents(this.pl, this);
/*  44: 36 */     getServer().getScheduler().scheduleSyncRepeatingTask(this, new Ticker(this), 200L, 10L);
/*  45: 37 */     getServer().getScheduler().scheduleSyncDelayedTask(this, new Message(this.broke, getServer().getLogger()), 40L);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void onDisable()
/*  49:    */   {
/*  50: 41 */     saveDat();
/*  51:    */   }
/*  52:    */   
/*  53:    */   private void convertOld()
/*  54:    */   {
/*  55: 45 */     File oldFile = new File("plugins/ItemScanner/Scanner.dat");
/*  56: 46 */     File newFile = new File("plugins/ItemScan/locations.dat");
/*  57:    */     try
/*  58:    */     {
/*  59: 48 */       Files.move(oldFile, newFile);
/*  60:    */     }
/*  61:    */     catch (IOException e)
/*  62:    */     {
/*  63: 50 */       return;
/*  64:    */     }
/*  65: 52 */     getServer().getLogger().info("File from ItemScan Imported, It is now safe to remove your ItemScan folder");
/*  66:    */   }
/*  67:    */   
/*  68:    */   private void readDat()
/*  69:    */   {
/*  70: 56 */     new File("plugins/ItemScan").mkdir();
/*  71: 57 */     this.instances.clear();
/*  72: 58 */     File data = new File("plugins/ItemScan/locations.dat");
/*  73:    */     try
/*  74:    */     {
/*  75: 60 */       FileInputStream fstream = new FileInputStream(data);
/*  76: 61 */       DataInputStream in = new DataInputStream(fstream);
/*  77: 62 */       BufferedReader br = new BufferedReader(new InputStreamReader(in));
/*  78:    */       String strLine;
/*  79: 65 */       while ((strLine = br.readLine()) != null)
/*  80:    */       {
/*  81:    */         String strLine;
/*  82: 66 */         boolean broken = false;
/*  83: 67 */         String[] params = strLine.split(",");
/*  84: 68 */         World world = getServer().getWorld(params[3]);
/*  85: 69 */         Location loc = new Location(world, Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
/*  86: 70 */         if (!checkLoc(loc, Boolean.valueOf(true))) {
/*  87: 70 */           broken = true;
/*  88:    */         }
/*  89: 71 */         int amount = Integer.parseInt(params[4]);
/*  90: 72 */         int delay = Integer.parseInt(params[5]);
/*  91: 73 */         int length = Integer.parseInt(params[6]);
/*  92: 74 */         boolean keep = false;
/*  93: 75 */         OfflinePlayer pl = null;
/*  94:    */         try
/*  95:    */         {
/*  96: 78 */           keep = Boolean.parseBoolean(params[7]);
/*  97: 79 */           pl = getServer().getOfflinePlayer(params[8]);
/*  98:    */         }
/*  99:    */         catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
/* 100: 84 */         if (!broken) {
/* 101: 84 */           this.instances.add(new Scanner(loc, amount, delay, length, keep, pl));
/* 102:    */         } else {
/* 103: 85 */           this.broke.add(loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getWorld().getName() + "," + amount + "," + delay + "," + length + "," + keep + "," + pl.getName());
/* 104:    */         }
/* 105:    */       }
/* 106: 88 */       br.close();
/* 107: 89 */       in.close();
/* 108: 90 */       fstream.close();
/* 109: 91 */       saveDat();
/* 110:    */     }
/* 111:    */     catch (IOException ex)
/* 112:    */     {
/* 113:    */       try
/* 114:    */       {
/* 115: 95 */         if ((ex instanceof FileNotFoundException)) {
/* 116: 96 */           data.createNewFile();
/* 117:    */         } else {
/* 118: 98 */           ex.printStackTrace();
/* 119:    */         }
/* 120:    */       }
/* 121:    */       catch (IOException ex2)
/* 122:    */       {
/* 123:101 */         ex2.printStackTrace();
/* 124:    */       }
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   private void saveDat()
/* 129:    */   {
/* 130:    */     try
/* 131:    */     {
/* 132:108 */       FileWriter File = new FileWriter("plugins/ItemScan/locations.dat");
/* 133:109 */       PrintWriter target = new PrintWriter(File);
/* 134:110 */       for (String s : this.broke) {
/* 135:111 */         target.println(s);
/* 136:    */       }
/* 137:113 */       for (Scanner scan : this.instances) {
/* 138:114 */         target.println(scan.generateSaveString());
/* 139:    */       }
/* 140:116 */       target.flush();
/* 141:117 */       target.close();
/* 142:    */     }
/* 143:    */     catch (IOException ex)
/* 144:    */     {
/* 145:120 */       ex.printStackTrace();
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/* 150:    */   {
/* 151:126 */     if (cmd.getName().equals("scanner"))
/* 152:    */     {
/* 153:127 */       if (args.length == 0) {
/* 154:127 */         return false;
/* 155:    */       }
/* 156:128 */       if (sender.hasPermission("itemscan.admin"))
/* 157:    */       {
/* 158:129 */         if (args[0].equals("save"))
/* 159:    */         {
/* 160:129 */           saveDat();sender.sendMessage("Saved");return true;
/* 161:    */         }
/* 162:130 */         if (args[0].equals("load"))
/* 163:    */         {
/* 164:130 */           readDat();sender.sendMessage("Loaded");return true;
/* 165:    */         }
/* 166:    */       }
/* 167:132 */       if ((sender instanceof Player))
/* 168:    */       {
/* 169:133 */         Player player = (Player)sender;
/* 170:134 */         Location loc = player.getTargetBlock(null, 20).getLocation();
/* 171:135 */         if (!checkLoc(loc, Boolean.valueOf(false)))
/* 172:    */         {
/* 173:136 */           player.sendMessage("Please look at a chest to use this command");
/* 174:137 */           return false;
/* 175:    */         }
/* 176:139 */         if (args[0].equals("create"))
/* 177:    */         {
/* 178:140 */           FileConfiguration config = getConfig();
/* 179:141 */           int amount = config.getInt("defaultamount");int delay = config.getInt("defaultdelay");int length = config.getInt("defaultpulse");
/* 180:142 */           boolean keep = config.getBoolean("defaultkeep");
/* 181:143 */           if (args.length > 1)
/* 182:    */           {
/* 183:    */             try
/* 184:    */             {
/* 185:145 */               amount = Integer.parseInt(args[1]);
/* 186:    */             }
/* 187:    */             catch (NumberFormatException ex)
/* 188:    */             {
/* 189:148 */               player.sendMessage("Amount must be a number");
/* 190:149 */               return false;
/* 191:    */             }
/* 192:151 */             if (args.length > 2)
/* 193:    */             {
/* 194:    */               try
/* 195:    */               {
/* 196:153 */                 length = Integer.parseInt(args[2]);
/* 197:    */               }
/* 198:    */               catch (NumberFormatException ex)
/* 199:    */               {
/* 200:156 */                 player.sendMessage("Pulse must be a number");
/* 201:157 */                 return false;
/* 202:    */               }
/* 203:160 */               if (args.length > 3)
/* 204:    */               {
/* 205:    */                 try
/* 206:    */                 {
/* 207:162 */                   delay = Integer.parseInt(args[3]);
/* 208:    */                 }
/* 209:    */                 catch (NumberFormatException ex)
/* 210:    */                 {
/* 211:165 */                   player.sendMessage("Delay must be a number");
/* 212:166 */                   return false;
/* 213:    */                 }
/* 214:168 */                 if (args.length == 5) {
/* 215:170 */                   keep = Boolean.parseBoolean(args[4]);
/* 216:    */                 }
/* 217:    */               }
/* 218:    */             }
/* 219:    */           }
/* 220:176 */           this.instances.add(new Scanner(loc, amount, delay, length, keep, player));
/* 221:177 */           player.sendMessage("Scanner Created");
/* 222:178 */           return true;
/* 223:    */         }
/* 224:181 */         Scanner scan = getScanner(loc);
/* 225:182 */         if ((player.hasPermission("itemscan.admin")) || (getServer().getPlayerExact(scan.getOwner()).equals(player)))
/* 226:    */         {
/* 227:183 */           if (args[0].equals("remove"))
/* 228:    */           {
/* 229:184 */             this.instances.remove(scan);
/* 230:185 */             loc.getBlock().breakNaturally();
/* 231:186 */             return true;
/* 232:    */           }
/* 233:188 */           if (args[0].equals("set"))
/* 234:    */           {
/* 235:189 */             if (args.length > 1)
/* 236:    */             {
/* 237:190 */               if (args[1].equals("keep"))
/* 238:    */               {
/* 239:    */                 try
/* 240:    */                 {
/* 241:193 */                   bool = Boolean.parseBoolean(args[2]);
/* 242:    */                 }
/* 243:    */                 catch (RuntimeException ex)
/* 244:    */                 {
/* 245:    */                   boolean bool;
/* 246:196 */                   player.sendMessage("Error: true or false only");
/* 247:197 */                   return false;
/* 248:    */                 }
/* 249:    */                 boolean bool;
/* 250:199 */                 scan.setKeep(bool);
/* 251:200 */                 player.sendMessage(args[1] + " set to " + args[2]);
/* 252:201 */                 return true;
/* 253:    */               }
/* 254:    */               try
/* 255:    */               {
/* 256:205 */                 parameter = Integer.parseInt(args[2]);
/* 257:    */               }
/* 258:    */               catch (RuntimeException ex)
/* 259:    */               {
/* 260:    */                 int parameter;
/* 261:208 */                 player.sendMessage("Number invalid");
/* 262:209 */                 return false;
/* 263:    */               }
/* 264:    */               int parameter;
/* 265:211 */               if (args[1].equals("delay"))
/* 266:    */               {
/* 267:212 */                 scan.setDelay(parameter);
/* 268:213 */                 player.sendMessage(args[1] + " set to " + args[2]);
/* 269:214 */                 return true;
/* 270:    */               }
/* 271:216 */               if (args[1].equals("pulse"))
/* 272:    */               {
/* 273:217 */                 scan.setPulse(parameter);
/* 274:218 */                 player.sendMessage(args[1] + " set to " + args[2]);
/* 275:219 */                 return true;
/* 276:    */               }
/* 277:221 */               if (args[1].equals("amount"))
/* 278:    */               {
/* 279:222 */                 scan.setAmount(parameter);
/* 280:223 */                 player.sendMessage(args[1] + " set to " + args[2]);
/* 281:224 */                 return true;
/* 282:    */               }
/* 283:    */             }
/* 284:227 */             player.sendMessage("set amount|delay|pulse|keep [number]|[true/false]");
/* 285:228 */             return false;
/* 286:    */           }
/* 287:    */         }
/* 288:232 */         player.sendMessage("You don't have permission to do that");
/* 289:233 */         return false;
/* 290:    */       }
/* 291:    */     }
/* 292:237 */     return false;
/* 293:    */   }
/* 294:    */   
/* 295:    */   public Scanner getScanner(Location loc)
/* 296:    */   {
/* 297:243 */     for (Scanner scan : this.instances) {
/* 298:244 */       if (scan.getLocation().equals(loc)) {
/* 299:245 */         return scan;
/* 300:    */       }
/* 301:    */     }
/* 302:248 */     return null;
/* 303:    */   }
/* 304:    */   
/* 305:    */   boolean checkLoc(Location loc, Boolean fix)
/* 306:    */   {
/* 307:    */     try
/* 308:    */     {
/* 309:253 */       if (!loc.getBlock().getType().equals(Material.CHEST))
/* 310:    */       {
/* 311:254 */         if (fix.booleanValue())
/* 312:    */         {
/* 313:255 */           loc.getBlock().setType(Material.CHEST);
/* 314:256 */           return true;
/* 315:    */         }
/* 316:258 */         return false;
/* 317:    */       }
/* 318:    */     }
/* 319:    */     catch (NullPointerException ex)
/* 320:    */     {
/* 321:262 */       return false;
/* 322:    */     }
/* 323:264 */     return true;
/* 324:    */   }
/* 325:    */ }


/* Location:           C:\Users\Regan\Downloads\ItemScan.1.1.jar
 * Qualified Name:     com.hotmail.bstern2011.ItemScan.ItemScan
 * JD-Core Version:    0.7.0.1
 */
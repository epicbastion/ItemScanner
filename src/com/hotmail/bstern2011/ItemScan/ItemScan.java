package com.hotmail.bstern2011.ItemScan;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemScan extends JavaPlugin {
	AllListener pl = new AllListener(this);
	ArrayList<Scanner> instances = new ArrayList<Scanner>();
	private ArrayList<String> broke = new ArrayList<String>();
	public static String prefix = ChatColor.GOLD + "[" + ChatColor.GRAY
			+ "ItemScan" + ChatColor.GOLD + "] " + ChatColor.RESET;
	public static ItemScan plugin;

	public void onEnable() {

		System.out.println("<<ItemScan>> Coded by: sternmin8or");
		System.out
				.println("<<ItemScan>> Edited by: Alvarez @ PluginRequest.com");

		ItemScan.plugin = this;

		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		findOld();
		saveFiles();
		readFiles();
		getServer().getPluginManager().registerEvents(this.pl, this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new Ticker(this), 20L, 10L);
		getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Message(this.broke), 40L);

		System.out.println("<<ItemScan>> v"
				+ this.getDescription().getVersion() + " is now Enabled!");
	}

	public void onDisable() {
		saveFiles();
		System.out.println("<<ItemScan>> v"
				+ this.getDescription().getVersion() + " is now Disabled!");
	}

	private void readFiles() {
		new File("plugins/ItemScan").mkdir();

		ArrayList<File> files = new ArrayList<File>();

		for (World w : this.getServer().getWorlds()) {
			File folder = new File("plugins/ItemScan/" + w.getName());
			folder.mkdir();
			for (File f : folder.listFiles())
				files.add(f);
		}
		this.instances.clear();

		for (File f : files) {
			boolean broken = false;
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			World w = this.getServer().getWorld(yml.getString("World"));
			Double x = yml.getDouble("Location.X");
			Double y = yml.getDouble("Location.Y");
			Double z = yml.getDouble("Location.Z");
			Location loc = new Location(w, x, y, z);
			if (!checkLoc(loc, true))
				broken = true;
			int amount = yml.getInt("Amount");
			int delay = yml.getInt("Delay");
			int length = yml.getInt("Length");
			boolean keep = yml.getBoolean("Keep");
			OfflinePlayer pl = getServer().getOfflinePlayer(
					yml.getString("Owner"));

			if (!broken)
				this.instances.add(new Scanner(loc, amount, delay, length,
						keep, pl));
			else {
				broke.add("<<ItemScan>> [ERROR] Broken Scanner @ X: " + x
						+ " Y: " + y + " Z: " + z + " World: " + w.getName());
			}
		}
	}

	private void findOld() {
		new File("plugins/ItemScan").mkdir();
		this.instances.clear();
		File data = new File("plugins/ItemScan/locations.dat");

		if (data.exists()) {
			System.out.println("<<ItemScan>> Attempting to recover past data. Please wait.");
			try {
				FileInputStream fstream = new FileInputStream(data);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;
				int line = 1;
				while ((strLine = br.readLine()) != null) {

					try {
						boolean broken = false;
						boolean justSave = false;
						String[] params = strLine.split(",");
						String worldName = params[3];
						World world = this.getServer().getWorld(worldName);
						if(world == null){
							System.out.println("<<ItemScan>> World: " + worldName + " was not found on the server! Scanner will be saved but not activated!");
							justSave = true;
						}
						double x = Double.parseDouble(params[0]);
						double y = Double.parseDouble(params[1]);
						double z = Double.parseDouble(params[2]);
						Location loc = new Location(world,x,y,z);
						if (!checkLoc(loc, Boolean.valueOf(true)))
							broken = true;
						int amount = Integer.parseInt(params[4]);
						int delay = Integer.parseInt(params[5]);
						int length = Integer.parseInt(params[6]);
						boolean keep = false;
						OfflinePlayer pl = null;
						try {
							keep = Boolean.parseBoolean(params[7]);
							pl = getServer().getOfflinePlayer(params[8]);
						} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
						
						if(justSave){
							String dir = "plugins/ItemScan/" + worldName;
							File worldFolder = new File(dir);
							if(!worldFolder.exists())
								worldFolder.mkdir();
							File f = getFile(worldName, x,y,z);
							if(!f.exists())
								f.createNewFile();
							

							YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);

							yml.set("World", worldName);
							yml.set("Location.X", x);
							yml.set("Location.Y", y);
							yml.set("Location.Z", z);
							yml.set("Amount", amount);
							yml.set("Delay", delay);
							yml.set("Length", length);
							yml.set("Keep", keep);
							yml.set("Owner", pl.getName());
							
							yml.save(f);
							
						}else if (!broken){
							this.instances.add(new Scanner(loc, amount, delay,length, keep, pl));
						}else {
							this.broke
									.add("<<ItemScan>> [ERROR] Broken Scanner @ X: "
											+ loc.getX()
											+ " Y: "
											+ loc.getY()
											+ " Z: "
											+ loc.getY()
											+ " World: "
											+ loc.getWorld().getName());
						}

					} catch (Exception e) {
						System.out.println("<<ItemScan>> [ERROR] There was an error when recovering Scanner on line: " + line);
						e.printStackTrace();
					}
					line++;
				}
				
				br.close();
				in.close();
				fstream.close();
				data.delete();
				
				System.out.println("<<ItemScan>> Scanners were recovered Successfully!");
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
			
		}
	}

	private void saveFiles() {
		for (World w : this.getServer().getWorlds()) {
			File folder = new File("plugins/ItemScan/" + w.getName());
			folder.mkdir();
		}
		for (Scanner s : this.instances) {
			File f = this.getFile(s);
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			yml.set("World", s.getLocation().getWorld().getName());
			yml.set("Location.X", s.getLocation().getX());
			yml.set("Location.Y", s.getLocation().getY());
			yml.set("Location.Z", s.getLocation().getZ());
			yml.set("Amount", s.getAmount());
			yml.set("Delay", s.getDelay());
			yml.set("Length", s.getPulse());
			yml.set("Keep", s.keep());
			yml.set("Owner", s.getOwner());

			try {
				yml.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (cmd.getName().equals("scanner")) {
			if (args.length == 0) {

				sender.sendMessage(ChatColor.RED + "-------" + ChatColor.WHITE
						+ "Item Scan" + ChatColor.RED + "-------");
				sender.sendMessage(ChatColor.YELLOW
						+ "/scanner create.............................Creates a Scanner");
				sender.sendMessage(ChatColor.YELLOW
						+ "/scanner remove.............................Removes a Scanner");
				sender.sendMessage(ChatColor.YELLOW
						+ "/scanner set [keep|delay|pulse|amount]......Changes a Scanner");
				sender.sendMessage(ChatColor.YELLOW
						+ "/scanner save.....................Save Scanners on the Server");
				sender.sendMessage(ChatColor.YELLOW
						+ "/scanner load.........................Load Scanners from Data");
				return true;
			}
			if (sender.hasPermission("itemscan.admin")) {
				if (args[0].equals("save")) {
					saveFiles();
					sender.sendMessage(ItemScan.prefix + "Saved");
					return true;
				}
				if (args[0].equals("load")) {
					readFiles();
					sender.sendMessage(ItemScan.prefix + "Loaded");
					return true;
				}
			}
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				Location loc = player.getTargetBlock(null, 20).getLocation();
				if (!checkLoc(loc, false)) {
					player.sendMessage(ItemScan.prefix
							+ "Please look at a chest to use this command");
					return false;
				}
				if (args[0].equals("create")) {
					FileConfiguration config = getConfig();
					int amount = config.getInt("defaultamount");
					int delay = config.getInt("defaultdelay");
					int length = config.getInt("defaultpulse");
					boolean keep = false;
					if (args.length > 1) {
						try {
							amount = Integer.parseInt(args[1]);
						} catch (NumberFormatException ex) {
							player.sendMessage(ItemScan.prefix
									+ "Amount must be a number");
							return false;
						}
						if (args.length > 2) {
							try {
								length = Integer.parseInt(args[2]);
							} catch (NumberFormatException ex) {
								player.sendMessage(ItemScan.prefix
										+ "Pulse must be a number");
								return false;
							}

							if (args.length > 3) {
								try {
									delay = Integer.parseInt(args[3]);
								} catch (NumberFormatException ex) {
									player.sendMessage(ItemScan.prefix
											+ "Delay must be a number");
									return false;
								}
								if (args.length == 5) {
									keep = Boolean.parseBoolean(args[4]);
								}
							}
						}
					}

					this.instances.add(new Scanner(loc, amount, delay, length,
							keep, player));
					player.sendMessage(ItemScan.prefix + "Scanner Created");
					return true;
				}

				Scanner scan = getScanner(loc);
				if ((player.hasPermission("itemscan.admin"))
						|| (getServer().getPlayerExact(scan.getOwner())
								.equals(player))) {
					if (args[0].equals("remove")) {
						this.instances.remove(scan);
						loc.getBlock().breakNaturally();

						File f = getFile(scan);
						if (f.exists())
							f.delete();

						return true;
					}
					if (args[0].equals("set")) {
						if (args.length > 1) {
							if (args[1].equals("keep")) {
								scan.setKeep(Boolean.parseBoolean(args[2]));
								return true;
							}
							int parameter;
							try {
								parameter = Integer.parseInt(args[2]);
							} catch (RuntimeException ex) {
								player.sendMessage(ItemScan.prefix
										+ "Number invalid");
								return false;
							}
							if (args[1].equals("delay")) {
								scan.setDelay(parameter);
								player.sendMessage(args[1] + " set to "
										+ args[2]);
								return true;
							}
							if (args[1].equals("pulse")) {
								scan.setPulse(parameter);
								player.sendMessage(args[1] + " set to "
										+ args[2]);
								return true;
							}
							if (args[1].equals("amount")) {
								scan.setAmount(parameter);
								player.sendMessage(args[1] + " set to "
										+ args[2]);
								return true;
							}
						}
						player.sendMessage(ItemScan.prefix
								+ "set amount|delay|pulse|keep");
						return false;
					}
				}

				player.sendMessage(ItemScan.prefix
						+ "You don't have permission to do that");
				return false;
			}
		}

		return false;
	}

	public Scanner getScanner(Location loc) {
		for (Scanner scan : this.instances) {
			if (scan.getLocation().equals(loc)) {
				return scan;
			}
		}
		return null;
	}

	public File getFile(Scanner s) {
		String world = s.getLocation().getWorld().getName();
		double x = s.getLocation().getX();
		double y = s.getLocation().getY();
		double z = s.getLocation().getZ();
		String dir = "plugins/ItemScan/" + world + "/" + world + "_"
				+ String.valueOf(x) + "_" + String.valueOf(y) + "_"
				+ String.valueOf(z) + ".yml";

		return new File(dir);
	}
	
	public File getFile(String world, double x, double y, double z) {
		String dir = "plugins/ItemScan/" + world + "/" + world + "_"
				+ String.valueOf(x) + "_" + String.valueOf(y) + "_"
				+ String.valueOf(z) + ".yml";

		return new File(dir);
	}

	boolean checkLoc(Location loc, Boolean fix) {
		try {
			if (!loc.getBlock().getType().equals(Material.CHEST)) {
				if (fix) {
					loc.getBlock().setType(Material.CHEST);
					return true;
				}
				return false;
			}
		} catch (NullPointerException ex) {
			return false;
		}
		return true;
	}

}
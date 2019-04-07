package me.mizaki.boussole.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aypi.utils.Button;
import com.aypi.utils.Menu;
import com.aypi.utils.inter.MenuItemListener;

import me.mizaki.boussole.main.CompassMain;

public class ClickCompassEvent implements Listener
{

	private Menu mainInventory;
	private Menu poleInventory;
	
	/*
	 * Cette Fonction gere l'ouverture de l'inventaire par l'intermediaire d'une boussole
	 */
	
	@EventHandler
	public void onMakeInventoryClick(PlayerInteractEvent event) 
	{
		final Player player = event.getPlayer();
		
		if(player.getInventory().getItemInMainHand() != null || player.getInventory().getItemInOffHand() != null)
		{
			if(player.getInventory().getItemInMainHand().getType() == Material.COMPASS || player.getInventory().getItemInOffHand().getType() == Material.COMPASS)
			{
				if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					this.mainInventory = new Menu(player, "Boussole de " + player.getName(), 9, false);
					fillMainInventory(player);
					this.mainInventory.openMenu();
				}
			}
		}
		
	}
	
	private void fillMainInventory(Player player)
	{
		ItemMeta meta;
		
		ItemStack lit = new ItemStack(Material.RED_BED);
		meta = lit.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Lit");
		lit.setItemMeta(meta);
		
		this.mainInventory.setButton(0, new Button(lit, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				if(player.getBedSpawnLocation() != null)
				{
					CompassMain.sendMessage(player, "Direction le lit !");
					player.setCompassTarget(player.getBedSpawnLocation());
				}
				else
					CompassMain.sendMessage(player, "Vous n'avez pas de lit !");
				
				mainInventory.closePlayerMenu();
				
			}
		}));
		
		
		ItemStack position = new ItemStack(Material.ENDER_PEARL);
		meta = position.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Position enregistrée");
		position.setItemMeta(meta);
		
		this.mainInventory.setButton(1, new Button(position, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				if(CompassMain.getInstance().getPositions().containsKey(player.getName())) {
					player.setCompassTarget(CompassMain.getInstance().getPositions().get(player.getName()));
					CompassMain.sendMessage(player, "Direction la position enregistrée !");
				}
				else
					CompassMain.sendMessage(player, "Aucune position enregistrée !");
				
				mainInventory.closePlayerMenu();
			}
		}));
		
		ItemStack suivre = new ItemStack(Material.SKELETON_SKULL);
		meta = suivre.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Suivre un joueur");
		suivre.setItemMeta(meta);
		
		this.mainInventory.setButton(2, new Button(suivre, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				CompassMain.sendMessage(player, "Veuillez entrer le nom du joueur ci-dessous");
				CompassMain.getInstance().getSearchDemands().add(player.getName());
				mainInventory.closePlayerMenu();
			}
		}));
		
		ItemStack spawn = new ItemStack(Material.BLUE_BED);
		meta = spawn.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Spawn par défaut");
		spawn.setItemMeta(meta);
		
		this.mainInventory.setButton(3, new Button(spawn, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				if(CompassMain.getInstance().getDefaultSpawn() != null) {
					player.setCompassTarget(CompassMain.getInstance().getDefaultSpawn());
					CompassMain.sendMessage(player, "Direction le spawn de départ !");
				}
				else
					CompassMain.sendMessage(player, "Aucun spawn de départ défini");
				
				mainInventory.closePlayerMenu();
			}
		}));
		
		ItemStack origine = new ItemStack(Material.APPLE);
		meta = origine.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Pointez le 0 !");
		origine.setItemMeta(meta);
		
		this.mainInventory.setButton(4, new Button(origine, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				CompassMain.sendMessage(player, "Direction l'origine du monde !");
				player.setCompassTarget(new Location(player.getWorld(), 0.0, 0.0, 0.0));
				mainInventory.closePlayerMenu();
			}
		}));
		
		
		ItemStack pole = new ItemStack(Material.COMPASS);
		meta = pole.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Pointez vers un pole");
		pole.setItemMeta(meta);
		
		this.mainInventory.setButton(5, new Button(pole, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				mainInventory.closePlayerMenu();
				poleInventory = new Menu(player, "Pole", 9, false);
				fillPoleInventory(player);
				poleInventory.openMenu();
			}
		}));
		
		ItemStack block = new ItemStack(Material.IRON_BARS);
		meta = block.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Bloquer un joueur");
		block.setItemMeta(meta);
		
		this.mainInventory.setButton(5, new Button(block, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				CompassMain.sendMessage(player, "Veuillez entrer le nom du joueur à bloquer ci-dessous");
				CompassMain.getInstance().getSearchDemands().add(player.getName());
				mainInventory.closePlayerMenu();
			}
		}));
		
	}
	
	
	private void fillPoleInventory(Player player) {
		
		ItemMeta meta;
		
		ItemStack nord = new ItemStack(Material.GRASS_BLOCK);
		meta = nord.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "NORD");
		nord.setItemMeta(meta);
		
		
		this.poleInventory.setButton(0, new Button(nord, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				double x, y, z;
				x = player.getLocation().getX();
				y = player.getLocation().getY();
				z = (player.getLocation().getZ() - 5000.0);
				
				CompassMain.sendMessage(player, "Direction le nord !");
				player.setCompassTarget(new Location(player.getWorld(), x, y, z));
				poleInventory.closePlayerMenu();
				
			}
		}));
		
		
		ItemStack sud = new ItemStack(Material.GRASS_BLOCK);
		meta = sud.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "SUD");
		sud.setItemMeta(meta);
		
		this.poleInventory.setButton(1, new Button(sud, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				double x, y, z;
				x = player.getLocation().getX();
				y = player.getLocation().getY();
				z = (player.getLocation().getZ() + 5000.0);
				Location loc = new Location(player.getWorld(), x, y, z);
				
				CompassMain.sendMessage(player, "Direction le sud !");
				player.setCompassTarget(loc);
				poleInventory.closePlayerMenu();
			}
		}));
		
		ItemStack est = new ItemStack(Material.GRASS_BLOCK);
		meta = est.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "EST");
		est.setItemMeta(meta);
		
		this.poleInventory.setButton(2, new Button(est, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				double x, y, z;
				x = (player.getLocation().getX() + 5000.0);
				y = player.getLocation().getY();
				z = player.getLocation().getZ();
				Location loc = new Location(player.getWorld(), x, y, z);
				
				CompassMain.sendMessage(player, "Direction l'est !");
				player.setCompassTarget(loc);
				poleInventory.closePlayerMenu();
			}
		}));
		
		ItemStack ouest = new ItemStack(Material.GRASS_BLOCK);
		meta = ouest.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "OUEST");
		ouest.setItemMeta(meta);
		
		this.poleInventory.setButton(3, new Button(ouest, new MenuItemListener() {
			
			@Override
			public void onItemClick()
			{
				double x, y, z;
				x = (player.getLocation().getX() - 5000.0);
				y = player.getLocation().getY();
				z = player.getLocation().getZ();
				Location loc = new Location(player.getWorld(), x, y, z);
				
				CompassMain.sendMessage(player, "Direction l'ouest !");
				player.setCompassTarget(loc);
				poleInventory.closePlayerMenu();
			}
		}));
		
	}
	
}
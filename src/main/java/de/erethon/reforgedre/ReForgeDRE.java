/*
 * Copyright (C) 2017-2018 Daniel Saukel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erethon.reforgedre;

import de.erethon.commons.misc.EnumUtil;
import de.erethon.commons.misc.NumberUtil;
import de.erethon.sakura.SakuraItem;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import static org.bukkit.Material.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Daniel Saukel
 */
public class ReForgeDRE extends JavaPlugin {

    private static ReForgeDRE instance;

    public List<Material> disabledRecipes = Arrays.asList(DIAMOND_SWORD, IRON_SWORD, GOLD_SWORD, DIAMOND_AXE, IRON_AXE, GOLD_AXE, STONE_AXE, WOOD_AXE,
            DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS, IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS,
            GOLD_HELMET, GOLD_CHESTPLATE, GOLD_LEGGINGS, GOLD_BOOTS);

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new RecipeListener(), this);
        getServer().getPluginManager().registerEvents(new ParticleListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new InvisibilityListener(), this);
    }

    public static ReForgeDRE getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || args.length < 1) {
            return false;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("start") && args.length == 2 && JoinListener.cache.contains(player.getUniqueId()) | sender.isOp()) {
            PlayerInventory inventory = player.getInventory();
            switch (args[1]) {
                case "cuthalorn":
                    inventory.addItem(new ItemStack(Material.IRON_HOE), new ItemStack(Material.BEETROOT_SEEDS),
                            new ItemStack(Material.SEEDS), new ItemStack(Material.PUMPKIN_SEEDS), new ItemStack(Material.MELON_SEEDS));
                    break;
                case "arachnida":
                    inventory.addItem(DREItem.DWARF_PICKAXE);
                    break;
                case "sohothin":
                    inventory.addItem(DREItem.HOLY_SWORD);
                    break;
                case "hohenstein":
                    inventory.addItem(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1));
                    break;
                case "golvathal":
                    inventory.addItem(new ItemStack(Material.GOLD_INGOT, 64));
                    break;
                case "daoshen":
                    inventory.addItem(Weapon.KATANA.toItemStack(false, 4, "unbekannt", "Dao-Shen"), SakuraItem.SAPLING);
                    break;
                case "pirate":
                    ItemStack parrot = new ItemStack(Material.MONSTER_EGG, 1);
                    SpawnEggMeta meta = (SpawnEggMeta) parrot.getItemMeta();
                    meta.setSpawnedType(EntityType.PARROT);
                    parrot.setItemMeta(meta);
                    inventory.addItem(Weapon.PIRATE_SABER.toItemStack(false, 3, "Arrrr!", "7 Weltmeere"), parrot);
                    break;
            }
            inventory.addItem(new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_LEGGINGS),
                    new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.BREAD, 32));
            JoinListener.cache.remove(player.getUniqueId());
        }
        if (!sender.isOp()) {
            return false;
        }
        Weapon weapon = EnumUtil.getEnumIgnoreCase(Weapon.class, args[0]);
        if (weapon == null) {
            for (Weapon w : Weapon.values()) {
                if (w.name.equalsIgnoreCase(args[0])) {
                    weapon = w;
                }
            }
        }
        int quality = args.length >= 2 ? NumberUtil.parseInt(args[1]) : -1;
        if (weapon != null) {
            ((Player) sender).getInventory().addItem(weapon.toItemStack(false, quality, "unbekannt", Weapon.getOrigin(player)));
        } else {
            return false;
        }
        return true;
    }

}

package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.items.IItem;
import com.panacea.RufusPyramid.game.items.usableItems.Equippable;
import com.panacea.RufusPyramid.game.items.usableItems.IItemType;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;
import com.panacea.RufusPyramid.game.items.usableItems.Weapon;
import com.panacea.RufusPyramid.game.items.usableItems.Wearable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Classe di gestione degli oggetti equipaggiati e trasportati.
 * Contiene un enum pubblico con i tipi di oggetti equipaggiabili.
 */
public class Backpack {
    private static int MAX_STORAGE_CAPACITY = 18;

    private List<UsableItem> storage;
    private LinkedHashMap<Backpack.EquippableType, Equippable> equippedItems;

    public Backpack() {
        this.equippedItems = new LinkedHashMap<Backpack.EquippableType, Equippable>(EquippableType.values().length);
        this.storage = new ArrayList<UsableItem>(MAX_STORAGE_CAPACITY);
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.PICK, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.PICK, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.setEquipItem(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.PALETTE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.SWORD, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.SWORD, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.SWORD, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.DAGGER, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
//        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
    }

    public Equippable getEquippedItem(Backpack.EquippableType itemToRetrieve) {
        return equippedItems.get(itemToRetrieve);
    }

    /**
     *  Add item to backpack storage.
     *  Returns true if the item has been added successfully, false otherwise (storage full).
     */
    public boolean addItemToStorage(UsableItem newItem) {
        if (this.storage == null) {
            //Lazy initialization of storage arraylist (most creatures will not use this)
            this.storage = new ArrayList<UsableItem>(MAX_STORAGE_CAPACITY);
        }
        if (this.storage.size() == MAX_STORAGE_CAPACITY) {
            //Storage full
            return false;
        }

        this.storage.add(newItem);
        return true;
    }

    /*
     * Put an item in an equippable position.
     * Returns old equipped Item, if there is, null otherwise.
     */
    public IItem setEquipItem(EquippableType position, Equippable newItem) {
        return this.equippedItems.put(position, newItem);
    }

    /**
     * Put an item in an equippable position.
     * Returns old equipped Item, if there is, null otherwise.
     */
    public Equippable setEquipItem(Equippable newItem) {
        EquippableType position = null;
        IItemType type = newItem.getItemType();

        if (type instanceof Weapon.WeaponType) {
            position = EquippableType.LEFT_HAND;
        } else if (type instanceof Wearable.WearableType) {
            switch ((Wearable.WearableType)type) {
                case ARMOR:
                case ARMOR2:
                    position = EquippableType.CHEST;
                    break;
                case BOOTS:
                    position = EquippableType.FEET;
                    break;
                case HELM:
                    position = EquippableType.HEAD;
                    break;
                case SHIELD:
                    position = EquippableType.RIGHT_HAND;
                    break;
            }
        }

        if (position != null) {
            return this.equippedItems.put(position, newItem);
        } else {
            return null;
        }
    }

    public List<UsableItem> getStorage() {
        return this.storage;
    }

    public int getMaxStorageCapacity() {
        return MAX_STORAGE_CAPACITY;
    }

    public enum EquippableType {
        HEAD,
        CHEST,
        LEFT_HAND,  //Per adesso solo arma
        RIGHT_HAND, //Per adesso solo scudo
//        TWO_HANDED_WEAPON,
        GAUNTLETS,
//        LEFT_RING,
//        RIGHT_RING,
        FEET
    }
}

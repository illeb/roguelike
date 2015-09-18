package com.panacea.RufusPyramid.game.items.usableItems;

import com.panacea.RufusPyramid.game.Effect.Effect;

import java.util.ArrayList;

/**
 * Created by Lux on 13/09/2015.
 */
public class MiscItem extends UsableItem{

    public enum MiscItemType{
        A,
        INVINCIBILITY_POTION,
        HEALTH_POTION,
        KEY,
        RAW_MEAT
    }

    private MiscItemType type;
    public MiscItem(MiscItemType type, ArrayList<Effect> effects, String itemName){
        super(type.name(), effects, itemName);
        this.type=type;
    }
}

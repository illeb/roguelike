package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.view.GameBatch;
import com.panacea.RufusPyramid.game.view.SoundsProvider;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 * <p/>
 * Created by gio on 11/07/15.
 */
public class AnimDeath extends AbstractCreatureAnimation {

    private Class modelClass;

    public AnimDeath(Class modelClass, GridPoint2 position, boolean flipX) {
        super(modelClass, position, flipX, SpritesProvider.Azione.DEATH);
        this.modelClass = modelClass;
    }

//    public void create() {
//        super.create();
//        if (this.modelClass == DefaultHero.class) {
//            int randSound = Utilities.randInt(0, SoundsProvider.Sounds.COMBAT_SLICE.getValue() - 1);
//            SoundsProvider.get().loadSound(SoundsProvider.Sounds.COMBAT_SLICE);
//            this.setAnimationSound(SoundsProvider.get().getSound(SoundsProvider.Sounds.COMBAT_SLICE)[randSound]);
//        }
//    }
}

package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.GameBatch;
import com.panacea.RufusPyramid.game.view.SoundsProvider;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 * <p/>
 * Created by gio on 11/07/15.
 */
public class AnimWalk extends AbstractCreatureAnimation {

    private GridPoint2 startPoint;
    private GridPoint2 endPoint;

    private float speed;

    private Vector2 currentPos;
    private Vector2 velocity;
    private Vector2 deltaMovement;
    private Vector2 endPos;
    private Vector2 direction;

    private ICreature creature;

    Sound footsteps;


    public AnimWalk(Class modelClass, GridPoint2 startPoint, GridPoint2 endPoint/*, float speed*/, boolean flipX, ICreature creature) {
        super(modelClass, startPoint, flipX, SpritesProvider.Azione.WALK);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.speed = 140f;
        this.creature = creature;
        this.setAbsolutePosition(Utilities.convertToAbsolutePos(startPoint));
        this.setSoundVolume(0.1f);
    }

    public void create() {
        super.create();
        if (this.creature.getClass() == DefaultHero.class) {
            int randSound = Utilities.randInt(0, SoundsProvider.Sounds.FOOTSTEPS_INTERNAL.getValue() - 1);
            SoundsProvider.get().loadSound(SoundsProvider.Sounds.FOOTSTEPS_INTERNAL);
            this.setAnimationSound(SoundsProvider.get().getSound(SoundsProvider.Sounds.FOOTSTEPS_INTERNAL)[randSound]);
        }

        // Reference: http://stackoverflow.com/questions/17694076/moving-a-point-vector-on-an-angle-libgdx
        // Declared as fields, so they will be reused
        GridPoint2 absoluteStartPoint = this.getAbsolutePosition();
        currentPos = new Vector2(absoluteStartPoint.x, absoluteStartPoint.y);
        velocity = new Vector2();
        deltaMovement = new Vector2();

        GridPoint2 absoluteEndPoint = Utilities.convertToAbsolutePos(endPoint);
        endPos = new Vector2(absoluteEndPoint.x, absoluteEndPoint.y);
        direction = new Vector2();
        // On touch events, set the touch vector, then do this to get the direction vector
        direction.set(endPos).sub(currentPos).nor();

        // scale it to the speed you want to move, then use it to update your position.
        velocity = new Vector2(direction).scl(speed);
    }

    @Override
    public void render(float delta) {
        boolean toDispose = false;
        deltaMovement.set(velocity).scl(delta);

        creature.setAbsoluteTickPosition(currentPos);
        if (currentPos.dst2(endPos) > deltaMovement.len2()) { //Se la distanza tra la posiz. attuale e la posiz. finale è minore di deltaMovement
            currentPos.add(deltaMovement);
        } else {
            currentPos.set(endPos);
            toDispose = true;
        }

        this.setAbsolutePosition(new GridPoint2(Math.round(currentPos.x), Math.round(currentPos.y)));

        super.render(delta);

        if (toDispose) {
            this.dispose();
        }
    }
}

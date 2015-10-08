package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.AbstractCreature;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.HeroController;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.animations.AbstractAnimation;
import com.panacea.RufusPyramid.game.view.animations.AnimDamage;
import com.panacea.RufusPyramid.game.view.animations.AnimDeath;
import com.panacea.RufusPyramid.game.view.animations.AnimInfo;
import com.panacea.RufusPyramid.game.view.animations.AnimStrike;
import com.panacea.RufusPyramid.game.view.animations.AnimWalk;
import com.panacea.RufusPyramid.game.view.animations.AnimationData;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedEvent;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedListener;
import com.panacea.RufusPyramid.game.view.input.HeroInputManager;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.ui.HealthBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.animation.Animation;

/**
 * Created by gio on 16/07/15.
 */
public class CreaturesDrawer extends ViewObject {

    private List<ICreature> creaturesList;
    private AbstractCreature.PositionChangeListener posChangeListener;

    ////////////////////////
    private HeroController heroController;
    private com.panacea.RufusPyramid.game.view.input.HeroInputManager heroInput;
    ///////////////////////

    //TODO creare un oggetto che contenga Texture, creaturestate e animations in modo da usare una sola hashmap?
    private HashMap<Integer, TextureRegion> sprites;
    private HashMap<Integer, CreatureState> currentStates;
    private HashMap<Integer, AbstractAnimation> currentAnimations = null;
    private DelayedRemovalArray<AbstractAnimation> generalAnimations = null;

    public CreaturesDrawer(List creaturesList) {
        this.creaturesList = creaturesList;
        this.sprites = new HashMap<Integer, TextureRegion>();
        this.currentStates = new HashMap<Integer, CreatureState>();
        this.currentAnimations = new HashMap<Integer, AbstractAnimation>();
        this.generalAnimations = new DelayedRemovalArray<AbstractAnimation>();

//        this.posChangeListener = new AbstractCreature.PositionChangeListener() {
//            @Override
//            public void changed(AbstractCreature.PositionChangeEvent event, Object source) {
                //Faccio l'animazione di camminata in base ai dati dell'evento e metto in pausa l'input utente
//                AnimationEndedListener listener = new AnimationEndedListener() {
//                    @Override
//                    public void ended(AnimationEndedEvent event, Object source) {
//                        //Poi renderizzo l'eroe in setStanding e riabilito l'input
//                        CreaturesDrawer.this.setStanding();
//                    }
//                };
//                CreaturesDrawer.this.walkAnimation((DefaultHero) source, event.getPath(), listener);
//            }
//        };
        for (ICreature creature : this.creaturesList) {
            this.sprites.put(creature.getID(), getSprite(creature));
            this.setStanding(creature.getID());
        }

        this.heroInput = InputManager.get().getHeroProcessor();
    }

    private static TextureRegion getSprite(ICreature creature) {
        if (creature instanceof DefaultHero) {
            return getHeroSprite((DefaultHero)creature);
        }
        return getCreatureSprite(creature);
    }

    private static TextureRegion getCreatureSprite(ICreature creature) {
        return SpritesProvider.getSprites(creature.getCreatureType(), SpritesProvider.Azione.STAND)[0];
    }

    private static TextureRegion getHeroSprite(DefaultHero heroModel) {
//        return SpritesProvider.getSprites(heroModel.getCreatureType() ,SpritesProvider.Azione.STAND)[0];
        return getCreatureSprite(heroModel);
    }

    private void setStanding(int creatureID) {
        currentStates.put(creatureID, CreatureState.STANDING);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        for (ICreature creature : this.creaturesList) {
            switch (this.currentStates.get(creature.getID())) {

                case STRIKING:
                case DYING:
                case WALKING:
                    AbstractAnimation currentAnimation = this.currentAnimations.get(creature.getID());
                    if (currentAnimation != null) {
                        currentAnimation.render(delta);
                    }
                    break;
                case STANDING:
                    GridPoint2 pos = creature.getPosition().getPosition();
                    GridPoint2 spritePosition = creature.getPosition().getPosition();
                    SpriteBatch batch = GameBatch.get();
                    batch.begin();
                    GridPoint2 newPos=Utilities.convertToAbsolutePos(spritePosition);
                    TextureRegion currentFrame = this.sprites.get(creature.getID());
                    if((!currentFrame.isFlipX() && creature.getFlipX()) || (currentFrame.isFlipX() && !creature.getFlipX()))
                        currentFrame.flip(true, false);
                    batch.draw(currentFrame,newPos.x,newPos.y,Utilities.DEFAULT_BLOCK_WIDTH, Utilities.DEFAULT_BLOCK_HEIGHT);
                    batch.end();

                    break;
                default:
                    Gdx.app.error(CreaturesDrawer.class.toString(), "Non so che sprite disegnare! Stato corrente: " + this.currentStates.get(creature.getID()));
                    break;
            }

            Vector2 position = creature.getAbsoluteTickPosition();
            HealthBar health = creature.getHealthBar();
            if(position != null && health.isVisible()) {
                SpriteBatch batch = GameBatch.get();
                batch.begin();
                health.setValue(creature.getHPCurrent());
                health.setX(position.x + 5);
                health.setY(position.y - 10);
                health.draw(batch, 1);
                batch.end();
            }
        }

        // Dato che la chiamata a render può provocare la rimozione dell'elemento stesso su cui è chiamata
        // (tramite lancio dell'evento AnimationEndedEvent, la sua cattura e la conseguente rimozione)
        // Utilizzo un tipo di lista appositamente implementato per effettuare rimozioni in un ciclo foreach.
        this.generalAnimations.begin();
        for (AbstractAnimation anim : this.generalAnimations) {
            anim.render(delta);
        }
        this.generalAnimations.end();
    }

    public void startWalk(final ICreature creature, GridPoint2 startPoint, final GridPoint2 endPoint) {
        //Faccio l'animazione di camminata in base ai dati dell'evento e metto in pausa l'input utente

//        ArrayList<GridPoint2> path = new ArrayList<GridPoint2>(2);
//        path.add(startPoint);
//        path.add(endPoint);
        AnimWalk.AnimWalkData data = new AnimWalk.AnimWalkData(startPoint, endPoint, creature.getFlipX());
        this.startCreatureAnimation(creature, data, CreatureState.WALKING);
    }

    public void startDeath(final ICreature dying) {
        AnimationData data = new AnimationData(dying.getPosition().getPosition(), dying.getFlipX());
        this.startCreatureAnimation(dying, data, CreatureState.DYING);
    }

    public void startStrike(final ICreature attacker) {
        AnimationData data = new AnimationData(attacker.getPosition().getPosition(), attacker.getFlipX());
        this.startCreatureAnimation(attacker, data, CreatureState.STRIKING);
    }

    public void startDamage(GridPoint2 position, int damage) {

        final AbstractAnimation currentAnimation = new AnimDamage(position, damage);
        currentAnimation.create();
        this.generalAnimations.add(currentAnimation);

        AnimationEndedListener listener = new AnimationEndedListener() {
            @Override
            public void ended(AnimationEndedEvent event, Object source) {
                generalAnimations.removeValue(currentAnimation, false);
            }
        };
        currentAnimation.addListener(listener);
    }

    public void displayInfo(GridPoint2 position, String info, Color color){
        final AbstractAnimation currentAnimation = new AnimInfo(position, info, color);
        currentAnimation.create();
        this.generalAnimations.add(currentAnimation);

        AnimationEndedListener listener = new AnimationEndedListener() {
            @Override
            public void ended(AnimationEndedEvent event, Object source) {
                generalAnimations.removeValue(currentAnimation, false);
            }
        };
        currentAnimation.addListener(listener);

    }

//    private void walkAnimation(ICreature creature, ArrayList<GridPoint2> path, AnimationEndedListener listener) {
////        this.startWalk(creature, path.get(0), path.get(1));
//        AbstractAnimation currentAnimation = new AnimWalk(creature, path.get(0), path.get(1), creature.getFlipX());
//        currentAnimation.create();
//        currentAnimation.addListener(listener);
//        this.currentAnimations.put(creature.getID(), currentAnimation);
//        this.currentStates.put(creature.getID(), CreatureState.WALKING);
//    }
//
//    private void strikeAnimation(ICreature attacker, AnimationEndedListener listener) {
////        this.startWalk(creature, path.get(0), path.get(1));
//        AbstractAnimation currentAnimation = new AnimStrike(attacker, attacker.getPosition().getPosition(), attacker.getFlipX());
//        currentAnimation.create();
//        currentAnimation.addListener(listener);
//        this.currentAnimations.put(attacker.getID(), currentAnimation);
//        this.currentStates.put(attacker.getID(), CreatureState.WALKING);
//    }
//
//    private void deathAnimation(ICreature dying, AnimationEndedListener listener) {
////        this.startWalk(creature, path.get(0), path.get(1));
//        AbstractAnimation currentAnimation = new AnimDeath(dying, dying.getPosition().getPosition(), dying.getFlipX());
//        currentAnimation.create();
//        currentAnimation.addListener(listener);
//        this.currentAnimations.put(dying.getID(), currentAnimation);
//        this.currentStates.put(dying.getID(), CreatureState.WALKING);
//    }
    private void startCreatureAnimation(final ICreature toAnimate, AnimationData data, CreatureState state) {
        this.startCreatureAnimation(toAnimate, data, state, null);
    }

    /**
     * Avvia l'animazione di una creatura mettendo in pausa l'input utente, aggiungendo un listener di tipo
     * AnimationEndedListener all'animazione e impostando lo stato in input alla creatura da animare.
     * L'animazione viene scelta in base allo stato di input.
     *
     * @param toAnimate La creatura da animare
     * @param data I dati necessari all'animazione
     * @param state Lo stato in cui la creatura deve essere impostata
     * @param listener Il listener da settare con le operazioni da effettuare alla fine dell'animazione (opzionale). Se null viene usato il listener di default.
     */
    private void startCreatureAnimation(final ICreature toAnimate, AnimationData data, CreatureState state, AnimationEndedListener listener) {
        //Preparo il gioco per iniziare l'animazione
        if(heroInput == null)
            this.heroInput = InputManager.get().getHeroProcessor();
        CreaturesDrawer.this.heroInput.setPaused(true);

        if (listener == null) { //Utilizzo il listener di default, che semplicemente reimposta lo stato della creatura a STANDING
            listener = new AnimationEndedListener() {
                @Override
                public void ended(AnimationEndedEvent event, Object source) {
                    //Poi renderizzo l'eroe in setStanding e riabilito l'input
                    CreaturesDrawer.this.setStanding(toAnimate.getID());
                    CreaturesDrawer.this.heroInput.setPaused(false);
                }
            };
        }

        //Avvio l'animazione
        AbstractAnimation currentAnimation;
        switch (state) {
            case WALKING:
                if (data instanceof AnimWalk.AnimWalkData) {
                    currentAnimation = new AnimWalk(toAnimate, (AnimWalk.AnimWalkData)data);
                } else {
                    Gdx.app.error(CreaturesDrawer.class.toString(), "I dati in input per l'animazione di camminata non sono di tipo AnimWalkData ma " + data.getClass().toString());
                }
                break;
            case STRIKING:
                currentAnimation = new AnimStrike(toAnimate, data);
                break;
            case DYING:
                currentAnimation = new AnimDeath(toAnimate, data);
                break;
            default:
                return;
        }

        currentAnimation = new AnimDeath(toAnimate, data);
        currentAnimation.create();
        currentAnimation.addListener(listener);
        this.currentAnimations.put(toAnimate.getID(), currentAnimation);
        this.currentStates.put(toAnimate.getID(), state);
    }

    private enum CreatureState {
        STANDING,
        WALKING,
        DYING,
        STRIKING
    }
}

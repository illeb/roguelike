package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.view.GameBatch;
import com.panacea.RufusPyramid.game.view.SpritesProvider;
import com.panacea.RufusPyramid.game.view.GameCamera;

import java.util.ArrayList;

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 *
 * Created by gio on 11/07/15.
 */
public class AnimWalk extends AbstractAnimation {

    //TODO mantieni un'unica immagine come TextureRegion e da lì prendi le immagini che ti servono
//    private static final int FRAME_COLS = 4;/*Grandezza della matrice degli sprite per l'animazione. Direi che 20 sprites bastano e avanzano*/
//    private static final int FRAME_ROWS = 1;
    private int frameCols;
    private int frameRows;


    private Animation walkAnimation;
    private Texture animationTexture;
    private TextureRegion[] walkFrames;
    private SpriteBatch spriteBatch; /*agent di libgdx per disegnare*/
    private TextureRegion currentFrame;

    private ArrayList<GridPoint2> path;
    private int walkImageIndex;     //Ricorda qual è l'ultimo frame disegnato

    private GridPoint2 startPoint;
    private GridPoint2 endPoint;

    private float speed;
    private float stateTime;
    private float frameDuration=0;

    /* Prova con i vector */
    private Vector2 currentPos;
    private Vector2 velocity;
    private Vector2 deltaMovement;
    private Vector2 endPos;
    private Vector2 direction;

    private Class modelClass;

    public AnimWalk(){
        this.frameDuration = 0.33f;
//        this.path = path;
    }

    public AnimWalk(Class modelClass, GridPoint2 startPoint, GridPoint2 endPoint, float speed){
        super();
        this.frameDuration = 0.2f;  //TODO imposta frameDuration in base alla velocità! Circa speed/200, ma deve essere in proporzione inversa
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.speed = speed;
        this.modelClass = modelClass;
    }

    public void create() {
        if (this.modelClass == DefaultHero.class) {
            this.frameCols = 4;
            this.frameRows = 1;
            animationTexture = new Texture(Gdx.files.internal("data/spritesheet2_walk.png"));
            walkFrames = SpritesProvider.getSprites(SpritesProvider.Oggetto.HERO1, SpritesProvider.Azione.WALK);
        } else {
            this.frameCols = 2;
            this.frameRows = 1;
            animationTexture = new Texture(Gdx.files.internal("data/thf2_rt2_walk.png"));

            TextureRegion[][] tmp = TextureRegion.split(animationTexture, animationTexture.getWidth()/this.frameCols, animationTexture.getHeight()/this.frameRows);
            walkFrames = new TextureRegion[this.frameCols * this.frameRows];
            int index = 0;
            for (int i = 0; i < this.frameRows; i++) {
                for (int j = 0; j < this.frameCols; j++) {
                    TextureRegion tr = tmp[i][j];
                    walkFrames[index++] = tr;
                }
            }
        }
        walkAnimation = new Animation(frameDuration, walkFrames);      // #11
        spriteBatch = GameBatch.get();                // #12
        stateTime = 0f;                         // #13
        this.walkImageIndex = -1;

        /* Prova con i vector */
        // Reference: http://stackoverflow.com/questions/17694076/moving-a-point-vector-on-an-angle-libgdx
        // Declared as fields, so they will be reused
        GridPoint2 absoluteStartPoint = Utilities.convertToAbsolutePos(startPoint);
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
        if (this.animationTexture == null) return;
        boolean toDispose = false;
        /* Prova con i vector */
        deltaMovement.set(velocity).scl(delta);

        GameModel.get().getHero().setAbsoluteTickPosition(currentPos);
        if (currentPos.dst2(endPos) > deltaMovement.len2()) { //Se la distanza tra la posiz. attuale e la posiz. finale è minore di deltaMovement
            currentPos.add(deltaMovement);
            stateTime += delta;
        } else {
            currentPos.set(endPos);
            stateTime += delta; //FIXME non è proprio delta, andrebbe fatta una proporzione!
            toDispose = true;
        }
        /* Fine */

//        stateTime = (stateTime + delta < this.animDuration ? stateTime + delta : this.animDuration);
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        spriteBatch.begin();
        spriteBatch.draw(currentFrame, currentPos.x, currentPos.y, 32, 32);
        spriteBatch.end();

        if (toDispose) {
            this.dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        //TODO libera tutte le risorse
        this.animationTexture.dispose();
        this.animationTexture = null;
        this.fireAnimationEndedEvent();
    }
}

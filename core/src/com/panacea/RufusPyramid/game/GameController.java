package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.screens.GameOverScreen;

/**
 * Permette dall'esterno di recuperare tutti i controller istanziati
 * Created by gio on 20/07/15.
 */
public class GameController {

    private static GameMaster gm;
    private static boolean gameInPlay;

    public static void initializeGame() {
        GameController.gameInPlay = true;
        GameController.resetAll();

        GameModel.createInstance();
        gm = new GameMaster();
    }

    private static void resetAll() {
        //TODO
        InputManager.reset();
    }

    public static void endGame() {
        GameController.gameInPlay = false;
        gm.disposeGame();
        GameModel.get().disposeAll();
        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen());
    }

    public static void step() {
        gm.step();
    }

    public static GameMaster getGm(){
        return gm;
    }

    public static boolean isGameEnded() {
        return !GameController.gameInPlay;
    }
}

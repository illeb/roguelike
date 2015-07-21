package com.panacea.RufusPyramid.game.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.view.GameDrawer;

public class GameScreen implements Screen {
    GameDrawer objectsDrawer;

    public void show() {
        GameModel.createInstance();
        objectsDrawer = new GameDrawer();
        objectsDrawer.create();
//        Gdx.app.log(GameScreen.class.toString(), "show() di GameScreen");
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        objectsDrawer.render(Gdx.graphics.getDeltaTime());
        objectsDrawer.render(delta);
    }

    public void resize(int width, int height) {
        objectsDrawer.resize(width, height);
    }

    public void pause() {
        objectsDrawer.pause();
    }

    public void resume() {
        objectsDrawer.resume();
    }

    public void hide() {
        objectsDrawer.pause();
    }

    public void dispose() {
        objectsDrawer.dispose();
    }
}
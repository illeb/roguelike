package com.panacea.RufusPyramid.game.view;

import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.map.MapContainer;

import java.util.LinkedList;
import java.util.List;

/**
 * Contiene tutti gli oggetti da renderizzare a video durante una sessione di gioco.
 * E' eventualmente possibile aggiungerne e rimuoverne di altri.
 * Created by gio on 11/07/15.
 */
public class GameDrawer extends com.panacea.RufusPyramid.game.view.ViewObject {
    private List<com.panacea.RufusPyramid.game.view.ViewObject> viewList;
    MapDrawer map;

    public GameDrawer() {
        map = new MapDrawer(new MapContainer(30, 30));  //map.create richiamato automaticamente da ViewObject
        this.viewList = new LinkedList<com.panacea.RufusPyramid.game.view.ViewObject>();
        this.viewList.add(map);
        this.viewList.add(new Animator());
        this.viewList.add(new HeroDrawer(GameModel.get().getHero()));
//        GameMaster gm = new GameMaster();
        this.viewList.add(new CreaturesDrawer(GameModel.get().getCreatures()));
    }

    public void add(com.panacea.RufusPyramid.game.view.ViewObject toAdd) {
        this.viewList.add(toAdd);
    }

    public void remove(com.panacea.RufusPyramid.game.view.ViewObject toRemove) {
        this.viewList.remove(toRemove);
    }

    @Override
    public void create() {
        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.create();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.resize(width, height);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        GameCamera.get().update();
        GameBatch.get().setProjectionMatrix(GameCamera.get().combined);

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.render(delta);
        }
    }

    @Override
    public void pause() {
        super.pause();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.resume();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.dispose();
        }
    }
}

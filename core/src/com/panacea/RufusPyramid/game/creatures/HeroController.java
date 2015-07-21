package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.InputManager;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;
import com.panacea.RufusPyramid.game.view.HeroInputManager;

import java.util.ArrayList;

/**
 * Created by gio on 11/07/15.
 */
public class HeroController {

    private DefaultHero hero;


    public HeroController(DefaultHero hero) {
        this.hero = hero;
    }

    public HeroController(DefaultHero hero, MapContainer spawnMap, Tile startingPosition) {
        this(hero);
        hero.setPosition(startingPosition);
    }

    public void setPosition(MapContainer spawnMap, GridPoint2 spawnPosition) {

    }

    public void moveToPosition(GridPoint2 finalPosition) {

    }

    /**
     * Move one step to input directory.
     * @param direction direction to move
     * @return true if moved successfully, false otherwise
     */
    public boolean moveOneStep(MoveDirection direction) {
        Tile startingTile = this.hero.getPosition();
        Tile arrivalTile = getNextTile(startingTile, direction);
        ArrayList<Tile> path = new ArrayList<Tile>();
        path.add(startingTile);
        path.add(arrivalTile);
        if (isTileWalkable(arrivalTile)) {
            //TODO Animator.walk(this.hero, startingTile, arrivalTile);
            this.hero.setPosition(arrivalTile, path);
            return true;
        }

        return false;
    }

    protected boolean isTileWalkable(Tile tileToCheck) {
        //TODO
        //return tileToCheck.isWalkable();
        return true;
    }

    private static Tile getNextTile(Tile startingTile, MoveDirection direction) {
        //TODO da integrare con il metodo fatto da belli per la mappa
        int tileDimension = 32;
        GridPoint2 pos = new GridPoint2(startingTile.getPosition());
        switch(direction) {
            case NORTH:
                pos.y += tileDimension;
                break;
            case SOUTH:
                pos.y += -tileDimension;
                break;
            case EAST:
                pos.x += tileDimension;
                break;
            case WEST:
                pos.x += -tileDimension;
                break;
        }
        return new Tile(pos, Tile.TileType.Solid);
    }


    public enum MoveDirection {
        NORTH,  NORTH_EAST,
        EAST,   SOUTH_EAST,
        SOUTH,  SOUTH_WEST,
        WEST,   NORTH_WEST
    }
}
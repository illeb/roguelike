package com.panacea.RufusPyramid.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

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
        if (isTileWalkable(arrivalTile)) {
            //TODO Animator.walk(this.hero, startingTile, arrivalTile);
            this.hero.setPosition(arrivalTile);
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
        //TODO
        return null;
    }


    public enum MoveDirection {
        NORTH,  NORTH_EAST,
        EAST,   SOUTH_EAST,
        SOUTH,  SOUTH_WEST,
        WEST,   NORTH_WEST
    }
}

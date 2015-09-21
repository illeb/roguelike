package com.panacea.RufusPyramid.game.view.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.HeroController;
import com.panacea.RufusPyramid.game.view.GameCamera;

import javax.rmi.CORBA.Util;

public class HeroInputManager extends InputAdapter {
    //TODO estendere GestureDetector?

    private int screenWidth = Gdx.graphics.getWidth();
    private int screenHeight = Gdx.graphics.getHeight();

    private HeroController hero;

    private boolean isPaused = false;

    private Vector2 touchDownPosition;

    public HeroInputManager(HeroController hero) {
        this.hero = hero;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (isPaused) return false;
        if (hasBeenDragged(this.touchDownPosition, new Vector2(screenX, screenY))) return false;

       GridPoint2 inputCoords = new GridPoint2(screenX,screenY);
        GridPoint2 oldHeroPos= GameModel.get().getHero().getPosition().getPosition();
        Utilities.Directions inputDirections = getInputDirections(inputCoords);
        Utilities.Directions slackDirections = getSlackPredominance(inputCoords);

        switch(inputDirections){
            case NORTH_EAST:
                if(slackDirections == Utilities.Directions.HORIZONTAL)
                    this.hero.chooseTheRightAction(Utilities.Directions.EAST);
                else if(slackDirections == Utilities.Directions.VERTICAL)
                    this.hero.chooseTheRightAction(Utilities.Directions.NORTH);
                break;

            case SOUTH_EAST:
                if(slackDirections == Utilities.Directions.HORIZONTAL)
                    this.hero.chooseTheRightAction(Utilities.Directions.EAST);
                else if(slackDirections == Utilities.Directions.VERTICAL)
                    this.hero.chooseTheRightAction(Utilities.Directions.SOUTH);
                break;

            case SOUTH_WEST:
                if(slackDirections == Utilities.Directions.HORIZONTAL)
                    this.hero.chooseTheRightAction(Utilities.Directions.WEST);
                else if(slackDirections == Utilities.Directions.VERTICAL)
                    this.hero.chooseTheRightAction(Utilities.Directions.SOUTH);
                break;

            case NORTH_WEST:
                if(slackDirections == Utilities.Directions.HORIZONTAL)
                    this.hero.chooseTheRightAction(Utilities.Directions.WEST);
                else if(slackDirections == Utilities.Directions.VERTICAL)
                    this.hero.chooseTheRightAction(Utilities.Directions.NORTH);
                break;
        }



        return true;
    }
    private Utilities.Directions getSlackPredominance(GridPoint2 clickCoords){ //this method tells which direction (vertical or horizontal) of the user input is more significant
        int screenX=clickCoords.x;
        int screenY = clickCoords.y;

        int verticalSlack;
        int horizontalSlack;
        if(screenX > screenWidth/2) //destra
            horizontalSlack = screenX - screenWidth/2;
        else//sinistra
            horizontalSlack = screenWidth/2 - screenX;


        if(screenY > screenHeight / 2) //su
            verticalSlack = screenY - screenHeight/2;
        else//giù
            verticalSlack = screenHeight/2 - screenY;

        if(horizontalSlack > verticalSlack)
            return Utilities.Directions.HORIZONTAL;
        else
            return Utilities.Directions.VERTICAL;
    }

    private Utilities.Directions getInputDirections(GridPoint2 clickCoords){ //this method understand which boundaries of the screen are been touched ( e.g: up and right of the screen-->NORTH_EAST)
        Utilities.Directions inputDirections;

        int screenX = clickCoords.x;
        int screenY = clickCoords.y;
        if(screenX >= screenWidth/2) //destra
        {
            if (screenY >= screenHeight / 2)//su
                inputDirections = Utilities.Directions.SOUTH_EAST;
            else
                inputDirections = Utilities.Directions.NORTH_EAST;
        }
        else                        //sinistra
        {
            if (screenY >= screenHeight / 2)//su
                inputDirections = Utilities.Directions.SOUTH_WEST;
            else
                inputDirections = Utilities.Directions.NORTH_WEST;
        }

        return inputDirections;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.touchDownPosition = new Vector2(screenX, screenY);
        return false;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    private boolean hasBeenDragged(Vector2 touchDownPosition, Vector2 touchUpPosition) {
        int sensibility = 5;
        Vector2 diff = touchDownPosition.sub(touchUpPosition);
        if (Math.abs(diff.x) > sensibility || Math.abs(diff.y) > sensibility) {
            return true;
        }
        return false;
    }
}
package com.panacea.RufusPyramid.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.OrderedSet;
import com.panacea.RufusPyramid.common.AttributeChangeEvent;
import com.panacea.RufusPyramid.common.AttributeChangeListener;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;

/**
 * Deve davvero essere una classe astratta? O basta chiamarla "GenericCreature" ?
 */
public abstract class AbstractCreature implements ICreature {

    private static int nextFreeIdCreature = -1;

    private ArrayList<PositionChangeListener> changeListeners;

    private int idCreature;
    private String name, description;
    private int maximumHP, currentHP;
    private double attack, defence, speed;
    private Tile position;
    private Backpack backpack;

    public AbstractCreature(String name, String description, int maximumHP, double attack, double defence, double speed) {
        this.idCreature = getUniqueCreatureId();
        this.setName(name);
        this.setDescription(description);
        this.setHPMaximum(maximumHP);
        this.setHPCurrent(this.getHPMaximum());
        this.setAttackValue(attack);
        this.setDefenceValue(defence);
        this.setSpeed(speed);
        this.setPosition(null);
        this.backpack = new Backpack();

        this.changeListeners = new ArrayList<PositionChangeListener>();
    }

    private static int getUniqueCreatureId() {
        return AbstractCreature.nextFreeIdCreature++;
    }

    @Override
    public int getID() {
        return this.idCreature;
    }

    @Override
    public int getHPCurrent() {
        return this.currentHP;
    }

    @Override
    public void setHPCurrent(int currentHP) {
        this.currentHP = currentHP;
    }

    @Override
    public int getHPMaximum() {
        return this.maximumHP;
    }

    @Override
    public void setHPMaximum(int maxHP) {
        this.maximumHP = maxHP;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = (name == null ? "Unknown" : name);
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = (description == null ? "" : description);
    }

    @Override
    public double getAttackValue() {
        return this.attack;
    }

    @Override
    public void setAttackValue(double attackValue) {
        this.attack = attackValue;
    }

    @Override
    public double getDefenceValue() {
        return this.defence;
    }

    @Override
    public void setDefenceValue(double defenceValue) {
        this.defence = defenceValue;
    }

    /**
     * Può essere null se la creatura non è nella mappa.
     * @return la posizione della creatura nella mappa, null se non è presente in mappa.
     */
    @Override
    public Tile getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Tile currentPosition) {
        this.position = currentPosition;
    }

    @Override
    public Backpack getEquipment() {
        return this.backpack;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

    @Override
    public void setSpeed(double currentSpeed) {
        this.speed = currentSpeed;
    }

    public void addChangeListener(PositionChangeListener listener) {
        this.changeListeners.add(listener);
    }

    private void firePositionChangeEvent() {
        PositionChangeEvent event = new PositionChangeEvent(this.getPosition().getPosition(), new ArrayList<GridPoint2>());
        for (PositionChangeListener listener : this.changeListeners) {
            listener.changed(event, this);
        }
    }

    public static class PositionChangeEvent extends AttributeChangeEvent<GridPoint2> {
        private final ArrayList<GridPoint2> path;

        public PositionChangeEvent(GridPoint2 newAttributeValue, ArrayList<GridPoint2> path) {
            super(newAttributeValue);
            this.path = path;
        }

        public ArrayList<GridPoint2> getPath() {
            return this.path;
        }
    }

    public static abstract class PositionChangeListener implements AttributeChangeListener<PositionChangeEvent> {
        public PositionChangeListener() {
        }

        public abstract void changed(PositionChangeEvent event, Object source);
    }
}
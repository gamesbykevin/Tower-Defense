package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.towerdefense.entity.Entity;
import com.gamesbykevin.towerdefense.level.map.Map;

/**
 * This class represents the enemy
 * @author GOD
 */
public final class Enemy extends Entity
{
    //the pixel dimensions of the enemy
    public static final double WIDTH = 46.0;
    public static final double HEIGHT = 46.0;
    
    /**
     * The max start health for the enemy
     */
    private double maxHealth = 100.0;
    
    /**
     * The enemy's current health
     */
    private double health = 100.0;
    
    /**
     * How fast does the enemy move from column to column, etc.....
     */
    private double speed = .01;
    
    //where the enemy is heading
    private Cell destination;
    
    public enum Type
    {
        Blue1(.01),
        Blue2(.01),
        Blue3(.01),
        Green1(.01),
        Green2(.01),
        Green3(.01),
        Red1(.01),
        Red2(.01),
        Red3(.01),
        Yellow1(.01),
        Yellow2(.01),
        Yellow3(.01),
        Boss1(.01),
        Boss2(.01);
        
        //the speed of the type of enemy
        private final double speed;
        
        private Type(final double speed)
        {
            this.speed = speed;
        }
        
        protected double getSpeed()
        {
            return this.speed;
        }
    }
    
    //the type of enemy
    private final Type type;
    
    protected Enemy(final Type type) throws Exception
    {
        //call parent
        super();
        
        //assign type
        this.type = type;
        
        //create default animation
        Animation animation = new Animation();
        
        //all enemy animations will loop 
        animation.setLoop(true);
        
        //set the enemy speed
        this.setSpeed(type.getSpeed());
        
        switch (type)
        {
            case Blue1:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 4), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 5), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Blue2:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 1), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 1), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 1), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 1), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(600L));
                break;
            
            case Blue3:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 2), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 2), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 2), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 2), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Green1:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 4), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 5), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Green2:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 4), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 4), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 4), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 4), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(600L));
                break;
            
            case Green3:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 5), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 5), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 5), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 5), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Red1:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 4), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 5), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Red2:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 7), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 7), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 7), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 7), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(600L));
                break;
            
            case Red3:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 8), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 8), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 8), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 8), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Yellow1:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 4), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 5), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Yellow2:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 10), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 10), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 10), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 10), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(600L));
                break;
            
            case Yellow3:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 11), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 11), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 11), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 11), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Boss1:
                animation.add(0,   (int)(HEIGHT * 12), 118, 118, Timers.toNanoSeconds(250L));
                animation.add(118, (int)(HEIGHT * 12), 118, 118, Timers.toNanoSeconds(250L));
                animation.add(236, (int)(HEIGHT * 12), 118, 118, Timers.toNanoSeconds(250L));
                break;
            
            case Boss2:
                animation.add(0,   670, 64, 64, Timers.toNanoSeconds(333L));
                animation.add(64,  670, 64, 64, Timers.toNanoSeconds(333L));
                animation.add(128, 670, 64, 64, Timers.toNanoSeconds(333L));
                animation.add(192, 670, 64, 64, Timers.toNanoSeconds(333L));
                break;
            
            default:
                throw new Exception("Type not setup here = " + type.toString());
        }
        
        //add animation
        super.addAnimation(animation);
    }
    
    /**
     * Get the location where the enemy is headed
     * @return The column, row
     */
    public Cell getDestination()
    {
        return this.destination;
    }
    
    public void setDestination(final Cell destination)
    {
        setDestination(destination.getCol(), destination.getRow());
    }
    
    public void setDestination(final double col, final double row)
    {
        if (getDestination() == null)
            this.destination = new Cell();
        
        //set the location
        this.destination.setCol(col);
        this.destination.setRow(row);
    }
    
    /**
     * Set the speed of the enemy
     * @param speed The rate at which the enemy can move
     */
    public final void setSpeed(final double speed)
    {
        this.speed = speed;
    }
    
    /**
     * Get the speed
     * @return The rate at which the enemy can move
     */
    public final double getSpeed()
    {
        return this.speed;
    }
    
    /**
     * Set the starting and current health of the enemy
     * @param health The max health we want to assign to the enemy
     */
    public void setStartHealth(final double health)
    {
        this.maxHealth = health;
        
        //assign the current health
        setHealth(health);
    }
    
    /**
     * Assign the current health 
     * @param health The current health we want to assign to the enemy
     */
    public void setHealth(final double health)
    {
        this.health = health;
    }
    
    /**
     * Get the enemy's starting health upon creation
     * @return The max starting health
     */
    public double getStartHealth()
    {
        return this.maxHealth;
    }
    
    /**
     * Get the enemy's current health
     * @return The current health
     */
    public double getHealth()
    {
        return this.health;
    }
    
    /**
     * Update the enemy's location based on the velocity
     */
    @Override
    public final void update()
    {
        //update the column location based on x-velocity
        setCol(getCol() + getVelocityX());
        
        //update the row location based on y-velocity
        setRow(getRow() + getVelocityY());
        
        //now assigned the x,y coordinates based on the column, row location
        setX(Map.getStartX(getCol()));
        setY(Map.getStartY(getRow()));
    }
    
    /**
     * Get the type of enemy this is
     * @return The type of enemy
     */
    public Type getType()
    {
        return this.type;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
}
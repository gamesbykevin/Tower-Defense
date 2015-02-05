package com.gamesbykevin.towerdefense.entity;

import com.gamesbykevin.towerdefense.level.object.LevelObject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Every tower, enemy, and projectile is an entity
 * @author GOD
 */
public abstract class Entity extends LevelObject
{
    public enum Direction
    {
        North, South, East, West
    }
    
    //the default facing direction of the entity animation
    private final Direction directionDefault;
    
    //the current facing direction of the entity
    private Direction direction;
    
    protected Entity(final Direction directionDefault)
    {
        this.directionDefault = directionDefault;
    }
    
    /**
     * Set the direction of the entity
     * @param direction The direction the entity is facing (North, South, East, West)
     */
    public void setDirection(final Direction direction)
    {
        this.direction = direction;
    }
    
    /**
     * Get the direction of this entity
     * @return The current facing direction (North, South, East, West)
     */
    public Direction getDirection()
    {
        return this.direction;
    }
    
    /**
     * Get the default facing direction of the entity animation
     * @return default facing direction (North, South, East, West)
     */
    private Direction getDefaultDirection()
    {
        return this.directionDefault;
    }
    
    /**
     * Here we render the entity applying the direction the entity should be facing
     * @param graphics Object used to draw image
     * @param image Image containing animation
     */
    public void render(final Graphics graphics, final Image image)
    {
        //cast to Graphics2D so we can apply rotation
        Graphics2D g2d = (Graphics2D)graphics;
        
        //draw image
        super.draw(g2d, image);
    }
}
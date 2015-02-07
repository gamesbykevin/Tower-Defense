package com.gamesbykevin.towerdefense.entity;

import com.gamesbykevin.towerdefense.level.object.LevelObject;

import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Every tower, enemy, and projectile is an entity
 * @author GOD
 */
public abstract class Entity extends LevelObject
{
    //our object used to apply rotation to this entity
    private AffineTransform at;
    
    /**
     * The different directions
     */
    public enum Direction
    {
        North, South, East, West
    }
    
    //the default facing direction of the entity animation
    private final Direction directionDefault;
    
    //the current facing direction of the entity
    private Direction direction;
    
    //the angle to face in radians
    private double angle = 0;
    
    protected Entity(final Direction directionDefault)
    {
        //assign the default directions
        this.directionDefault = directionDefault;
        
        //create new instance
        this.at = new AffineTransform();
    }
    
    /**
     * Get the angle
     * @return The angle the entity is facing in radians
     */
    public double getAngle()
    {
        return this.angle;
    }
    
    /**
     * Set the facing angle of this entity
     * @param angle The angle is in radians
     */
    public void setAngle(final double angle)
    {
        this.angle = angle;
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
        
        //rotate 90 degrees at this anchor position
        at.rotate(getAngle(), getX(), getY());
        
        //get the location
        final double x = getX();
        final double y = getY();
        
        //place in center
        super.setX(x - (getWidth() / 2));
        super.setY(y - (getHeight() / 2));
        
        //assign transformation
        g2d.setTransform(at);
        
        //draw image
        super.draw(g2d, image);
        
        //reset rotation
        at.setToIdentity();
        
        //apply reset back
        g2d.setTransform(at);
        
        //restore the location
        super.setX(x);
        super.setY(y);
    }
}
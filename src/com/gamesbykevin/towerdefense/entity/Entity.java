package com.gamesbykevin.towerdefense.entity;

import com.gamesbykevin.towerdefense.level.object.LevelObject;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Every tower, enemy, and projectile is an entity
 * @author GOD
 */
public abstract class Entity extends LevelObject
{
    //the angle to face in radians
    private double angle = 0;
    
    protected Entity()
    {
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
     * Set the facing angle of this entity.<br>
     * If the angle is greater than PI * 2 we will adjust to keep in bounds
     * @param angle The angle is in radians
     */
    public void setAngle(final double angle)
    {
        this.angle = angle;
        
        //keep angle in bounds
        if (getAngle() > Math.PI * 2)
            setAngle(getAngle() - (Math.PI * 2));
        if (getAngle() < 0)
            setAngle(Math.PI * 2);
    }
    
    /**
     * Offset the entity so the current location will be in the center of the entity
     */
    public void positionCenter()
    {
        //position entity in center
        setX(getX() - (getWidth()  / 2));
        setY(getY() - (getHeight() / 2));
    }
    
    /**
     * Reset the entity back before positionCenter() was called
     */
    public void positionReset()
    {
        //move entity back
        setX(getX() + (getWidth()  / 2));
        setY(getY() + (getHeight() / 2));
    }
    
    /**
     * Here we render the entity applying the direction the entity should be facing
     * @param graphics Object used to draw image
     * @param image Image containing animation
     */
    public void render(final Graphics graphics, final Image image)
    {
        //cetner location
        this.positionCenter();
        
        //draw entity
        super.draw(graphics, image);
        
        //restore location
        this.positionReset();
    }
}
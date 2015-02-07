package com.gamesbykevin.towerdefense.entity.projectile;

import com.gamesbykevin.towerdefense.entity.Entity;

/**
 * The projectile fired by a tower
 * @author GOD
 */
public final class Projectile extends Entity
{
    public enum Type
    {
        Blue1, Blue2, Blue3, Blue4, 
        Green1, Green2, Green3, Green4, 
        Red1, Red2, Red3, Red4, 
    }
    
    //the type of projectile this is
    private final Type type;
    
    protected Projectile(final Type type)
    {
        //all projectiles are vertical facing north
        super(Direction.North);
        
        //assign type
        this.type = type;
    }
    
    public Type getType()
    {
        return this.type;
    }
    
    @Override
    public void dispose()
    {
        dispose();
    }
}
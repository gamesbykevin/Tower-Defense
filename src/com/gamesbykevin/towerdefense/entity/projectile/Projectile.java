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
    
    protected Projectile(final Type type) throws Exception
    {
        //call parent
        super();
        
        //assign type
        this.type = type;
        
        switch (type)
        {
            case Blue1:
                super.addAnimation(0, 0, 13, 37);
                break;
                
            case Blue2:
                super.addAnimation(13, 0, 13, 37);
                break;
                
            case Blue3:
                super.addAnimation(26, 0, 9, 37);
                break;
                
            case Blue4:
                super.addAnimation(35, 0, 9, 37);
                break;
                
            case Green1:
                super.addAnimation(0, 37, 13, 37);
                break;
                
            case Green2:
                super.addAnimation(13, 37, 13, 37);
                break;
                
            case Green3:
                super.addAnimation(26, 37, 9, 37);
                break;
                
            case Green4:
                super.addAnimation(35, 37, 9, 37);
                break;
                
            case Red1:
                super.addAnimation(0, 74, 13, 37);
                break;
                
            case Red2:
                super.addAnimation(13, 74, 13, 37);
                break;
                
            case Red3:
                super.addAnimation(26, 74, 9, 37);
                break;
                
            case Red4:
                super.addAnimation(35, 74, 9, 37);
                break;
        }
    }
    
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
package com.gamesbykevin.towerdefense.entity.effects;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.towerdefense.entity.Entity;
import com.gamesbykevin.towerdefense.level.object.LevelObject;

/**
 * Object representing an effect in the game
 * @author GOD
 */
public final class Effect extends Entity
{
    public enum Type
    {
        //Explosion1, 
        Explosion2, Explosion3, Explosion4, 
        Explosion5, Explosion6, Explosion7, Explosion8, 
        Explosion9, Explosion10, Explosion11, Explosion12, 
    }
    
    /**
     * Default delay between each animation in the effect
     */
    private static final long DEFAULT_DELAY = Timers.toNanoSeconds(100L);
    
    //the type of effect
    private final Type type;
    
    protected Effect(final Type type) throws Exception
    {
        //call parent
        super();
        
        //assign the type
        this.type = type;
        
        //create new animation
        Animation animation = new Animation();
        
        //animations will not loop
        animation.setLoop(false);
        
        //the information that will help us retrieve the animation
        int x = 0, y = 0, w = 0, h = 0, cols = 0, rows = 0;
        
        switch (type)
        {
            /*
            case Explosion1:
                x = 0;
                y = 0;
                w = 128;
                h = 128;
                cols = 8;
                rows = 8;
                break;
            */
            
            case Explosion2:
                x = 0;
                y = 1024;
                w = 64;
                h = 64;
                cols = 16;
                rows = 1;
                break;
            
            case Explosion3:
                x = 0;
                y = 1088;
                w = 64;
                h = 64;
                cols = 16;
                rows = 1;
                break;
            
            case Explosion4:
                x = 0;
                y = 1152;
                w = 64;
                h = 64;
                cols = 16;
                rows = 1;
                break;
            
            case Explosion5:
                x = 0;
                y = 1216;
                w = 64;
                h = 64;
                cols = 16;
                rows = 1;
                break;
            
            case Explosion6:
                x = 0;
                y = 1280;
                w = 64;
                h = 64;
                cols = 16;
                rows = 1;
                break;
            
            case Explosion7:
                x = 0;
                y = 1344;
                w = 64;
                h = 64;
                cols = 16;
                rows = 1;
                break;
            
            case Explosion8:
                x = 0;
                y = 1408;
                w = 64;
                h = 64;
                cols = 16;
                rows = 1;
                break;
            
            case Explosion9:
                x = 0;
                y = 1472;
                w = 64;
                h = 64;
                cols = 16;
                rows = 1;
                break;
            
            case Explosion10:
                x = 0;
                y = 1536;
                w = 96;
                h = 96;
                cols = 5;
                rows = 4;
                break;
            
            case Explosion11:
                x = 0;
                y = 1920;
                w = 128;
                h = 128;
                cols = 10;
                rows = 1;
                break;
            
            case Explosion12:
                x = 0;
                y = 2048;
                w = 118;
                h = 118;
                cols = 6;
                rows = 1;
                break;
            
            default:
                throw new Exception("Type not setup here " + type.toString());
        }
        
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                animation.add(x + (col * w), y + (row * h), w, h, DEFAULT_DELAY);
            }
        }
        
        //add animation
        super.addAnimation(animation);
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
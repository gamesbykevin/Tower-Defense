package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.towerdefense.entity.Entity;

/**
 * This class represents the enemy
 * @author GOD
 */
public final class Enemy extends Entity
{
    //the pixel dimensions of the enemy
    public static final double WIDTH = 46.0;
    public static final double HEIGHT = 46.0;
    
    public enum Type
    {
        Blue1,
        Blue2,
        Blue3,
        Green1,
        Green2,
        Green3,
        Red1,
        Red2,
        Red3,
        Yellow1,
        Yellow2,
        Yellow3,
        Boss1,
        Boss2
    }
    
    //the type of enemy
    private final Type type;
    
    protected Enemy(final Type type) throws Exception
    {
        //all enemies default facing east
        super(Direction.East);
        
        //assign type
        this.type = type;
        
        //create default animation
        Animation animation = new Animation();
        
        //all enemy animations will loop 
        animation.setLoop(true);
        
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
        dispose();
    }
}
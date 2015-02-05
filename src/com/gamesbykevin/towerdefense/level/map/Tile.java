package com.gamesbykevin.towerdefense.level.map;

import com.gamesbykevin.towerdefense.level.object.LevelObject;

/**
 * The tiles make up the map
 * @author GOD
 */
public final class Tile extends LevelObject
{
    public enum Type
    {
        ES,
        WES,
        WS,
        NES,
        NESW,
        NSW,
        NE,
        WEN,
        NW,
        NS,
        EW,
        OPEN
    }
    
    private final Type type;
    
    protected Tile(final Type type) throws Exception
    {
        //assign tile type
        this.type = type;
        
        switch (type)
        {
            case ES:
                super.addAnimation(0, 0, Map.WIDTH, Map.HEIGHT);
                break;
                
            case WES:
                super.addAnimation(64, 0, Map.WIDTH, Map.HEIGHT);
                break;
                
            case WS:
                super.addAnimation(128, 0, Map.WIDTH, Map.HEIGHT);
                break;
                
            case NES:
                super.addAnimation(0, 64, Map.WIDTH, Map.HEIGHT);
                break;
                
            case NESW:
                super.addAnimation(64, 64, Map.WIDTH, Map.HEIGHT);
                break;
                
            case NSW:
                super.addAnimation(128, 64, Map.WIDTH, Map.HEIGHT);
                break;
                
            case NE:
                super.addAnimation(0, 128, Map.WIDTH, Map.HEIGHT);
                break;
                
            case WEN:
                super.addAnimation(64, 128, Map.WIDTH, Map.HEIGHT);
                break;
                
            case NW:
                super.addAnimation(128, 128, Map.WIDTH, Map.HEIGHT);
                break;
                
            case NS:
                super.addAnimation(0, 192, Map.WIDTH, Map.HEIGHT);
                break;
                
            case EW:
                super.addAnimation(64, 192, Map.WIDTH, Map.HEIGHT);
                break;
                
            case OPEN:
                super.addAnimation(128, 192, Map.WIDTH, Map.HEIGHT);
                break;

            default:
                throw new Exception("Type not setup here = " + type.toString());
        }
    }
    
    /**
     * Get the type
     * @return The tile type
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
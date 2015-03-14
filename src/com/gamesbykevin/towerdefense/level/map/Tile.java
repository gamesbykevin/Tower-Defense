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
        Horizontal,
        Vertical,
        Center,
        NW,
        NE,
        SW,
        SE
    }
    
    //the type of tile
    private Type type;
    
    //each tile is 46px width/height
    private static final int DIMENSIONS = 46;
    
    protected Tile()
    {
    }
    
    /**
     * Set the tile type so we know which animation to use
     * @param type The type of tile
     * @throws Exception Exception if type has already been set
     */
    public void setType(final Type type) throws Exception
    {
        if (this.type != null)
            throw new Exception("Tile type has already been set");
        
        //assign tile type
        this.type = type;
        
        switch (getType())
        {
            case Horizontal:
                super.addAnimation(46, 0, DIMENSIONS, DIMENSIONS);
                break;

            case Vertical:
                super.addAnimation(0, 46, DIMENSIONS, DIMENSIONS);
                break;

            case Center:
                super.addAnimation(46, 46, DIMENSIONS, DIMENSIONS);
                break;

            case NW:
                super.addAnimation(0, 0, DIMENSIONS, DIMENSIONS);
                break;

            case NE:
                super.addAnimation(92, 0, DIMENSIONS, DIMENSIONS);
                break;

            case SW:
                super.addAnimation(0, 92, DIMENSIONS, DIMENSIONS);
                break;

            case SE:
                super.addAnimation(92, 92, DIMENSIONS, DIMENSIONS);
                break;

            default:
                throw new Exception("Type not setup here = " + type.toString());
        }
        
        //set the dimentions
        super.setDimensions(Map.WIDTH, Map.HEIGHT);
    }
    
    /**
     * Can the enemy move in this direction?
     * @return true=yes, false=no
     */
    public boolean isSouthAvailable()
    {
        switch (getType())
        {
            case Center:
            case Vertical:
            case NW:
            case NE:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Can the enemy move in this direction?
     * @return true=yes, false=no
     */
    public boolean isNorthAvailable()
    {
        switch (getType())
        {
            case Center:
            case Vertical:
            case SW:
            case SE:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Can the enemy move in this direction?
     * @return true=yes, false=no
     */
    public boolean isEastAvailable()
    {
        switch (getType())
        {
            case Center:
            case Horizontal:
            case NW:
            case SW:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Can the enemy move in this direction?
     * @return true=yes, false=no
     */
    public boolean isWestAvailable()
    {
        switch (getType())
        {
            case Center:
            case Horizontal:
            case NE:
            case SE:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Get the type
     * @return The tile type
     */
    public final Type getType()
    {
        return this.type;
    }
    
    @Override
    public void dispose()
    {
        //clean up performed here
        super.dispose();
    }
}
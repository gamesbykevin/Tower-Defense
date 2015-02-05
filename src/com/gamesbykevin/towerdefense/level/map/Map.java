package com.gamesbykevin.towerdefense.level.map;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.level.object.LevelObject;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * The map game play will take place
 * @author GOD
 */
public final class Map extends Sprite implements Disposable, IElement
{
    //tiles that make up the map
    private LevelObject[][] tiles;
    
    //dimensions for the map
    public static final int ROWS = 8;
    public static final int COLS = 12;
    
    //the size of each cell in the map
    public static final double WIDTH = 64.0;
    public static final double HEIGHT = 64.0;
    
    //the start location
    public static final double START_X = 0.0;
    public static final double START_Y = 0.0;
    
    public Map(final Image road)
    {
        super.setImage(road);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (tiles != null)
        {
            for (int row = 0; row < tiles.length; row++)
            {
                for (int col = 0; col < tiles[0].length; col++)
                {
                    tiles[row][col].dispose();
                    tiles[row][col] = null;
                }
            }
            
            tiles = null;
        }
    }
    
    /**
     * Create the map
     * @throws Exception if an error occurs creating tile
     */
    private void createMap() throws Exception
    {
        for (int row = 0; row < tiles.length; row++)
        {
            for (int col = 0; col < tiles[0].length; col++)
            {
                this.tiles[row][col] = new Tile(Tile.Type.OPEN);
                this.tiles[row][col].setX(START_X + (col * WIDTH));
                this.tiles[row][col].setY(START_Y + (row * HEIGHT));
            }
        }
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (tiles == null)
        {
            //create array for the map
            this.tiles = new LevelObject[ROWS][COLS];
            
            //create map
            createMap();
        }
        else
        {
            
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        if (tiles != null)
        {
            for (int row = 0; row < tiles.length; row++)
            {
                for (int col = 0; col < tiles[0].length; col++)
                {
                    if (tiles[row][col] != null)
                    {
                        tiles[row][col].draw(graphics, getImage());
                    }
                }
            }
        }
    }
}
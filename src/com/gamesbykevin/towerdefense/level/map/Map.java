package com.gamesbykevin.towerdefense.level.map;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.level.object.LevelObject;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public static final int COLS = 11;
    
    /**
     * The dimensions of each cell in the map
     */
    public static final double WIDTH = 64.0;
    
    /**
     * The dimensions of each cell in the map
     */
    public static final double HEIGHT = 64.0;
    
    //the start location
    public static final double START_X = 0.0;
    public static final double START_Y = 0.0;
    
    //the location of the start and finish
    private Cell start, finish;
    
    //the background
    private Image background;
    
    public Map(final Image road, final Image background)
    {
        super.setImage(road);
        
        //set the bounds
        super.setBounds(0, COLS - 1, 0, ROWS - 1);
        
        //set the background image
        this.background = background;
    }
    
    /**
     * Get the start y-coordinate
     * @param row The column location
     * @return The starting y-coordinate on the map for this location
     */
    public static double getStartY(double row)
    {
        return (Map.START_Y + (row * Map.HEIGHT));
    }
    
    /**
     * Get the start x-coordinate
     * @param column The column location
     * @return The starting x-coordinate on the map for this location
     */
    public static double getStartX(double column)
    {
        return (Map.START_X + (column * Map.WIDTH));
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (background != null)
        {
            background.flush();
            background = null;
        }
        
        if (tiles != null)
        {
            for (int row = 0; row < tiles.length; row++)
            {
                for (int col = 0; col < tiles[0].length; col++)
                {
                    if (tiles[row][col] != null)
                    {
                        tiles[row][col].dispose();
                        tiles[row][col] = null;
                    }
                }
            }
            
            tiles = null;
        }
    }
    
    /**
     * Create the map
     * @param random Object used to make random decisions
     * @throws Exception if an error occurs creating tile
     */
    private void createMap(final Random random) throws Exception
    {
        //store start locaton
        this.start = new Cell();
        this.start.setCol(tiles[0].length - 1 + 0.5);
        this.start.setRow((tiles.length / 2) + .5);
        
        //store finish locaton
        this.finish = new Cell();
        this.finish.setCol(.5);
        this.finish.setRow((tiles.length / 2) + .5);
        
        for (int col = 0; col < tiles[0].length; col++)
        {
            for (int row = 0; row < tiles.length; row++)
            {
                if (row == tiles.length / 2)
                {
                    createTile(col, row, Tile.Type.Horizontal);
                }
            }
        }
    }
    
    /**
     * Create the tile and assign (x,y) location
     * @param col Column
     * @param row Row
     * @param type Type of tile
     * @exception Will be thrown if the Tile type is not setup in Tile constructor
     */
    private void createTile(final int col, final int row, final Tile.Type type) throws Exception
    {
        //create new tile
        tiles[row][col] = new Tile(type);

        //assign location
        tiles[row][col].setX(Map.getStartX(col));
        tiles[row][col].setY(Map.getStartY(row));
    }
    
    /**
     * Get the next destination.<br>
     * The next location with a cost higher than the current location.<br>
     * If no locations exist we will continue to move in the same current direction.<br>
     * If that is not possible will make a turn
     * @param entity Enemy containing column, row, etc...
     * @param random Object used to make random decisions
     * @return The column, row of the next place to head in. 
     * @throws Exception If next destination is not found
     */
    public Cell getNextDestination(final Enemy entity, final Random random) throws Exception
    {
        //get the current location
        final double currentCol = entity.getCol();
        final double currentRow = entity.getRow();
        
        //if we are at the finish, move to the left column
        if (getFinish().equals(currentCol, currentRow))
            return new Cell(currentCol - 1.0, currentRow);
        
        //get current tile
        Tile tmp = getTile((int)currentCol, (int)currentRow);
        
        if (entity.getPrevious().getCol() > entity.getCol())
        {
            if (tmp.isWestAvailable())
            {
                return new Cell(currentCol - 1.0, currentRow);
            }
            else if (tmp.isNorthAvailable())
            {
                return new Cell(currentCol, currentRow - 1.0);
            }
            else if (tmp.isSouthAvailable())
            {
                return new Cell(currentCol, currentRow + 1.0);
            }
        }
        else if (entity.getPrevious().getCol() < entity.getCol())
        {
            if (tmp.isEastAvailable())
            {
                return new Cell(currentCol + 1.0, currentRow);
            }
            else if (tmp.isNorthAvailable())
            {
                return new Cell(currentCol, currentRow - 1.0);
            }
            else if (tmp.isSouthAvailable())
            {
                return new Cell(currentCol, currentRow + 1.0);
            }
        }
        else if (entity.getPrevious().getRow() > entity.getRow())
        {
            if (tmp.isEastAvailable())
            {
                return new Cell(currentCol + 1.0, currentRow);
            }
            else if (tmp.isWestAvailable())
            {
                return new Cell(currentCol - 1.0, currentRow);
            }
            else if (tmp.isNorthAvailable())
            {
                return new Cell(currentCol, currentRow - 1.0);
            }
        }
        else if (entity.getPrevious().getRow() < entity.getRow())
        {
            if (tmp.isEastAvailable())
            {
                return new Cell(currentCol + 1.0, currentRow);
            }
            else if (tmp.isWestAvailable())
            {
                return new Cell(currentCol - 1.0, currentRow);
            }
            else if (tmp.isSouthAvailable())
            {
                return new Cell(currentCol, currentRow + 1.0);
            }
        }
        
        //this should not happen
        throw new Exception("Next destination was not found (" + currentCol + "," + currentRow + ")");
    }
    
    /**
     * Get the start location
     * @return The col, row where the enemy is to begin
     */
    public Cell getStart()
    {
        return this.start;
    }
    
    /**
     * Get the finish location
     * @return The col, row where the enemy is to finish
     */
    public Cell getFinish()
    {
        return this.finish;
    }
    
    /**
     * Is this location valid?
     * @param col Column
     * @param row Row
     * @return true if there is no tile here, false otherwise
     */
    public boolean isValid(final double col, final double row)
    {
        if (col < 0 || col >= tiles[0].length)
            return false;
        if (row < 0 || row >= tiles.length)
            return false;
        
        return (getTile((int)col, (int) row) == null);
    }
    
    /**
     * Get the tile
     * @param col Column
     * @param row Row
     * @return The tile at the specified location, if out of bounds null is returned
     */
    private Tile getTile(final int col, final int row)
    {
        if (!hasBounds(col, row))
            return null;
        
        return (Tile)tiles[row][col];
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (tiles == null)
        {
            //create array for the map
            this.tiles = new LevelObject[ROWS][COLS];
            
            //create map
            createMap(engine.getRandom());
        }
        else
        {
            //anything else here need to be done?
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //first draw the background
        graphics.drawImage(background, (int)START_X, (int)START_Y, (int)(COLS * WIDTH), (int)(ROWS * HEIGHT), null);
        
        if (tiles != null)
        {
            for (int row = 0; row < tiles.length; row++)
            {
                for (int col = 0; col < tiles[0].length; col++)
                {
                    if (getTile(col, row) != null)
                        getTile(col, row).draw(graphics, getImage());
                }
            }
        }
    }
}
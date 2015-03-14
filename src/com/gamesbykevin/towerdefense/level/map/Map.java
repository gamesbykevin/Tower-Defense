package com.gamesbykevin.towerdefense.level.map;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.menu.CustomMenu.LayerKey;
import com.gamesbykevin.towerdefense.menu.CustomMenu.OptionKey;
import com.gamesbykevin.towerdefense.level.object.LevelObject;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;

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
    
    //the different track options
    public static final int TRACK1 = 0;
    public static final int TRACK2 = 1;
    public static final int TRACK3 = 2;
    
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
     * @throws Exception if an error occurs creating tile
     */
    private void createMap(final int trackIndex) throws Exception
    {
        //create array for the map
        this.tiles = new LevelObject[ROWS][COLS];
        
        //save start locaton
        this.start = new Cell();
        
        //start will always be on the far east
        this.start.setCol(tiles[0].length - 1 + 0.5);
        
        //save finish locaton
        this.finish = new Cell();
        
        //finish will always be on the far west
        this.finish.setCol(.5);
        
        //create the track
        switch (trackIndex)
        {
            case TRACK1:
                createLongVerticalTrack();
                break;
                
            case TRACK2:
                createLongHorizontalTrack();
                break;
                
            case TRACK3:
                createHorizontalTrack();
                break;
                
            default:
                throw new Exception("Track is not setup here");
        }
        
        //assign the tile types
        assignTiles();
    }
    
    /**
     * Create longer track stretching the length of the screen
     */
    private void createLongHorizontalTrack()
    {
        //set the start and finish row
        this.start.setRow(0.5);
        this.finish.setRow(tiles.length - 2 + 0.5);
        
        for (int row = 0; row < tiles.length; row++)
        {
            for (int col = 0; col < tiles[0].length; col++)
            {
                //tiles will always be every 3 rows
                if (row % 3 == 0)
                {
                    createTile(col, row);
                }
                else
                {
                    if (col == 0)
                    {
                        if (row == 1 || row == 2)
                            createTile(col, row);
                    }
                    else if (col == tiles[0].length - 1)
                    {
                        if (row == 4 || row == 5)
                            createTile(col, row);
                    }
                }
            }
        }
    }
    
    /**
     * Create longer track stretching the length of the screen
     */
    private void createLongVerticalTrack()
    {
        //set the start and finish row
        this.start.setRow(tiles.length - 1 + 0.5);
        this.finish.setRow(tiles.length - 1 + 0.5);
        
        for (int row = 0; row < tiles.length; row++)
        {
            for (int col = 0; col < tiles[0].length; col++)
            {
                //tiles will be every other column
                if (col % 2 == 0)
                {
                    createTile(col, row);
                }
                else
                {
                    if (col == 1 || col == 5 || col == 9)
                    {
                        if (row == 0)
                            createTile(col, row);
                    }
                    else if (col == 3 || col == 7)
                    {
                        if (row == tiles.length - 1)
                            createTile(col, row);
                    }
                }
            }
        }
    }
    
    /**
     * Create a direct horizontal track from start to finish
     */
    private void createHorizontalTrack()
    {
        //set the start and finish row
        this.start.setRow((tiles.length / 2) + .5);
        this.finish.setRow((tiles.length / 2) + .5);
        
        for (int col = 0; col < tiles[0].length; col++)
        {
            for (int row = 0; row < tiles.length; row++)
            {
                if (row == tiles.length / 2)
                {
                    createTile(col, row);
                }
            }
        }
    }
    
    /**
     * Assign the tile type for the created tiles
     * @throws Exception will be thrown if type already set or another issue
     */
    private void assignTiles() throws Exception
    {
        //now set the appropriate tile type
        for (int col = 0; col < tiles[0].length; col++)
        {
            for (int row = 0; row < tiles.length; row++)
            {
                //skip if not exists
                if (getTile(col, row) == null)
                    continue;
                
                //do tiles exist in these directions
                boolean north = false, south = false, east = false, west = false;
                
                if (getTile(col + 1, row) != null)
                    east = true;
                if (getTile(col - 1, row) != null)
                    west = true;
                if (getTile(col, row - 1) != null)
                    north = true;
                if (getTile(col, row + 1) != null)
                    south = true;
                if ((int)start.getCol() == col && (int)start.getRow() == row)
                    east = true;
                if ((int)finish.getCol() == col && (int)finish.getRow() == row)
                    west = true;

                if (north && east)
                {
                    getTile(col, row).setType(Tile.Type.SW);
                }
                else if (north && west)
                {
                    getTile(col, row).setType(Tile.Type.SE);
                }
                else if (north && south)
                {
                    getTile(col, row).setType(Tile.Type.Vertical);
                }
                else if (east && west)
                {
                    getTile(col, row).setType(Tile.Type.Horizontal);
                }
                else if (south && west)
                {
                    getTile(col, row).setType(Tile.Type.NE);
                }
                else if (south && east)
                {
                    getTile(col, row).setType(Tile.Type.NW);
                }
                else
                {
                    System.out.println("north=" + north + ",south=" + south + ",west=" + west + ",east=" + east);
                    System.out.println("col=" + col + ", row=" + row);
                    throw new Exception("Tile not created");
                }
            }
        }
    }
    
    /**
     * Create the tile and assign (x,y) location
     * @param col Column
     * @param row Row
     * @param type Type of tile
     */
    private void createTile(final int col, final int row)
    {
        //create new tile
        tiles[row][col] = new Tile();

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
            //create map
            createMap(engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Track));
        }
        else
        {
            //anything else here need to be done?
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //first draw the background
        graphics.drawImage(background, (int)START_X, (int)START_Y, (int)(COLS * WIDTH), (int)(ROWS * HEIGHT), null);
        
        if (tiles != null)
        {
            for (int row = 0; row < tiles.length; row++)
            {
                for (int col = 0; col < tiles[0].length; col++)
                {
                    try
                    {
                        if (getTile(col, row) != null)
                            getTile(col, row).draw(graphics, getImage());
                    }
                    catch (Exception e)
                    {
                        ;
                    }
                }
            }
        }
    }
}
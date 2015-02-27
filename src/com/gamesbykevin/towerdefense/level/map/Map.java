package com.gamesbykevin.towerdefense.level.map;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.labyrinth.*;
import com.gamesbykevin.framework.labyrinth.Location.Wall;
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
    
    //object used to create random maze
    private Labyrinth maze;
    
    //list of directions for the next move for the respected ai
    private List<Cell> options;
    
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
    
    //how many walls determine a dead end
    private static final int DEAD_END_WALL_COUNT = 3;
    
    //chance to correct a dead end, example 1 in 3
    private static final int FIX_DEAD_END_PROBABILITY = 3;
    
    //the location of the start and finish
    private Cell start, finish;
    
    public Map(final Image road)
    {
        super.setImage(road);
        
        super.setBounds(0, COLS - 1, 0, ROWS - 1);
        
        //create start and finish
        this.start = new Cell();
        this.finish = new Cell();
        
        //create new list of options
        this.options = new ArrayList<>();
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
        
        options.clear();
        options = null;
    }
    
    /**
     * Create the map
     * @param random Object used to make random decisions
     * @throws Exception if an error occurs creating tile
     */
    private void createMap(final Random random) throws Exception
    {
        //create a labyrinth of the specified size, and using the specified algorithm
        maze = new Labyrinth(tiles[0].length, tiles.length, Labyrinth.Algorithm.DepthFirstSearch);
        
        //set random start on right side
        maze.setStart(tiles[0].length - 1, random.nextInt(tiles.length));
        
        //set random finish on left side
        maze.setFinish(0, random.nextInt(tiles.length));
        
        //create maze
        maze.generate();
        
        //default all locations to visited
        for (int row = 0; row < tiles.length; row++)
        {
            for (int col = 0; col < tiles[0].length; col++)
            {
                maze.getLocation(col, row).setVisited(true);
            }
        }

        //connect some random locations that are a dead end
        for (int row = 0; row < tiles.length; row++)
        {
            for (int col = 0; col < tiles[0].length; col++)
            {
                //get current location
                Location current = maze.getLocation(col, row);
                
                //skip start and finish location
                if (maze.getStart().equals(current))
                    continue;
                if (maze.getFinish().equals(current))
                    continue;
                
                //if this location has 3 walls, it is a dead end
                if (current.getWalls().size() == DEAD_END_WALL_COUNT)
                {
                    //choose at random if we are to correct this dead end
                    if (random.nextInt(FIX_DEAD_END_PROBABILITY) == 0)
                    {
                        //our options that we can remove
                        List<Wall> options = new ArrayList<>();
                        
                        //add our options to the list
                        if (current.hasWall(Wall.West) && col > 0)
                        {
                            //only join the neighbor if the cost is greater
                            if (maze.getLocation(col - 1, row).getCost() > current.getCost())
                                options.add(Wall.West);
                        }
                        
                        if (current.hasWall(Wall.East) && col < tiles[0].length - 1)
                        {
                            //only join the neighbor if the cost is greater
                            if (maze.getLocation(col + 1, row).getCost() > current.getCost())
                                options.add(Wall.East);
                        }
                        
                        if (current.hasWall(Wall.North) && row > 0)
                        {
                            //only join the neighbor if the cost is greater
                            if (maze.getLocation(col, row - 1).getCost() > current.getCost())
                                options.add(Wall.North);
                        }
                        
                        if (current.hasWall(Wall.South) && row < tiles.length - 1)
                        {
                            //only join the neighbor if the cost is greater
                            if (maze.getLocation(col, row + 1).getCost() > current.getCost())
                                options.add(Wall.South);
                        }
                        
                        //make sure options are available
                        if (!options.isEmpty())
                        {
                            switch (options.get(random.nextInt(options.size())))
                            {
                                case North:
                                    //remove wall from current
                                    current.remove(Wall.North);

                                    //remove wall from neighbor
                                    maze.getLocation(col, row - 1).remove(Wall.South);
                                    break;

                                case South:
                                    //remove wall from current
                                    current.remove(Wall.South);

                                    //remove wall from neighbor
                                    maze.getLocation(col, row + 1).remove(Wall.North);
                                    break;

                                case West:
                                    //remove wall from current
                                    current.remove(Wall.West);

                                    //remove wall from neighbor
                                    maze.getLocation(col - 1, row).remove(Wall.East);
                                    break;

                                case East:
                                    //remove wall from current
                                    current.remove(Wall.East);

                                    //remove wall from neighbor
                                    maze.getLocation(col + 1, row).remove(Wall.West);
                                    break;
                            }
                        }
                    }
                }
            }
        }
        
        //now unvisit all locations that lead to a dead end
        while (true)
        {
            //was there a change
            boolean change = false;
            
            for (int row = 0; row < tiles.length; row++)
            {
                for (int col = 0; col < tiles[0].length; col++)
                {
                    //get current location
                    Location current = maze.getLocation(col, row);

                    //if we already marked this as not visited, skip
                    if (!current.hasVisited())
                        continue;

                    //ignore the start and finish locations
                    if (current.equals(maze.getStart()))
                    {
                        //this location will always be visited
                        current.setVisited(true);
                        
                        //skip to next
                        continue;
                    }
                    if (current.equals(maze.getFinish()))
                    {
                        //this location will always be visited
                        current.setVisited(true);
                        
                        //skip to next
                        continue;
                    }
                    
                    //count the number of bordering valid locations
                    int count = 0;
                    
                    //if the current location does not have a wall in this location
                    if (!current.hasWall(Wall.East))
                    {
                        //get location
                        Location east = maze.getLocation(col + 1, row);
                        
                        //check if neighbor is valid
                        if (east != null && east.hasVisited())
                            count++;
                    }
                    
                    //if the current location does not have a wall in this location
                    if (!current.hasWall(Wall.West))
                    {
                        //get location
                        Location west = maze.getLocation(col - 1, row);
                        
                        //check if neighbor is valid
                        if (west != null && west.hasVisited())
                            count++;
                    }
                    
                    //if the current location does not have a wall in this location
                    if (!current.hasWall(Wall.North))
                    {
                        //get location
                        Location north = maze.getLocation(col, row - 1);
                        
                        //check if neighbor is valid
                        if (north != null && north.hasVisited())
                            count++;
                    }
                    
                    //if the current location does not have a wall in this location
                    if (!current.hasWall(Wall.South))
                    {
                        //get location
                        Location south = maze.getLocation(col, row + 1);
                        
                        //check if neighbor is valid
                        if (south != null && south.hasVisited())
                            count++;
                    }
                    
                    //if we have less than 2 valid neighbors this is a dead end
                    if (count < 2)
                    {
                        //mark current location as invalid
                        current.setVisited(false);
                        
                        //flag change
                        change = true;
                    }
                }
            }
            
            //if no changes were made we are finished
            if (!change)
                break;
        }
        
        //now we assign the appropriate tiles
        for (int row = 0; row < tiles.length; row++)
        {
            for (int col = 0; col < tiles[0].length; col++)
            {
                //the type of tile to add, default OPEN
                Tile.Type type = Tile.Type.OPEN;
                
                //get current location
                Location current = maze.getLocation(col, row);
                
                //if valid, check which tile to display
                if (current.hasVisited())
                {
                    //which directions are valid for the current location
                    boolean east = false, west = false, south = false, north = false;

                    //if the current location does not have a wall here
                    if (!current.hasWall(Wall.East))
                    {
                        //get location
                        Location e = maze.getLocation(col + 1, row);

                        //check if neighbor is valid
                        if (e != null && e.hasVisited())
                            east = true;
                    }

                    //if the current location does not have a wall here
                    if (!current.hasWall(Wall.West))
                    {
                        //get location
                        Location w = maze.getLocation(col - 1, row);

                        //check if neighbor is valid
                        if (w != null && w.hasVisited())
                            west = true;
                    }

                    //if the current location does not have a wall here
                    if (!current.hasWall(Wall.North))
                    {
                        //get location
                        Location n = maze.getLocation(col, row - 1);

                        //check if neighbor is valid
                        if (n != null && n.hasVisited())
                            north = true;
                    }

                    //if the current location does not have a wall here
                    if (!current.hasWall(Wall.South))
                    {
                        //get location
                        Location s = maze.getLocation(col, row + 1);

                        //check if neighbor is valid
                        if (s != null && s.hasVisited())
                            south = true;
                    }

                    if (north && east)
                        type = Tile.Type.NE;
                    if (north && west)
                        type = Tile.Type.NW;
                    if (south && east)
                        type = Tile.Type.ES;
                    if (south && west)
                        type = Tile.Type.WS;
                    if (north && south)
                        type = Tile.Type.NS;
                    if (east && west)
                        type = Tile.Type.EW;
                    if (north && south && west)
                        type = Tile.Type.NSW;
                    if (north && south && east)
                        type = Tile.Type.NES;
                    if (east && west && north)
                        type = Tile.Type.WEN;
                    if (east && west && south)
                        type = Tile.Type.WES;
                    if (east && west && north && south)
                        type = Tile.Type.NESW;
                    
                    //if the current location is at the finish
                    if (maze.getFinish().equals(current))
                    {
                        //store finish locaton
                        this.finish.setCol(maze.getFinish().getCol() + 0.5);
                        this.finish.setRow(maze.getFinish().getRow() + 0.5);
                                
                        //add west to the appropriate location
                        switch (type)
                        {
                            case OPEN:
                                if (north)
                                    type = Tile.Type.NW;
                                if (south)
                                    type = Tile.Type.WS;
                                if (east)
                                    type = Tile.Type.EW;
                                break;
                            
                            case NES:
                                type = Tile.Type.NESW;
                                break;
                                
                            case NE:
                                type = Tile.Type.WEN;
                                break;

                            case ES:
                                type = Tile.Type.WES;
                                break;
                        
                            case NS:
                                type = Tile.Type.NSW;
                                break;
                        }
                    }
                    else if (maze.getStart().equals(current))
                    {
                        //store start locaton
                        this.start.setCol(maze.getStart().getCol() + 0.5);
                        this.start.setRow(maze.getStart().getRow() + 0.5);
                        
                        //add east to the appropriate location
                        switch (type)
                        {
                            case OPEN:
                                if (north)
                                    type = Tile.Type.NE;
                                if (south)
                                    type = Tile.Type.ES;
                                if (west)
                                    type = Tile.Type.EW;
                                break;
                                
                            case NS:
                                type = Tile.Type.NES;
                                break;
                            
                            case NW:
                                type = Tile.Type.WEN;
                                break;
                                
                            case WS:
                                type = Tile.Type.WES;
                                break;
                                
                            case NSW:
                                type = Tile.Type.NESW;
                                break;
                        }
                    }
                }
                else
                {
                    //if this is not a visited location, it will be open
                    type = Tile.Type.OPEN;
                }
                
                //if the tile is open the cost will be 0
                if (type == Tile.Type.OPEN)
                {
                    //create new tile of type
                    this.tiles[row][col] = new Tile(type, 0);
                }
                else
                {
                    //create new tile of type
                    this.tiles[row][col] = new Tile(type, current.getCost());
                }
                
                //assign x,y coordinates
                this.tiles[row][col].setX(getStartX(col));
                this.tiles[row][col].setY(getStartY(row));
            }
        }
    }
    
    /**
     * Get the next destination.<br>
     * The next location with a cost higher than the current location.<br>
     * If no locations exist we will continue to move in the same current direction.<br>
     * If that is not possible will make a turn
     * @param entity Enemy containing column, row, etc...
     * @param random Object used to make random decisions
     * @return The column, row of the next place to head in. 
     */
    public Cell getNextDestination(final Enemy entity, final Random random) throws Exception
    {
        //get the current location
        final double currentCol = entity.getCol();
        final double currentRow = entity.getRow();
        
        //if we are at the finish, move to the left column
        if (maze.getFinish().equals((int)currentCol, (int)currentRow))
            return new Cell(currentCol - 1, currentRow);
        
        //get the current tile
        Tile tile = getTile((int)currentCol, (int)currentRow);
        
        //the current highest cost to beat
        int cost = tile.getCost();
        
        for (int col = -1; col <= 1; col++)
        {
            //skip 0
            if (col == 0)
                continue;
            
            //if not in bounds skip
            if (!hasBounds((int)currentCol + col, (int)currentRow))
                continue;
            
            //get current tile
            tile = getTile((int)currentCol + col, (int)currentRow);
            
            //skip open tiles
            if (tile.getType() == Tile.Type.OPEN)
                continue;
            
            if (col < 0)
            {
                //skip if there is no opening
                if (!isEastAvailable(tile.getType()))
                    continue;
            }
            else if (col > 0)
            {
                //skip if there is no opening
                if (!isWestAvailable(tile.getType()))
                    continue;
            }
            
            //make sure the tile has a higher cost
            if (tile.getCost() > cost)
            {
                //add possible option
                options.add(new Cell(currentCol + col, currentRow));
            }
        }
        
        for (int row = -1; row <= 1; row++)
        {
            //skip 0
            if (row == 0)
                continue;
            
            //if not in bounds skip
            if (!hasBounds((int)currentCol, (int)currentRow + row))
                continue;
            
            //get current tile
            tile = getTile((int)currentCol, (int)currentRow + row);
            
            //skip open tiles
            if (tile.getType() == Tile.Type.OPEN)
                continue;
            
            if (row < 0)
            {
                //skip if there is no opening
                if (!isSouthAvailable(tile.getType()))
                    continue;
            }
            else if (row > 0)
            {
                //skip if there is no opening
                if (!isNorthAvailable(tile.getType()))
                    continue;
            }
            
            //make sure the tile has a higher cost
            if (tile.getCost() > cost)
            {
                //add possible option
                options.add(new Cell(currentCol, currentRow + row));
            }
        }
        
        //if no options, we need to at least add one
        if (options.isEmpty())
        {
            if (entity.hasVelocityX())
            {
                if (entity.getVelocityX() > 0)
                {
                    //make sure east is available
                    if (isEastAvailable(tile.getType()))
                        options.add(new Cell(currentCol + 1, currentRow));
                }
                else
                {
                    //make sure west is available
                    if (isWestAvailable(tile.getType()))
                        options.add(new Cell(currentCol - 1, currentRow));
                }
                
                //if options is still empty will shift direction
                if (options.isEmpty())
                {
                    //add south if available
                    if (isSouthAvailable(tile.getType()))
                        options.add(new Cell(currentCol, currentRow + 1));
                    
                    //add north if available
                    if (isNorthAvailable(tile.getType()))
                        options.add(new Cell(currentCol, currentRow - 1));
                }
            }
            else if (entity.hasVelocityY())
            {
                if (entity.getVelocityY()> 0)
                {
                    //make sure south is available
                    if (isSouthAvailable(tile.getType()))
                        options.add(new Cell(currentCol, currentRow + 1));
                }
                else
                {
                    //make sure north is available
                    if (isNorthAvailable(tile.getType()))
                        options.add(new Cell(currentCol, currentRow - 1));
                }
                
                //if options is still empty will shift direction
                if (options.isEmpty())
                {
                    //add west if available
                    if (isWestAvailable(tile.getType()))
                        options.add(new Cell(currentCol - 1, currentRow));
                    
                    //add east if available
                    if (isEastAvailable(tile.getType()))
                        options.add(new Cell(currentCol + 1, currentRow));
                }
            }
            else
            {
                //if no movement just pick an open spot
                if (entity.getPrevious() == null)
                    entity.setPrevious(entity);
                
                //just pick any place that was not the previous and is in bounds
                if (!entity.getPrevious().equals(currentCol - 1, currentRow) && isWestAvailable(tile.getType()))
                    options.add(new Cell(currentCol - 1, currentRow));
                if (!entity.getPrevious().equals(currentCol + 1, currentRow) && isEastAvailable(tile.getType()))
                    options.add(new Cell(currentCol + 1, currentRow));
                if (!entity.getPrevious().equals(currentCol, currentRow - 1) && isNorthAvailable(tile.getType()))
                    options.add(new Cell(currentCol, currentRow - 1));
                if (!entity.getPrevious().equals(currentCol, currentRow + 1) && isSouthAvailable(tile.getType()))
                    options.add(new Cell(currentCol, currentRow + 1));
            }
        }
        
        //pick random option
        Cell destination = options.get(random.nextInt(options.size()));
        
        //clear list
        options.clear();
        
        //return destination
        return destination;
    }
    
    /**
     * Does this tile have a west path
     * @param type The type of tile
     * @return true=yes, false=no
     */
    private boolean isWestAvailable(final Tile.Type type)
    {
        switch (type)
        {
            case WES:
            case WS:
            case NESW:
            case NSW:
            case WEN:
            case NW:
            case EW:
                return true;
                
            default:
                return false;
            
        }
    }
    
    /**
     * Does this tile have a east path
     * @param type The type of tile
     * @return true=yes, false=no
     */
    private boolean isEastAvailable(final Tile.Type type)
    {
        switch (type)
        {
            case ES:
            case WES:
            case NES:
            case NESW:
            case NE:
            case WEN:
            case EW:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Does this tile have a south path
     * @param type The type of tile
     * @return true=yes, false=no
     */
    private boolean isSouthAvailable(final Tile.Type type)
    {
        switch (type)
        {
            case ES:
            case WES:
            case WS:
            case NES:
            case NESW:
            case NSW:
            case NS:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Does this tile have a north path
     * @param type The type of tile
     * @return true=yes, false=no
     */
    private boolean isNorthAvailable(final Tile.Type type)
    {
        switch (type)
        {
            case NES:
            case NESW:
            case NSW:
            case NE:
            case WEN:
            case NW:
            case NS:
                return true;
                
            default:
                return false;
        }
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
     * @return true if the tile is type OPEN, false otherwise
     */
    public boolean isValid(final double col, final double row)
    {
        if (col < 0 || col >= tiles[0].length)
            return false;
        if (row < 0 || row >= tiles.length)
            return false;
        
        return getTile((int)col, (int) row).getType() == Tile.Type.OPEN;
    }
    
    private Tile getTile(final int col, final int row)
    {
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
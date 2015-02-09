package com.gamesbykevin.towerdefense.entity.tower;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Image;

/**
 * This class will manage all towers in the game
 * @author GOD
 */
public final class Towers extends Entities implements Disposable, IElement
{
    /**
     * The distance we have to be within to select a tower
     */
    public static final double SELECTION_RANGE = 0.15;
    
    public Towers(final Image image)
    {
        super.setImage(image);
    }
    
    /**
     * Add tower
     */
    public void add(final Tower.Type type, final double col, final double row) throws Exception
    {
        //create a new tower
        Tower tower = new Tower(type);
        
        //set position
        tower.setCol(col);
        tower.setRow(row);
        
        //set the coordinates
        tower.setX(Map.getStartX(col));
        tower.setY(Map.getStartY(row));
        
        //add to list
        getEntities().add(tower);
    }
    
    /**
     * Get the tower
     * @param col Column
     * @param row Row
     * @return The tower located
     */
    public Tower getTower(final double col, final double row)
    {
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get the current tower
            Tower tower = getTower(i);
            
            //get the distance from the current tower
            final double distance = Cell.getDistance(tower.getCol(), tower.getRow(), col, row);
            
            //if the provided location is close enough return the tower
            if (distance <= SELECTION_RANGE)
                return tower;
        }
        
        //we were not close to any tower, return null
        return null;
    }
    
    public Tower getTower(final int index)
    {
        return (Tower)getEntities().get(index);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update parent
        super.updateEntities(engine.getMain().getTime());

        //additional update logic here
        for (int i = 0; i < getEntities().size(); i++)
        {
            //update the current tower
            Tower tower = (Tower)getEntities().get(i);
            
            
        }
    }
}
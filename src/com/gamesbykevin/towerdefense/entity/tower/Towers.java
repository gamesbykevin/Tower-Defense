package com.gamesbykevin.towerdefense.entity.tower;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;

/**
 * This class will manage all towers in the game
 * @author GOD
 */
public final class Towers extends Entities implements Disposable, IElement
{
    //the id of the selected tower
    private long id;
    
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
     * Get the tower at the specified location.<br>
     * If a tower is returned we will also draw that towers range.
     * @param col Column
     * @param row Row
     * @return The tower located, if not located null is returned
     */
    public Tower getTower(final double col, final double row)
    {
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get the current tower
            Tower tower = getTower(i);
            
            //get the distance from the current tower
            final double distance = Cell.getDistance(tower.getCol(), tower.getRow(), col, row);
            
            //get the width of the entity
            final double requirement = tower.getWidth() / Map.WIDTH;
            
            //if the provided location is close enough return the tower
            if (distance <= requirement / 2)
            {
                //get the id so we know to draw the tower range
                setAssigned(tower);
                
                //return located tower
                return tower;
            }
        }
        
        //no assigned
        setAssigned(null);
        
        //we were not close to any tower, return null
        return null;
    }
    
    /**
     * Upgrade the tower
     * @param tower The tower we want to upgrade
     */
    public void upgrade(final Tower tower)
    {
        for (int i = 0; i < getEntities().size(); i++)
        {
            //if this is the tower upgrade
            if (getTower(i).getId() == tower.getId())
                getTower(i).upgrade();
        }
    }
    
    /**
     * Set the assigned tower that we want to draw the range for
     * @param tower The tower we want to draw the range for
     */
    public void setAssigned(final Tower tower)
    {
        this.id = (tower != null) ? tower.getId() : 0;
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
            
            //check if there is an enemy to target
            tower.setTagret(engine.getManager().getEnemies().getEnemy(tower));
            
            //if there is a target within range, aim at target
            if (tower.getTarget() != null)
            {
                //calculate the slope
                final double slope = (tower.getTarget().getRow() - tower.getRow()) / (tower.getTarget().getCol() - tower.getCol());
                
                //calculat the facing angle
                double angle = Math.atan(slope);
                
                //if the difference is negative adjust
                if (tower.getTarget().getCol() - tower.getCol() < 0)
                    angle += Math.PI;
                
                //adjust due to the default facing east direction of the animation
                angle += Math.toRadians(90);
                
                //make sure radians stay within range
                if (angle > (2 * Math.PI))
                    angle -= (2 * Math.PI);
                if (angle < 0)
                    angle += (2 * Math.PI);
                
                //now set the final angle
                tower.setAngle(angle);
            }
            
            //check if time has passed
            if (tower.getTimer().hasTimePassed())
            {
                //time has passed reset timer
                tower.getTimer().reset();
            }
            else
            {
                //update timer until we can fire again
                tower.getTimer().update(engine.getMain().getTime());
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //draw the tower range of the selected tower first
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get the current tower
            Tower tower = (Tower)getEntities().get(i);
        
            //we only want the matching tower
            if (tower.getId() != id)
                continue;
            
            //store the tower information
            final double x = tower.getX();
            final double y = tower.getY();
            final double w = tower.getWidth();
            final double h = tower.getHeight();
            final Object key = tower.getSpriteSheet().getCurrent();
            
            //the x,y location of the tower's center
            final int x1 = (int)Map.getStartX(tower.getCol());
            final int y1 = (int)Map.getStartY(tower.getRow());
            
            //the reach of the range
            final int w1 = (int)(tower.getRange() * Map.WIDTH) * 2;
            
            //assign tower coordinates and dimensions
            tower.setX(x1 - (w1 / 2));
            tower.setY(y1 - (w1 / 2));
            
            //assign size of animation
            tower.setWidth(w1);
            tower.setHeight(w1);
            
            //set animation
            tower.setAnimation(Tower.RangeKey.Regular);
            
            //draw the range
            tower.draw(graphics, getImage());
            
            //now restore the tower info
            tower.setLocation(x, y);
            tower.setDimensions(w, h);
            tower.setAnimation(key);
        }
        
        //then draw the towers
        super.render(graphics);
    }
}
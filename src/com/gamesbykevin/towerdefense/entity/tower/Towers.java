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
    /**
     * Add tower to our collection
     * @param type The type of tower
     * @param col Column
     * @param row Row
     * @param time Time (nano-seconds) per update
     * @throws Exception 
     */
    public void add(final Tower.Type type, final double col, final double row) throws Exception
    {
        //create a new tower
        Tower tower = new Tower(type);
        
        //set position
        tower.setCol(col);
        tower.setRow(row);
        
        //add to list
        add(tower);
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
    
    /**
     * Get the tower id selection
     * @return The unique id of the tower the user selected
     */
    public long getTowerIdSelection()
    {
        return this.id;
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
            Tower tower = getTower(i);
            
            //check if there is an enemy to target
            Enemy enemy = engine.getManager().getEnemies().getEnemy(tower);
            
            //check if there is an enemy to target
            tower.setTagret(enemy);
            
            //don't continue if there is no enemy to interact with
            if (tower.getTarget() == null)
                continue;
            
            //calculate the slope
            final double slope = (enemy.getRow() - tower.getRow()) / (enemy.getCol() - tower.getCol());

            //calculat the facing angle
            double angle = Math.atan(slope);

            //if the difference is negative adjust
            if (enemy.getCol() - tower.getCol() < 0)
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
            
            //check to see if time has passed to attack
            if (tower.getTimer().hasTimePassed())
            {
                //time has passed reset timer
                tower.getTimer().reset();
                
                //we will handle the enemies different here without 
                if (tower.canFreeze() || tower.canPoison())
                {
                    if (tower.canFreeze())
                    {
                        //play sound effect?
                        
                        //freeze nearby enemies
                        engine.getManager().getEnemies().freezeEnemies(tower);
                    }
                    else if (tower.canPoison())
                    {
                        //play sound effect?
                        
                        //poison nearby enemies
                        engine.getManager().getEnemies().poisonEnemies(tower);
                    }
                }
                else
                {
                    //make sure the tower has a target
                    if (tower.getTarget() != null)
                    {
                        //fire projectile at enemy
                        engine.getManager().getProjectiles().add(tower);
                        
                        //play projectile sound effect??
                        
                    }
                }
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
            Tower tower = getTower(i);
        
            //if tower can't freeze and can't poison and is not the current selection
            if (!tower.canFreeze() && !tower.canPoison() && tower.getId() != getTowerIdSelection())
                continue;
            
            if (tower.getId() != getTowerIdSelection())
            {
                //only draw the range right when the time passes
                if (tower.canFreeze() && !tower.getTimer().hasTimePassed())
                    continue;
                if (tower.canPoison()&& !tower.getTimer().hasTimePassed())
                    continue;
            }
                
            //draw the range
            tower.renderRange(graphics, getImage());
        }
        
        //then draw the towers
        super.render(graphics);
    }
}
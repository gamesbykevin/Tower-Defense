package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Image;

/**
 * This object contains all the enemies in play
 * @author GOD
 */
public final class Enemies extends Entities implements Disposable, IElement
{
    public Enemies(final Image image)
    {
        super.setImage(image);
    }
    
    /**
     * Add enemy
     */
    public void add(final Enemy.Type type, final double col, final double row) throws Exception
    {
        //create a new enemy
        Enemy enemy = new Enemy(type);
        
        //set position
        enemy.setCol(col);
        enemy.setRow(row);
        
        //add to list
        getEntities().add(enemy);
    }
    
    /**
     * Get the enemy
     * @param col Column
     * @param row Row
     * @return The enemy located, if not located return null
     */
    public Enemy getEnemy(final double col, final double row)
    {
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get the current enemy
            Enemy enemy = getEnemy(i);
            
            //get the distance from the current enemy
            final double distance = Cell.getDistance(enemy.getCol(), enemy.getRow(), col, row);
            
            //get the width of the entity
            final double requirement = enemy.getWidth() / Map.WIDTH;
            
            //if the provided location is close enough return the enemy
            if (distance <= requirement / 2)
                return enemy;
        }
        
        //we were not close to any enemy, return null
        return null;
    }
    
    public Enemy getEnemy(final int index)
    {
        return (Enemy)getEntities().get(index);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update parent
        super.updateEntities(engine.getMain().getTime());
        
        //additional update logic here
        for (int i = 0; i < getEntities().size(); i++)
        {
            //update the current enemy
            Enemy enemy = (Enemy)getEntities().get(i);
            
        }
    }
}
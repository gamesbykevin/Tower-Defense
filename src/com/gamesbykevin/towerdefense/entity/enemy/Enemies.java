package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
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
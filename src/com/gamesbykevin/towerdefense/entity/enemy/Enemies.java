/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.entity.tower.Tower;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.shared.IElement;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * This object contains all the enemies in play
 * @author GOD
 */
public class Enemies extends Sprite implements Disposable, IElement
{
    private List<Enemy> enemies;
    
    public Enemies(final Image image)
    {
        super.setImage(image);
        
        //create new list for the towers
        this.enemies = new ArrayList<>();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        for (int i = 0; i < enemies.size(); i++)
        {
            enemies.get(i).dispose();
            enemies.set(i, null);
        }
        
        enemies.clear();
        enemies = null;
    }
    
    /**
     * Add enemy
     */
    public void add(final Enemy.Type type, final double col, final double row) throws Exception
    {
        //create a new tower
        Enemy enemy = new Enemy(type);
        
        //set position
        enemy.setCol(col);
        enemy.setRow(row);
        
        //set the coordinates
        enemy.setX(Map.START_X + (col * Map.WIDTH));
        enemy.setY(Map.START_Y + (row * Map.HEIGHT));
        
        //add tower to list
        enemies.add(enemy);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        for (int i = 0; i < enemies.size(); i++)
        {
            //update the current enemy
            Enemy enemy = enemies.get(i);
            
            //update the enemy animation
            enemy.getSpriteSheet().update(engine.getMain().getTime());
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        if (super.getImage() == null)
            return;
        
        for (int i = 0; i < enemies.size(); i++)
        {
            //render the current enemy
            Enemy enemy = enemies.get(i);
            
            //draw tower
            enemy.render(graphics, getImage());
        }
    }
}
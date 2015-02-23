package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Image;
import java.util.Random;

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
     * Add random enemy type
     * @param random Object used to make random decisions
     * @param location The column, row of the enemy
     * @throws Exception 
     */
    public void add(final Random random, final Cell location) throws Exception
    {
        add(Enemy.Type.values()[random.nextInt(Enemy.Type.values().length)], location);
    }
    
    /**
     * Add enemy
     * @param type The type of enemy
     * @param location The column, row of the enemy
     */
    public void add(final Enemy.Type type, final Cell location) throws Exception
    {
        add(type, location.getCol(), location.getRow());
    }
    
    /**
     * Add enemy
     * @param type The type of enemy
     * @param col The column of the enemy
     * @param row The row of the enemy
     */
    public void add(final Enemy.Type type, final double col, final double row) throws Exception
    {
        //create a new enemy
        Enemy enemy = new Enemy(type);
        
        //set position
        enemy.setCol(col);
        enemy.setRow(row);
        
        //set next destination
        enemy.setDestination(col, row);
        
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
            
            //reset moving speed
            enemy.resetVelocity();
                
            //if the enemy is at the destination
            if (enemy.getDestination().equals(enemy))
            {
                //pick the next destination for the enemy
                enemy.setDestination(engine.getManager().getMap().getNextDestination(enemy));
            }
            else
            {

                //determine which direction we are to move in
                if (enemy.getCol() < enemy.getDestination().getCol() && enemy.getCol() + enemy.getSpeed() < enemy.getDestination().getCol())
                {
                    enemy.setVelocityX(enemy.getSpeed());
                    enemy.setAngle(Math.toRadians(180));
                }
                else if (enemy.getCol() > enemy.getDestination().getCol() && enemy.getCol() - enemy.getSpeed() > enemy.getDestination().getCol())
                {
                    enemy.setVelocityX(-enemy.getSpeed());
                    enemy.setAngle(Math.toRadians(0));
                }
                else if (enemy.getRow() < enemy.getDestination().getRow() && enemy.getRow() + enemy.getSpeed() < enemy.getDestination().getRow())
                {
                    enemy.setVelocityY(enemy.getSpeed());
                    enemy.setAngle(Math.toRadians(270));
                }
                else if (enemy.getRow() > enemy.getDestination().getRow() && enemy.getRow() - enemy.getSpeed() > enemy.getDestination().getRow())
                {
                    enemy.setVelocityY(-enemy.getSpeed());
                    enemy.setAngle(Math.toRadians(90));
                }
                else
                {
                    //place at destination
                    enemy.setCol(enemy.getDestination().getCol());
                    enemy.setRow(enemy.getDestination().getRow());
                }

                //update location
                enemy.update();
            }
        }
    }
}
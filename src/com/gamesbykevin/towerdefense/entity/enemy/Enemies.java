package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.entity.tower.Tower;
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
    //different facing directions for the enemies
    private static double NORTH = Math.toRadians(270);
    private static double SOUTH = Math.toRadians(90);
    private static double EAST = Math.toRadians(0);
    private static double WEST = Math.toRadians(180);
    
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
     * Add enemy.
     * @param type The type of enemy
     * @param col The column of the enemy
     * @param row The row of the enemy
     */
    public void add(final Enemy.Type type, final double col, final double row) throws Exception
    {
        //create a new enemy
        Enemy enemy = new Enemy(type);
        
        //set position, which will be 1 column each of the specified location
        enemy.setCol(col + 1);
        enemy.setRow(row);
        
        //set x, y coordinates
        enemy.update();
        
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
    
    /**
     * Get the enemy within the range of the Tower
     * @param tower The tower looking for an enemy target
     * @return The enemy closest to the tower, if none are found, null is returned
     */
    public Enemy getEnemy(final Tower tower)
    {
        //the enemy we are looking for
        Enemy enemy = null;
        
        //the distance to beat
        double distance = 0;
        
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get the current enemy
            Enemy tmp = getEnemy(i);
            
            //make sure the enemy is within the range of the tower
            if (Cell.getDistance(tmp, tower) <= tower.getRange())
            {
                //if enemy does not exist yet or we beat the previous distance
                if (enemy == null || Cell.getDistance(tmp, tower) < distance)
                {
                    //assign the new distance to beat
                    distance = Cell.getDistance(tmp, tower);
                    
                    //this is our current enemy target
                    enemy = tmp;
                }
                    
            }
        }
        
        //none of the enemies were found within the range
        return enemy;
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
            
            //if the enemy is at the destination
            if (enemy.getDestination().equals(enemy))
            {
                if (enemy.getCol() > 0)
                {
                    //pick the next destination for the enemy
                    enemy.setDestination(engine.getManager().getMap().getNextDestination(enemy, engine.getRandom()));

                    //set the destination as now the previous
                    enemy.setPrevious(enemy.getDestination());
                }
                else
                {
                    //remove from list
                    super.remove(enemy);
                    
                    //move index back 1
                    i--;
                    
                    //enemy has passed goal, deduct 1 life from player
                    engine.getManager().getPlayer().getUIMenu().deductLife();
                    
                    
                    //play sound effect?
                }
            }
            else
            {
                //check the location and velocity
                manageEnemyLocation(enemy);
            }
        }
    }
    
    /**
     * Manage the enemy location and velocity
     * @param enemy The enemy
     */
    private void manageEnemyLocation(final Enemy enemy)
    {
        //determine which direction we are to move in
        if (enemy.getCol() < enemy.getDestination().getCol())
        {
            //set velocity
            enemy.setVelocityX(enemy.getSpeed());

            //set facing angle
            enemy.setAngle(EAST);
            
            //if the next move will pass the destination
            if (enemy.getCol() + enemy.getSpeed() >= enemy.getDestination().getCol())
            {
                //stop movement
                enemy.resetVelocityX();
                
                //set at destination
                enemy.setCol(enemy.getDestination().getCol());
            }
        }
        else if (enemy.getCol() > enemy.getDestination().getCol())
        {
            //set velocity
            enemy.setVelocityX(-enemy.getSpeed());
            
            //set facing angle
            enemy.setAngle(WEST);

            //if the next move will pass the destination
            if (enemy.getCol() - enemy.getSpeed() <= enemy.getDestination().getCol())
            {
                //stop movement
                enemy.resetVelocityX();
                
                //set at destination
                enemy.setCol(enemy.getDestination().getCol());
            }
        }

        if (enemy.getRow() < enemy.getDestination().getRow())
        {
            //set velocity
            enemy.setVelocityY(enemy.getSpeed());

            //set facing angle
            enemy.setAngle(SOUTH);
            
            //if the next move will pass the destination
            if (enemy.getRow() + enemy.getSpeed() >= enemy.getDestination().getRow())
            {
                //stop movement
                enemy.resetVelocityY();
                
                //set at destination
                enemy.setRow(enemy.getDestination().getRow());
            }
        }
        else if (enemy.getRow() > enemy.getDestination().getRow())
        {
            //set velocity
            enemy.setVelocityY(-enemy.getSpeed());

            //set facing angle
            enemy.setAngle(NORTH);
            
            //if the next move will pass the destination
            if (enemy.getRow() - enemy.getSpeed() <= enemy.getDestination().getRow())
            {
                //stop movement
                enemy.resetVelocityY();
                
                //set at destination
                enemy.setRow(enemy.getDestination().getRow());
            }
        }
    }
}
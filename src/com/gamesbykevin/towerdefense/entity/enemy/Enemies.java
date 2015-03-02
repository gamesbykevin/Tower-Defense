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
    public Enemies(final Image image)
    {
        super.setImage(image);
    }
    
    /**
     * Add random enemy type
     * @param random Object used to make random decisions
     * @param location The column, row of the enemy
     * @param time The amount of time per each update (nano-seconds)
     * @throws Exception 
     */
    public void add(final Random random, final Cell location, final long time) throws Exception
    {
        add(Enemy.Type.values()[random.nextInt(Enemy.Type.values().length)], location, time);
    }
    
    /**
     * Add enemy
     * @param type The type of enemy
     * @param location The column, row of the enemy
     * @param time The amount of time per each update (nano-seconds)
     */
    public void add(final Enemy.Type type, final Cell location, final long time) throws Exception
    {
        add(type, location.getCol(), location.getRow(), time);
    }
    
    /**
     * Add enemy.
     * @param type The type of enemy
     * @param col The column of the enemy
     * @param row The row of the enemy
     * @param time The amount of time per each update (nano-seconds)
     */
    public void add(final Enemy.Type type, final double col, final double row, final long time) throws Exception
    {
        //create a new enemy
        Enemy enemy = new Enemy(type);
        
        //create timers
        enemy.createTimers(time);
        
        //set position, which will be 1 column each of the specified location
        enemy.setCol(col + 1);
        enemy.setRow(row);
        
        //set the previous location
        enemy.setPrevious(enemy);
        
        //set x, y coordinates
        enemy.update();
        
        //set next destination
        enemy.setDestination(col, row);
        
        //add to list
        add(enemy);
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
    
    /**
     * Get the enemy
     * @param id The unique id all LevelObjects have upon creation
     * @return The enemy containing the id, if not found null is returned
     */
    public Enemy getEnemy(final long id)
    {
        for (int i = 0; i < getEntities().size(); i++)
        {
            if (getEnemy(i).getId() == id)
                return getEnemy(i);
        }
        
        return null;
    }
    
    /**
     * Get the enemy
     * @param index The index location where the enemy is in the list
     * @return The enemy at the specified location
     */
    public Enemy getEnemy(final int index)
    {
        return (Enemy)getEntities().get(index);
    }
    
    /**
     * Poison all enemies within the tower range
     * @param tower The tower looking for enemy targets
     */
    public void poisonEnemies(final Tower tower)
    {
        //check all enemies
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get the current enemy
            Enemy enemy = getEnemy(i);
            
            //make sure the enemy is within the range of the tower
            if (Cell.getDistance(enemy, tower) <= tower.getRange())
            {
                //set poison to true
                enemy.setPoison(true);
                
                //reset timer
                enemy.getTimerPoison().reset();
            }
        }
    }
    
    /**
     * Slow down all enemies within the tower range
     * @param tower The tower looking for enemy targets
     */
    public void freezeEnemies(final Tower tower)
    {
        //check all enemies
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get the current enemy
            Enemy enemy = getEnemy(i);
            
            //make sure the enemy is within the range of the tower
            if (Cell.getDistance(enemy, tower) <= tower.getRange())
            {
                //set frozen to true
                enemy.setFrozen(true);
                
                //reset timer
                enemy.getTimerFrozen().reset();
            }
        }
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
            
            //check if enemy is dead
            if (enemy.isDead())
            {
                //add funds to player
                engine.getManager().getPlayer().getUIMenu().addReward(enemy);
                
                //add explosion effect here
                engine.getManager().getEffects().add(enemy.getType().getEffectType(), enemy);
                
                //play sound effect
                
                
                //remove from list
                super.remove(enemy);

                //move index back 1
                i--;
                
                //no need to continue for a dead enemy
                continue;
            }
            
            //if enemy is frozen check timer
            if (enemy.isFrozen())
            {
                //update frozen timer
                enemy.getTimerFrozen().update(engine.getMain().getTime());
                
                //if the timer has finished
                if (enemy.getTimerFrozen().hasTimePassed())
                {
                    //no longer frozen
                    enemy.setFrozen(false);
                    
                    //reset timer
                    enemy.getTimerFrozen().reset();
                }
            }
            
            //if enemy is poisoned check
            if (enemy.isPoisoned())
            {
                enemy.setHealth(enemy.getHealth() - Enemy.POISON_DAMAGE);
                
                //update posioned timer
                enemy.getTimerPoison().update(engine.getMain().getTime());
                
                //if the timer has finished
                if (enemy.getTimerPoison().hasTimePassed())
                {
                    //no longer poisoned
                    enemy.setPoison(false);
                    
                    //reset timer
                    enemy.getTimerPoison().reset();
                }
            }
            
            //make sure we are heading towards destination
            enemy.manageLocation();
                
            //if the enemy is at the destination
            if (enemy.hasReachedDestination())
            {
                if (enemy.getCol() > 0)
                {
                    //pick the next destination for the enemy
                    enemy.setDestination(engine.getManager().getMap().getNextDestination(enemy, engine.getRandom()));

                    //set the current as the previous location
                    enemy.setPrevious(enemy);
                    
                    //new destination set, stop moving the enemy
                    enemy.resetVelocity();
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
        }
    }
}
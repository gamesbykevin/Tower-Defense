package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

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
    public enum Wave
    {
        Wave1(10,   625L),
        Wave2(20,   600L),
        Wave3(30,   575L),
        Wave4(40,   550L),
        Wave5(50,   525L),
        Wave6(60,   500L),
        Wave7(70,   475L),
        Wave8(80,   450L),
        Wave9(90,   425L),
        Wave10(100,  400L),
        Wave11(110,  375L),
        Wave12(120,  350L),
        Wave13(130,  325L),
        Wave14(140,  300L),
        Wave15(150,  275L),
        Wave16(160,  250L),
        Wave17(170,  225L),
        Wave18(180,  200L),
        Wave19(190,  175L),
        Wave20(200, 150L);
        
        //the total number of enemies for the wave
        private final int total;
        
        //the amount of time to wait before spawning enemies
        private final long rate;
        
        private Wave(final int total, final long rate)
        {
            this.total = total;
            this.rate = Timers.toNanoSeconds(rate);
        }
        
        /**
         * Get the total
         * @return The number of enemies to add in this wave
         */
        public int getTotal()
        {
            return this.total;
        }
        
        /**
         * Get the rate
         * @return the number of nanoseconds between each enemy spawn
         */
        public long getRate()
        {
            return this.rate;
        }
    }
    
    //the timer that determines how long to wait between spawning enemies
    private Timer timer;
    
    //do we start the wave
    private boolean start = false;
    
    //count the number of enemies added
    private int count = 0;
    
    public Enemies(final Image image)
    {
        //assign spritesheet image
        super.setImage(image);
        
        //setup wave
        setupNextWave(0);
    }
    
    /**
     * Setup the next wave.
     * @param index The wave we want
     */
    private void setupNextWave(final int index)
    {
        //reset the number of enemies created
        setCount(0);
        
        //setup timer
        setupTimer(index);
        
        //we have not started spawning enemies yet
        setStart(false);
    }
    
    private int getCount()
    {
        return this.count;
    }
    
    private void setCount(final int count)
    {
        this.count = count;
    }
    
    /**
     * Has the current wave started
     * @return true=yes, false=no
     */
    public boolean hasStarted()
    {
        return this.start;
    }
    
    /**
     * Set the wave to start?
     * @param start true=yes, false=no
     */
    public void setStart(final boolean start)
    {
        this.start = start;
    }
    
    /**
     * Get the wave
     * @param index The wave we want
     * @return The specified wave
     */
    private Wave getWave(final int index)
    {
        return Wave.values()[index];
    }
    
    /**
     * Set the timer depending on the current wave
     * @param index The wave we want
     */
    private void setupTimer(final int index)
    {
        if (getTimer() == null)
            timer = new Timer(getWave(index).getRate());
        
        //set reset value
        timer.setReset(getWave(index).getRate());
        
        //reset timer
        timer.reset();
    }
    
    public Timer getTimer()
    {
        return this.timer;
    }
    
    /**
     * Add random enemy type
     * @param random Object used to make random decisions
     * @param location The column, row of the enemy
     * @param time The amount of time per each update (nano-seconds)
     * @throws Exception 
     */
    private void add(final Random random, final Cell location, final long time) throws Exception
    {
        add(Enemy.Type.values()[random.nextInt(Enemy.Type.values().length)], location, time);
    }
    
    /**
     * Add enemy
     * @param type The type of enemy
     * @param location The column, row of the enemy
     * @param time The amount of time per each update (nanoseconds)
     */
    private void add(final Enemy.Type type, final Cell location, final long time) throws Exception
    {
        add(type, location.getCol(), location.getRow(), time);
    }
    
    /**
     * Add enemy.
     * @param type The type of enemy
     * @param col The column of the enemy
     * @param row The row of the enemy
     * @param time The amount of time per each update (nanoseconds)
     */
    private void add(final Enemy.Type type, final double col, final double row, final long time) throws Exception
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
        
        //track count as well
        setCount(getCount() + 1);
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
     * Poison up to 5 enemies within the tower range
     * @param tower The tower looking for enemy targets
     */
    public void poisonEnemies(final Tower tower)
    {
        int count = 0;
        
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
                
                //increase count
                count++;
                
                //exit if we reached the limit
                if (count >= Tower.AFFECTED_ENEMY_COUNT)
                    break;
            }
        }
    }
    
    /**
     * Slow down up to 5 enemies within the tower range
     * @param tower The tower looking for enemy targets
     */
    public void freezeEnemies(final Tower tower)
    {
        int count = 0;
        
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
                
                //increase count
                count++;
                
                //exit if we reached the limit
                if (count >= Tower.AFFECTED_ENEMY_COUNT)
                    break;
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
    
    /**
     * Are there more waves?
     * @param index The wave we want
     * @return true if there are more waves, false otherwise
     */
    public boolean hasMoreWaves(final int index)
    {
        return (index <= Wave.values().length - 1);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update parent
        super.updateEntities(engine.getMain().getTime());
        
        //no need to continue if there are no more waves
        if (!hasMoreWaves(engine.getManager().getPlayer().getUIMenu().getWaveIndex()))
            return;
        
        //check if the wave has started
        if (hasStarted())
        {
            //if the timer passed
            if (getTimer().hasTimePassed())
            {
                //reset the timer
                getTimer().reset();
                
                //total number of enemies allowed
                final int total = getWave(engine.getManager().getPlayer().getUIMenu().getWaveIndex()).getTotal(); 
                
                //only add enemy if we have not yet reached the total
                if (getCount() < total)
                {
                    //add an enemy
                    add(engine.getRandom(), engine.getManager().getMap().getStart(), engine.getMain().getTime());
                    
                    //update display for user
                    engine.getManager().getPlayer().getUIMenu().setLeft(total - getCount());
                }
            }
            else
            {
                //update timer
                getTimer().update(engine.getMain().getTime());
            }
        }
        
        //check if there are enemies
        final boolean hasEnemies = (!getEntities().isEmpty());
        
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
        
        //if we had enemies, but no longer do
        if (hasEnemies && getEntities().isEmpty())
        {
            //the total number of enemies to spawn in the wave
            final int total = getWave(engine.getManager().getPlayer().getUIMenu().getWaveIndex()).getTotal();
            
            //make sure we spawned the number of enemies for this wave
            if (getCount() >= total)
            {
                //move to the next wave in the menu
                engine.getManager().getPlayer().getUIMenu().increaseWaveIndex();

                if (hasMoreWaves(engine.getManager().getPlayer().getUIMenu().getWaveIndex()))
                {
                    //setup next wave
                    setupNextWave(engine.getManager().getPlayer().getUIMenu().getWaveIndex());

                    //update display for user
                    engine.getManager().getPlayer().getUIMenu().setLeft(total - getCount());
                }
            }
        }
    }
}
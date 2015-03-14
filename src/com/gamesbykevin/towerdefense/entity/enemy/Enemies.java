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
    //the number of enemies to spawn
    private static final int ENEMY_COUNT_MIN = 5;
    
    //the number of enemies to increase with each wave
    private static final int ENEMY_RATE_INCREASE = 5;
    
    //the max to wait to spawn enemies
    private static final long RATE_MAX = 750L;
    
    //the min to wait to spawn enemies
    private static final long RATE_MIN = 250L;
    
    //the rate of decrease
    private static final long RATE_DECREASE = 25L;
    
    //the total number of waves in the game
    public static final int WAVE_TOTAL = 20;
    
    //the enemy health will increase by this amount every wave
    private static final double HEALTH_INCREASE = .15;
    
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
    
    /**
     * Get the current count of enemies created
     * @return The total number of enemies created for the current wave
     */
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
     * Get the rate at which to spawn enemies
     * @param index The progress in the wave
     * @return The rate (nanoseconds) at which to spawn enemies
     */
    public long getRate(final int index)
    {
        //get the delay
        long delay = RATE_MAX - (index * RATE_DECREASE);
        
        //keep within minimum
        if (delay < RATE_MIN)
            delay = RATE_MIN;
        
        //return nanoseconds
        return Timers.toNanoSeconds(delay);
    }
    
    /**
     * Get the total number of enemies
     * @param index The wave index
     * @return The total number of enemies to create for the given wave index
     */
    public int getTotal(final int index)
    {
        return ENEMY_COUNT_MIN + (index * ENEMY_RATE_INCREASE);
    }
    
    /**
     * Set the timer depending on the current wave
     * @param index The wave we want
     */
    private void setupTimer(final int index)
    {
        if (getTimer() == null)
            timer = new Timer(getRate(index));
        
        //set reset value
        timer.setReset(getRate(index));
        
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
     * @param waveIndex The current wave
     * @throws Exception 
     */
    private void add(final Random random, final Cell location, final long time, final int waveIndex) throws Exception
    {
        add(Enemy.Type.values()[random.nextInt(Enemy.Type.values().length)], location, time, waveIndex);
    }
    
    /**
     * Add enemy
     * @param type The type of enemy
     * @param location The column, row of the enemy
     * @param time The amount of time per each update (nanoseconds)
     * @param waveIndex The current wave
     */
    private void add(final Enemy.Type type, final Cell location, final long time, final int waveIndex) throws Exception
    {
        add(type, location.getCol(), location.getRow(), time, waveIndex);
    }
    
    /**
     * Add enemy.
     * @param type The type of enemy
     * @param col The column of the enemy
     * @param row The row of the enemy
     * @param time The amount of time per each update (nanoseconds)
     * @param waveIndex The current wave
     */
    private void add(final Enemy.Type type, final double col, final double row, final long time, final int waveIndex) throws Exception
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
        
        //now increase the health based on the current wave
        enemy.setStartHealth(enemy.getHealth() + ((enemy.getHealth() * HEALTH_INCREASE) * waveIndex));
        
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
        int tmpCount = 0;
        
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
                tmpCount++;
                
                //exit if we reached the limit
                if (tmpCount >= Tower.AFFECTED_ENEMY_COUNT)
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
        int tmpCount = 0;
        
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
                tmpCount++;
                
                //exit if we reached the limit
                if (tmpCount >= Tower.AFFECTED_ENEMY_COUNT)
                    break;
            }
        }
    }
    
    /**
     * Get the enemy within the range of the Tower
     * @param tower The tower looking for an enemy target
     * @return The enemy that has traveled the farthest within the range of the tower, null is returned if no enemies found
     */
    public Enemy getEnemy(final Tower tower)
    {
        //the enemy we are looking for
        Enemy enemy = null;
        
        //the distance the enemy has traveled
        double distanceTraveled = 0;
        
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get the current enemy
            Enemy tmp = getEnemy(i);
            
            //make sure the enemy is within the range of the tower
            if (Cell.getDistance(tmp, tower) <= tower.getRange())
            {
                //if we have no target yet, or if we beat our previous target
                if (enemy == null || tmp.getDistanceTraveled() > distanceTraveled)
                {
                    //assign the new distance to beat
                    distanceTraveled = tmp.getDistanceTraveled();
                    
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
        return (index <= WAVE_TOTAL - 1);
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
                final int total = getTotal(engine.getManager().getPlayer().getUIMenu().getWaveIndex());
                
                //only add enemy if we have not yet reached the total
                if (getCount() < total)
                {
                    //add an enemy
                    add(
                        engine.getRandom(), 
                        engine.getManager().getMap().getStart(), 
                        engine.getMain().getTime(),
                        engine.getManager().getPlayer().getUIMenu().getWaveIndex()
                    );
                    
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
            
            //update the distance traveled
            enemy.updateDistanceTraveled();
            
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
            //make sure enemy menu is not showing
            engine.getManager().getPlayer().getEnemyMenu().setVisible(false);
            
            //the total number of enemies to spawn in the wave
            final int total = getTotal(engine.getManager().getPlayer().getUIMenu().getWaveIndex());
            
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
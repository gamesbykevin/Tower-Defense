package com.gamesbykevin.towerdefense.entity.enemy;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.towerdefense.entity.effects.Effect;
import com.gamesbykevin.towerdefense.entity.Entity;

/**
 * This class represents the enemy
 * @author GOD
 */
public final class Enemy extends Entity
{
    private static final double SPEED_SLOW = .010;
    private static final double SPEED_MED = .015;
    private static final double SPEED_FAST = .020;
    
    public enum Type
    {
        Blue1(SPEED_SLOW, 2, Effect.Type.Explosion2, 5),
        Blue2(SPEED_MED, 4, Effect.Type.Explosion3, 10),
        Blue3(SPEED_FAST, 6, Effect.Type.Explosion4, 15),
        
        Green1(SPEED_SLOW, 1, Effect.Type.Explosion5, 5),
        Green2(SPEED_MED, 3, Effect.Type.Explosion6, 10),
        Green3(SPEED_FAST, 5, Effect.Type.Explosion7, 15),
        
        Red1(SPEED_SLOW, 2, Effect.Type.Explosion8, 5),
        Red2(SPEED_MED, 4, Effect.Type.Explosion9, 10),
        Red3(SPEED_FAST, 6, Effect.Type.Explosion10, 15),
        
        Yellow1(SPEED_SLOW, 1, Effect.Type.Explosion11, 5),
        Yellow2(SPEED_MED, 3, Effect.Type.Explosion12, 10),
        Yellow3(SPEED_FAST, 5, Effect.Type.Explosion12, 15);
        
        //the speed of the type of enemy
        private final double speed;
        
        //the reward for destroying the enemy
        private final int reward;
        
        //the type of explosion upon being destroyed
        private final Effect.Type effectType;
        
        //the starting health
        private final int startHealth;
        
        private Type(final double speed, final int reward, final Effect.Type effectType, final int startHealth)
        {
            //assign the starting health
            this.startHealth = startHealth;
            
            //assign the speed
            this.speed = speed;
            
            //assign the reward
            this.reward = reward;
            
            //assign the effect type
            this.effectType = effectType;
        }
        
        public int getStartHealth()
        {
            return this.startHealth;
        }
        
        public Effect.Type getEffectType()
        {
            return this.effectType;
        }
        
        protected double getSpeed()
        {
            return this.speed;
        }
        
        private int getReward()
        {
            return this.reward;
        }
    }
    
    //different facing directions for the enemies
    private static double NORTH = Math.toRadians(270);
    private static double SOUTH = Math.toRadians(90);
    private static double EAST = Math.toRadians(0);
    private static double WEST = Math.toRadians(180);
    
    //the pixel dimensions of the enemy
    public static final double WIDTH = 46.0;
    public static final double HEIGHT = 46.0;
    
    /**
     * The minimum allowed health
     */
    private static final int HEALTH_MIN = 0;
    
    /**
     * The max start health for the enemy
     */
    private int maxHealth = 10;
    
    /**
     * The enemy's current health
     */
    private double health = 10;
    
    /**
     * How fast does the enemy move from column to column, etc.....
     */
    private double speed = .01;
    
    //where the enemy is heading
    private Cell destination;
    
    //where the enemy previously was
    private Cell previous;
    
    //is the enemy poisoned
    private boolean poison = false;
    
    //does the enemy move slower
    private boolean frozen = false;
    
    //track the duration of posion and frozen
    private Timers timers;
    
    //the different timers we will be tracking
    public enum TimerKey
    {
        Frozen, Poison
    }
    
    //how long the posion will be in effect for
    private static final long DURATION_POISON = Timers.toNanoSeconds(5000L);
    
    //how long the enemy will be frozen for
    private static final long DURATION_FROZEN = Timers.toNanoSeconds(5000L);
    
    //the amount of damage per update when poisoned
    public static final double POISON_DAMAGE = .1;
    
    //the type of enemy
    private final Type type;
    
    protected Enemy(final Type type) throws Exception
    {
        //call parent
        super();
        
        //assign type
        this.type = type;
        
        //create default animation
        Animation animation = new Animation();
        
        //all enemy animations will loop 
        animation.setLoop(true);
        
        //set the start health
        this.setStartHealth(getType().getStartHealth());
        
        //set the enemy speed
        this.setSpeed(getType().getSpeed());
        
        switch (getType())
        {
            case Blue1:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 4), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 5), (int)(HEIGHT * 0), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Blue2:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 1), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 1), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 1), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 1), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(600L));
                break;
            
            case Blue3:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 2), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 2), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 2), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 2), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Green1:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 4), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 5), (int)(HEIGHT * 3), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Green2:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 4), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 4), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 4), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 4), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(600L));
                break;
            
            case Green3:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 5), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 5), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 5), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 5), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Red1:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 4), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 5), (int)(HEIGHT * 6), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Red2:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 7), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 7), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 7), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 7), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(600L));
                break;
            
            case Red3:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 8), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 8), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 8), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 8), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Yellow1:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 4), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(100L));
                animation.add((int)(WIDTH * 5), (int)(HEIGHT * 9), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
            
            case Yellow2:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 10), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 10), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 10), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 10), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(600L));
                break;
            
            case Yellow3:
                animation.add((int)(WIDTH * 0), (int)(HEIGHT * 11), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(1000L));
                animation.add((int)(WIDTH * 1), (int)(HEIGHT * 11), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 2), (int)(HEIGHT * 11), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                animation.add((int)(WIDTH * 3), (int)(HEIGHT * 11), (int)WIDTH, (int)HEIGHT, Timers.toNanoSeconds(200L));
                break;
                
            default:
                throw new Exception("Type not setup here = " + type.toString());
        }
        
        //add animation
        super.addAnimation(animation);
    }
    
    public void createTimers(final long time)
    {
        this.timers = new Timers(time);
        this.timers.add(TimerKey.Frozen, DURATION_FROZEN);
        this.timers.add(TimerKey.Poison, DURATION_POISON);
    }
    
    public Timer getTimerPoison()
    {
        return timers.getTimer(TimerKey.Poison);
    }
    
    public Timer getTimerFrozen()
    {
        return timers.getTimer(TimerKey.Frozen);
    }
    
    public void setPoison(final boolean poison)
    {
        this.poison = poison;
    }
    
    public boolean isPoisoned()
    {
        return this.poison;
    }
    
    public void setFrozen(final boolean frozen)
    {
        this.frozen = frozen;
    }
    
    public boolean isFrozen()
    {
        return this.frozen;
    }
    
    /**
     * Get the reward
     * @return The cash reward for destroying the enemy
     */
    public int getReward()
    {
        return getType().getReward();
    }
    
    /**
     * Get the location where the enemy was previously
     * @return The column, row
     */
    public Cell getPrevious()
    {
        return this.previous;
    }
    
    public void setPrevious(final Cell previous)
    {
        setPrevious(previous.getCol(), previous.getRow());
    }
    
    public void setPrevious(final double col, final double row)
    {
        if (getPrevious() == null)
            this.previous = new Cell();
        
        //set the location
        this.previous.setCol(col);
        this.previous.setRow(row);
    }
    
    /**
     * Get the location where the enemy is headed
     * @return The column, row
     */
    private Cell getDestination()
    {
        return this.destination;
    }
    
    public void setDestination(final Cell destination)
    {
        setDestination(destination.getCol(), destination.getRow());
    }
    
    public void setDestination(final double col, final double row)
    {
        if (getDestination() == null)
            this.destination = new Cell();
        
        //set the location
        this.destination.setCol(col);
        this.destination.setRow(row);
    }
    
    /**
     * Is the enemy at the destination
     * @return true=yes, false=no
     */
    public boolean hasReachedDestination()
    {
        return getDestination().equals(this);
    }
    
    /**
     * Set the speed of the enemy
     * @param speed The rate at which the enemy can move
     */
    public final void setSpeed(final double speed)
    {
        this.speed = speed;
    }
    
    /**
     * Get the speed.<br>
     * If the enemy is frozen the speed will be 50%
     * @return The rate at which the enemy can move
     */
    public final double getSpeed()
    {
        if (isFrozen())
            return (this.speed / 2);
        
        return this.speed;
    }
    
    /**
     * Set the starting and current health of the enemy
     * @param health The max health we want to assign to the enemy
     */
    public void setStartHealth(final int health)
    {
        this.maxHealth = health;
        
        //assign the current health
        setHealth(health);
    }
    
    /**
     * Assign the current health 
     * @param health The current health we want to assign to the enemy
     */
    public void setHealth(final double health)
    {
        this.health = health;
        
        //make sure the health does not go below the minimum
        if (this.health < HEALTH_MIN)
            this.health = HEALTH_MIN;
    }
 
    /**
     * Is the enemy dead?
     * @return true if the enemy health is less than the health minimum "0"
     */
    public boolean isDead()
    {
        return (getHealth() <= HEALTH_MIN);
    }
    
    /**
     * Get the enemy's starting health upon creation
     * @return The max starting health
     */
    public int getStartHealth()
    {
        return this.maxHealth;
    }
    
    /**
     * Get the enemy's current health
     * @return The current health
     */
    public double getHealth()
    {
        return this.health;
    }
    
    /**
     * Make sure the enemy is heading towards the destination
     */
    public void manageLocation()
    {
        //determine which direction we are to move in
        if (getCol() < getDestination().getCol())
        {
            //set velocity
            setVelocityX(getSpeed());

            //set facing angle
            setAngle(EAST);
            
            //if the next move will pass the destination
            if (getCol() + getSpeed() >= getDestination().getCol())
            {
                //set at destination
                setCol(getDestination().getCol());
            }
        }
        else if (getCol() > getDestination().getCol())
        {
            //set velocity
            setVelocityX(-getSpeed());
            
            //set facing angle
            setAngle(WEST);

            //if the next move will pass the destination
            if (getCol() - getSpeed() <= getDestination().getCol())
            {
                //set at destination
                setCol(getDestination().getCol());
            }
        }

        if (getRow() < getDestination().getRow())
        {
            //set velocity
            setVelocityY(getSpeed());

            //set facing angle
            setAngle(SOUTH);
            
            //if the next move will pass the destination
            if (getRow() + getSpeed() >= getDestination().getRow())
            {
                //set at destination
                setRow(getDestination().getRow());
            }
        }
        else if (getRow() > getDestination().getRow())
        {
            //set velocity
            setVelocityY(-getSpeed());

            //set facing angle
            setAngle(NORTH);
            
            //if the next move will pass the destination
            if (getRow() - getSpeed() <= getDestination().getRow())
            {
                //set at destination
                setRow(getDestination().getRow());
            }
        }
    }
    
    /**
     * Get the type of enemy this is
     * @return The type of enemy
     */
    public Type getType()
    {
        return this.type;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
}
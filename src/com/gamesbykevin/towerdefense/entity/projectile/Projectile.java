package com.gamesbykevin.towerdefense.entity.projectile;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.towerdefense.entity.Entity;

import java.util.UUID;

/**
 * The projectile fired by a tower
 * @author GOD
 */
public final class Projectile extends Entity
{
    public enum Type
    {
        Blue1, Blue2, Blue3, Blue4, 
        Green1, Green2, Green3, Green4, 
        Red1, Red2, Red3, Red4, 
    }
    
    //the type of projectile this is
    private final Type type;
    
    //where the projectile is headed
    private Cell goal;
    
    //where we begin
    private Cell start;
    
    //the obect we are heading towards
    private final UUID targetId;
    
    //the time until the projectile reaches the goal
    private Timer timer;
    
    //the amount of time until the projectile reaches the destination
    private static final long DEFAULT_DELAY = Timers.toNanoSeconds(125L);
    
    //the amount of damage the projectile will cause
    private final double damage;
    
    protected Projectile(final Type type, final double damage, final UUID targetId) throws Exception
    {
        //call parent
        super();
        
        //assign type
        this.type = type;
        
        //the amount of damage caused by this projectile
        this.damage = damage;
        
        //assign the object we are heading towards
        this.targetId = targetId;
        
        //create a new timer with the delay until it reaches the goal
        this.timer = new Timer(DEFAULT_DELAY);
        
        switch (type)
        {
            case Blue1:
                super.addAnimation(0, 0, 13, 37);
                break;
                
            case Blue2:
                super.addAnimation(13, 0, 13, 37);
                break;
                
            case Blue3:
                super.addAnimation(26, 0, 9, 37);
                break;
                
            case Blue4:
                super.addAnimation(35, 0, 9, 37);
                break;
                
            case Green1:
                super.addAnimation(0, 37, 13, 37);
                break;
                
            case Green2:
                super.addAnimation(13, 37, 13, 37);
                break;
                
            case Green3:
                super.addAnimation(26, 37, 9, 37);
                break;
                
            case Green4:
                super.addAnimation(35, 37, 9, 37);
                break;
                
            case Red1:
                super.addAnimation(0, 74, 13, 37);
                break;
                
            case Red2:
                super.addAnimation(13, 74, 13, 37);
                break;
                
            case Red3:
                super.addAnimation(26, 74, 9, 37);
                break;
                
            case Red4:
                super.addAnimation(35, 74, 9, 37);
                break;
        }
    }
    
    /**
     * The amount of damage this projectile can cause
     * @return The damage
     */
    public double getDamage()
    {
        return this.damage;
    }
    
    /**
     * Get the timer 
     * @return The timer representing the progress towards the goal
     */
    public Timer getTimer()
    {
        return this.timer;
    }
    
    public void setStart(final Cell cell)
    {
        setStart(cell.getCol(), cell.getRow());
    }
    
    /**
     * Set the start/current location
     * @param col Column
     * @param row Row
     */
    public void setStart(final double col, final double row)
    {
        //set the start location
        this.start = new Cell(col, row);
        
        //set the current position
        super.setCol(start);
        super.setRow(start);
    }
    
    /**
     * Get the start
     * @return The starting location
     */
    public Cell getStart()
    {
        return this.start;
    }
    
    /**
     * Update the location of the projectile based on the progress of the timer
     * @param time The time to deduct from the timer
     * @param goal The goal we are targeting
     */
    public void updateLocation(final long time, final Cell goal)
    {
        //update timer
        getTimer().update(time);
        
        //get the progress of the timer
        final float progress = getTimer().getProgress();
        
        if (goal != null)
        {
            //update the goal
            setGoal(goal.getCol(), goal.getRow());
        }
        
        //if the timer has not completed
        if (!getTimer().hasTimePassed())
        {
            //set the new column position
            super.setCol(getStart().getCol() + ((getGoal().getCol() - getStart().getCol()) * progress));

            //set the new row position
            super.setRow(getStart().getRow() + ((getGoal().getRow() - getStart().getRow()) * progress));
        }
        else
        {
            //position at goal
            super.setCol(getGoal());
            super.setRow(getGoal());
        }
    }
    
    public void setGoal(final double col, final double row)
    {
        if (this.goal == null)
            this.goal = new Cell();
        
        this.goal.setCol(col);
        this.goal.setRow(row);
    }
    
    public UUID getTargetId()
    {
        return this.targetId;
    }
    
    /**
     * Get the goal 
     * @return The destination the projectile is heading to
     */
    public Cell getGoal()
    {
        return this.goal;
    }
    
    public Type getType()
    {
        return this.type;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        this.timer = null;
    }
}
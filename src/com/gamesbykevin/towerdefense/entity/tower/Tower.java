package com.gamesbykevin.towerdefense.entity.tower;

import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;
import com.gamesbykevin.towerdefense.entity.Entity;

/**
 * The tower that attacks the enemy
 * @author GOD
 */
public final class Tower extends Entity
{
    //the pixel dimensions of the tower
    public static final double WIDTH = 40.0;
    public static final double HEIGHT = 40.0;
    
    public enum Upgrade
    {
        Default, Upgrade1, Upgrade2;
    }
    
    /**
     * There are 8 different towers to choose from.<br>
     * The type will also determine the attributes set
     */
    public enum Type
    {
        Tower1(0, 0, 0), 
        Tower2(0, 0, 0), 
        Tower3(0, 0, 0), 
        Tower4(0, 0, 0), 
        Tower5(0, 0, 0), 
        Tower6(0, 0, 0), 
        Tower7(0, 0, 0), 
        Tower8(0, 0, 0);
        
        private final int levelRange, levelDamage, levelRate;
        
        private Type(final int levelRange, final int levelDamage, final int levelRate)
        {
            this.levelDamage = levelDamage;
            this.levelRange = levelRange;
            this.levelRate = levelRate;
        }
        
        private int getLevelRate()
        {
            return this.levelRate;
        }
        
        private int getLevelDamage()
        {
            return this.levelDamage;
        }
        
        private int getLevelRange()
        {
            return this.levelRange;
        }
    }
    
    /**
     * The maximum number of upgrades that can be performed on this tower
     */
    private static final int UPGRADE_COUNT_LIMIT = Upgrade.values().length;
    
    /**
     * The highest level an upgrade can get to 
     */
    private static final int UPGRADE_MAXIMUM_LEVEL = 5;
    
    //upgrade count
    private int indexUpgrade = 0;
    
    /**
     * The levels of damage the tower can cause
     */
    public enum Damage
    {
        Damage1(1),
        Damage2(2.5),
        Damage3(6.25),
        Damage4(15.625),
        Damage5(39.0625);
        
        private final double amount;
        
        private Damage(final double amount)
        {
            this.amount = amount;
        }
        
        public double getDamageAmount()
        {
            return this.amount;
        }
    }
    
    /**
     * The levels of range the tower can reach the enemies.<br>
     * The range is the number of cells away the tower can detect an enemy
     */
    public enum Range
    {
        Range1(1),
        Range2(1.25),
        Range3(2),
        Range4(3.25),
        Range5(6);
        
        private final double amount;
        
        private Range(final double amount)
        {
            this.amount = amount;
        }
        
        public double getRange()
        {
            return this.amount;
        }
    }
    
    /**
     * The levels of rate the tower has to wait to attack
     */
    public enum Rate
    {
        Rate1(Timers.toNanoSeconds(1750L)),
        Rate2(Timers.toNanoSeconds(1500L)),
        Rate3(Timers.toNanoSeconds(1250L)),
        Rate4(Timers.toNanoSeconds(1000L)),
        Rate5(Timers.toNanoSeconds(250L));
        
        private final long amount;
        
        private Rate(final long amount)
        {
            this.amount = amount;
        }
        
        public long getRate()
        {
            return this.amount;
        }
    }
    
    //timer to determine when the tower can shoot
    private Timer timer;
    
    //current index for each variable
    private int indexRange = 0;
    private int indexDamage = 0;
    private int indexRate = 0;
    
    //which tower is this
    private final Type type;
    
    /**
     * @param type
     * @param levelRange
     * @param levelDamage
     * @param levelRate 
     */
    protected Tower(final Type type) throws Exception
    {
        //all tower animations in our sprite sheet face north by default
        super(Direction.North);
        
        //assign the type of tower
        this.type = type;
        
        //create empty timer
        this.timer = new Timer();
        
        //assign the attributes of the tower
        this.setLevelDamage(type.getLevelDamage());
        this.setLevelRange(type.getLevelRange());
        this.setLevelRate(type.getLevelRate());
        
        switch (type)
        {
            case Tower1:
                super.addAnimation(0.0,  0.0, WIDTH, HEIGHT, Upgrade.Default);
                super.addAnimation(40.0, 0.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 0.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower2:
                super.addAnimation(0.0,  40.0, WIDTH, HEIGHT, Upgrade.Default);
                super.addAnimation(40.0, 40.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 40.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower3:
                super.addAnimation(0.0,  80.0, WIDTH, HEIGHT, Upgrade.Default);
                super.addAnimation(40.0, 80.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 80.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower4:
                super.addAnimation(0.0,  120.0, WIDTH, HEIGHT, Upgrade.Default);
                super.addAnimation(40.0, 120.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 120.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower5:
                super.addAnimation(0.0,  160.0, WIDTH, HEIGHT, Upgrade.Default);
                super.addAnimation(40.0, 160.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 160.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower6:
                super.addAnimation(0.0,  200.0, WIDTH, HEIGHT, Upgrade.Default);
                super.addAnimation(40.0, 200.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 200.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower7:
                super.addAnimation(0.0,  240.0, WIDTH, HEIGHT, Upgrade.Default);
                super.addAnimation(40.0, 240.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 240.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower8:
                super.addAnimation(0.0, 280.0, WIDTH, HEIGHT, Upgrade.Default);
                break;
                
            default:
                throw new Exception("Tower type not setup here " + type.toString());
        }
        
        //set default animation
        super.setAnimation(Upgrade.Default);
    }
    
    public double getRange()
    {
        return Range.values()[indexRange].getRange();
    }
    
    public double getDamage()
    {
        return Damage.values()[indexDamage].getDamageAmount();
    }
    
    /**
     * Set the level for the range this tower should have.<br>
     * If the parameter is greater than the UPGRADE_MAXIMUM_LEVEL we will restrict.<br>
     * Also if the parameter will be set to 0 if a lower number is provided
     * @param level Level ranging from 0 to (UPGRADE_MAXIMUM_LEVEL - 1)
     */
    private void setLevelRange(int level)
    {
        if (level > UPGRADE_MAXIMUM_LEVEL - 1)
            level = (UPGRADE_MAXIMUM_LEVEL - 1);
        if (level < 0)
            level = 0;
        
        //assign value
        this.indexRange = level;
    }
    
    /**
     * Set the level for the damage this tower should be able to cause.<br>
     * If the parameter is greater than the UPGRADE_MAXIMUM_LEVEL we will restrict.<br>
     * Also if the parameter will be set to 0 if a lower number is provided
     * @param level Level ranging from 0 to (UPGRADE_MAXIMUM_LEVEL - 1)
     */
    private void setLevelDamage(int level)
    {
        if (level > UPGRADE_MAXIMUM_LEVEL - 1)
            level = (UPGRADE_MAXIMUM_LEVEL - 1);
        if (level < 0)
            level = 0;
        
        //assign value
        this.indexDamage = level;
    }
    
    /**
     * Set the level for the fire rate this tower should have.<br>
     * If the parameter is greater than the UPGRADE_MAXIMUM_LEVEL we will restrict.<br>
     * Also if the parameter will be set to 0 if a lower number is provided
     * @param level Level ranging from 0 to (UPGRADE_MAXIMUM_LEVEL - 1)
     */
    private void setLevelRate(int level)
    {
        if (level > UPGRADE_MAXIMUM_LEVEL - 1)
            level = (UPGRADE_MAXIMUM_LEVEL - 1);
        if (level < 0)
            level = 0;
        
        //assign value
        this.indexRate = level;
        
        //update the timer reset value
        updateTimer();
    }
    
    /**
     * Update the timer length to the current rate setting
     */
    private void updateTimer()
    {
        getTimer().setReset(Rate.values()[indexRate].getRate());
    }
    
    /**
     * Get the timer
     * @return The timer that determines when the tower can fire
     */
    public Timer getTimer()
    {
        return this.timer;
    }
    
    /**
     * Upgrade the tower.<br>
     * Tower8 is the only tower that cannot be upgraded
     */
    public void upgrade()
    {
        //this is the only tower that can't be upgraded
        if (getType() == Tower.Type.Tower8)
            return;
        
        //if we have already reached the limit, don't continue
        if(indexUpgrade >= UPGRADE_COUNT_LIMIT - 1)
            return;
        
        //increase upgrade count
        this.indexUpgrade++;
        
        //set the correct animation for the tower
        super.setAnimation(Upgrade.values()[indexUpgrade]);
        
        //increase the level of each attribute
        indexRange++;
        indexDamage++;
        indexRate++;
        
        //make sure we don't exceed the maximum
        if (indexRange > UPGRADE_MAXIMUM_LEVEL)
            indexRange = UPGRADE_MAXIMUM_LEVEL;
        if (indexRate > UPGRADE_MAXIMUM_LEVEL)
            indexRate = UPGRADE_MAXIMUM_LEVEL;
        if (indexDamage > UPGRADE_MAXIMUM_LEVEL)
            indexDamage = UPGRADE_MAXIMUM_LEVEL;
    }
    
    /**
     * Get the type of tower this is
     * @return The type (Tower1, Tower2, etc...)
     */
    public Type getType()
    {
        return this.type;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        this.timer.dispose();
        this.timer = null;
    }
}
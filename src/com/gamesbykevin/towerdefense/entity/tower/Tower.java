package com.gamesbykevin.towerdefense.entity.tower;

import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.Timers;

import com.gamesbykevin.towerdefense.entity.Entity;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.entity.projectile.Projectile;
import com.gamesbykevin.towerdefense.level.map.Map;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * The tower that attacks the enemy
 * @author GOD
 */
public final class Tower extends Entity
{
    //when freezing or poison, how many enemies can be affected at once
    public static final int AFFECTED_ENEMY_COUNT = 5;
    
    //the timer for how long to wait before attacking
    private Timer timer;
    
    public enum Upgrade
    {
        Default, Upgrade1, Upgrade2;
    }
    
    public enum RangeKey
    {
        Default, Poision, Freeze
    }
    
    private boolean poison = false;
    private boolean freeze = false;
    
    /**
     * There are 8 different towers to choose from.<br>
     * The type will also determine the attributes set
     */
    public enum Type
    {
        Tower1(0, 0, 0, 1,  5,  0, 0,  (int)WIDTH, (int)HEIGHT, Projectile.Type.Blue3), 
        Tower2(0, 2, 0, 10, 20,  0, 40, (int)WIDTH, (int)HEIGHT, Projectile.Type.Red3), 
        Tower3(2, 0, 1, 10, 30,  0, 80, (int)WIDTH, (int)HEIGHT, Projectile.Type.Green3), 
        
        //freeze and poison towers
        Tower4(1, 0, 0, 0,  100,  0, 120, (int)WIDTH, (int)HEIGHT, null), 
        Tower5(1, 0, 0, 0,  100,  0, 160, (int)WIDTH, (int)HEIGHT, null), 
        
        Tower6(0, 0, 4, 10, 25,  0, 200,(int)WIDTH, (int)HEIGHT, Projectile.Type.Green4), 
        Tower7(1, 1, 2, 40, 40,  0, 240,(int)WIDTH, (int)HEIGHT, Projectile.Type.Red4), 
        Tower8(4, 4, 4, 0,  200, 0, 280,(int)WIDTH, (int)HEIGHT, Projectile.Type.Red1);
        
        //index for the 3 attributes
        private final int levelRange, levelDamage, levelRate;
        
        //the sell price, upgrade price, and urchase price
        private final int sell, upgrade, purchase;
        
        //the initial location of the animation
        private final Rectangle location;
        
        //the type of projectile the tower shoots
        private final Projectile.Type projectileType;
        
        private Type(
                final int levelRange, 
                final int levelDamage, 
                final int levelRate, 
                final int upgrade, 
                final int purchase,
                final int x, final int y, final int w, final int h,
                final Projectile.Type projectileType)
        {
            this.projectileType = projectileType;
            this.levelDamage = levelDamage;
            this.levelRange = levelRange;
            this.levelRate = levelRate;
            this.sell = (purchase / 2);
            this.upgrade = upgrade;
            this.purchase = purchase;
            this.location = new Rectangle(x, y, w, h);
        }
        
        public Projectile.Type getProjectileType()
        {
            return this.projectileType;
        }
        
        public int getCostSell()
        {
            return this.sell;
        }
        
        public int getCostUpgrade()
        {
            return this.upgrade;
        }
        
        public int getCostPurchase()
        {
            return this.purchase;
        }
        
        public int getLevelRate()
        {
            return this.levelRate;
        }
        
        public int getLevelDamage()
        {
            return this.levelDamage;
        }
        
        public int getLevelRange()
        {
            return this.levelRange;
        }
        
        public Rectangle getLocation()
        {
            return this.location;
        }
    }
    
    /**
     * The levels of damage the tower can cause
     */
    public enum Damage
    {
        Damage1(4),
        Damage2(6),
        Damage3(8),
        Damage4(10),
        Damage5(12);
        
        private final int damage;
        
        private Damage(final int damage)
        {
            this.damage = damage;
        }
        
        public int getDamage()
        {
            return this.damage;
        }
    }
    
    /**
     * The levels of range the tower can reach the enemies.<br>
     * The range is the number of cells away the tower can detect an enemy
     */
    public enum Range
    {
        Range1(1.0),
        Range2(1.5),
        Range3(2.0),
        Range4(2.5),
        Range5(3.0);
        
        private final double range;
        
        private Range(final double range)
        {
            this.range = range;
        }
        
        public double getRange()
        {
            return this.range;
        }
    }
    
    /**
     * The levels of rate the tower has to wait to attack
     */
    public enum Rate
    {
        Rate1(Timers.toNanoSeconds(5000L)),
        Rate2(Timers.toNanoSeconds(1500L)),
        Rate3(Timers.toNanoSeconds(1000L)),
        Rate4(Timers.toNanoSeconds(750L)),
        Rate5(Timers.toNanoSeconds(250L));
        
        private final long rate;
        
        private Rate(final long rate)
        {
            this.rate = rate;
        }
        
        public long getRate()
        {
            return this.rate;
        }
    }
    
    //current index for each variable
    private int indexRange = 0;
    private int indexDamage = 0;
    private int indexRate = 0;
    
    //which tower is this
    private final Type type;
    
    //the pixel dimensions of the tower
    public static final double WIDTH = 40.0;
    public static final double HEIGHT = 40.0;
    
    /**
     * The money the tower is worth when sold
     */
    private int sell;
    
    /**
     * The cost to upgrade the tower
     */
    private int upgrade;
    
    /**
     * The rate at which we increase the upgrade cost, per upgrade
     */
    private static final double UPGRADE_INCREASE_RATIO = .33;
    
    /**
     * The rate at which we increase the sell value, per upgrade
     */
    private static final double SELL_INCREASE_RATIO = .2;
    
    /**
     * The maximum number of upgrades that can be performed on this tower
     */
    public static final int UPGRADE_COUNT_LIMIT = Upgrade.values().length;
    
    /**
     * The highest level an upgrade can get to 
     */
    public static final int UPGRADE_MAXIMUM_LEVEL = 5;
    
    //upgrade count
    private int indexUpgrade = 0;
    
    //the enemy targeted
    private Enemy enemy;
    
    protected Tower(final Type type) throws Exception
    {
        //call parent
        super();
        
        //assign the type of tower
        this.type = type;
        
        //create timer
        this.timer = new Timer();
        
        //set the upgrade and sell costs
        this.upgrade = type.getCostUpgrade();
        this.sell = type.getCostSell();
        
        //assign the attributes of the tower
        this.setLevelDamage(type.getLevelDamage());
        this.setLevelRange(type.getLevelRange());
        this.setLevelRate(type.getLevelRate());
        
        //add default
        super.addAnimation(type.getLocation(), Upgrade.Default);
        
        switch (type)
        {
            case Tower1:
                super.addAnimation(40.0, 0.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 0.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower2:
                super.addAnimation(40.0, 40.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 40.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower3:
                super.addAnimation(40.0, 80.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 80.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower4:
                this.freeze = true;
                super.addAnimation(40.0, 120.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 120.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower5:
                this.poison = true;
                super.addAnimation(40.0, 160.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 160.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower6:
                super.addAnimation(40.0, 200.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 200.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower7:
                super.addAnimation(40.0, 240.0, WIDTH, HEIGHT, Upgrade.Upgrade1);
                super.addAnimation(80.0, 240.0, WIDTH, HEIGHT, Upgrade.Upgrade2);
                break;
                
            case Tower8:
                //this tower will have no upgrade option
                break;
                
            default:
                throw new Exception("Tower type not setup here " + type.toString());
        }
        
        //add range animation(s)
        super.addAnimation(0,   320, 400, 400, RangeKey.Default);
        super.addAnimation(400, 320, 400, 400, RangeKey.Poision);
        super.addAnimation(800, 320, 400, 400, RangeKey.Freeze);

        //assign default animation
        super.setAnimation(Upgrade.Default);
    }
    
    /**
     * Can this tower slow down the enemy
     * @return true=yes, false=no
     */
    public boolean canFreeze()
    {
        return this.freeze;
    }
    
    /**
     * Can this tower poison the enemy
     * @return true=yes, false=no
     */
    public boolean canPoison()
    {
        return this.poison;
    }
    
    public void setTagret(final Enemy enemy)
    {
        this.enemy = enemy;
    }
    
    public Enemy getTarget()
    {
        return this.enemy;
    }
    
    /**
     * Get the range.
     * @return The distance of cells which this tower can detect an enemy
     */
    public double getRange()
    {
        return Range.values()[getIndexRange()].getRange();
    }
    
    /**
     * Get the amount of damage the tower places
     * @return The damage
     */
    public int getDamage()
    {
        return Damage.values()[getIndexDamage()].getDamage();
    }
    
    /**
     * Get the cost of the next upgrade
     * @return The cost of the next upgrade
     */
    public int getCostUpgrade()
    {
        return this.upgrade;
    }
    
    /**
     * Get the value of selling the tower
     * @return The value of selling the tower
     */
    public int getCostSell()
    {
        return this.sell;
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
        
        //Update the timer length to the current rate setting
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
     * Get the index range of tower
     * @return The index representing the level for this attribute
     */
    public int getIndexRange()
    {
        return this.indexRange;
    }
    
    /**
     * Get the index tower damage
     * @return The index representing the level for this attribute
     */
    public int getIndexDamage()
    {
        return this.indexDamage;
    }
    
    /**
     * Get the index rate of fire
     * @return The index representing the level for this attribute
     */
    public int getIndexRate()
    {
        return this.indexRate;
    }
    
    /**
     * Get the index upgrade
     * @return The number of upgrades performed
     */
    public int getIndexUpgrade()
    {
        return this.indexUpgrade;
    }
    
    /**
     * Has the upgrade reached the max.
     * @param indexUpgrade The specific upgrade level to check
     * @return true if the indexUpgrade has reached the (UPGRADE_COUNT_LIMIT - 1), false otherwise
     */
    public boolean hasUpgradeMax(final int indexUpgrade)
    {
        return (indexUpgrade >= UPGRADE_COUNT_LIMIT - 1);
    }
            
    /**
     * Has the tower been upgraded to the allowed max
     * @return true if the tower upgrade count has reached the (UPGRADE_COUNT_LIMIT - 1), false otherwise
     */
    public boolean hasUpgradeMax()
    {
        return hasUpgradeMax(getIndexUpgrade());
    }
    
    /**
     * Can we upgrade the tower?<br>
     * Some towers are not allowed to upgrade
     * @return true=yes upgrades can be bought, false=no they cannot :)
     */
    public boolean isUpgradable()
    {
        switch (getType())
        {
            case Tower4:
            case Tower5:
            case Tower8:
                return false;
                
            default:
                return true;
        }
    }
    
    /**
     * Upgrade the tower.<br>
     */
    public void upgrade()
    {
        //if we can't upgrade this tower
        if (!isUpgradable())
            return;
        
        //if we have already reached the limit, don't continue
        if(hasUpgradeMax())
            return;
        
        //increase upgrade count
        this.indexUpgrade++;
        
        //set the correct animation for the tower
        super.setAnimation(Upgrade.values()[getIndexUpgrade()]);
        
        //increase the level of each attribute
        setLevelDamage(getIndexDamage() + 1);
        setLevelRange(getIndexRange() + 1);
        setLevelRate(getIndexRate() + 1);
        
        //make sure we don't exceed the maximum
        if (getIndexRange() > UPGRADE_MAXIMUM_LEVEL)
            setLevelRange(UPGRADE_MAXIMUM_LEVEL);
        if (getIndexRate() > UPGRADE_MAXIMUM_LEVEL)
            setLevelRate(UPGRADE_MAXIMUM_LEVEL);
        if (getIndexDamage() > UPGRADE_MAXIMUM_LEVEL)
            setLevelDamage(UPGRADE_MAXIMUM_LEVEL);
        
        //increase the sell value and upgrade cost
        this.sell = getCostSell() + (int)Math.round(getCostSell() * SELL_INCREASE_RATIO);
        this.upgrade = getCostUpgrade() + (int)Math.round(getCostUpgrade() * UPGRADE_INCREASE_RATIO);
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
    
    /**
     * Draw the towers range animation
     * @param graphics Object used to create final Image
     * @param image Image containing animations
     * @throws Exception if issue with render
     */
    protected void renderRange(final Graphics graphics, final Image image) throws Exception
    {
        //store the tower information
        final double x = getX();
        final double y = getY();
        final double w = getWidth();
        final double h = getHeight();
        final Object key = getSpriteSheet().getCurrent();

        //the x,y location of the tower's center
        final int x1 = (int)Map.getStartX(getCol());
        final int y1 = (int)Map.getStartY(getRow());

        //the reach of the range
        final int w1 = (int)(getRange() * Map.WIDTH) * 2;
        
        //assign tower coordinates and dimensions
        setX(x1 - (w1 / 2));
        setY(y1 - (w1 / 2));

        //assign size of animation
        setWidth(w1);
        setHeight(w1);

        //set the appropriate animation
        if (canFreeze())
        {
            setAnimation(Tower.RangeKey.Freeze);
        }
        else if (canPoison())
        {
            setAnimation(Tower.RangeKey.Poision);
        }
        else
        {
            setAnimation(Tower.RangeKey.Default);
        }

        //draw the range
        draw(graphics, image);

        //now restore the tower info
        setLocation(x, y);
        setDimensions(w, h);
        setAnimation(key);
    }
}
package com.gamesbykevin.towerdefense.player.ui.menu.mini;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.entity.tower.Tower;
import com.gamesbykevin.towerdefense.level.map.Map;
import java.awt.Color;

import java.awt.Rectangle;

/**
 * The mini-menu to display when the user clicks on a tower
 * @author GOD
 */
public final class TowerMenu extends MiniMenu implements Disposable
{
    //the dimension of the menu
    private static final int WIDTH = 250;
    private static final int HEIGHT = 225;
    
    //the starting point of the upper left attribute
    private static final int ATTRIBUTE1_X = 110;
    private static final int ATTRIBUTE1_Y = 15;
    
    //the pixel spacing between each attribute
    private static final int ATTRIBUTE_OFFSET_X = 25;
    private static final int ATTRIBUTE_OFFSET_Y = 35;
    
    //start location for text description
    private static final int DESCRIPTION1_X = 30;
    private static final int DESCRIPTION1_Y = 30;
    
    //space between each description
    private static final int DESCRIPTION_OFFSET_Y = 35;
    
    //the tower for this mini-menu
    private Tower tower;
    
    //show the active upgrade icon
    private boolean active = false;
    
    public enum Key
    {
        Background(0,0, 250, 225),
        StatGreen(19, 225, 19,26), 
        StatYellow(0, 225, 19,26), 
        StatRed(38, 225, 19,26),
        ActiveUpgrade(57, 225, 48, 48),
        InActiveUpgrade(185, 165, 48, 48),
        TowerSell(185, 115, 48, 48);
        
        //location of key on sprite sheet
        private final Rectangle location;
        
        private Key(final int x, final int y, final int w, final int h)
        {
            this.location = new Rectangle(x, y, w, h);
        }
        
        protected Rectangle getLocation()
        {
            return this.location;
        }
    }
    
    public TowerMenu() throws Exception
    {
        super(WIDTH, HEIGHT);
    }
    
    public void setActiveUpgradeIcon(final boolean active)
    {
        this.active = active;
    }
    
    public boolean hasActiveUpgradeIcon()
    {
        return this.active;
    }

    /**
     * Is the specified location within the provided window
     * @param x x-coordinate 
     * @param y y-coordinate 
     * @param window The area of the tower menu we want to check
     * @return true if the coordinate is within the window, false otherwise
     */
    private boolean contains(final double x, final double y, final Rectangle window)
    {
        //verify x-coordinate is within boundary
        if (x >= getX() + window.getX() && x <= getX() + window.getX() + window.getWidth())
        {
            //verify y-coordinate is within boundary
            if (y >= getY() + window.getY() && y <= getY() + window.getY() + window.getHeight())
            {
                //location is within window, return true
                return true;
            }
        }
        
        //the location was not within the window
        return false;
    }
    
    /**
     * Is the specified location within the sell icon?
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if the coordinate is within the sell tower icon boundary, false otherwise
     */
    public boolean containsTowerSell(final double x, final double y)
    {
        //returns result
        return contains(x, y, Key.TowerSell.getLocation());
    }
    
    /**
     * Is the specified location within the upgrade icon?
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if the coordinate is within the upgrade tower icon boundary and can still upgrade, false otherwise
     */
    public boolean containsTowerUpgrade(final double x, final double y)
    {
        //always return false if this tower has reached the max allowed
        if (tower.hasUpgradeMax())
            return false;
        
        //return result
        return contains(x, y, Key.InActiveUpgrade.getLocation());
    }
    
    /**
     * Get the tower tied to this menu
     * @return The tower tied to this menu
     */
    public Tower getTower()
    {
        return this.tower;
    }
    
    /**
     * Assign the tower to this menu
     * @param tower The tower we want to display in the mini-menu
     */
    public void assignTower(final Tower tower)
    {
        this.tower = tower;
        
        //if tower exists assign menu location
        if (tower != null)
        {
            //check if menu should be placed on west or east side
            if (tower.getX() < Map.START_X + (Map.COLS / 2) * Map.WIDTH)
            {
                //add menu to right side
                super.setX(tower.getX() + (Map.WIDTH / 2));
            }
            else
            {
                //add menu to left side
                super.setX(tower.getX() - WIDTH - (Map.WIDTH / 2));
            }
            
            //check if menu should be placed on north or south side
            if (tower.getY() < Map.START_Y + (Map.ROWS / 2) * Map.HEIGHT)
            {
                //add menu to south side
                super.setY(tower.getY() + (Map.HEIGHT / 2));
            }
            else
            {
                //add menu to north side
                super.setY(tower.getY() - HEIGHT - (Map.HEIGHT / 2));
            }
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    public void render() throws Exception
    {
        //draw background first
        draw(getGraphics2D(), getImage(), Key.Background.getLocation());
        
        //set color
        getGraphics2D().setColor(Color.BLACK);
        
        //draw text description
        getGraphics2D().drawString("Rate",   DESCRIPTION1_X, DESCRIPTION1_Y + (DESCRIPTION_OFFSET_Y * 0));
        getGraphics2D().drawString("Damage", DESCRIPTION1_X, DESCRIPTION1_Y + (DESCRIPTION_OFFSET_Y * 1));
        getGraphics2D().drawString("Range",  DESCRIPTION1_X, DESCRIPTION1_Y + (DESCRIPTION_OFFSET_Y * 2));
        
        //sell price
        getGraphics2D().drawString("Sell $" + tower.getCostSell(), DESCRIPTION1_X, DESCRIPTION1_Y + (int)(DESCRIPTION_OFFSET_Y * 3.25));
        
        //if reached the limit or are a tower that is upgradable
        if (tower.hasUpgradeMax() || !tower.isUpgradable())
        {
            //upgrade cost
            getGraphics2D().drawString("Upgrade: N/A", DESCRIPTION1_X, DESCRIPTION1_Y + (int)(DESCRIPTION_OFFSET_Y * 4.75));
        }
        else
        {
            //upgrade cost
            getGraphics2D().drawString("Upgrade $" + tower.getCostUpgrade(), DESCRIPTION1_X, DESCRIPTION1_Y + (int)(DESCRIPTION_OFFSET_Y * 4.75));
        }
        
        //assign proper dimensions
        super.setDimensions(Key.StatGreen.getLocation());
        
        //display the attributes here
        for (int i = 0; i < Tower.UPGRADE_MAXIMUM_LEVEL; i++)
        {
            final Key key;
            
            if (i <= 1)
            {
                key = Key.StatRed;
            }
            else if (i >= 2 && i <= 3)
            {
                key = Key.StatYellow;
            }
            else
            {
                key = Key.StatGreen;
            }
            
            //assign x-coordinate
            setX(ATTRIBUTE1_X + (ATTRIBUTE_OFFSET_X * i));
            
            if (i <= tower.getIndexDamage())
            {
                setY(ATTRIBUTE1_Y + (ATTRIBUTE_OFFSET_Y * 1));
                
                //draw stats
                draw(getGraphics2D(), getImage(), key.getLocation());
            }
            
            if (i <= tower.getIndexRange())
            {
                setY(ATTRIBUTE1_Y + (ATTRIBUTE_OFFSET_Y * 2));
                
                //draw stats
                draw(getGraphics2D(), getImage(), key.getLocation());
            }
            
            if (i <= tower.getIndexRate())
            {
                setY(ATTRIBUTE1_Y + (ATTRIBUTE_OFFSET_Y * 0));
                
                //draw stats
                draw(getGraphics2D(), getImage(), key.getLocation());
            }
        }
        
        //if we have not upgraded to the max, and have the money to upgrade
        if (!tower.hasUpgradeMax() && hasActiveUpgradeIcon())
        {
            //set the location to draw
            setX(Key.InActiveUpgrade.getLocation().getX());
            setY(Key.InActiveUpgrade.getLocation().getY());
            
            //assign proper dimensions
            setWidth(Key.InActiveUpgrade.getLocation().getWidth());
            setHeight(Key.InActiveUpgrade.getLocation().getHeight());
            
            //now draw the icon
            draw(getGraphics2D(), getImage(), Key.ActiveUpgrade.getLocation());
        }
    }
}
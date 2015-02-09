package com.gamesbykevin.towerdefense.player.ui.menu.mini;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.entity.tower.Tower;
import java.awt.Color;

import java.awt.Graphics;
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
    
    public enum Key
    {
        Background(0,0, 250, 225),
        StatGreen(19, 225, 19,26), 
        StatYellow(0, 225, 19,26), 
        StatRed(38, 225, 19,26),
        ActiveUpgrade(57, 225, 48, 48),
        InActiveUpgrade(135, 165, 48, 48),
        TowerSell(135, 115, 48, 48);
        
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
            super.setX(tower.getX());
            super.setY(tower.getY());
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    protected void renderImage() throws Exception
    {
        //draw background first
        draw(getGraphics(), getImage(), Key.Background.getLocation());
        
        //assign the font
        getGraphics().setFont(getFont());
        
        //set color
        getGraphics().setColor(Color.BLACK);
        
        //draw text description
        getGraphics().drawString("Rate",   DESCRIPTION1_X, DESCRIPTION1_Y + (DESCRIPTION_OFFSET_Y * 0));
        getGraphics().drawString("Damage", DESCRIPTION1_X, DESCRIPTION1_Y + (DESCRIPTION_OFFSET_Y * 1));
        getGraphics().drawString("Range",  DESCRIPTION1_X, DESCRIPTION1_Y + (DESCRIPTION_OFFSET_Y * 2));
        
        //sell price
        getGraphics().drawString("Sell $", DESCRIPTION1_X, DESCRIPTION1_Y + (int)(DESCRIPTION_OFFSET_Y * 3.25));
        
        //if reached the limit display max
        if (tower.getIndexUpgrade() >= Tower.UPGRADE_COUNT_LIMIT - 1)
        {
            //upgrade cost
            getGraphics().drawString("Upgrade: N/A", DESCRIPTION1_X, DESCRIPTION1_Y + (int)(DESCRIPTION_OFFSET_Y * 4.75));
        }
        else
        {
            //upgrade cost
            getGraphics().drawString("Upgrade $", DESCRIPTION1_X, DESCRIPTION1_Y + (int)(DESCRIPTION_OFFSET_Y * 4.75));
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
                draw(getGraphics(), getImage(), key.getLocation());
            }
            
            if (i <= tower.getIndexRange())
            {
                setY(ATTRIBUTE1_Y + (ATTRIBUTE_OFFSET_Y * 2));
                
                //draw stats
                draw(getGraphics(), getImage(), key.getLocation());
            }
            
            if (i <= tower.getIndexRate())
            {
                setY(ATTRIBUTE1_Y + (ATTRIBUTE_OFFSET_Y * 0));
                
                //draw stats
                draw(getGraphics(), getImage(), key.getLocation());
            }
        }
    }
}
package com.gamesbykevin.towerdefense.player.ui.menu.mini;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.player.ui.menu.main.MainMenu;

import java.awt.Rectangle;

/**
 * This class manages the element locations for the mini menus
 * @author GOD
 */
public abstract class MiniMenu extends MainMenu implements Disposable
{
    protected enum MenuKey
    {
        Background(0, 0, 100, 100), 
        
        HealthLeftYellow(400, 52, 6, 26), 
        HealthMiddleYellow(399, 262, 16, 26), 
        HealthRightYellow(402, 340, 6, 26), 
        HealthLeftRed(400, 130, 6, 26), 
        HealthMiddleRed(386, 340, 16, 26), 
        HealthRightRed(400, 156, 6, 26), 
        HealthLeftGreen(400, 105, 6, 26), 
        HealthMiddleGreen(386, 366, 16, 26), 
        HealthRightGreen(400, 26, 6, 26),
        HealthLeftOutline(400, 182, 6, 26), 
        HealthMiddleOutline(386, 314, 16, 26), 
        HealthRightOutline(400, 208, 6, 26);
        
        //location of key on sprite sheet
        private final Rectangle location;
        
        private MenuKey(final int x, final int y, final int w, final int h)
        {
            this.location = new Rectangle(x, y, w, h);
        }
        
        protected Rectangle getLocation()
        {
            return this.location;
        }
    }
    
    protected MiniMenu(final int width, final int height) throws Exception
    {
        super(width, height);
    }
}
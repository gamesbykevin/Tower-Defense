package com.gamesbykevin.towerdefense.player.ui.menu.mini;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.player.ui.menu.mini.MiniMenu;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The mini-menu to display when the user selects the enemy
 * @author GOD
 */
public final class EnemyMenu extends MiniMenu implements Disposable
{
    //dimensions of menu
    private static final int WIDTH = 250;
    private static final int HEIGHT = 75;
    
    //the ratio for each which will determine on the graphic to display
    private static final double HEALTH_MED = .5;
    private static final double HEALTH_HI = .75;
    
    //start location for the health
    private static final int HEALTH_START_X = 30;
    private static final int HEALTH_START_Y = 40;
    private static final int HEALTH_WIDTH = 190;
    private static final int HEALTH_HEIGHT = 26;
    
    //text description
    private static final int HEALTH_DESC_X = 30;
    private static final int HEALTH_DESC_Y = 30;
    
    //the enemy for this menu
    private Enemy enemy;
    
    public enum Key
    {
        Background(0,300, 250, 75),
        HealthRed(30, 375, 190, 26),
        HealthGreen(30, 401, 190, 26),
        HealthYellow(30, 427, 190, 26);
        
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
    
    public EnemyMenu() throws Exception
    {
        super(WIDTH, HEIGHT);
    }
    
    /**
     * Assign the enemy to this menu
     * @param enemy The enemy we want to display in the menu
     */
    public void assignEnemy(final Enemy enemy)
    {
        this.enemy = enemy;
        
        //if enemy exists assign menu location
        if (enemy != null)
        {
            super.setX(enemy.getX());
            super.setY(enemy.getY());
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    protected void renderImage()
    {
        //draw background first
        draw(getGraphics(), getImage(), Key.Background.getLocation());
        
        //assign the font
        getGraphics().setFont(getFont());
        
        //set color
        getGraphics().setColor(Color.BLACK);
        
        //draw text description
        getGraphics().drawString("Health: " + enemy.getHealth(), HEALTH_DESC_X, HEALTH_DESC_Y);
        
        //get the health ratio
        final double ratio = enemy.getHealth() / enemy.getStartHealth();
        
        //set location
        super.setX(HEALTH_START_X);
        super.setY(HEALTH_START_Y);
        
        //set dimensions
        super.setWidth(HEALTH_WIDTH * ratio);
        super.setHeight(HEALTH_HEIGHT);
        
        //use the appropriate animation depending on remaining health
        if (ratio >= HEALTH_HI)
        {
            draw(getGraphics(), getImage(), Key.HealthGreen.getLocation());
        }
        else if (ratio >= HEALTH_MED)
        {
            draw(getGraphics(), getImage(), Key.HealthYellow.getLocation());
        }
        else
        {
            draw(getGraphics(), getImage(), Key.HealthRed.getLocation());
        }
    }
}
package com.gamesbykevin.towerdefense.player.ui.menu.mini;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.player.ui.menu.mini.MiniMenu;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.UUID;

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
    
    //the unique enemy id we are targeting for this menu
    private UUID enemyId;
    
    //the current and start health of our targeted enemy
    private double currentHealth;
    private double startingHealth;
    
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
     * Keep the enemy health up to date
     * @param enemy The enemy to update
     */
    public void updateEnemyStats(final Enemy enemy)
    {
        //if the enemy does not exist
        if (enemy == null)
        {
            //hide the menu
            super.setVisible(false);
            
            //do not continue
            return;
        }
        
        //flag change if the enemy health is different
        if (enemy.getHealth() != this.currentHealth)
            setChange(true);
        
        this.currentHealth = enemy.getHealth();
        this.startingHealth = enemy.getStartHealth();
    }
    
    /**
     * The id of the enemy we are targeting
     * @return The unique key of the enemy we want to target
     */
    public UUID getEnemyId()
    {
        return this.enemyId;
    }
    
    /**
     * Assign the enemy id and position to this menu
     * @param enemy The enemy to be displayed on the menu
     */
    public void assignPosition(final Enemy enemy)
    {
        //assign the unique id
        this.enemyId = enemy.getId();
        
        //update the stats
        updateEnemyStats(enemy);
        
        //check if menu should be placed on west or east side
        if (enemy.getX() < (Map.COLS / 2) * Map.WIDTH)
        {
            //add menu to right side
            super.setX(enemy.getX() + Map.WIDTH);
        }
        else
        {
            //add menu to left side
            super.setX(enemy.getX() - WIDTH);
        }

        //check if menu should be placed on north or south side
        if (enemy.getY() < (Map.ROWS / 2) * Map.HEIGHT)
        {
            //add menu to south side
            super.setY(enemy.getY() + Map.HEIGHT);
        }
        else
        {
            //add menu to north side
            super.setY(enemy.getY() - HEIGHT);
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
        getGraphics2D().drawString("Enemy Health", HEALTH_DESC_X, HEALTH_DESC_Y);
        
        //get the health ratio
        final double ratio = (double)this.currentHealth / (double)this.startingHealth;
        
        //set location
        super.setX(HEALTH_START_X);
        super.setY(HEALTH_START_Y);
        
        //set dimensions
        super.setWidth(HEALTH_WIDTH * ratio);
        super.setHeight(HEALTH_HEIGHT);
        
        //keep width at least 1 pixel
        if (super.getWidth() < 1)
            super.setWidth(1);
        
        //use the appropriate animation depending on remaining health
        if (ratio >= HEALTH_HI)
        {
            draw(getGraphics2D(), getImage(), Key.HealthGreen.getLocation());
        }
        else if (ratio >= HEALTH_MED)
        {
            draw(getGraphics2D(), getImage(), Key.HealthYellow.getLocation());
        }
        else
        {
            draw(getGraphics2D(), getImage(), Key.HealthRed.getLocation());
        }
    }
}
package com.gamesbykevin.towerdefense.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entity;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.entity.tower.Tower;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.player.ui.menu.mini.EnemyMenu;
import com.gamesbykevin.towerdefense.player.ui.menu.mini.TowerMenu;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

/**
 * The class containing the player's info and UI components
 * @author GOD
 */
public final class Player extends Sprite implements Disposable, IElement
{
    private TowerMenu towerMenu;
    private EnemyMenu enemyMenu;
    
    public Player(final Image miniMenu, final Font font) throws Exception
    {
        //super.setImage(image);
        
        //create menu for the towers
        this.towerMenu = new TowerMenu();
        this.towerMenu.setImage(miniMenu);
        this.towerMenu.setFont(font.deriveFont(14f));
        
        this.enemyMenu = new EnemyMenu();
        this.enemyMenu.setImage(miniMenu);
        this.enemyMenu.setFont(font.deriveFont(14f));
    }
    
    public EnemyMenu getEnemyMenu()
    {
        return this.enemyMenu;
    }
    
    public TowerMenu getTowerMenu()
    {
        return this.towerMenu;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    public void update(final Engine engine)
    {
        if (engine.getMouse().isMouseReleased())
        {
            //get the location of the mouse
            final double x = engine.getMouse().getLocation().getX();
            final double y = engine.getMouse().getLocation().getY();
            
            //if the tower menu is visible
            if (getTowerMenu().isVisible())
            {
                //check if the user selected an option in the mini-menu
                if (getTowerMenu().getRectangle().contains(x, y))
                {
                    
                }
                else
                {
                    //we did not click in the menu, hide it
                    getTowerMenu().setVisible(false);
                }
            }
            else if (getEnemyMenu().isVisible())
            {
                if (getEnemyMenu().getRectangle().contains(x, y))
                {
                    
                }
                else
                {
                    //we did not click in the menu, hide it
                    getEnemyMenu().setVisible(false);
                }
            }
            else
            {
                //determine our position
                final double col = (x - Map.START_X) / Map.WIDTH;
                final double row = (y - Map.START_Y) / Map.HEIGHT;

                //get the tower based on the position
                Tower tower = engine.getManager().getTowers().getTower(col, row);

                //assign tower accordingly
                getTowerMenu().assignTower(tower);

                //determine if the menu is visible
                getTowerMenu().setVisible(tower != null);
                
                //if tower is not made visible, check if enemy menu should be visible
                if (!getTowerMenu().isVisible())
                {
                    //get the enemy based on the position
                    Enemy enemy = engine.getManager().getEnemies().getEnemy(col, row);

                    //assign the enemy to the menu
                    getEnemyMenu().assignEnemy(enemy);

                    //determine if the menu is visible
                    getEnemyMenu().setVisible(enemy != null);
                }
            }
            
            //reset mouse released
            engine.getMouse().reset();
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //draw tower menu
        getTowerMenu().render(graphics);
        
        //draw enemy menu
        getEnemyMenu().render(graphics);
    }
}
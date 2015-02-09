package com.gamesbykevin.towerdefense.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
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
            if (this.towerMenu.isVisible())
            {
                //check if the user selected an option in the mini-menu
                if (this.towerMenu.getRectangle().contains(x, y))
                {
                    
                }
                else
                {
                    //we did not click in the menu, hide it
                    this.towerMenu.setVisible(false);
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
                this.towerMenu.assignTower(tower);

                //determine if the tower is visible
                this.towerMenu.setVisible((tower != null));
            }
            
            //reset mouse released
            engine.getMouse().reset();
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        this.towerMenu.render(graphics);
    }
}
package com.gamesbykevin.towerdefense.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.entity.tower.Tower;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.menu.CustomMenu;
import com.gamesbykevin.towerdefense.player.ui.menu.main.UIMenu;
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
    private UIMenu uimenu;
    
    public Player(final Image miniMenu, final Image mainmenu, final Font font) throws Exception
    {
        //create menu this
        this.towerMenu = new TowerMenu();
        this.towerMenu.setImage(miniMenu);
        this.towerMenu.setFont(font.deriveFont(14f));
        
        //create menu this
        this.enemyMenu = new EnemyMenu();
        this.enemyMenu.setImage(miniMenu);
        this.enemyMenu.setFont(font.deriveFont(14f));
        
        //create main menu
        this.uimenu = new UIMenu();
        this.uimenu.setImage(mainmenu);
        this.uimenu.setFont(font.deriveFont(16f));
        this.uimenu.setX(704);
        this.uimenu.setY(0);
        
        //make visible and flag change
        this.uimenu.setVisible(true);
        this.uimenu.setChange(true);
    }
    
    public UIMenu getUIMenu()
    {
        return this.uimenu;
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
        
        this.uimenu.dispose();
        this.uimenu = null;
        
        this.enemyMenu.dispose();
        this.enemyMenu = null;
        
        this.towerMenu.dispose();
        this.towerMenu = null;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
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
                    if (getTowerMenu().containsTowerSell(x, y))
                    {
                        //sell the tower
                        getUIMenu().setFunds(getUIMenu().getFunds() + getTowerMenu().getTower().getCostSell());
                        
                        //remove the tower
                        engine.getManager().getTowers().remove(getTowerMenu().getTower());
                        
                        //no longer display menu
                        getTowerMenu().setVisible(false);
                        
                        //also flag change
                        getTowerMenu().setChange(true);
                        getUIMenu().setChange(true);
                    }
                    else if (getTowerMenu().containsTowerUpgrade(x, y))
                    {
                        //make sure the player has enough funds
                        if (getUIMenu().getFunds() >= getTowerMenu().getTower().getCostUpgrade())
                        {
                            //only upgrade the tower if it is upgradable
                            if (getTowerMenu().getTower().isUpgradable())
                            {
                                //subtract from our funds
                                getUIMenu().setFunds(getUIMenu().getFunds() - getTowerMenu().getTower().getCostUpgrade());

                                //upgrade the tower
                                engine.getManager().getTowers().upgrade(getTowerMenu().getTower());

                                //check if we have enough funds for another upgrade regardless if we reached upgrade limit
                                getTowerMenu().setActiveUpgradeIcon((getUIMenu().getFunds() >= getTowerMenu().getTower().getCostUpgrade()) ? true : false);

                                //also flag change
                                getTowerMenu().setChange(true);
                                getUIMenu().setChange(true);
                            }
                        }
                    }
                }
                else
                {
                    //we did not click in the menu, hide it
                    getTowerMenu().setVisible(false);
                    
                    //no assigned tower
                    engine.getManager().getTowers().setAssigned(null);
                }
            }
            else if (getEnemyMenu().isVisible())
            {
                if (!getEnemyMenu().getRectangle().contains(x, y))
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

                //only check tower menu and enemy menu if we have no tower selection
                if (!getUIMenu().hasTowerSelection())
                {
                    //get the tower based on the position
                    Tower tower = engine.getManager().getTowers().getTower(col, row);

                    //assign tower accordingly
                    getTowerMenu().assignTower(tower);

                    if (tower != null)
                    {
                        //tower exists, display menu
                        getTowerMenu().setVisible(true);

                        if (getUIMenu().getFunds() >= getTowerMenu().getTower().getCostUpgrade())
                        {
                            //only allow upgrades if upgradable
                            getTowerMenu().setActiveUpgradeIcon(getTowerMenu().getTower().isUpgradable());
                        }
                        else
                        {
                            //disable upgrade icon
                            getTowerMenu().setActiveUpgradeIcon(false);
                        }
                        
                        //flag change
                        getTowerMenu().setChange(true);
                    }
                    else
                    {
                        //no tower selected, do not display
                        getTowerMenu().setVisible(false);

                        //get the enemy based on the position to see if the enemy menu should be visible
                        Enemy enemy = engine.getManager().getEnemies().getEnemy(col, row);

                        //assign the enemy to the menu if exists
                        if (enemy != null)
                            getEnemyMenu().assignPosition(enemy);

                        //determine if the menu is visible
                        getEnemyMenu().setVisible(enemy != null);
                    }
                }
                
                //if the tower menu and enemy menu are not visible lets check the main UI menu
                if (!getTowerMenu().isVisible() && !getEnemyMenu().isVisible())
                {
                    //if no tower currently selected
                    if (!getUIMenu().hasTowerSelection())
                    {
                        //check to see what selection was made
                        if (getUIMenu().performTowerSelection(x, y))
                        {
                            //possibly play sound effect here
                        }
                        else if (getUIMenu().performAudioSelection(x, y))
                        {
                            //switch the audio selection
                            engine.getMenu().switchAudioEnabled(engine.getResources());
                            
                            //set correct icon
                            getUIMenu().setAudioEnabled(engine.getResources().isAudioEnabled());
                        }
                        else if (getUIMenu().hasStartSelection(x, y))
                        {
                            //if we haven't already started
                            if (!getUIMenu().hasStarted())
                            {
                                getUIMenu().setStart(true);
                            }
                        }
                        else if (getUIMenu().performMenuSelection(x, y))
                        {
                            //open menu since icon was selected
                            engine.getMenu().setLayer(CustomMenu.LayerKey.OptionsInGame);
                        }
                    }
                    else
                    {
                        //we have a selection, lets see if it can be placed in terms of the map
                        if (engine.getManager().getMap().isValid(col, row))
                        {
                            //now lets see if it does not conflict with the other towers
                            if (engine.getManager().getTowers().isValid(col, row))
                            {
                                //add tower at location
                                engine.getManager().getTowers().add(getUIMenu().getTowerSelection().getTowerType(), col, row);

                                //complete transaction
                                getUIMenu().completeTransaction();
                                
                                //play placement sound effect
                                
                            }
                            else
                            {
                                //play invalid sound effect
                                
                            }
                        }
                        else
                        {
                            //if we have a tower selection and the mouse was released over the menu
                            if (getUIMenu().getRectangle().contains(x, y))
                            {
                                //reset the tower selection
                                getUIMenu().resetTowerSelection();
                            }
                        }
                    }
                }
            }
            
            //reset mouse released
            engine.getMouse().reset();
            
            //reset keyboard
            engine.getKeyboard().reset();
        }
        else if (engine.getMouse().hasMouseMoved())
        {
            //get the location of the mouse
            final double x = engine.getMouse().getLocation().getX();
            final double y = engine.getMouse().getLocation().getY();
            
            //determine our position
            final double col = (x - Map.START_X) / Map.WIDTH;
            final double row = (y - Map.START_Y) / Map.HEIGHT;

            if (getUIMenu().hasTowerSelection())
            {
                //set the new position
                getUIMenu().getTowerSelection().setLocation(x, y);
                getUIMenu().getTowerSelection().setValid(engine.getManager().getMap().isValid(col, row) && engine.getManager().getTowers().isValid(col, row));
            }
        }
        
        //if the enemy menu is visible, make sure the stats are up to date
        if (getEnemyMenu().isVisible())
        {
            //the enemy we want to target
            Enemy enemy = engine.getManager().getEnemies().getEnemy(getEnemyMenu().getEnemyId());
            
            if (enemy != null)
            {
                //update the enemy stats
                getEnemyMenu().updateEnemyStats(enemy);
            }
            else
            {
                //if the enemy was not found, we should not display the menu
                getEnemyMenu().setVisible(false);
            }
        }
        
        //if the player has started
        if (getUIMenu().hasStarted())
        {
            //check if timer has not yet passed
            if (!getUIMenu().getTimer().hasTimePassed())
            {
                //update timer
                getUIMenu().getTimer().update(engine.getMain().getTime());

                //flag change
                getUIMenu().setChange(true);
                
                //set time to 0 if time has passed
                if (getUIMenu().getTimer().hasTimePassed())
                {
                    //set time left to 0
                    getUIMenu().getTimer().setRemaining(0);
                    
                    //flag wave to start
                    engine.getManager().getEnemies().setStart(true);
                }
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //draw tower menu
        if (getTowerMenu() != null)
            getTowerMenu().render(graphics);
        
        //draw enemy menu
        if (getEnemyMenu() != null)
            getEnemyMenu().render(graphics);
        
        //draw the main menu
        if (getUIMenu() != null)
            getUIMenu().render(graphics);
    }
}
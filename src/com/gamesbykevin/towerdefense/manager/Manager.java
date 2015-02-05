package com.gamesbykevin.towerdefense.manager;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.tower.Tower;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.entity.enemy.Enemies;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.entity.tower.Towers;
import com.gamesbykevin.towerdefense.menu.CustomMenu;
import com.gamesbykevin.towerdefense.menu.CustomMenu.*;
import com.gamesbykevin.towerdefense.resources.GameAudio;
import com.gamesbykevin.towerdefense.resources.GameFont;
import com.gamesbykevin.towerdefense.resources.GameImages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //where gameplay occurs
    private Rectangle window;
    
    //map of level
    private Map map;
    
    //the object containing the towers in play
    private Towers towers;
    
    //the object containing the enemies in the game
    private Enemies enemies;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine Engine for our game that contains all objects needed
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //set the audio depending on menu setting
        engine.getResources().setAudioEnabled(engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Sound) == CustomMenu.SOUND_ENABLED);
        
        //set the game window where game play will occur
        setWindow(engine.getMain().getScreen());
    }
    
    @Override
    public void reset(final Engine engine) throws Exception
    {
        if (map == null)
        {
            map = new Map(engine.getResources().getGameImage(GameImages.Keys.Road));
        }
        
        if (enemies == null)
        {
            enemies = new Enemies(engine.getResources().getGameImage(GameImages.Keys.Enemies));
        }
        
        enemies.add(Enemy.Type.Boss1, 5, 5);
        
        if (towers == null)
        {
            towers = new Towers(engine.getResources().getGameImage(GameImages.Keys.Towers));
        }
        
        //add default tower for testing
        towers.add(Tower.Type.Tower1, 0.5, 0.5);
        
        /*
        towers.add(Tower.Type.Tower2, 0.5, 0.5);
        towers.add(Tower.Type.Tower3, 4.5, 0.5);
        towers.add(Tower.Type.Tower4, 2.5, 5.5);
        towers.add(Tower.Type.Tower5, 8.5, 6.5);
        towers.add(Tower.Type.Tower6, 5.65, 07.5);
        towers.add(Tower.Type.Tower7, 5.5, 3.5);
        */
    }
    
    @Override
    public Rectangle getWindow()
    {
        return this.window;
    }
    
    @Override
    public void setWindow(final Rectangle window)
    {
        this.window = new Rectangle(window);
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        if (window != null)
            window = null;
        
        if (map != null)
        {
            map.dispose();
            map = null;
        }
        
        if (towers != null)
        {
            towers.dispose();
            towers = null;
        }
        
        if (enemies != null)
        {
            enemies.dispose();
            enemies = null;
        }
        
        try
        {
            //recycle objects
            super.finalize();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Update all elements
     * @param engine Our game engine
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (map != null)
        {
            map.update(engine);
        }
        
        if (towers != null)
        {
            towers.update(engine);
        }
        
        if (enemies != null)
        {
            enemies.update(engine);
        }
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        if (map != null)
        {
            map.render(graphics);
        }
        
        if (towers != null)
        {
            towers.render(graphics);
        }
        
        if (enemies != null)
        {
            enemies.render(graphics);
        }
    }
}
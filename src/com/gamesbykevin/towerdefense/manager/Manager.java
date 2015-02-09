package com.gamesbykevin.towerdefense.manager;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.tower.Tower;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.entity.effects.Effects;
import com.gamesbykevin.towerdefense.entity.enemy.Enemies;
import com.gamesbykevin.towerdefense.entity.projectile.Projectiles;
import com.gamesbykevin.towerdefense.entity.tower.Towers;
import com.gamesbykevin.towerdefense.menu.CustomMenu;
import com.gamesbykevin.towerdefense.menu.CustomMenu.*;
import com.gamesbykevin.towerdefense.player.Player;
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
 * The class that contains all of the game elements
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
    
    //object containing the projectiles in the game
    private Projectiles projectiles;
    
    //object containing the effects in the game
    private Effects effects;
    
    //the user controlling the game
    private Player player;
    
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
        if (this.map == null)
            this.map = new Map(engine.getResources().getGameImage(GameImages.Keys.Road));
        
        if (this.enemies == null)
            this.enemies = new Enemies(engine.getResources().getGameImage(GameImages.Keys.Enemies));
        
        if (this.towers == null)
            this.towers = new Towers(engine.getResources().getGameImage(GameImages.Keys.Towers));
        
        //add default tower for testing
        getTowers().add(Tower.Type.Tower1, .5, .5);
        
        if (this.projectiles == null)
            this.projectiles = new Projectiles(engine.getResources().getGameImage(GameImages.Keys.Projectiles));
        
        if (this.effects == null)
            this.effects = new Effects(engine.getResources().getGameImage(GameImages.Keys.Effects));
        
        if (this.player == null)
        {
            this.player = new Player(
                engine.getResources().getGameImage(GameImages.Keys.MiniMenu), 
                engine.getResources().getGameFont(GameFont.Keys.MiniMenu));
            
            this.player.getTowerMenu().assignTower(getTowers().getTower(0));
        }
        
        //add default tower for testing
        //getTowers().add(Tower.Type.Tower1, .5, .5);
        
        //getEnemies().add(Enemy.Type.Boss1, 5, 5);
        
        /*
        getTowers().add(Tower.Type.Tower2, 0.5, 0.5);
        getTowers().add(Tower.Type.Tower3, 4.5, 0.5);
        getTowers().add(Tower.Type.Tower4, 2.5, 5.5);
        getTowers().add(Tower.Type.Tower5, 8.5, 6.5);
        getTowers().add(Tower.Type.Tower6, 5.65, 07.5);
        getTowers().add(Tower.Type.Tower7, 5.5, 3.5);
        */
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
    
    public Map getMap()
    {
        return this.map;
    }
    
    public Enemies getEnemies()
    {
        return this.enemies;
    }
    
    public Towers getTowers()
    {
        return this.towers;
    }
    
    public Projectiles getProjectiles()
    {
        return this.projectiles;
    }
    
    public Effects getEffects()
    {
        return this.effects;
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
        
        if (projectiles != null)
        {
            projectiles.dispose();
            projectiles = null;
        }
        
        if (player != null)
        {
            player.dispose();
            player = null;
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
        if (getMap() != null)
            getMap().update(engine);
        
        if (getTowers() != null)
            getTowers().update(engine);
        
        if (getEnemies() != null)
            getEnemies().update(engine);
        
        if (getProjectiles() != null)
            getProjectiles().update(engine);
        
        if (getEffects() != null)
            getEffects().update(engine);
        
        if (getPlayer() != null)
            getPlayer().update(engine);
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        if (getMap() != null)
            getMap().render(graphics);
        
        if (getTowers() != null)
            getTowers().render(graphics);
        
        if (getEnemies() != null)
            getEnemies().render(graphics);
        
        if (getProjectiles() != null)
            getProjectiles().render(graphics);
        
        if (getEffects() != null)
            getEffects().render(graphics);
        
        if (getPlayer() != null)
            getPlayer().render(graphics);
    }
}
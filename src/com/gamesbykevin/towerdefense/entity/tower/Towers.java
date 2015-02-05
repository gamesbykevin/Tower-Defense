package com.gamesbykevin.towerdefense.entity.tower;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.level.map.Map;
import com.gamesbykevin.towerdefense.resources.GameImages;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will manage all towers in the game
 * @author GOD
 */
public final class Towers extends Sprite implements Disposable, IElement
{
    private List<Tower> towers;
    
    public Towers(final Image image)
    {
        super.setImage(image);
        
        //create new list for the towers
        this.towers = new ArrayList<>();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        for (int i = 0; i < towers.size(); i++)
        {
            towers.get(i).dispose();
            towers.set(i, null);
        }
        
        towers.clear();
        towers = null;
    }
    
    /**
     * Add tower
     */
    public void add(final Tower.Type type, final double col, final double row) throws Exception
    {
        //create a new tower
        Tower tower = new Tower(type);
        
        //set position
        tower.setCol(col);
        tower.setRow(row);
        
        //set the coordinates
        tower.setX(Map.START_X + (col * Map.WIDTH));
        tower.setY(Map.START_Y + (row * Map.HEIGHT));
        
        //add tower to list
        towers.add(tower);
    }
    
    @Override
    public void update(final Engine engine)
    {
        if (super.getImage() == null)
            super.setImage(engine.getResources().getGameImage(GameImages.Keys.Towers));
        
        for (int i = 0; i < towers.size(); i++)
        {
            //update the current tower
            Tower tower = towers.get(i);
            
            //update tower timer
            tower.getTimer().update(engine.getMain().getTime());
            
            //if time passed, check if the tower can detect an enemy to attack
            if (tower.getTimer().hasTimePassed())
            {
                //reset the timer as well
                tower.getTimer().reset();
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        if (super.getImage() == null)
            return;
        
        for (int i = 0; i < towers.size(); i++)
        {
            //update the current tower
            Tower tower = towers.get(i);
            
            //draw tower
            tower.render(graphics, getImage());
        }
    }
}
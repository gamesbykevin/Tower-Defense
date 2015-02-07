package com.gamesbykevin.towerdefense.entity.tower;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
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
public final class Towers extends Entities implements Disposable, IElement
{
    public Towers(final Image image)
    {
        super.setImage(image);
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
        tower.setX(Map.getStartX(col));
        tower.setY(Map.getStartY(row));
        
        //add to list
        getEntities().add(tower);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update parent
        super.updateEntities(engine.getMain().getTime());

        //additional update logic here
        for (int i = 0; i < getEntities().size(); i++)
        {
            //update the current tower
            Tower tower = (Tower)getEntities().get(i);
            
            
            tower.setAngle(tower.getAngle() + Math.toRadians(1d));
            
            if (tower.getAngle() > Math.PI * 2)
                tower.setAngle(tower.getAngle() - (Math.PI * 2));
        }
        
    }
}
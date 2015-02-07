package com.gamesbykevin.towerdefense.entity.projectile;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.entity.Entity;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will contain all projectiles in play
 * @author GOD
 */
public final class Projectiles extends Entities  implements Disposable, IElement
{
    public Projectiles(final Image image)
    {
        super.setImage(image);
    }
    
    /**
     * Add projectile
     */
    public void add(final Projectile.Type type, final double col, final double row) throws Exception
    {
        //create a new tower
        Projectile projectile = new Projectile(type);
        
        //set position
        projectile.setCol(col);
        projectile.setRow(row);
        
        //add tower to list
        getEntities().add(projectile);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update parent
        super.updateEntities(engine.getMain().getTime());
        
        //additional update logic here
        for (int i = 0; i < getEntities().size(); i++)
        {
            //update the current projectile
            Projectile projectile = (Projectile)getEntities().get(i);
            
        }
    }
}
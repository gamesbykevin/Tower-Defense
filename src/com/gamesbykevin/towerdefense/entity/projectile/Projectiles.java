package com.gamesbykevin.towerdefense.entity.projectile;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Image;
import java.util.Random;

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
     * @param random Object used to pick a random projectile type
     * @param start Start
     * @param goal Goal 
     * @param angle facing angle
     * @throws Exception 
     */
    public void add(final Random random, final Cell start, final Cell goal, final double angle) throws Exception
    {
        add(Projectile.Type.values()[random.nextInt(Projectile.Type.values().length)], start, goal, angle);
    }
    
    /**
     * Add projectile
     * @param type The type of animation
     * @param start Start
     * @param goal Goal 
     * @param angle facing angle
     * @throws Exception 
     */
    public void add(final Projectile.Type type, final Cell start, final Cell goal, final double angle) throws Exception
    {
        //create a new tower
        Projectile projectile = new Projectile(type, goal);
        
        //set position
        projectile.setCol(start);
        projectile.setRow(start);
        
        //set the facing angle
        projectile.setAngle(angle);
        
        //add to list
        add(projectile);
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
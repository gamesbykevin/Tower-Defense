package com.gamesbykevin.towerdefense.entity.projectile;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.entity.tower.Tower;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Image;

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
     * @param tower The projectile source
     * @throws Exception 
     */
    public void add(final Tower tower) throws Exception
    {
        //create a new tower
        Projectile projectile = new Projectile(tower.getType().getProjectileType(), tower.getDamage(), tower.getTarget().getId());
        
        //set start position
        projectile.setStart(tower);
        
        //set the facing angle
        projectile.setAngle(tower.getAngle());
        
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
            
            //get the targeted enemy
            final Enemy enemy = engine.getManager().getEnemies().getEnemy(projectile.getTargetId());
            
            //if the projectile timer has finished, or if the enemy no longer exists
            if (projectile.getTimer().hasTimePassed() || enemy == null)
            {
                if (enemy != null)
                {
                    //deduct damage from enemy
                    enemy.setHealth(enemy.getHealth() - projectile.getDamage());
                }
                
                //remove from list
                remove(projectile);
                
                //reduce the index count
                i--;
            }
            else
            {
                //update the location based on the timer progress
                projectile.updateLocation(engine.getMain().getTime(), enemy);
            }
        }
    }
}
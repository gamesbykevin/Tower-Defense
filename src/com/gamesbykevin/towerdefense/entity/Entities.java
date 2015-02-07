package com.gamesbykevin.towerdefense.entity;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * All entities will be managed in a similar format
 * @author GOD
 */
public abstract class Entities extends Sprite implements Disposable, IElement
{
    //our object used to apply rotation to entities
    private AffineTransform at;
    
    //list of entites in this collection
    private List<Entity> entities;
    
    protected Entities()
    {
        //create new list of entites
        this.entities = new ArrayList<>();
        
        //create new instance
        this.at = new AffineTransform();
    }
    
    @Override
    public final void dispose()
    {
        super.dispose();
        
        for (int i = 0; i < entities.size(); i++)
        {
            if (entities.get(i) != null)
            {
                entities.get(i).dispose();
                entities.set(i, null);
            }
        }
        
        entities.clear();
        entities = null;
        
        at = null;
    }
    
    /**
     * Get the entities
     * @return The list of entities
     */
    protected final List<Entity> getEntities()
    {
        return this.entities;
    }
    
    /**
     * Add Entity to list
     * @param entity The entity we want to add
     */
    protected void add(final Entity entity)
    {
        getEntities().add(entity);
    }
    
    /**
     * Remove the entity from our collection
     * @param entity The entity we want to remove
     * @throws Exception if entity is not found
     */
    protected final void remove(final Entity entity) throws Exception
    {
        //check each entity until we find the one
        for (int i = 0; i < getEntities().size(); i++)
        {
            //each object has a unique id upon creation, if these match then we found it
            if (getEntities().get(i).getId() == entity.getId())
            {
                //remove from list
                getEntities().remove(i);
                
                //exit 
                return;
            }
        }
        
        //throw exception, because it should have been found and removed
        throw new Exception("Entity not found and was not removed id=" + entity.getId());
    }
    
    /**
     * Update the basic elements of each entity (location and animation)
     * @param time The time to deduct, which will update the animation
     * @throws Exception 
     */
    public void updateEntities(final long time) throws Exception
    {
        for (int i = 0; i < getEntities().size(); i++)
        {
            //update the current entity
            Entity entity = getEntities().get(i);
            
            //update the entities animation
            entity.getSpriteSheet().update(time);
            
            //update the location
            entity.update();
        }
    }
    
    @Override
    public final void render(final Graphics graphics) throws Exception
    {
        //if the image does not exist there is nothing to draw
        if (super.getImage() == null)
            throw new Exception("Image does not exist so nothing can be drawn here");
        
        //cast to Graphics2D so we can apply rotation
        Graphics2D g2d = (Graphics2D)graphics;
        
        for (int i = 0; i < getEntities().size(); i++)
        {
            //get current entity
            Entity entity = getEntities().get(i);
            
            //rotate at this anchor position
            at.rotate(entity.getAngle(), entity.getX(), entity.getY());
        
            //assign transform
            g2d.setTransform(at);
        
            //draw entty
            entity.render(graphics, getImage());
            
            //reset rotation
            at.setToIdentity();
        
            //restore original transform
            g2d.setTransform(at);
        }
    }
}
package com.gamesbykevin.towerdefense.entity;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * All entities will be managed in a similar format
 * @author GOD
 */
public abstract class Entities extends Sprite implements Disposable, IElement
{
    private List<Entity> entities;
    
    protected Entities()
    {
        this.entities = new ArrayList<>();
    }
    
    @Override
    public final void dispose()
    {
        super.dispose();
        
        for (int i = 0; i < entities.size(); i++)
        {
            entities.get(i).dispose();
            
            entities.set(i, null);
        }
        
        entities.clear();
        entities = null;
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
        
        for (int i = 0; i < getEntities().size(); i++)
        {
            //draw entity
            getEntities().get(i).render(graphics, getImage());
        }
    }
}
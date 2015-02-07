package com.gamesbykevin.towerdefense.entity.effects;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.shared.IElement;
import java.awt.Image;

/**
 * Object containing all of the Effects in the game
 * @author GOD
 */
public final class Effects extends Entities implements Disposable, IElement
{
    public Effects(final Image image)
    {
        super.setImage(image);
    }
    
    /**
     * Add enemy
     */
    public void add(final Effect.Type type, final double col, final double row) throws Exception
    {
        //create a new effect
        Effect effect = new Effect(type);
        
        //set position
        effect.setCol(col);
        effect.setRow(row);
        
        //add to list
        getEntities().add(effect);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update parent
        super.updateEntities(engine.getMain().getTime());
        
        //additional update logic here
        for (int i = 0; i < getEntities().size(); i++)
        {
            //update the current enemy
            Effect effect = (Effect)getEntities().get(i);
            
        }
    }
}
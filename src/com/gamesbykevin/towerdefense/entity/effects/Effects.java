package com.gamesbykevin.towerdefense.entity.effects;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.engine.Engine;
import com.gamesbykevin.towerdefense.entity.Entities;
import com.gamesbykevin.towerdefense.shared.IElement;

import java.awt.Image;
import java.util.Random;

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
     * Add effect of random type at location
     * @param random Object used to pick random effect type
     * @param start Location
     * @throws Exception 
     */
    public void add(final Random random, final Cell start) throws Exception
    {
        add(Effect.Type.values()[random.nextInt(Effect.Type.values().length)], start.getCol(), start.getRow());
    }
    
    /**
     * Add effect 
     * @param type The type of effect to add
     * @param start Location
     * @throws Exception 
     */
    public void add(final Effect.Type type, final Cell start) throws Exception
    {
        add(type, start.getCol(), start.getRow());
    }
    
    /**
     * Add effect
     */
    public void add(final Effect.Type type, final double col, final double row) throws Exception
    {
        //create a new effect
        Effect effect = new Effect(type);
        
        //set position
        effect.setCol(col);
        effect.setRow(row);
        
        //add to list
        add(effect);
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
            
            //once the animation is finished 
            if (effect.getSpriteSheet().hasFinished())
            {
                //remove from list
                remove(effect);
                
                //move index back 1
                i--;
            }
        }
    }
}
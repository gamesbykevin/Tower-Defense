package com.gamesbykevin.towerdefense.level.object;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

/**
 * Every object in the game is a level object
 * @author GOD
 */
public abstract class LevelObject extends Sprite implements Disposable
{
    /**
     * Add animation to level object
     * @param animation Animation we want to add
     * @param key Unique key identifying this animation
     * @throws Exception If there is a problem setting default animation
     */
    protected void addAnimation(final Animation animation, final String key) throws Exception
    {
        //if sprite sheet has not been created yet
        if (getSpriteSheet() == null)
            createSpriteSheet();
        
        //add animation
        super.getSpriteSheet().add(animation, key);
        
        //if no current animation is set default
        if (getSpriteSheet().getCurrent() == null)
        {
            //set this as the current animation
            super.getSpriteSheet().setCurrent(key);
            
            //set the height/width
            super.setDimensions();
        }
    }
    
    @Override
    public abstract void dispose();
}
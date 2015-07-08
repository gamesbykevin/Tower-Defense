package com.gamesbykevin.towerdefense.level.object;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import java.awt.Rectangle;

/**
 * Every object in the game is a level object
 * @author GOD
 */
public abstract class LevelObject extends Sprite implements Disposable
{
    /**
     * no unique key value
     */
    public static final Object NO_KEY = "";
    
    /**
     * no time delay default value
     */
    public static final long NO_DELAY = 0;
    
    /**
     * Add single animation, that will not loop or have a unique key for identification.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     * @throws Exception If there is a problem creating animation
     */
    protected void addAnimation(final double x, final double y, final double w, final double h) throws Exception
    {
        addAnimation((int)x, (int)y, (int)w, (int)h);
    }
    
    /**
     * Add single animation, that will not loop
     * @param location The coordinates of the image
     * @param key Unique key to identify the animation
     * @throws Exception If there is a problem creating animation
     */
    protected void addAnimation(final Rectangle location, final Object key) throws Exception
    {
        addAnimation(location.getX(), location.getY(), location.getWidth(), location.getHeight(), key);
    }
    
    /**
     * Add single animation, that will not loop
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     * @param key Unique key to identify the animation
     * @throws Exception If there is a problem creating animation
     */
    protected void addAnimation(final double x, final double y, final double w, final double h, final Object key) throws Exception
    {
        addAnimation((int)x, (int)y, (int)w, (int)h, key);
    }
    
    /**
     * Add single animation, that will not loop or have a unique key for identification.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     * @throws Exception If there is a problem creating animation
     */
    protected void addAnimation(final int x, final int y, final int w, final int h) throws Exception
    {
        addAnimation(x, y, w, h, NO_KEY);
    }
    
    /**
     * Add single animation, that will not loop
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     * @param key Unique key to identify the animation
     * @throws Exception If there is a problem creating animation
     */
    protected void addAnimation(final int x, final int y, final int w, final int h, final Object key) throws Exception
    {
        Animation animation = new Animation(x, y, w, h, NO_DELAY);
        //animation.add(x, y, w, h, NO_DELAY);
        animation.setLoop(false);
        
        //add animation
        addAnimation(animation, key);
    }
    
    /**
     * Add animation to level object
     * @param animation Animation we want to add
     * @throws Exception If there is a problem setting default animation
     */
    protected void addAnimation(final Animation animation) throws Exception
    {
        addAnimation(animation, NO_KEY);
    }
    
    /**
     * Add animation to level object
     * @param animation Animation we want to add
     * @param key Unique key identifying this animation
     * @throws Exception If there is a problem setting default animation
     */
    protected void addAnimation(final Animation animation, final Object key) throws Exception
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
            setAnimation(key);
            
            //set the height/width
            super.setDimensions();
        }
    }
    
    /**
     * Assign the current animation
     * @param key Unique key of the animation
     */
    public void setAnimation(final Object key) throws Exception
    {
        //set this as the current animation
        super.getSpriteSheet().setCurrent(key);
    }
}
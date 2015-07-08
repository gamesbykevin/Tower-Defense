package com.gamesbykevin.towerdefense.player.ui.menu.main;

import com.gamesbykevin.framework.awt.CustomImage;

import java.awt.Graphics;

/**
 * Every menu will contain these common elements
 * @author GOD
 */
public abstract class MainMenu extends CustomImage
{
    //do we show the mini menu
    private boolean visible = false;
    
    //flag a change was made to know if we need to render a new image
    private boolean change = true;
    
    protected MainMenu(final int width, final int height)
    {
        //call parent
        super(width, height);
    }
    
    /**
     * Is the menu to be displayed?
     * @return true if yes, false otherwise
     */
    public boolean isVisible()
    {
        return this.visible;
    }
    
    private boolean hasChange()
    {
        return this.change;
    }
    
    public void setChange(final boolean change)
    {
        this.change = change;
    }
    
    /**
     * Mark the menu to be displayed
     * @param visible true if yes, false otherwise
     */
    public void setVisible(final boolean visible)
    {
        this.visible = visible;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    /**
     * Children will need to implement how to draw the menu
     * @param graphics Object used to draw graphics
     */
    public void render(final Graphics graphics) throws Exception
    {
        //don't continue if not visible
        if (!isVisible())
            return;
        
        //if a change was made, render new image
        if (hasChange())
        {
            //store current location
            final double x = getX();
            final double y = getY();
            
            //store dimensions
            final double w = getWidth();
            final double h = getHeight();
            
            //clear image
            super.clear();
            
            //set position to 0,0
            super.setX(0);
            super.setY(0);
            
            //render new image
            render();
            
            //restore location
            super.setX(x);
            super.setY(y);
            
            //restore dimensions
            super.setWidth(w);
            super.setHeight(h);
            
            //flag false
            setChange(false);
        }
        
        //draw our buffered image
        super.draw(graphics, super.getBufferedImage());
    }
}
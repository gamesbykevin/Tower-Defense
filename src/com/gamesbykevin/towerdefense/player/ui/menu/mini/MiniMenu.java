package com.gamesbykevin.towerdefense.player.ui.menu.mini;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.towerdefense.level.object.LevelObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the common elements for the mini menus
 * @author GOD
 */
public abstract class MiniMenu extends LevelObject implements Disposable
{
    //do we show the mini menu
    private boolean visible = false;
    
    //flag a change was made to know if we need to render a new image
    private boolean change = true;
    
    //the font used for this menu
    private Font font;
    
    //buffered image to create our minimenu
    private BufferedImage bi;
    
    //graphics to draw the buffered image
    private Graphics2D g2d;
    
    //color with 100% transparency so you can't see it
    private static final Color TRANSPARENT_COLOR = new Color(0,0,0,0);
    
    protected enum MenuKey
    {
        Background(0, 0, 100, 100), 
        
        HealthLeftYellow(400, 52, 6, 26), 
        HealthMiddleYellow(399, 262, 16, 26), 
        HealthRightYellow(402, 340, 6, 26), 
        HealthLeftRed(400, 130, 6, 26), 
        HealthMiddleRed(386, 340, 16, 26), 
        HealthRightRed(400, 156, 6, 26), 
        HealthLeftGreen(400, 105, 6, 26), 
        HealthMiddleGreen(386, 366, 16, 26), 
        HealthRightGreen(400, 26, 6, 26),
        HealthLeftOutline(400, 182, 6, 26), 
        HealthMiddleOutline(386, 314, 16, 26), 
        HealthRightOutline(400, 208, 6, 26);
        
        //location of key on sprite sheet
        private final Rectangle location;
        
        private MenuKey(final int x, final int y, final int w, final int h)
        {
            this.location = new Rectangle(x, y, w, h);
        }
        
        protected Rectangle getLocation()
        {
            return this.location;
        }
    }
    
    protected MiniMenu(final int width, final int height) throws Exception
    {
        //set dimensions
        super.setDimensions(width, height);
        
        //create buffered image
        this.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        //get graphics object to draw image
        this.g2d = this.bi.createGraphics();
        
        //set transparent background
        this.g2d.setBackground(TRANSPARENT_COLOR);
    }
    
    public void setFont(final Font font)
    {
        this.font = font;
    }
    
    protected Font getFont()
    {
        return this.font;
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
        
        if (font != null)
            font = null;
    }
    
    /**
     * Get the graphics object for the buffered image
     * @return Object used to draw image
     */
    protected Graphics2D getGraphics()
    {
        return this.g2d;
    }
    
    /**
     * Children will need to create the image
     */
    protected abstract void renderImage() throws Exception;
    
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
            
            //clear image
            getGraphics().clearRect(0, 0, bi.getWidth(), bi.getHeight());
            
            //set position to 0,0
            super.setX(0);
            super.setY(0);
            
            //render new image
            renderImage();
            
            //restore location
            super.setX(x);
            super.setY(y);
            
            //restore dimensions
            super.setWidth(bi.getWidth());
            super.setHeight(bi.getHeight());
            
            //flag false
            setChange(false);
        }
        
        //draw our buffered image
        super.draw(graphics, bi);
    }
}
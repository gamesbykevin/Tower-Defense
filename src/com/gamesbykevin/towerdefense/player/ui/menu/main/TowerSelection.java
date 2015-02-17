package com.gamesbykevin.towerdefense.player.ui.menu.main;

import com.gamesbykevin.framework.awt.CustomImage;
import com.gamesbykevin.towerdefense.entity.tower.Tower;

import java.awt.Image;

/**
 * This class will be for the tower we attempt to place/purchase
 * @author GOD
 */
public final class TowerSelection extends CustomImage
{
    //is the tower currently in a valid location
    private boolean valid = false;
    
    //the tower selected
    private final UIMenu.PlaceKey key;
    
    public TowerSelection(final int width, final int height, final Image image, final UIMenu.PlaceKey key)
    {
        //call parent with specified dimensions
        super(width, height);
        
        //assign the tower type
        this.key = key;
        
        //assign image
        super.setImage(image);
    }
    
    public Tower.Type getTowerType()
    {
        return this.key.getType();
    }
    
    public void setValid(final boolean isValid)
    {
        //if there was a change, render a new image
        if (this.valid != isValid)
        {
            //store change
            this.valid = isValid;
            
            //render new image
            renderImage();
        }
        else
        {
            this.valid = isValid;
        }
    }
    
    @Override
    public void renderImage()
    {
        //clear image
        super.clear();
        
        //store settings
        final double x = getX();
        final double y = getY();
        final double w = getWidth();
        final double h = getHeight();
        
        //reset to origin
        super.setX(0);
        super.setY(0);
        
        //draw range animation
        if (valid)
        {
            draw(getGraphics2D(), getImage(), UIMenu.Key.RangeValid.getLocation());
        }
        else
        {
            draw(getGraphics2D(), getImage(), UIMenu.Key.RangeInvalid.getLocation());
        }
        
        //set tower dimensions
        super.setWidth(Tower.WIDTH);
        super.setHeight(Tower.HEIGHT);
        
        //set location
        super.setX((w / 2) - (Tower.WIDTH / 2));
        super.setY((h / 2) - (Tower.HEIGHT / 2));
        
        //draw tower on top of range
        super.draw(getGraphics2D(), getImage(), key.getType().getLocation());
        
        //restore settings
        super.setX(x);
        super.setY(y);
        super.setWidth(w);
        super.setHeight(h);
    }
}
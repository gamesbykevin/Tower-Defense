package com.gamesbykevin.towerdefense.player.ui.menu.main;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.towerdefense.entity.enemy.Enemy;
import com.gamesbykevin.towerdefense.entity.tower.Tower;
import com.gamesbykevin.towerdefense.level.map.Map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * The object representing the main menu
 * @author GOD
 */
public final class UIMenu extends MainMenu implements Disposable
{
    public enum Key
    {
        RangeValid(200, 0, 400, 400), 
        RangeInvalid(600, 0, 400, 400), 
        AudioUnmute(50, 0, 48, 48), 
        AudioMute(50, 48, 48, 48), 
        Play(50, 96, 48, 48), 
        Fast(50, 144, 48, 48),
        Menu(50, 240, 48, 48),
        Money(50, 288, 48, 48),
        Lives(50, 336, 48, 48);
        
        //location of key on sprite sheet
        private final Rectangle location;
        
        private Key(final int x, final int y, final int w, final int h)
        {
            this.location = new Rectangle(x, y, w, h);
        }
        
        public Rectangle getLocation()
        {
            return this.location;
        }
    }
    
    public enum PlaceKey
    {
        Tower1(LEFT_X, 275, Tower.Type.Tower1),
        Tower2(RIGHT_X, 275, Tower.Type.Tower2),
        Tower3(LEFT_X, 325, Tower.Type.Tower3),
        Tower4(RIGHT_X, 325, Tower.Type.Tower4),
        Tower5(LEFT_X, 375, Tower.Type.Tower5),
        Tower6(RIGHT_X, 375, Tower.Type.Tower6),
        Tower7(LEFT_X, 425, Tower.Type.Tower7),
        Tower8(RIGHT_X, 425, Tower.Type.Tower8);
        
        //location where it will be drawn
        private final int x, y;
        
        //which tower is it
        private final Tower.Type type;
        
        private PlaceKey(final int x, final int y, final Tower.Type type)
        {
            this.x = x;
            this.y = y;
            this.type = type;
        }
        
        private int getX()
        {
            return this.x;
        }
        
        private int getY()
        {
            return this.y;
        }
        
        public Tower.Type getType()
        {
            return this.type;
        }
    }
    
    //dimension of ui menu
    private static final int WIDTH = 256;
    private static final int HEIGHT = 512;
    
    //locations of items in menu
    private static final int LEFT_X = 10;
    private static final int RIGHT_X = 96;
    
    //how much money the player has
    private int funds = 10000;
    
    //the number of lives the player has
    private int lives = 100;
    
    //the current wave
    private int wave = 1;
    
    //where the lives icon will be drawn
    private static final Point LOCATION_LIVES = new Point(LEFT_X, 5);
    
    //where the money icon will be drawn
    private static final Point LOCATION_MONEY = new Point(LEFT_X, 50);
    
    //where we display the wave
    private static final Point LOCATION_WAVE = new Point(LEFT_X, 115);
    
    //where we display the audio icon
    private static final Point LOCATION_AUDIO = new Point(LEFT_X, 130);
    
    //where we display the speed icon
    private static final Point LOCATION_SPEED = new Point(RIGHT_X, 130);
    
    //where we display the menu icon
    private static final Point LOCATION_MENU = new Point(LEFT_X, 180);
    
    //the object representing the tower selection
    private TowerSelection towerSelection;
    
    //is audio enabled
    private boolean audioEnabled = true;
    
    //is the speed enabled
    private boolean speedEnabled = false;
    
    public UIMenu()
    {
        super(WIDTH, HEIGHT);
    }
    
    public void setAudioEnabled(final boolean enabled)
    {
        //if setting changed, flag change
        if (this.audioEnabled != enabled)
            setChange(true);
        
        this.audioEnabled = enabled;
    }
    
    public boolean hasSpeedEnabled()
    {
        return this.speedEnabled;
    }
    
    public void setSpeedEnabled(final boolean enabled)
    {
        //if setting changed, flag change
        if (this.speedEnabled != enabled)
            setChange(true);
        
        this.speedEnabled = enabled;
    }
    
    public boolean performAudioSelection(final double x, final double y)
    {
        //make sure within x coordinates
        if (x >= getX() + LOCATION_AUDIO.x && x <= getX() + LOCATION_AUDIO.x + Tower.WIDTH)
        {
            //make sure within y coordinates
            if (y >= getY() + LOCATION_AUDIO.y && y <= getY() + LOCATION_AUDIO.y + Tower.HEIGHT)
                return true;
        }
        
        //no selection was made
        return false;
    }
    
    public boolean performSpeedSelection(final double x, final double y)
    {
        //make sure within x coordinates
        if (x >= getX() + LOCATION_SPEED.x && x <= getX() + LOCATION_SPEED.x + Tower.WIDTH)
        {
            //make sure within y coordinates
            if (y >= getY() + LOCATION_SPEED.y && y <= getY() + LOCATION_SPEED.y + Tower.HEIGHT)
                return true;
        }
        
        //no selection was made
        return false;
    }
    
    public boolean performMenuSelection(final double x, final double y)
    {
        //make sure within x coordinates
        if (x >= getX() + LOCATION_MENU.x && x <= getX() + LOCATION_MENU.x + Tower.WIDTH)
        {
            //make sure within y coordinates
            if (y >= getY() + LOCATION_MENU.y && y <= getY() + LOCATION_MENU.y + Tower.HEIGHT)
                return true;
        }
        
        //no selection was made
        return false;
    }
    
    public TowerSelection getTowerSelection()
    {
        return this.towerSelection;
    }
    
    /**
     * Is there currently a tower selected
     * @return true = yes, otherwise false = no
     */
    public boolean hasTowerSelection()
    {
        return (getTowerSelection() != null);
    }
    
    /**
     * Check if the location is contained inside a tower.<br>
     * The selected type found will be assigned for display as long as funds available<br>
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if the location was inside a tower, false otherwise
     */
    public boolean performTowerSelection(final double x, final double y)
    {
        //check each tower to see if a selection was made
        for (int i = 0; i < PlaceKey.values().length; i++)
        {
            PlaceKey tmp = PlaceKey.values()[i];
            
            //is within x coordinates
            if (x >= getX() + tmp.getX() && x <= getX() + tmp.getX() + Tower.WIDTH)
            {
                if (y >= getY() + tmp.getY() && y <= getY() + tmp.getY() + Tower.HEIGHT)
                {
                    //make sure we can purchase the selection
                    if (getFunds() >= tmp.getType().getCostPurchase())
                    {
                        //calculate the dimensions of the range
                        final int width = (int)(Tower.Range.values()[tmp.getType().getLevelRange()].getRange() * Map.WIDTH) * 2;
                        
                        //create new tower selection
                        towerSelection = new TowerSelection(width, width, getImage(), tmp);
                        
                        //set the location
                        getTowerSelection().setLocation(x, y);
                        
                        //default to invalid location
                        getTowerSelection().setValid(false);
                        
                        //create image
                        getTowerSelection().renderImage();

                        //return true
                        return true;
                    }
                    else
                    {
                        //we don't have the funds to purchase
                        return false;
                    }
                }
            }
        }
        
        //no tower selected return false
        return false;
    }
    
    /**
     * Set the wave
     * @param num The assigned wave #
     */
    public void setWave(final int num)
    {
        //flag change
        if (this.wave != num)
            setChange(true);
        
        this.wave = num;
    }
    
    /**
     * Get the wave
     * @return The current wave we are on
     */
    public int getWave()
    {
        return this.wave;
    }
    
    /**
     * Get the lives
     * @return The number of lives the player has remaining
     */
    public int getLives()
    {
        return this.lives;
    }
    
    /**
     * Set the lives
     * @param totalLives The number of lives to assign the player
     */
    public void setLives(final int totalLives)
    {
        //flag change
        if (this.lives != totalLives)
            setChange(true);
        
        this.lives = totalLives;
    }
    
    /**
     * Deduct 1 life from total
     */
    public void deductLife()
    {
        setLives(getLives() - 1);
        
        //flag change to menu
        super.setChange(true);
    }
    
    /**
     * Get the funds
     * @return The amount of cash the player has to make purchases
     */
    public int getFunds()
    {
        return this.funds;
    }
    
    /**
     * Add the reward.<br>
     * The reward will increase with every wave completed
     * @param enemy The enemy destroyed that we are adding the reward to
     */
    public void addReward(final Enemy enemy)
    {
        this.setFunds(getFunds() + (enemy.getReward() * getWave()));
    }
    
    /**
     * Set the funds
     * @param cash The amount of cash the player should have
     */
    public void setFunds(final int cash)
    {
        //flag change
        if (this.funds != cash)
            setChange(true);
        
        this.funds = cash;
    }
    
    public void resetTowerSelection()
    {
        this.towerSelection = null;
    }
    
    /**
     * Deduct funds from the tower selected, also removes tower selection
     */
    public void completeTransaction()
    {
        //deduct purchase from funds
        setFunds(getFunds() - getTowerSelection().getTowerType().getCostPurchase());
        
        //we no longer have a tower selected
        resetTowerSelection();
        
        //flag change
        setChange(true);
    }
    
    @Override
    public void renderImage()
    {
        //set background color
        getGraphics2D().setColor(Color.BLACK);
        
        //fill background
        getGraphics2D().fillRect(0, 0, WIDTH, HEIGHT);
        
        //set color
        getGraphics2D().setColor(Color.WHITE);
        
        //set dimensions, all will be the same
        super.setWidth(Tower.WIDTH);
        super.setHeight(Tower.HEIGHT);
            
        for (int i = 0; i < PlaceKey.values().length; i++)
        {
            PlaceKey tmp = PlaceKey.values()[i];
            
            //set location
            setX(tmp.getX());
            setY(tmp.getY());
            
            //draw image
            draw(getGraphics2D(), getImage(), tmp.getType().getLocation());
            
            //draw cost of tower
            getGraphics2D().drawString("$" + tmp.getType().getCostPurchase(), (int)(getX() + getWidth()), (int)(getY() + (getHeight() / 2)));
        }
        
        //draw lives info
        drawLivesInfo();
        
        //draw money info
        drawMoneyInfo();
        
        //draw wave info
        drawWaveInfo();
        
        //draw audio info
        drawAudioInfo();
        
        //draw speed info
        drawSpeedInfo();
        
        //draw menu info
        drawMenuInfo();
    }
    
    private void drawLivesInfo()
    {
        //the the location and dimension
        super.setLocation(LOCATION_LIVES);
        
        //draw lives icon
        draw(getGraphics2D(), getImage(), Key.Lives.getLocation());
        
        //draw info
        getGraphics2D().drawString("" + getLives(), (int)(getX() + getWidth()) + 5, (int)(getY() + (getHeight() / 2)));
    }
    
    private void drawMenuInfo()
    {
        //draw menu icon
        setLocation(LOCATION_MENU);
        draw(getGraphics2D(), getImage(), Key.Menu.getLocation());
    }
    
    private void drawSpeedInfo()
    {
        //draw speed icon
        setLocation(LOCATION_SPEED);
        draw(getGraphics2D(), getImage(), (speedEnabled) ? Key.Fast.getLocation() : Key.Play.getLocation());
    }
    
    private void drawAudioInfo()
    {
        //draw audio icon
        setLocation(LOCATION_AUDIO);
        draw(getGraphics2D(), getImage(), (audioEnabled) ? Key.AudioUnmute.getLocation() : Key.AudioMute.getLocation());
    }
    
    private void drawMoneyInfo()
    {
        //draw the funds
        setLocation(LOCATION_MONEY);
        draw(getGraphics2D(), getImage(), Key.Money.getLocation());
        getGraphics2D().drawString("" + getFunds(), (int)(getX() + getWidth()) + 5, (int)(getY() + (getHeight() / 2)));
    }
    
    private void drawWaveInfo()
    {
        //draw wave info
        getGraphics2D().drawString("Wave :" + getWave(), LOCATION_WAVE.x, LOCATION_WAVE.y);
    }
    
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        //draw parent menu
        super.render(graphics);
        
        //if there is a tower selection we need to draw it
        if (hasTowerSelection())
        {
            final double x = getTowerSelection().getX();
            final double y = getTowerSelection().getY();
            
            //offset location
            getTowerSelection().setX(x - (getTowerSelection().getWidth() / 2));
            getTowerSelection().setY(y - (getTowerSelection().getHeight() / 2));
            
            //draw tower selection image
            getTowerSelection().draw(graphics, getTowerSelection().getBufferedImage());
            
            //restore location
            getTowerSelection().setX(x);
            getTowerSelection().setY(y);
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
}
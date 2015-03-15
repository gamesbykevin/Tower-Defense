package com.gamesbykevin.towerdefense.resources;

import com.gamesbykevin.framework.resources.*;

/**
 * All audio for game
 * @author GOD
 */
public final class GameAudio extends AudioManager
{
    //description for progress bar
    private static final String DESCRIPTION = "Loading Audio Resources";
    
    /**
     * These are the keys used to access the resources and need to match the id in the xml file
     */
    public enum Keys
    {
        Explosion1, Explosion2, Explosion3, Explosion4, Explosion5, Explosion6, Explosion7,
        Shoot1, Shoot2, Shoot3, Shoot4, Shoot5, Shoot6, Shoot7, Shoot8,
        
        Upgrade, Sell, Gameover, LoseLife, Ready, TowerPlaceInvalid, TowerPlaceValid, Victory
    }
    
    public GameAudio() throws Exception
    {
        super(Resources.XML_CONFIG_GAME_AUDIO);
        
        //the description that will be displayed for the progress bar
        super.setProgressDescription(DESCRIPTION);
        
        if (Keys.values().length < 1)
            super.increase();
    }
}
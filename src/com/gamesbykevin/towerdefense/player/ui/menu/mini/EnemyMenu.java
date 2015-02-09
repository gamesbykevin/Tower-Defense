package com.gamesbykevin.towerdefense.player.ui.menu.mini;

import com.gamesbykevin.towerdefense.player.ui.menu.mini.MiniMenu;
import com.gamesbykevin.framework.resources.Disposable;
import java.awt.Graphics;

/**
 * The mini-menu to display when the user selects the enemy
 * @author GOD
 */
public final class EnemyMenu extends MiniMenu implements Disposable
{
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    
    public EnemyMenu() throws Exception
    {
        super(WIDTH, HEIGHT);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    protected void renderImage()
    {
        //draw background first
        super.setAnimation(MenuKey.Background);
    }
}
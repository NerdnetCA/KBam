package com.rixonsoft.kbam.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * The westower splash/bootstrap scene.
 *
 * This is not really documented - best to keep your paws out of here.
 *
 */

public class SplashScreen extends BasicScene {
    private static final String TAG = "SPLASH";

    // Const
    public static final float DISPLAY_TIME = 1.2f;  // minimum display time.
    public static final float F_WIDTH = 800f;  // This version of Splash works in 800x480
    public static final float F_HEIGHT = 480f; // virtual resolution.

    // Private fields
    private Texture mySplash;
    private OrthographicCamera myCamera;
    private SpriteBatch myBatch;
    private float sx, sy;

    private float accTime;
    private boolean completed;

    @Override
    public void preload() {
        myGame.assetManager.load("brucie/splash.png",Texture.class);
    }
    @Override
    public void dispose() {
        if(myBatch != null) myBatch.dispose();
        myGame.assetManager.unload("brucie/splash.png");
        super.dispose();
    }


    public boolean isDone() {
        return completed;
    }

    @Override
    public void start() {
        myCamera = new OrthographicCamera();
        myCamera.setToOrtho(false,F_WIDTH,F_HEIGHT);
        myBatch = new SpriteBatch();

        //mySplash = manager.get(BrucieConfig.asset_splash_img,Texture.class);
        mySplash = myGame.assetManager.get("brucie/splash.png",Texture.class);

        sx = F_WIDTH/2f - mySplash.getWidth()/2f;
        sy = F_HEIGHT/2f - mySplash.getHeight()/2f;
        accTime = 0f;
    }

    @Override
    public void draw(float delta) {
        accTime += delta;

        if(accTime > DISPLAY_TIME) {
            if(!completed) {
                completed = true;
            }
        }

        Gdx.gl20.glClearColor(0f,.5f,.355f,1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myBatch.begin();
        if(accTime < 1f) {
            myBatch.setColor(1,1f,1f,1f * accTime);
        } else {
            myBatch.setColor(1,1f,1f,1);
        }
        myBatch.draw(mySplash,sx,sy);
        myBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        if(myCamera!=null)myCamera.update();
        myBatch.setProjectionMatrix(myCamera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

}

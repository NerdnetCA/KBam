package com.rixonsoft.kbam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.rixonsoft.kbam.lib.BasicScene;
import com.rixonsoft.kbam.lib.BrucieConfig;

public class KbamGame extends BasicScene {
	SpriteBatch batch;
	Texture img;
	Sprite sprite;

	@Override
	public void preload() {

	}

	@Override
	public void start () {
		batch = new SpriteBatch();
		img = new Texture("roses.png");
		sprite = new Sprite(img);

		// Set the "origin" of the sprite to a point at its centre
		sprite.setOrigin(sprite.getWidth()/2f,sprite.getHeight()/2f);
	}

	@Override
	public void draw(float delta) {

		// Rotate sprite by 60 degrees per second.
		// delta is the time elapsed in seconds.
		sprite.rotate(60 * delta);

		// Clear the screen to red
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw the sprite at its preferred location, with its preferred rotation.
		batch.begin();
		batch.draw(sprite,
				BrucieConfig.desktopWidth/2f-sprite.getWidth()/2f,
				BrucieConfig.desktopHeight/2f-sprite.getHeight()/2f,
				sprite.getOriginX(), sprite.getOriginY(),
				sprite.getWidth(), sprite.getHeight(),
				1f,1f,
				sprite.getRotation());
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}

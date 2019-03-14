package com.rixonsoft.kbam.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Utility Scene class, providing simple fade in and fade out.
 *
 *
 */


public abstract class BasicScene extends AbstractScene {
    private static final String TAG = "BASICSCENE";

    public static final float WIPE_TIME = 0.9f;

    protected StateMachine<BasicScene, BasicFadeyState> stateMachine;

    private ShapeRenderer fadeRenderer;
    private OrthographicCamera mCamera;
    private float mWidth=256f, mHeight=256f;
    private float wipeSpeedFactor;

    public float wipeTime;

    public void show() {
        super.show();
        wipeSpeedFactor = 1f/WIPE_TIME;
        mCamera = new OrthographicCamera();
        mCamera.setToOrtho(false,mWidth,mHeight);
        mCamera.update();
        fadeRenderer = new ShapeRenderer();
        stateMachine = new DefaultStateMachine<BasicScene, BasicFadeyState>(this, BasicFadeyState.FADEIN);
        beginFade();
    }

    public void resize(int screenWidth, int screenHeight) {
        //mCamera.update(); // Is this needed??
    }

    public void beginFade() {
        wipeTime = 0f;
    }

    public abstract void draw(float delta);

    public void render(float delta) {
        stateMachine.update();
    }

    public boolean renderFade(float delta) {
        mCamera.update();
        wipeTime += (delta * wipeSpeedFactor);

        float fadeAmount = stateMachine.getCurrentState().getFadeValue(wipeTime);

        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        fadeRenderer.setProjectionMatrix(mCamera.combined);
        fadeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        fadeRenderer.setColor(0f, 0f, 0f, fadeAmount);
        fadeRenderer.rect(0f, 0f, mWidth, mHeight);
        fadeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        if(wipeTime > 1f) {
            return true;
        }
        return false;
    }

    public void setFadeIn() {
        stateMachine.changeState(BasicFadeyState.FADEIN);
    }

    public void setFadeOut() {
        stateMachine.changeState(BasicFadeyState.FADEOUT);
    }

    public void pause() {}
    public void resume() {}

    public static enum BasicFadeyState implements State<BasicScene> {
        FADEIN() {
            @Override
            public float getFadeValue(float time) {
                if(time > 1.0f) time = 1.0f;
                return 1f-time;
            }

            @Override
            public void enter(BasicScene entity) {
                Gdx.app.log(TAG,"Entering fadein");
                entity.beginFade();
            }

            @Override
            public void update(BasicScene entity) {
                super.update(entity);
                if(entity.renderFade(Gdx.graphics.getDeltaTime())) {
                    entity.stateMachine.changeState(BasicFadeyState.NORMAL_RUN);
                }
            }
        },

        FADEOUT() {
            @Override
            public void enter(BasicScene entity) {
                entity.beginFade();
            }

            @Override
            public void update(BasicScene entity) {
                super.update(entity);
                if(entity.renderFade(Gdx.graphics.getDeltaTime())) {
                    entity.setDone(true);
                    entity.stateMachine.changeState(BasicFadeyState.DONE);
                }
            }
        },

        DONE() {
            @Override
            public float getFadeValue(float time) {
                return 1.0f;
            }
            @Override
            public void update(BasicScene entity) {
                super.update(entity);
                entity.renderFade(Gdx.graphics.getDeltaTime());
            }
        },

        NORMAL_RUN() {
        };

        private static final String TAG="SCENESTATE";

        @Override
        public void enter(BasicScene entity) {

        }

        @Override
        public void update(BasicScene entity) {
            entity.draw(Gdx.graphics.getDeltaTime());
        }

        @Override
        public void exit(BasicScene entity) {

        }

        @Override
        public boolean onMessage(BasicScene entity, Telegram telegram) {
            return false;
        }

        public float getFadeValue(float time) {
            if(time > 1.0f) time = 1.0f;
            return time;
        }
    }
}

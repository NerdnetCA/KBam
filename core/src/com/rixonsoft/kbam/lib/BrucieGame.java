package com.rixonsoft.kbam.lib;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public abstract class BrucieGame implements ApplicationListener {
    private static final String TAG = "BRUCIEGAME";

    // The global config and asset manager instances.
    // Classes within your game generally need to be able to find these.
    public BrucieConfig brucieConfig;
    public AssetManager assetManager;

    // BasicScene management
    protected SplashScreen splashScene;
    protected Scene currentScene, nextScene;

    protected StateMachine<BrucieGame, GameState> gameStateMachine;

    // Internals
    private boolean booted;

    @Override
    public void create() {
        brucieConfig = new BrucieConfig();
        assetManager = new AssetManager();
        assetManager.setLoader(
                TiledMap.class,
                new TmxMapLoader(new InternalFileHandleResolver())
        );
        gameStateMachine =
                new DefaultStateMachine<BrucieGame, GameState>(this, GameState.BOOTSPLASH);

        // load perma-assets
        assetManager.load(brucieConfig.loading_img,Texture.class);

        // load splash
        splashScene = new SplashScreen();
        splashScene.configure(this);
        splashScene.preload();

        assetManager.finishLoading();

        splashScene.show();
        splashScene.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    public void bootScene(Scene bootscene) {
        if(!booted) {
            nextScene = bootscene;
            nextScene.configure(this);
            nextScene.preload();
            booted = true;
        }
    }

    public void queueScene(Scene scene) {
        if(nextScene != null) {
            // The queue has a size limit of one.
        } else {
            nextScene = scene;
            gameStateMachine.changeState(GameState.PRELOAD);
        }
    }
    public void toNextScene() {
        if(currentScene != null)
            currentScene.hide();
        currentScene = nextScene;
        currentScene.show();
        resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        nextScene = null;
    }
    public void render() {
        gameStateMachine.update();
    }

    @Override
    public void resize(int width, int height) {
        if(currentScene != null) {
            currentScene.resize(width,height);
        }
    }

    public void pause() {}

    public void resume() {}


    public void dispose() {
        if(currentScene != null) currentScene.dispose();
        if(nextScene != null) nextScene.dispose();
        assetManager.dispose();
    }

    public static enum GameState implements State<BrucieGame> {

        BOOTSPLASH() {
            @Override
            public void update(BrucieGame game) {
                game.splashScene.draw(Gdx.graphics.getDeltaTime());
                if(game.assetManager.update() && game.splashScene.isDone()) {
                    game.toNextScene();
                    game.gameStateMachine.changeState(GameState.NORMAL_RUN);
                }
            }
        },

        PRELOAD() {
            @Override
            public void update(BrucieGame game) {
                super.update(game);
                boolean loaded = game.assetManager.update();
                if(loaded) {
                    if(game.currentScene.isDone()) {
                        game.toNextScene();
                        game.gameStateMachine.changeState(NORMAL_RUN);
                    } else {
                        game.gameStateMachine.changeState(GameState.CUE);
                    }
                } else {
                    if(game.currentScene.isDone()) {
                        game.currentScene.hide();
                        game.currentScene = new LoadingScreen();
                        game.currentScene.configure(game);
                        game.currentScene.show();
                        game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                        game.gameStateMachine.changeState(GameState.LOADING);
                    }
                }
            }

            @Override
            public void enter(BrucieGame game) {
                game.nextScene.configure(game);
                game.nextScene.preload();
            }
        },

        CUE() {
            @Override
            public void update(BrucieGame game) {
                super.update(game);
                if(game.currentScene.isDone()) {
                    game.toNextScene();
                    game.gameStateMachine.changeState(GameState.NORMAL_RUN);
                }
            }
        },

        LOADING() {
            @Override
            public void update(BrucieGame game) {
                super.update(game);
                if(game.assetManager.update()) {
                    game.toNextScene();
                    game.gameStateMachine.changeState(GameState.NORMAL_RUN);
                }
            }
        },

        NORMAL_RUN() {
        };

        @Override
        public void update(BrucieGame game) {
            game.currentScene.render(Gdx.graphics.getDeltaTime());
        }

        @Override
        public void enter(BrucieGame game) {

        }

        @Override
        public void exit(BrucieGame game) {

        }

        @Override
        public boolean onMessage(BrucieGame game, Telegram tele) {
            return false;
        }

    }
}

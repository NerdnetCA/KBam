package com.rixonsoft.kbam.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** Wrangler is a tool to help initialize and manage complex game objects.
 *
 * A wrangler is created by the default AbstractScene, and initialized with a
 * handle to the BrucieGame instance.
 *
 *
 */
public class Wrangler implements Disposable {
    private static final String TAG = "WRANGLER";

    private AssetManager assetManager;
    private final BrucieGame brucieGame;

    private Array<Disposable> disposables = new Array<Disposable>();

    public Wrangler(BrucieGame game) {
        assetManager = game.assetManager;
        brucieGame = game;
    }

    /** Initialize an instance of the given class.
     *
     * If the class implements Wrangled, call the new object's setGame method.
     *
     * If the class implements Disposable, add it to internal list of disposables,
     * to be freed when the wrangler itself is disposed.
     */
    public <T> T wrangle(Class<T> type) {
        try {
            T o = type.newInstance();
            if(o instanceof Wrangled) {
                ((Wrangled)o).setGame(brucieGame);
                if (o instanceof Disposable) {
                    disposables.add((Disposable)o);
                }
            } else if(o instanceof  Disposable) {
                disposables.add((Disposable)o);
            }
            return o;
        } catch (IllegalAccessException e) {
            Gdx.app.log(TAG,"IllegalAccessException wrangling "+type.getCanonicalName());
        } catch (InstantiationException e) {
            Gdx.app.log(TAG,"InstantiationException wrangling "+type.getCanonicalName());
            e.printStackTrace();
        }
        return null;
    }

    /** Add an object to disposal tracking.
     *
     * Add any Disposable to the scene's wrangler to ensure it is disposed with
     * the scene.
     *
     * @param obj
     */
    public void add(Disposable obj) {
        disposables.add(obj);
    }

    public void dispose() {
        for (Disposable d:disposables
             ) {
            d.dispose();
        }
    }
}

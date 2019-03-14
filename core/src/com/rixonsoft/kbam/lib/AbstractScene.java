package com.rixonsoft.kbam.lib;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** Bare-bones implementation of a Scene.
 *
 * This handles loading asset bags and instantiating the wrangler.
 */
public abstract class AbstractScene implements Scene {

    protected BrucieGame myGame;
    protected Wrangler wrangler;
    protected boolean done;

    private Array<AssetBag> assetBags = new Array<AssetBag>();

    public void addAssetBag(AssetBag bag) {
        assetBags.add(bag);
    }

    public <T extends AssetBag> T wrangleAssetBag(Class<T> clazz) {
        T bag=wrangler.wrangle(clazz);
        assetBags.add(bag);
        return bag;
    }

    public <T> T wrangle(Class<T> clazz) {
        T o = wrangler.wrangle(clazz);
        return o;
    }

    public void addDisposable(Disposable disposable) {
        wrangler.add(disposable);
    }

    public abstract void start();

    @Override
    public void show() {
        resolveBags();
        start();
    }

    protected void resolveBags() {
        for(AssetBag bag : assetBags) {
            bag.resolveAssets();
        }
    }


    @Override
    public void configure(BrucieGame game) {
        myGame = game;
        wrangler = new Wrangler(game);
    }

    /** Scenes are generally allowed to run until isDone returns false.
     * @return
     */
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }


    /** hide will call dispose. See dispose. If you override this, make sure to call super.
     *
     */
    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        wrangler.dispose();
    }
}

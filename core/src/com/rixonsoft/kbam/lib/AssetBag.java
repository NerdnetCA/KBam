package com.rixonsoft.kbam.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.Iterator;

public abstract class AssetBag implements Wrangled, Disposable {
    private static final String TAG = "ASSETBAG";

    protected AssetManager assetManager;

    private Array<String> assetList;

    public AssetBag() {
        assetList = new Array<String>();
    }

    public void setGame(BrucieGame game) {
        assetManager = game.assetManager;
        queueAssets();
    }

    public abstract void resolveAssets();
    public abstract void queueAssets();

    /**
     *
     * @param name
     * @param assetType
     */
    public void loadAsset(String name, Class assetType) {
        assetManager.load(name, assetType);
        assetList.add(name);
    }

    /** loadAsset, but with parameter.
     *
     * @param name
     * @param assetType
     * @param param
     */
    public void loadAsset(String name, Class assetType, AssetLoaderParameters param) {
        assetManager.load(name, assetType, param);
        assetList.add(name);
    }

    /**
     * dispose really REALLY needs to be called for Scenes, in order to mark
     * assets for unloading. The hide method will do this.
     */
    @Override
    public void dispose() {
        Gdx.app.log(TAG, "Disposing AssetBag " + getClass().getName());
        Iterator<String> iter = assetList.iterator();
        while(iter.hasNext()) {
            assetManager.unload(iter.next());
        }
    }
}

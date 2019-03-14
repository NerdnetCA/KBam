package com.rixonsoft.kbam.lib;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;

/** Interface for all game scenes.
 *
 *
 */
public interface Scene extends Screen, Disposable {


    /** A scene should return true when it is finished and the
     * next scene should be activated.
     * @return true if scene is done.
     */
    public boolean isDone();

    /** Called by the engine when creating the scene.
     *
     * Absolutely MUST be called right after being constructed.
     *
     * @param game
     */
    public void configure(BrucieGame game);

    /** Called by the engine after configure().
     *
     * Scenes should queue any asset loading required in this method.
     */
    public void preload();
}

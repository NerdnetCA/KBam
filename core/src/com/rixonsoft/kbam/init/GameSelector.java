package com.rixonsoft.kbam.init;

import com.badlogic.gdx.utils.ObjectMap;
import com.rixonsoft.kbam.KbamGame;
import com.rixonsoft.kbam.lib.*;

import java.lang.reflect.InvocationTargetException;

public class GameSelector extends BrucieGame {
    private int appNum =0;
    public void setAppNum(int appNum) { this.appNum = appNum; }

    private static Class[] sceneIndex = {
            KbamGame.class
    };


    @Override
    public void create() {
        super.create();

        if(appNum==0) {
            bootScene(new KbamGame());
        } else {
            try {
                bootScene((Scene)sceneIndex[appNum].getConstructor().newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return;
    }
}

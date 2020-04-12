package com.eastcom.harup.utils;

import com.eastcom.harup.BuildConfig;

public class SoundEffectUtil {

    public native static void soundFix(String path, int type);


    static {
        for (String lib : BuildConfig.FMOD_LIBS) {
            System.loadLibrary(lib);
        }
        System.loadLibrary("example");
    }

}

package com.eastcom.harup.utils;

import java.util.UUID;

public class PosixUtil {

    public static native void runOtherThread();

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }


    static {
        System.loadLibrary("posix");
    }
}

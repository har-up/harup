package com.example.ffmpeg;

import android.view.Surface;

public class FFmpegUtil {

    public native static void test(String inputStr, String outStr);


    public native static void play(String inputPath, Surface surface);

    static {
        System.loadLibrary("avutil-54");
        System.loadLibrary("swresample-1");
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avformat-56");
        System.loadLibrary("swscale-3");
        System.loadLibrary("postproc-53");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("myffmpeg");
    }
}

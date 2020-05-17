package com.example.ffmpeg;

import android.view.Surface;

public class FFmpegUtil {

    public native static void test(String inputStr, String outStr);


    public native static void play(String inputPath, Surface surface);

    static {
        System.loadLibrary("swresample");
        System.loadLibrary("avutil");
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("postproc");
        System.loadLibrary("avfilter");
        System.loadLibrary("avdevice");
        System.loadLibrary("player");
    }
}

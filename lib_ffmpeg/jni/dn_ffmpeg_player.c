#include "com_example_ffmpeg_FFmpegUtil.h"

#include <unistd.h>
#include <pthread.h>
#include <android/log.h>
#include <libavutil/avutil.h>
#include <libavutil/frame.h>
#include <libavcodec/avcodec.h>
#include <libavcodec/dv_profile.h>
//编码
#include "libavcodec/avcodec.h"
//封装格式处理
#include "libavformat/avformat.h"
//像素处理
#include "libswscale/swscale.h"

#include <android/native_window_jni.h>
#include <android/native_window.h>
#include <libyuv/libyuv/convert_argb.h>

#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"jason",FORMAT,##__VA_ARGS__);

JNIEXPORT void JNICALL Java_com_example_ffmpeg_FFmpegUtil_test
        (JNIEnv * env, jclass cls, jstring path, jstring type){

}
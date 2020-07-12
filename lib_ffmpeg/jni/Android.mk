LOCAL_PATH := $(call my-dir)

#ffmpeg lib
include $(CLEAR_VARS)
LOCAL_MODULE := avcodec
LOCAL_SRC_FILES := libavcodec.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avdevice
LOCAL_SRC_FILES := libavdevice.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avfilter
LOCAL_SRC_FILES := libavfilter.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avformat
LOCAL_SRC_FILES := libavformat.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avutil
LOCAL_SRC_FILES := libavutil.so
include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE := postproc
LOCAL_SRC_FILES := libpostproc.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swresample
LOCAL_SRC_FILES := libswresample.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swscale
LOCAL_SRC_FILES := libswscale.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := yuv
LOCAL_SRC_FILES := libyuv.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE            := player
LOCAL_SRC_FILES         := dn_ffmpeg_player.c test.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libyuv
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libavfrmat
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libavcodec
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libavdevice
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libavfilter
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libavfrmat
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libavfutil
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libpostproc
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libswresample
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include/libswscale
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include F:/android-ndk-r17b/sources/cxx-stl/llvm-libc++/include
LOCAL_LDLIBS +=-L$(SYSROOT)/usr/lib -lm -llog
##-landroid参数 for native windows
LOCAL_LDLIBS += -llog -landroid
LOCAL_SHARED_LIBRARIES := avcodec avdevice avfilter avformat avutil postproc swresample swscale yuv
include $(BUILD_SHARED_LIBRARY)

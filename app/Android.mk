LOCAL_PATH := $(call my-dir)
FMOD_API_ROOT := $(LOCAL_PATH)
#
# FMOD Shared Library
#

include $(CLEAR_VARS)

LOCAL_MODULE            := fmodL
LOCAL_SRC_FILES         := ${FMOD_API_ROOT}/jniLibs/$(TARGET_ARCH_ABI)/libfmod$(FMOD_LIB_SUFFIX).so
LOCAL_EXPORT_C_INCLUDES := src/main/cpp/inc

include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE            := fmod
LOCAL_SRC_FILES         := ${FMOD_API_ROOT}/jniLibs/$(TARGET_ARCH_ABI)/libfmod.so
LOCAL_EXPORT_C_INCLUDES := src/main/cpp/inc


include $(PREBUILT_SHARED_LIBRARY)

#
# Example Library
#
include $(CLEAR_VARS)

LOCAL_MODULE            := example
LOCAL_SRC_FILES         := src/main/cpp/common_platform.cpp src/main/cpp/common.cpp src/main/cpp/play_sound.cpp src/main/cpp/com_eastcom_harup_utils_SoundEffectUtil.cpp
LOCAL_C_INCLUDES        := src/main/cpp
LOCAL_SHARED_LIBRARIES  := fmod fmodL

include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE            := posix
LOCAL_SRC_FILES         := src/main/cpp/posix.c
LOCAL_C_INCLUDES        := src/main/cpp
LOCAL_LDLIBS +=-L$(SYSROOT)/usr/lib -lm -llog

include $(BUILD_SHARED_LIBRARY)

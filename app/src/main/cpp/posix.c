//
// Created by Administrator on 2020/4/12.
//

#include "com_eastcom_harup_utils_PosixUtil.h"
#include <pthread.h>
#include <unistd.h>
#include <android/log.h>
#include <stdio.h>

#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"har-up",FORMAT,##__VA_ARGS__)

JavaVM* javaVm;
jclass golbal_clss;
jmethodID method_id1;
jmethodID method_id2;

struct Node{
    void* data;
};


void* callback(void* arg){
    LOGI("THIS IS OTHER THREAD %s",arg);
    for (int i = 0; i < 10; ++i) {
        LOGI("%s: %d",arg,i);
    }

    JNIEnv* env;
    (*javaVm)->AttachCurrentThread(javaVm,&env,NULL);
    method_id1 = (*env)->GetStaticMethodID(env,golbal_clss,"randomUUID","()Ljava/util/UUID;");
    method_id2 = (*env)->GetMethodID(env,golbal_clss,"toString","()Ljava/lang/String;");
    jobject obj= (*env)->CallStaticObjectMethod(env,golbal_clss,method_id1);
    jobject jstr=(*env)->CallObjectMethod(env,obj,method_id2);
    const char* uuid =(*env)->GetStringUTFChars(env,jstr,NULL);
    LOGI("%s",uuid);
    (*javaVm)->DetachCurrentThread(javaVm);
};

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved){
    javaVm=vm;
    LOGI("ONLOAD");
    return JNI_VERSION_1_2;
};

JNIEXPORT void JNICALL Java_com_eastcom_harup_utils_PosixUtil_runOtherThread(JNIEnv * env, jclass cls){
    jclass clss = (*env)->FindClass(env,"java/util/UUID");
    golbal_clss = (*env)->NewGlobalRef(env,clss);

    pthread_t  thread;
    pthread_create(&thread,NULL,callback,"thread 1");
    pthread_join(thread,NULL);
    (*env)->DeleteGlobalRef(env,golbal_clss);

};
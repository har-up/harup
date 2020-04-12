#include "common.h"
#include "inc/fmod.hpp"
#include "com_eastcom_harup_utils_SoundEffectUtil.h"

#define MODE_NORMAL 0;
#define MODE_LOLLY 1
#define MODE_MAN 2
#define MODE_WOMAN 3
#define MODE_CRITIS 4
#define MODE_KONGLING 5

JNIEXPORT void JNICALL Java_com_eastcom_harup_utils_SoundEffectUtil_soundFix
        (JNIEnv *env, jclass cls, jstring path, jint type){
    FMOD::System *system;
    FMOD::Sound *sound;
    FMOD::ChannelGroup *mastergroup   = 0;
    FMOD::Channel *channel = 0;
    FMOD::DSP *dsp = 0;
    bool playing = true;


    const char *str_path = env-> GetStringUTFChars(path,NULL);

    FMOD_RESULT result = FMOD::System_Create(&system);
    ERRCHECK(result);

    result = system->init(32,FMOD_INIT_NORMAL,NULL);
    ERRCHECK(result);

    result = system->getMasterChannelGroup(&mastergroup);
    ERRCHECK(result);

    result = system->createSound(Common_MediaPath(str_path), FMOD_DEFAULT, 0, &sound);
    ERRCHECK(result);


    result = sound->setMode(FMOD_LOOP_OFF);
    ERRCHECK(result);

    switch (type){
        case MODE_LOLLY: result = system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH,3.0);
            break;
        case MODE_MAN: result = system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH,0.9);
            break;
        case MODE_WOMAN: result = system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH,1.3);
            break;
        case MODE_CRITIS: result = system->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_TREMOLO_SHAPE,3.0);
            break;
        case MODE_KONGLING: result = system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK,30);
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY,200);
                dsp->setParameterFloat(FMOD_DSP_TYPE_PITCHSHIFT,2.5);
            break;
        default:result = system->createDSPByType(FMOD_DSP_TYPE_NORMALIZE, &dsp);
            break;
    }
    ERRCHECK(result);

    result = system->playSound(sound,0,false,&channel);
    ERRCHECK(result);


    result = mastergroup->addDSP(0, dsp);
    ERRCHECK(result);


    result = system->update();
    ERRCHECK(result);

    while (playing){
        channel->isPlaying(&playing);
    }
    goto end;


    end:
        env->ReleaseStringUTFChars(path,str_path);
        system->close();
        system->release();
};
#include "com_example_ffmpeg_FFmpegUtil.h"

#include <unistd.h>
#include <pthread.h>
#include <android/log.h>
#include <libavutil/avutil.h>
#include <libavutil/frame.h>
#include <libavformat/avio.h>
#include <libavutil/file.h>
#include <libavutil/imgutils.h>
#include <libavutil/samplefmt.h>
#include <libavutil/timestamp.h>
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

#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"har-up",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"har-up",FORMAT,##__VA_ARGS__);

int interruptCallBack(void* ctx){
    LOGI("中断");
    return 1;
}

struct buffer_data{
    uint8_t* ptr;
    size_t size;
};

static int read_packet(void *opaque, uint8_t *buf, int buf_size)
{
    LOGI("READ.......................");
    struct buffer_data *bd = (struct buffer_data *)opaque;
    LOGI("bufferSize:%d,bdSize:%d",buf_size,bd->size);
    buf_size = FFMIN(buf_size, bd->size);
    if (!buf_size)
        return AVERROR_EOF;
    LOGI("ptr:%p size:%zu\n", bd->ptr, bd->size);
    /* copy internal buffer data to buf */
    memcpy(buf, bd->ptr, buf_size);
    LOGI("size: %d", sizeof(buf));
    bd->ptr  += buf_size;
    bd->size -= buf_size;
    return buf_size;
}

static void pgm_save(unsigned char *buf, int wrap, int xsize, int ysize,
                     char *filename)
{
    FILE *f;
    int i;
    f = fopen(filename,"w");
    fprintf(f, "P5\n%d %d\n%d\n", xsize, ysize, 255);
    for (i = 0; i < ysize; i++)
        fwrite(buf + i * wrap, 1, xsize, f);
    fclose(f);
}

void* printLog(void* s,int code ,char* str){
    LOGE("ERROR:")
    LOGE("%s",str);
}

static void decode(AVCodecContext *dec_ctx, AVFrame *frame, AVPacket *pkt,
                   const char *filename)
{
//    av_log_set_callback(printLog);
    LOGI("decode")
    char buf[1024];
    int ret;
    if (pkt->stream_index != 0) return;
    ret = avcodec_send_packet(dec_ctx, pkt);
    if (ret < 0) {
        LOGE("Error sending a packet for decoding %d\n",ret);
        exit(1);
    }
    LOGI("send a packet success");
    while (ret >= 0) {
        ret = avcodec_receive_frame(dec_ctx, frame);
        if (ret == AVERROR(EAGAIN) || ret == AVERROR_EOF)
            return;
        else if (ret < 0) {
            LOGE("Error during decoding\n");
            exit(1);
        }
        LOGI("saving frame %3d\n", dec_ctx->frame_number);
        fflush(stdout);
        /* the picture is allocated by the decoder. no need to
           free it */
        snprintf(buf, sizeof(buf), "%s-%d", filename, dec_ctx->frame_number);
        pgm_save(frame->data[0], frame->linesize[0],
                 frame->width, frame->height, buf);
    }
}


#define INBUF_SIZE 4096
JNIEXPORT void JNICALL Java_com_example_ffmpeg_FFmpegUtil_test(JNIEnv * env, jclass cls, jstring path, jstring out){
    const char* filePath = (*env)->GetStringUTFChars(env,path,NULL);
    const char* outputPath = (*env)->GetStringUTFChars(env,out,NULL);
    LOGI("%s",filePath)

    const char *filename, *outfilename;
    const AVCodec *codec;
    AVCodecParserContext *parser;
    AVFormatContext* avFormatContext = NULL;
    AVCodecContext *c= NULL;
    FILE *f;
    AVFrame *frame;
    uint8_t inbuf[INBUF_SIZE + AV_INPUT_BUFFER_PADDING_SIZE];
    uint8_t *data;
    size_t   data_size;
    int ret;
    AVPacket *pkt;


    avFormatContext = avformat_alloc_context();
    if (!avFormatContext) {
        LOGE("avformat_alloc_context  error");
        goto end;
    }

    ret = avformat_open_input(&avFormatContext,filePath,NULL,NULL);

    if(ret < 0){
        LOGE("avformat_open_input  error : %d",ret);
        goto end;
    }else{
        LOGI("avformat_open_input  success : %d",ret);
    }

    avformat_find_stream_info(avFormatContext,NULL);

    LOGI("CODEC ID: %d",avFormatContext->video_codec_id);

end:
    (*env)->ReleaseStringUTFChars(env,path,filePath);
    (*env)->ReleaseStringUTFChars(env,out,outputPath);
    avformat_close_input(avFormatContext);
    avformat_free_context(avFormatContext);
}




JNIEXPORT void JNICALL Java_com_example_ffmpeg_FFmpegUtil_play(JNIEnv *env, jclass cls, jstring jstr, jobject surface){

};
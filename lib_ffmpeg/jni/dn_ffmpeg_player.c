#include "com_example_ffmpeg_FFmpegUtil.h"

#include <unistd.h>
#include <pthread.h>
#include <android/log.h>
#include <libavutil/avutil.h>
#include <libavutil/frame.h>
#include <libavformat/avio.h>
#include <libavutil/file.h>
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

static void decode(AVCodecContext *dec_ctx, AVFrame *frame, AVPacket *pkt,
                   const char *filename)
{
    char buf[1024];
    int ret;
    ret = avcodec_send_packet(dec_ctx, pkt);
    if (ret < 0) {
        LOGE("Error sending a packet for decoding\n");
        exit(1);
    }
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



JNIEXPORT void JNICALL Java_com_example_ffmpeg_FFmpegUtil_test(JNIEnv * env, jclass cls, jstring path, jstring out){
    const char* filePath = (*env)->GetStringUTFChars(env,path,NULL);
    const char* outputPath = (*env)->GetStringUTFChars(env,out,NULL);
    LOGI("%s",filePath)

    AVFormatContext* avFormatContext = NULL;
    AVCodecContext* codecContext = NULL;
    AVCodec* avCodec = NULL;
    AVCodecParserContext *parser = NULL;
    AVInputFormat* avInputFormat;
    AVDictionary* avDictionary;
    int ret = 0;

    AVIOContext* avioContext= NULL;
    uint8_t *buffer, *avioContextBuffer = NULL;
    size_t bufferSize, avioContextBufferSize = 4096;
    struct buffer_data bufferData = {0};

    AVPacket* avPacket;
    AVFrame* avFrame;


    //根据给的文件路径获取文件指针buffer,和读取总的大小bufferSize
    ret = av_file_map(filePath,&buffer,&bufferSize,0,NULL);
    if (ret < 0){
        LOGE("file prepare file error")
    }
    bufferData.ptr = buffer;
    bufferData.size = bufferSize;
    LOGI("bufferDataSize:%d",bufferData.size)

    //初始化avFormatContext
    avFormatContext = avformat_alloc_context();
    if(!avFormatContext){
        LOGE("Could not allocate context")
    }
    LOGI("allocate context success")


    //初始化avio_alloc_context所需的参数buffer,必须使用av_malloc分配
    avioContextBuffer = av_malloc(avioContextBufferSize);
    if (!avioContextBuffer){
        LOGE("Alloc avioBuffer error")
    }

    //将文件数据读入缓冲区
    avioContext = avio_alloc_context(avioContextBuffer,avioContextBufferSize,0,&bufferData,read_packet,NULL,NULL);
    if (!avioContext){
        LOGE("Could not alloc avioContext")
    }

    avFormatContext->pb = avioContext;
    ret = avformat_open_input(&avFormatContext,NULL,NULL,NULL);
    if(ret < 0){
        LOGE("Could not open file")
    }
    LOGI("open input file success")

    //通过avFormatContext的pb获取流信息，初始化avFormatContext
    ret = avformat_find_stream_info(avFormatContext,NULL);
    if (ret < 0){
        LOGE("Could not find stream info")
    }

    LOGI("input file has stream info")
    av_dump_format(avFormatContext,0,outputPath,0);

    //通过codec id获取解码器

    LOGI("stream number:%D",avFormatContext->nb_streams)
    int videoIndex = -1;
    for (int i = 0; i < avFormatContext->nb_streams ; ++i) {
        if (avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO){
            videoIndex = i;
        }
    }
    LOGI("videoIndex:%d",videoIndex);
    avCodec = avcodec_find_decoder(avFormatContext->streams[videoIndex]->codecpar->codec_id);
    if (!avCodec){
        LOGE("找不到解码器 id: %d",avFormatContext->streams[videoIndex]->codecpar->codec_id);
    }

    parser = av_parser_init(avCodec->id);
    if (!parser){
        LOGE("找不到解析器");
        goto end;
    }

    codecContext = avcodec_alloc_context3(avCodec);
    if (!codecContext){
        LOGE("初始化解码器上下文失败");
        goto end;
    }

    //打开解码器
    ret = avcodec_open2(codecContext,avCodec,NULL);
    if (ret < 0){
        LOGE("打开解码器失败")
        goto end;
    }

    parser = avFormatContext->streams[1]->parser;
    int size = bufferData.size;
    while(size > 0){
        ret = av_parser_parse2(parser,codecContext,&avPacket->data,&avPacket->size,bufferData.ptr,bufferData.size,AV_NOPTS_VALUE,AV_NOPTS_VALUE,0);
        if (ret < 0){
            LOGE("解码器解析数据时出错");
            goto end;
        }
        size -= ret;
        if (avPacket->size){
            decode(codecContext,avFrame,avPacket,outputPath);
        }
    }

    /* flush the decoder */
    decode(codecContext, avFrame, NULL, outputPath);



end:
    (*env)->ReleaseStringUTFChars(env,path,filePath);
    (*env)->ReleaseStringUTFChars(env,out,outputPath);
    av_parser_close(parser);
    avcodec_free_context(&codecContext);
    av_frame_free(&avFrame);
    av_packet_free(&avPacket);
    avformat_close_input(&avFormatContext);
    /* note: the internal buffer could have changed, and be != avio_ctx_buffer */
    if (avioContext)
        av_freep(&avioContext->buffer);
    avio_context_free(&avioContext);
    av_file_unmap(buffer, bufferSize);
    if (ret < 0) {
        fprintf(stderr, "Error occurred: %s\n", av_err2str(ret));
    }
}




JNIEXPORT void JNICALL Java_com_example_ffmpeg_FFmpegUtil_play(JNIEnv *env, jclass cls, jstring jstr, jobject surface){

};
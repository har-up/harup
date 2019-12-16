package com.example.lib_imageloader

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import java.io.FileDescriptor

class ImageResizer {
    val TAG = "ImageResizer"

    fun decodeBitmapFromResource(res:Resources,resId:Int,reqWidth:Int,regHeight:Int):Bitmap{
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res,resId,options)
        options.inSampleSize = caculateInSampleSize(options,reqWidth,regHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun decodeBitmapFromFileDescriptor(fileDescriptor: FileDescriptor,reqWidth: Int,reqHeight: Int): Bitmap{
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options)
        options.inSampleSize = caculateInSampleSize(options,reqWidth,reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options)
    }

    private fun caculateInSampleSize(options: BitmapFactory.Options,reqWidth: Int,reqHeight: Int) : Int{
        var inSampleSize = 1
        if ((reqWidth == 0) or (reqHeight == 0) ){
            return inSampleSize
        }

        var outWidth = options.outWidth
        var outHeight = options.outHeight
        Log.d(TAG,"origin,w=${outWidth},h=${outHeight}")

        if ((outWidth == 0) or (outHeight == 0) ){
            return inSampleSize
        }

        if ((outHeight > reqHeight) and  (outWidth > reqWidth)){
            outHeight /= 2
            outWidth /= 2

            while ((outHeight/inSampleSize >= reqHeight) and (outWidth/inSampleSize >= reqWidth)){
                inSampleSize *= 2
            }
        }
        Log.d(TAG,"sampleSize = $inSampleSize")
        return inSampleSize
    }
}
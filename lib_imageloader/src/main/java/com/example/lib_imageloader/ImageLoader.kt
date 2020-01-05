package com.example.lib_imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.LruCache
import android.widget.ImageView
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.experimental.and
import kotlin.math.min

class ImageLoader {
    val TAG = "image_loader"
    val DISK_CACHE_SIZE = 1024 * 1024 * 50L
    val CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1
    val MAX_POOL_SIZE = CORE_POOL_SIZE * 2
    val KEEP_ALIVE_TIME = 20L

    private var mMemoryCache:LruCache<String,Bitmap>
    private var mDiskLruCache: DiskLruCache
    private var mContext:Context ?= null
    private var sThreadPoolExecutor:ThreadPoolExecutor
    private val mHandler = Handler(Looper.getMainLooper()){
        if ( (it.arg1 == MESSAGE_FLAG) and (it.what == MESSAGE_FLAG)){
            if (it.obj != null){
                val data = it.obj as SendData
                data.imageView.setImageBitmap(data.bitmap ?: BitmapFactory.decodeResource(mContext?.resources,android.R.drawable.stat_notify_error))
            }
        }
         true
    }


    private constructor(context: Context){
        mContext = context
        val maxMemory = Runtime.getRuntime().maxMemory() / 1024
        val cacheSize = maxMemory / 8
        mMemoryCache = LruCache(cacheSize.toInt())
        val cacheDir = getCacheDir(context, "bitmap")
        if (!cacheDir.exists()){
            cacheDir.mkdirs()
        }
        mDiskLruCache = DiskLruCache.open(cacheDir,1,1, DISK_CACHE_SIZE)
        sThreadPoolExecutor = ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE_TIME, TimeUnit.SECONDS, LinkedBlockingDeque<Runnable>(),
            ThreadFactory { r -> Thread(r)})
    }

    companion object{
        private var mInstance: ImageLoader ?= null
        private const val MESSAGE_FLAG = 0xdd
        fun build(context: Context):ImageLoader?{
            if(mInstance == null){
                mInstance = ImageLoader(context)
            }
            return mInstance
        }

        class  SendData{
            val bitmap:Bitmap?
            val imageView: ImageView
            constructor(bitmap: Bitmap?,imageView: ImageView){
                this.bitmap = bitmap
                this.imageView = imageView
            }

            fun getMessage():Message{
                val message = Message()
                message.obj = this
                message.what = MESSAGE_FLAG
                message.arg1 = MESSAGE_FLAG
                return message
            }
        }
    }


    fun bindBitmap(uri: String,imageView: ImageView,reqWidth: Int,reqHeight: Int){
        var bitmap = loadFromMeCache(uri,reqWidth, reqHeight)
        if (bitmap != null){
            imageView.setImageBitmap(bitmap)
            println("GET FROM MEMORY CACHE")
            return
        }
        sThreadPoolExecutor.execute {
            val bitmap = loadFromDiskCache(uri, reqWidth, reqHeight)
            println("GET FROM DISK CACHE")
            if (bitmap != null){
                mHandler.sendMessage(SendData(bitmap,imageView).getMessage())
            }else{
                addToDiskCache(uri)
                val bitmap = getFromDiskCache(uri, reqWidth, reqHeight)
                mHandler.sendMessage(SendData(bitmap,imageView).getMessage())
            }
        }
    }

    fun loadFromMeCache(uri:String,reqWidth: Int,reqHeight: Int): Bitmap?{
        return getFromMemoryCache(uri)
    }

    fun loadFromDiskCache(uri:String,reqWidth: Int,reqHeight: Int): Bitmap?{
        if (mDiskLruCache.get(uri) == null){
            return null
        }
        return getFromDiskCache(uri,reqWidth,reqHeight)
    }

    fun loadFromHttp(uri:String){
        var openConnection = URI.create(uri).toURL().openConnection()
        var bufferedInputStream = BufferedInputStream(openConnection.getInputStream())
        openConnection.getInputStream()
    }

    private fun addToMemoryCache(key:String,value:Bitmap){
        mMemoryCache?.put(key, value)
    }

    private fun getFromMemoryCache(key: String):Bitmap?{
        var get = mMemoryCache?.get(key)
        return get
    }

    private fun addToDiskCache(url:String){
        val key = hashKeyFromUrl(url)
        val edit = mDiskLruCache.edit(key)
        if (edit != null){
            try {
                var newOutputStream = edit.newOutputStream(0)
                if (downloadBitmap(newOutputStream,url))
                    edit.commit()
                else
                    edit.abort()
            }catch (e:java.lang.Exception){
                print(e.message)
            }

        }
        mDiskLruCache.flush()
    }

    private fun getFromDiskCache(url:String,reqWidth:Int, reqHeight:Int):Bitmap?{
        val hashKeyFromUrl = hashKeyFromUrl(url)
        var snapshot: DiskLruCache.Snapshot = mDiskLruCache[hashKeyFromUrl] ?: return null
        var fileInputStream:FileInputStream = snapshot.getInputStream(0) as FileInputStream
        var fd = fileInputStream.fd
        val bitmap:Bitmap = ImageResizer().decodeBitmapFromFileDescriptor(fd,reqWidth,reqHeight)
        addToMemoryCache(url,bitmap)
        return bitmap
    }

    private fun downloadBitmap(outputStream: OutputStream,urlStr:String):Boolean{
        var urlConnection:HttpURLConnection? = null
        var bufferedInputStream:BufferedInputStream? = null
        var bufferedOutputStream:BufferedOutputStream? = null
        val url = URL(urlStr)
        try {
            urlConnection = url.openConnection() as HttpURLConnection?
            val inputStream = urlConnection?.inputStream
            bufferedInputStream = BufferedInputStream(inputStream)
            bufferedOutputStream = BufferedOutputStream(outputStream)
            var b:Int
            while (true){
                b = bufferedInputStream.read()
                if (b!=-1){
                    bufferedOutputStream.write(b)
                }else{
                    break
                }
            }
            return true
        }catch (e:Exception){
            Log.d(TAG,"download image fail")
        }finally {
            urlConnection?.disconnect()
            bufferedOutputStream?.close()
            bufferedInputStream?.close()
        }
        return false
    }



    private fun getCacheDir(context: Context,filename:String) : File {
        var path:String
        val  available = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        path = if (available) context.externalCacheDir.path else context.cacheDir.path
        return File(path + File.pathSeparator + filename)
    }

    private fun  hashKeyFromUrl(url: String):String{
        val cacheKey = try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(url.toByteArray())
            bytesToHexString(digest.digest())
        }catch (e: NoSuchAlgorithmException){
            url.hashCode().toString()
        }
        println("cachekey: $cacheKey")
        return cacheKey
    }

    private fun bytesToHexString(bytes: ByteArray):String {
        var stringBuilder = StringBuilder()
        for (element in bytes){
            val hexString = Integer.toHexString(0xFF and element.toInt())
            if (hexString.length == 1) stringBuilder.append('0') else stringBuilder.append(hexString)
        }
        return stringBuilder.toString()
    }
}


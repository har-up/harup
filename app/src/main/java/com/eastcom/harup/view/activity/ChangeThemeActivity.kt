package com.eastcom.harup.view.activity

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.eastcom.harup.R
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_change_theme.*
import java.io.File

class ChangeThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_theme)
        init()
    }

    private fun init() {
        button.setOnClickListener {
            getSkinResources()
        }
    }

    private fun getSkinResources() {
        //获取AssetManager
        var assetManager:AssetManager = AssetManager::class.java.newInstance()
        var method= assetManager::class.java.getMethod("addAssetPath", String::class.java)
        val apkPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "skin-debug.apk"
        //反射调用assetManager的addAssetPath添加资源搜索路径
        method.invoke(assetManager,apkPath)

        val dexDir: File = applicationContext.getDir("dex", Context.MODE_PRIVATE)
        if (!dexDir.exists()) {
            dexDir.mkdir()
        }

        //获取resources  通过assetManager获取到resources
        var resources = Resources(assetManager, resources.displayMetrics, resources.configuration)
        //将应用外的资源包放到dexDir目录并加载资源类
        var dexClassLoader = DexClassLoader(apkPath, dexDir.absolutePath, null, classLoader)
        var loadClass = dexClassLoader.loadClass("com.example.skin.R$" + "drawable")
        //根据资源名获取对于R文件中的id
        var id:Int = loadClass.getField("image_love").get("null") as Int
        //替换资源
        iv_bg.setImageDrawable(resources.getDrawable(id,theme))

    }


}

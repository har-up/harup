package com.eastcom.harup.view.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.DIRECTORY_MUSIC
import android.util.Log
import android.view.View
import com.eastcom.harup.R
import com.eastcom.harup.utils.SoundEffectUtil
import kotlinx.android.synthetic.main.activity_sound_effect.*
import org.fmod.FMOD
import java.io.File

class SoundEffectActivity : AppCompatActivity(),View.OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_effect)
        init();
    }

    private fun init() {
        FMOD.init(this)
        btn_one.setOnClickListener(this)
        btn_two.setOnClickListener(this)
        btn_three.setOnClickListener(this)
        btn_four.setOnClickListener(this)
        btn_five.setOnClickListener(this)
        btn_six.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btn_one -> SoundEffectUtil.soundFix("640165main_Lookin At It.ogg",0)
            btn_two -> SoundEffectUtil.soundFix("640165main_Lookin At It.ogg",1)
            btn_three -> SoundEffectUtil.soundFix("640165main_Lookin At It.ogg",2)
            btn_four -> SoundEffectUtil.soundFix("640165main_Lookin At It.ogg",3)
            btn_five-> SoundEffectUtil.soundFix("640165main_Lookin At It.ogg",4)
            btn_six -> SoundEffectUtil.soundFix("640165main_Lookin At It.ogg",5)
        }
    }

    override fun onDestroy() {
        FMOD.close()
        super.onDestroy()
    }


}

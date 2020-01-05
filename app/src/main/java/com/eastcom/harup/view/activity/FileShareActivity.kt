package com.eastcom.harup.view.activity

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_file_share.*
import java.io.*

class FileShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_share)
        init()
    }

    private fun init() {
        var dir = File(getCachePath())
        if (!dir.exists()){
            dir.mkdirs()
        }

        save.setOnClickListener {

            var file = File(dir.path + "/cache")
            var objectOutputStream: ObjectOutputStream? = null
            try {
                objectOutputStream = ObjectOutputStream(FileOutputStream(file))
                objectOutputStream.writeObject(User(editName.text.toString(),10))
            }catch (e:IOException){
                print(e.message)
            }finally {
                objectOutputStream?.close()
            }
        }

        query.setOnClickListener {
            var file = File(dir.path + "/cache")
            var objectInputStream: ObjectInputStream? = null
            try {
                objectInputStream = ObjectInputStream(FileInputStream(file))
                var user = objectInputStream.readObject() as User
                showName.text = user.name
            }catch (e:IOException){
                print(e.message)
            }finally {
                objectInputStream?.close()
            }
        }
    }

    private fun getCachePath(): String {
        return this.cacheDir.path
    }
}

class User : Parcelable,Serializable {
    var name:String
    var age:Int

    constructor(parcel: Parcel) {
        name = parcel.readString()
        age = parcel.readInt()
    }

    constructor(name:String,age:Int){
        this.name = name
        this.age = age
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
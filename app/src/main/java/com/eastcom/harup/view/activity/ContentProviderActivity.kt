package com.eastcom.harup.view.activity

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eastcom.harup.R
import com.eastcom.harup.aidl.Book
import com.eastcom.harup.database.DBOpenHelper
import kotlinx.android.synthetic.main.activity_content_provider.*

class ContentProviderActivity : AppCompatActivity() {
    private val TAG: String = "ContentProviderActivity"
    private var books : MutableList<Book> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)
        init()
    }

    private fun init() {
        var uri = BookProvider.BOOK_URI
        var cursor: Cursor? = contentResolver.query(uri, arrayOf("_id", "name"), null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()!!) {
                Log.d(TAG, "query book id:${cursor.getInt(0)}  name:${cursor.getString(1)}")
                books.add(Book(cursor.getString(1),cursor.getInt(0)))
            }
        }
        text.text = books.joinToString { it.toString() }
        insert.setOnClickListener {
            val values = ContentValues()
            values.put("_id",8)
            values.put("name","new Book")
            contentResolver.insert(uri, values)
        }

        getData.setOnClickListener {
            var cursor: Cursor? = contentResolver.query(uri, arrayOf("_id", "name"), null, null, null)
            if (cursor != null) {
                books.clear()
                while (cursor.moveToNext()!!) {
                    Log.d(TAG, "query book id:${cursor.getInt(0)}  name:${cursor.getString(1)}")
                    books.add(Book(cursor.getString(1),cursor.getInt(0)))
                }
                text.text = books.joinToString { it.toString() }
            }
        }

        delete.setOnClickListener {
            contentResolver.delete(uri,"name = ?", arrayOf("new Book"))
        }
    }
}


class BookProvider : ContentProvider() {

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sUriMatcher.addURI(AUTHORITY, "book", 0)
        sUriMatcher.addURI(AUTHORITY, "user", 1)
    }

    private var mDb: SQLiteDatabase? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert,current thread: ${Thread.currentThread().name}")
        mDb?.insert(getTableName(uri),null,values)
        return uri
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query,current thread: ${Thread.currentThread().name}")
        var tableName: String? =
            getTableName(uri) ?: throw IllegalArgumentException("not support uri:$uri")
        return mDb?.query(tableName, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate,current thread: ${Thread.currentThread().name}")
        mDb = DBOpenHelper(this.context).writableDatabase
        mDb?.apply {
            execSQL("delete from ${DBOpenHelper.USER_TABLE_NAME}")
            execSQL("delete from ${DBOpenHelper.BOOK_TABLE_NAME}")
            execSQL("insert into ${DBOpenHelper.BOOK_TABLE_NAME} values (1,'Android')")
            execSQL("insert into ${DBOpenHelper.BOOK_TABLE_NAME} values (2,'Ios')")
            execSQL("insert into ${DBOpenHelper.BOOK_TABLE_NAME} values (3,'Java')")
            execSQL("insert into ${DBOpenHelper.USER_TABLE_NAME} values (1,'james',1)")
            execSQL("insert into ${DBOpenHelper.USER_TABLE_NAME} values (2,'west',2)")
        }
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.d(TAG, "update,current thread: ${Thread.currentThread().name}")
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.d(TAG, "delete,current thread: ${Thread.currentThread().name}")
        mDb?.delete(getTableName(uri),selection,selectionArgs)
        return 0
    }

    override fun getType(uri: Uri): String? {
        Log.d(TAG, "getType,current thread: ${Thread.currentThread().name}")
        return null
    }


    private fun getTableName(uri: Uri): String? {
        val code = sUriMatcher.match(uri)
        var tableName: String? = null
        when (code) {
            0 -> tableName = DBOpenHelper.BOOK_TABLE_NAME
            1 -> tableName = DBOpenHelper.USER_TABLE_NAME
        }
        return tableName
    }

    companion object {
        const val TAG = "BookProvider"
        const val AUTHORITY = "com.eastcom.harup.view.activity.BookProvider"
        val BOOK_URI = Uri.parse("content://$AUTHORITY/book")
        val USER_URI = Uri.parse("content://$AUTHORITY/user")
    }

}
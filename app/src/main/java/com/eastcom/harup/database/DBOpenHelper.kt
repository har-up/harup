package com.eastcom.harup.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper : SQLiteOpenHelper {

    constructor(context: Context) : super(context, DB_NAME,null,DB_VERSION)

    private val CREATE_BOOK_TABLE = "create table if not exists $BOOK_TABLE_NAME (_id integer primary key,name text)"

    private val CREATE_USER_TABLE = "create table if not exists $USER_TABLE_NAME (_id integer primary key,name text,sex integer)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_BOOK_TABLE)
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        const val DB_NAME = "book_provider_db"
        const val BOOK_TABLE_NAME = "book"
        const val USER_TABLE_NAME = "user"
        const val DB_VERSION = 1
    }
}
package com.example.fetchhiringapp.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.fetchhiringapp.Data.FetchDataItem


class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    companion object {
        public const val DATABASE_VERSION = 1
        public const val DATABASE_NAME = "FetchHiringDB"
        public const val TABLE_ITEMS = "Items"
        public const val COLUMN_ITEM_ID = "id"
        public const val COLUMN_ITEM_LIST_ID = "list_id"
        public const val COLUMN_ITEM_NAME = "name"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableItemsQuery = (
                "CREATE TABLE " + TABLE_ITEMS + "(" +
                        COLUMN_ITEM_ID + " INTEGER," +
                        COLUMN_ITEM_LIST_ID + " INTEGER," +
                        COLUMN_ITEM_NAME + " TEXT);"
                )
        db?.execSQL(createTableItemsQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS)
        onCreate(db)
    }
    fun addItemToDB(item: FetchDataItem): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ITEM_ID, item.id)
        values.put(COLUMN_ITEM_LIST_ID, item.listId)
        values.put(COLUMN_ITEM_NAME, item.name)
        val result = db.insert(TABLE_ITEMS, null, values)
        db.close()
        return result
    }
    fun checkIfTableExists(table: String): Boolean {
        val query = "SELECT * FROM $TABLE_ITEMS"
        val db = this.writableDatabase

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLException) {
            db.execSQL(query)
        }
        val checkResult = cursor!!.moveToFirst()
        Log.d("MY_TAG", "TABLE ITEMS FULL??? $checkResult")
        return checkResult
    }
    fun getFilteredItemsFromDB(groupByListId: Boolean): ArrayList<FetchDataItem> {
        val itemsList = ArrayList<FetchDataItem>()
        var query = ""
        if(groupByListId) {
            query = "SELECT * FROM $TABLE_ITEMS " +
                    "WHERE $COLUMN_ITEM_NAME IS NOT NULL AND length($COLUMN_ITEM_NAME)>0 " +
                    "GROUP BY $COLUMN_ITEM_LIST_ID " +
                    "ORDER BY $COLUMN_ITEM_LIST_ID, $COLUMN_ITEM_NAME"
        } else {
            query = "SELECT * FROM $TABLE_ITEMS " +
                    "WHERE $COLUMN_ITEM_NAME IS NOT NULL AND length($COLUMN_ITEM_NAME)>0 " +
                    "ORDER BY $COLUMN_ITEM_LIST_ID, $COLUMN_ITEM_NAME"

        }

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
             cursor = db.rawQuery(query, null)
        } catch (e: SQLException) {
            db.execSQL(query)
            return ArrayList()
        }

        var id: Int
        var listId: Int
        var name: String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID))
                listId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_LIST_ID))
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME))
                val itemObj = FetchDataItem(id, listId, name)
                itemsList.add(itemObj)
            } while (cursor.moveToNext())
        }
        return itemsList
    }

    fun getTableName(): String {
        return TABLE_ITEMS
    }
}
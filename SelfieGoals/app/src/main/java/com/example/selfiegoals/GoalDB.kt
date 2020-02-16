package com.example.selfiegoals

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class GoalDB(context : Context) :
    SQLiteOpenHelper(context, "goaldb.db", null, DB_VERSION) {

    var context: Context? = null

    init {
        this.context = context
    }

    companion object {
        val GOAL = "goal"
        val DB_VERSION = 1
        val CREATE_TABLE_GOAL = "CREATE TABLE goal(gid INT, gname TEXT, gdate TEXT)"
        val SELECT_GOAL = "SELECT * from goal"
    }

    override fun onCreate(db: SQLiteDatabase?) {
       db?.execSQL(CREATE_TABLE_GOAL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertIntoGoal(gid: Int, gname: String, gdate: String): Boolean {
        var contentValues = ContentValues()
        contentValues.put("gid", gid)
        contentValues.put("gname", gname)
        contentValues.put("gdate", gdate)

        val rowId = writableDatabase.insert(GOAL, null, contentValues)
        return rowId > 0
    }

    fun updateGoalWith(gid: Int, gname: String, gdate: String): Boolean {
        var contentValues = ContentValues()
        contentValues.put("gname", gname)
        contentValues.put("gdate", gdate)

        val rowId = writableDatabase.update(GOAL, contentValues, "gid = $gid", null)
        return rowId > 0
    }

    fun deleteGoalWith(gid: Int): Boolean {
        val rowId = writableDatabase.delete(GOAL, "gid = $gid", null)
        return rowId > 0
    }

    fun checkIfInDB(gid: Int): Boolean {
        var query = ("SELECT * FROM goal WHERE gid = $gid")
        var cursor = readableDatabase.rawQuery(query, null, null )
        if (cursor.count <=0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }


    fun selectAllGoal(): ArrayList<GoalModel> {
        var goals : ArrayList<GoalModel> = ArrayList()
        var cursor = readableDatabase.rawQuery(SELECT_GOAL, null, null)

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast){
                var goal = GoalModel()
                goal.gid = cursor.getInt(cursor.getColumnIndex("gid"))
                goal.gname = cursor.getString(cursor.getColumnIndex("gname"))
                goal.gdate = cursor.getString(cursor.getColumnIndex("gdate"))
                goals.add(goal)
                cursor.moveToNext()
            }
        }

        cursor.close()

        return goals
    }
}
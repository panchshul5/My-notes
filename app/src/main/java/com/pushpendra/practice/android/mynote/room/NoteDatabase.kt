package com.pushpendra.practice.android.mynote.room

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pushpendra.practice.android.mynote.modalclass.Note


private const val DATABASE_NAME = "note_database"

@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao() : NoteDao

    companion object {
        private  var instance : NoteDatabase? = null

        fun getInstance(context: Context) : NoteDatabase {

            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,NoteDatabase::class.java,
                    DATABASE_NAME).fallbackToDestructiveMigration()
                    .addCallback(roomDatabaseCallback)
                    .build()
            }
            return instance as NoteDatabase
        }

        private val roomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                instance?.let { PopulateDBAsyncTask(it).execute() }
            }
        }

        private class PopulateDBAsyncTask(db: NoteDatabase) : AsyncTask <Void,Void,Void>() {

            private val noteDao = db.noteDao()

            override fun doInBackground(vararg p0: Void?): Void? {
                noteDao.insert(Note(title = "Title 1",description = "description 1",priority = 8))
                noteDao.insert(Note(title = "Title 2",description = "description 2",priority = 10))
                noteDao.insert(Note(title = "Title 3",description = "description 3",priority = 5))
                return null
            }

        }

    }


}
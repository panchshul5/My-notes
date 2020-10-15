package com.pushpendra.practice.android.mynote.room

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.pushpendra.practice.android.mynote.modalclass.Note



class NoteRepository(application: Application){

    private val database : NoteDatabase = NoteDatabase.getInstance(application)


    private val noteDao = database.noteDao()

    fun insert(note : Note) {
        InsertNodeAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNote() {
        DeleteAllNoteAsyncTask(noteDao).execute()
    }

    fun getAllNotes() : LiveData<List<Note>>{
        return  noteDao.getAllNotes()
    }

    private class InsertNodeAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {


        override fun doInBackground(vararg notes: Note?): Void? {
            notes[0]?.let { noteDao.insert(it) }
            return null
        }

    }

    private class UpdateNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {


        override fun doInBackground(vararg notes: Note?): Void? {
            notes[0]?.let { noteDao.update(it) }
            return null
        }

    }

    private class DeleteNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {


        override fun doInBackground(vararg notes: Note?): Void? {
            notes[0]?.let { noteDao.delete(it) }
            return null
        }

    }

    private class DeleteAllNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Void, Void, Void>() {


        override fun doInBackground(vararg p0: Void?): Void? {
            noteDao.deleteAllNote()
            return null
        }

    }

}
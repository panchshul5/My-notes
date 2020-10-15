package com.pushpendra.practice.android.mynote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pushpendra.practice.android.mynote.modalclass.Note
import com.pushpendra.practice.android.mynote.room.NoteRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NoteRepository(application)
    private val allNotes = repository.getAllNotes()

    fun insert(note : Note) {
        repository.insert(note)
    }

    fun update(note : Note) {
        repository.update(note)
    }

    fun delete(note : Note) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNote()
    }

    fun getAllNotes() : LiveData<List<Note>>{
        return repository.getAllNotes()
    }


}
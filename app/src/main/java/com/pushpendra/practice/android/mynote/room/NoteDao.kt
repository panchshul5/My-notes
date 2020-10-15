package com.pushpendra.practice.android.mynote.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pushpendra.practice.android.mynote.modalclass.Note

@Dao
interface NoteDao {

    @Insert
    fun insert(note : Note)

    @Update
    fun update(note : Note)

    @Delete
    fun delete(note: Note)

    @Query("Delete FROM note_table")
    fun deleteAllNote()

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllNotes() : LiveData<List<Note>>
}
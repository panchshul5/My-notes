package com.pushpendra.practice.android.mynote.modalclass

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var title: String,
    var description: String,
    var priority: Int
)
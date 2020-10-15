package com.pushpendra.practice.android.mynote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.android.synthetic.main.note_item.*

class AddEditNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        number_picker_priority.minValue = 1
        number_picker_priority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val intent = intent

        if(intent.hasExtra(ID_EXTRA)) {

            title = "Edit Node"
            edit_text_title.setText(intent.getStringExtra(TITLE_EXTRA))
            edit_text_description.setText(intent.getStringExtra(DESCRIPTION_EXTRA))
            number_picker_priority.value = intent.getIntExtra(PRIORITY_EXTRA,1)

        }else {
            title = "Add Note"
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title: String = edit_text_title.text.toString()
        val description: String = edit_text_description.text.toString()
        val priority: Int = number_picker_priority.value

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Title or Description cannot be Empty", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent().apply {
            putExtra(TITLE_EXTRA, title)
            putExtra(DESCRIPTION_EXTRA, description)
            putExtra(PRIORITY_EXTRA, priority)

            val id = intent.getStringExtra(ID_EXTRA)

            if(id != null) {
                putExtra(ID_EXTRA,id)
            }
        }

        setResult(RESULT_OK, data)
        finish()
    }

    companion object {
        const val TITLE_EXTRA = "com.pushpendra.practice.android.mynote.TITLE_EXTRA"
        const val DESCRIPTION_EXTRA = "com.pushpendra.practice.android.mynote.DESCRIPTION_EXTRA"
        const val PRIORITY_EXTRA = "com.pushpendra.practice.android.mynote.PRIORITY_EXTRA"
        const val ID_EXTRA = "com.pushpendra.practice.android.mynote.ID_EXTRA"
    }
}
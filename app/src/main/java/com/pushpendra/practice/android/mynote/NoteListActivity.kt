package com.pushpendra.practice.android.mynote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pushpendra.practice.android.mynote.modalclass.Note
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.custom_toast.*

private const val ADD_NOTE_REQUEST = 1
private const val EDIT_NOTE_REQUEST = 2

class NoteListActivity : AppCompatActivity() {

    val adapter = NoteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        button_add_note.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        //val adapter = NoteAdapter()
        recycler_view.adapter = adapter

        noteViewModel.getAllNotes().observe(this, {
            adapter.setNotes(it.toMutableList())
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@NoteListActivity, "Note Deleted", Toast.LENGTH_SHORT).show()

            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        adapter.setOnItemClickable(object : NoteAdapter.OnItemClickable {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@NoteListActivity,AddEditNoteActivity::class.java).apply {

                    putExtra(AddEditNoteActivity.ID_EXTRA,note.id)
                    putExtra(AddEditNoteActivity.TITLE_EXTRA,note.title)
                    putExtra(AddEditNoteActivity.DESCRIPTION_EXTRA,note.description)
                    putExtra(AddEditNoteActivity.PRIORITY_EXTRA,note.priority)
                }
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })


    }

    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            val title = data?.getStringExtra(AddEditNoteActivity.TITLE_EXTRA)
            val description = data?.getStringExtra(AddEditNoteActivity.DESCRIPTION_EXTRA)
            val priority = data?.getIntExtra(AddEditNoteActivity.PRIORITY_EXTRA, 1)


            val note = Note(
                title = title ?: "Title",
                description = description ?: "Description",
                priority = priority ?: 1
            )

            noteViewModel.insert(note)
            showToast(1)
        }else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            val id = data?.getStringExtra(AddEditNoteActivity.ID_EXTRA)

            if (id == null) {
                Toast.makeText(this, "Note can't be updated!", Toast.LENGTH_SHORT).show()
            }

            val title = data?.getStringExtra(AddEditNoteActivity.TITLE_EXTRA)
            val description = data?.getStringExtra(AddEditNoteActivity.DESCRIPTION_EXTRA)
            val priority = data?.getIntExtra(AddEditNoteActivity.PRIORITY_EXTRA, 1)

            val note = Note(
                title = title ?: "Title",
                description = description ?: "Description",
                priority = priority ?: 1
            )

            note.id = id ?: ""
            noteViewModel.update(note)
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
        } else {
            showToast(0)
        }
    }

    private fun showToast(success: Int) {

        if (success == 1) {
            val inflater = layoutInflater
            val layout: View =
                inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_layout))
            //layout.setBackgroundColor(getColor(R.color.toastBGColor))

            val toastTextView = layout.findViewById<TextView>(R.id.toast_text_view)
            toastTextView.text = "Note Saved"

            val toastImageView = layout.findViewById<ImageView>(R.id.toast_image_view)
            toastImageView.setImageResource(R.drawable.ic_thumb_up)

            val toast = Toast(applicationContext)

            toast.duration = Toast.LENGTH_SHORT;
            toast.view = layout;
            toast.show();
        } else {
            val inflater = layoutInflater
            val layout: View =
                inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_layout))
            //layout.setBackgroundColor(getColor(R.color.toastBGColor))

            val toastTextView = layout.findViewById<TextView>(R.id.toast_text_view)
            toastTextView.text = "Note Not Saved"

            val toastImageView = layout.findViewById<ImageView>(R.id.toast_image_view)
            toastImageView.setImageResource(R.drawable.ic_thumb_down)

            val toast = Toast(applicationContext)

            toast.duration = Toast.LENGTH_SHORT;
            toast.view = layout;
            toast.show();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater : MenuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu,menu)

        val searchItem = menu?.findItem(R.id.search_action)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {


                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {

            R.id.delete_all_note -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes Deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
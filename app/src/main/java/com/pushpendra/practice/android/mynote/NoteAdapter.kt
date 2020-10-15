package com.pushpendra.practice.android.mynote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pushpendra.practice.android.mynote.modalclass.Note
import kotlinx.android.synthetic.main.note_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(), Filterable {

    private var listNotes = mutableListOf<Note>()
    private lateinit var fullListNotes: MutableList<Note>
    private lateinit var thisListener: OnItemClickable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = listNotes[position]

        holder.titleTextView.text = currentNote.title
        holder.descriptionTextView.text = currentNote.description
        holder.priorityTextView.text = currentNote.priority.toString()
    }

    override fun getItemCount(): Int = listNotes.size

    fun setNotes(notes: MutableList<Note>) {
        listNotes = notes
        fullListNotes = ArrayList(notes)
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int): Note {
        return listNotes[position]
    }


    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var titleTextView: TextView = itemView.title_text_view
        var descriptionTextView: TextView = itemView.description_text_view
        var priorityTextView: TextView = itemView.priority_text_view

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (thisListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                thisListener.onItemClick(listNotes[adapterPosition])
            }
        }
    }

    interface OnItemClickable {
        fun onItemClick(note: Note)
    }

    fun setOnItemClickable(listener: OnItemClickable) {
        thisListener = listener
    }

    override fun getFilter(): Filter {
        return noteFilter
    }

    private val noteFilter = object : Filter() {

        override fun performFiltering(charSequence : CharSequence?): FilterResults {
            val filteredList : MutableList<Note> = mutableListOf()

            val filterResult : FilterResults = FilterResults()
            if (charSequence != null && charSequence.isNotEmpty()) {
                val filterPattern = charSequence.toString().toLowerCase().trim();

                for (note in fullListNotes) {
                    if (note.title.toLowerCase().contains(filterPattern)) {
                        filteredList.add(note)
                    }
                }
                filterResult.values = filteredList
            }else {
                filteredList.addAll(fullListNotes)
                filterResult.values = filterResult
            }

            return filterResult
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            listNotes.clear()
            if (results != null) {
                listNotes.addAll(results.values as MutableList<Note>)
            }
            notifyDataSetChanged()
        }

    }
}
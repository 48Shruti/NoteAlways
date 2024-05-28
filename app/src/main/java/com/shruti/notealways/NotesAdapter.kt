package com.shruti.notealways

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class NotesAdapter(val item :ArrayList<NotesDataClass>, val notesInterface: NotesInterface): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    class ViewHolder (var view : View) : RecyclerView.ViewHolder(view) {
        var title  = view.findViewById<TextView>(R.id.tvtitleview)
        var description = view.findViewById<TextView>(R.id.tvdescriptionview)
        var date = view.findViewById<TextView>(R.id.tvdateview)
       // var update = view.findViewById<ImageButton>(R.id.btnupdate)
        //var delete = view.findViewById<ImageButton>(R.id.btndelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_layout_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(item[position].title)
        holder.description.setText(item[position].description)
        holder.date.setText(item[position].date)
        holder.itemView.setOnClickListener{
            notesInterface.notesUpdate(item[position],position)
        }
//        holder.update.setOnClickListener {
//            notesInterface.notesUpdate(item[position],position)
//        }
//        holder.delete.setOnClickListener{
//            notesInterface.notesDelete(item[position],position)
//        }
    }


    
}
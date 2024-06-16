package com.shruti.notealways

import android.content.ContentValues.TAG
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.firebase.ktx.Firebase
import com.shruti.notealways.databinding.TodoLayoutViewBinding

class TodoAdapter(var item : ArrayList<TodoDataClass>, var todoInterface: TodoInterface) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){
    class ViewHolder(val view : View):RecyclerView.ViewHolder(view) {
        var title = view.findViewById<TextView>(R.id.titletodo)
        var time = view.findViewById<TextView>(R.id.datetodo)
        var checkbox = view.findViewById<CheckBox>(R.id.checkboxtodo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_layout_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = item[position].title
        holder.time.text =  item[position].time
        holder.checkbox.isChecked =  item[position].completed
        if (item[position].completed){
            item[position].completed = true
            holder.title.setTextColor(Color.GRAY)
            holder.time.text = "Completed"
        }
        else{
            item[position].completed = false
            holder.title.setTextColor(Color.BLACK)
            holder.time.text =  item[position].time
        }
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            item[position].completed = true
            holder.title.setTextColor(Color.GRAY)
            holder.time.text = "Completed"

        } else {
            item[position].completed = false
            holder.title.setTextColor(Color.BLACK)
            holder.time.text =  item[position].time
        }
            todoInterface.todoMark( item[position],position)
    }
}

    }

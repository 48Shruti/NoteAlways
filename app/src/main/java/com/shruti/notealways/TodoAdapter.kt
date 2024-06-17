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

class TodoAdapter(private var item: ArrayList<TodoDataClass>, private var todoInterface: TodoInterface) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titletodo)
        val time: TextView = view.findViewById(R.id.datetodo)
        val checkbox: CheckBox = view.findViewById(R.id.checkboxtodo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_layout_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = item[position]
        holder.title.text = currentItem.title
        holder.time.text = currentItem.time
        holder.checkbox.isChecked = currentItem.completed
        if (currentItem.completed) {
            holder.title.setTextColor(Color.GRAY)
            holder.time.text = "Completed"
        } else {
            holder.title.setTextColor(Color.BLACK)
            holder.time.text = currentItem.time
        }
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.completed = isChecked
            if (isChecked) {
                holder.title.setTextColor(Color.GRAY)
                holder.time.text = "Completed"

            } else {
                holder.title.setTextColor(Color.BLACK)
                holder.time.text = currentItem.time

            }
            todoInterface.todoMark(currentItem, position)
        }
    }


}


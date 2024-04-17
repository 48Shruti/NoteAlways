package com.shruti.notealways

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class TodoAdapter(val item : ArrayList<TodoDataClass>,todoInterface: TodoInterface) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){
    class ViewHolder(val view : View):RecyclerView.ViewHolder(view) {
        var title = view.findViewById<TextView>(R.id.titletodo)
        var time = view.findViewById<TextView>(R.id.datetodo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_layout_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(item[position].title)
        holder.time.setText(item[position].time)
    }
}
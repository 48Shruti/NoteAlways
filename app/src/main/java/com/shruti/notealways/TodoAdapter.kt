package com.shruti.notealways

import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.firebase.ktx.Firebase
import com.shruti.notealways.databinding.TodoLayoutViewBinding

class TodoAdapter(val item : ArrayList<TodoDataClass>,val todoInterface: TodoInterface) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){
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
        holder.title.setText(item[position].title)
        holder.time.setText(item[position].time)
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                holder.title.setTextColor(Color.argb(128,128,128,128))
                holder.time.setText("Completed")
                holder.checkbox.visibility = View.GONE
            }
            else{
                holder.checkbox.visibility = View.VISIBLE
            }
        }
    }
}
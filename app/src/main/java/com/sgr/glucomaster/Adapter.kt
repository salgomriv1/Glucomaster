package com.sgr.glucomaster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MAdapter: RecyclerView.Adapter<MAdapter.ViewHolder>() {

    var data: MutableList<String> = ArrayList()
    lateinit var context: Context

    fun MAdapter(data: MutableList<String>, context: Context) {
        this.data = data
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {

        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data.get(position)
        holder.bind(item, context)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dat = view.findViewById(R.id.textView) as TextView

        fun bind(data :String, context: Context) {

            dat.text = data
        }
    }

}


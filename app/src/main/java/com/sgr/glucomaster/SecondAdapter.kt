package com.sgr.glucomaster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SAdapter: RecyclerView.Adapter<SAdapter.ViewHolder>() {

    var data: MutableList<String> = ArrayList()
    lateinit var context: Context

    fun SAdapter(data: MutableList<String>, context: Context) {
        this.data = data
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.list_item_sel_del, parent, false))
    }

    override fun getItemCount(): Int {

        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data.get(position)
        holder.bind(item, context)

        holder.btnBorrar.setOnClickListener {

            Toast.makeText(context, "No implementado", Toast.LENGTH_LONG).show()
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dat = view.findViewById(R.id.tvList) as TextView
        val textView: TextView = itemView.findViewById(R.id.tvList)
        val btnBorrar: ImageButton = itemView.findViewById(R.id.btnBorrar)

        fun bind(data :String, context: Context) {

            dat.text = data
        }
    }

}
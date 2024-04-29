package com.sgr.glucomaster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SAdapter(private val onDelete: OnDeleteItemClickListener, private val onClick: OnItemClickedListener): RecyclerView.Adapter<SAdapter.ViewHolder>() {

    var data: MutableList<String> = ArrayList()
    lateinit var context: Context

    fun SAdapter(data: MutableList<String>, context: Context) {
        this.data = data
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.list_item_sel_del, parent, false), onDelete, onClick, data)
    }

    override fun getItemCount(): Int {

        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data.get(position)
        holder.bind(item, context)

    }

    class ViewHolder(
        view: View,
        onDelete: OnDeleteItemClickListener,
        onClick: OnItemClickedListener,
        data: MutableList<String>
    ): RecyclerView.ViewHolder(view) {

        val dat = view.findViewById(R.id.tvList) as TextView

        private val btnBorrar: ImageButton = itemView.findViewById(R.id.btnBorrar)
        private val btnCambiar: TextView = itemView.findViewById(R.id.tvList)

        init {
            btnBorrar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDelete.onDeleteItemClick(data.get(position))
                }
            }

            btnCambiar.setOnClickListener {

                onClick.onItemClicked(data.get(position))
            }

        }

        fun bind(data :String, context: Context) {

            dat.text = data

        }

    }

    interface OnDeleteItemClickListener {
        fun onDeleteItemClick(item: String)
    }

    interface OnItemClickedListener {
        fun onItemClicked(item: String)
    }

}
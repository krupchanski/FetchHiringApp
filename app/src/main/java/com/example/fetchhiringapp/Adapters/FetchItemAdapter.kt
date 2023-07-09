package com.example.fetchhiringapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchhiringapp.Data.FetchDataItem
import com.example.fetchhiringapp.R

class FetchItemAdapter(var items: ArrayList<FetchDataItem>) : RecyclerView.Adapter<FetchItemAdapter.FetchItemViewHolder>() {

    inner class FetchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvItemId)
        val tvListId: TextView = itemView.findViewById(R.id.tvItemListId)
        val tvName: TextView = itemView.findViewById(R.id.tvItemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FetchItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return FetchItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FetchItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.tvId.setText(currentItem.id.toString())
        holder.tvListId.setText(currentItem.listId.toString())
        holder.tvName.setText(currentItem.name.toString())
    }

    override fun getItemCount(): Int = items.size
}
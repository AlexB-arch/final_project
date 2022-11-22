package com.example.afinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.data.ReceiptEntity
import com.example.afinal.databinding.ListItemBinding

class ReceiptListAdapter(private val receiptsList: List<ReceiptEntity>) : RecyclerView.Adapter<ReceiptListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val receipt = receiptsList[position]
        with(holder.binding) {
            receiptText.text = receipt.contents
        }
    }
    override fun getItemCount() = receiptsList.size
}
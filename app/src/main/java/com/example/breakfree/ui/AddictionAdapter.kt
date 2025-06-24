package com.example.breakfree.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.breakfree.R
import com.example.breakfree.data.Addiction
import java.text.SimpleDateFormat
import java.util.*

class AddictionAdapter(
    private var items: List<Addiction>,
    private val onDeleteClick: (Addiction) -> Unit
) : RecyclerView.Adapter<AddictionAdapter.AddictionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddictionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_addiction, parent, false)
        return AddictionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddictionViewHolder, position: Int) {
        holder.bind(items[position], onDeleteClick)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Addiction>) {
        items = newItems
        notifyDataSetChanged()
    }

    class AddictionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.textAddictionName)
        private val dateText: TextView = itemView.findViewById(R.id.textAddictionDate)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(addiction: Addiction, onDeleteClick: (Addiction) -> Unit) {
            nameText.text = addiction.name
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateText.text = "Started: ${sdf.format(Date(addiction.dateStarted))}"
            buttonDelete.setOnClickListener {
                Toast.makeText(itemView.context, "Delete pressed for ${addiction.name}", Toast.LENGTH_SHORT).show()
                onDeleteClick(addiction)
            }
        }
    }
} 
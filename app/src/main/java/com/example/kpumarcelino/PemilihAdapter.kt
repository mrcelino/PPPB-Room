package com.example.kpumarcelino

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.database.Note
import com.example.kpumarcelino.databinding.ItemListBinding

class PemilihAdapter(
    val listPemilih: List<Note>, // Ubah dari private ke val agar publik
    private val onDeleteClick: (Int) -> Unit,
    private val onEditClick: (Int) -> Unit,
    private val onViewClick: (Int) -> Unit
) : RecyclerView.Adapter<PemilihAdapter.ItemListViewHolder>() {

    // ViewHolder untuk mengikat data ke item layout
    class ItemListViewHolder(val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            data: Note,
            position: Int,
            onDeleteClick: (Int) -> Unit,
            onEditClick: (Int) -> Unit,
            onViewClick: (Int) -> Unit
        ) {
            with(binding) {
                txtAngka.text = (position + 1).toString()
                txtNamapemilih.text = data.nama_pemilih

                imgEdit.setOnClickListener { onEditClick(position) }
                imgDelete.setOnClickListener { onDeleteClick(position) }
                imgSee.setOnClickListener { onViewClick(position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding = ItemListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemListViewHolder(binding)
    }

    override fun getItemCount(): Int = listPemilih.size

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.bind(
            data = listPemilih[position],
            position = position,
            onDeleteClick = onDeleteClick,
            onEditClick = onEditClick,
            onViewClick = onViewClick
        )
    }
}


package com.example.pract2_2


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class PlaylistAdapter: ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder>(DiffCallback()) {
    private var onDeleteClickListener: ((Playlist) -> Unit)? = null
    private var onEditClickListener: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist, onDeleteClickListener, onEditClickListener)

    }
    fun setOnDeleteClickListener(listener: (Playlist) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: (Playlist) -> Unit) {
        onEditClickListener = listener
    }
    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val info1: TextView = itemView.findViewById(R.id.title)
        private val info2: TextView = itemView.findViewById(R.id.Singer)

        private val deleteButton: Button = itemView.findViewById(R.id.delete)
        private val redactorButton: Button = itemView.findViewById(R.id.edit)


        fun bind(playlist: Playlist, onDeleteClickListener: ((Playlist) -> Unit)?, onEditClickListener: ((Playlist) -> Unit)?) {
            info1.text = "${playlist.name}"
            info2.text = "${playlist.singer}"

            deleteButton.setOnClickListener {
                onDeleteClickListener?.invoke(playlist)
            }

            redactorButton.setOnClickListener {
                onEditClickListener?.invoke(playlist)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }





}


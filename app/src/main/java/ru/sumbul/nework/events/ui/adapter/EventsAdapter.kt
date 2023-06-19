package ru.sumbul.nework.events.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.nework.databinding.EventCardBinding
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.util.load


interface OnInteractionListener {
    fun onClick(event: EventResponse) {}
    fun onAuthor(event: EventResponse) {}
    fun onLike(event: EventResponse) {}
    //  fun onDeleteLike(event: EventResponse)
    fun onEdit(event: EventResponse) {}
    fun onRemove(event: EventResponse) {}
}

class EventsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : PagingDataAdapter<EventResponse, RecyclerView.ViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = EventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as EventViewHolder).bind(item)
        }
    }
}

class EventViewHolder(
    private val onInteractionListener: OnInteractionListener,
    private val binding: EventCardBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(event: EventResponse) {

        event.authorAvatar?.let { binding.avatar.load(it) }
        event.attachment?.url?.let { binding.attachment.load(it) }
        //TODO FIX ATTACHMENT CUZ IT IS ALSO CAN BE VIDEO OR AUDIO LOL

        binding.author.text = event.author
        binding.published.text = event.published
        binding.dateTime.text = event.datetime
        binding.content.text = event.content


        itemView.setOnClickListener {
            onInteractionListener.onClick(event)
        }
        binding.author.setOnClickListener {
            onInteractionListener.onAuthor(event)
        }
        binding.avatar.setOnClickListener {
            onInteractionListener.onAuthor(event)
        }
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<EventResponse>() {
    override fun areItemsTheSame(
        oldItem: EventResponse,
        newItem: EventResponse
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EventResponse,
        newItem: EventResponse
    ): Boolean {
        return oldItem == newItem
    }

}
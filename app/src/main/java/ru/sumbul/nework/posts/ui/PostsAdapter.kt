package ru.sumbul.nework.posts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.nework.databinding.PostCardBinding
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.util.load

interface PostOnInteractionListener {
    fun onClick(post: PostResponse) {}
    fun onAuthor(post: PostResponse) {}
    fun onLike(post: PostResponse) {}

    //  fun onDeleteLike(post: PostResponse)
    fun onEdit(post: PostResponse) {}
    fun onRemove(post: PostResponse) {}
}

class PostsAdapter(
    private val onInteractionListener: PostOnInteractionListener
) : PagingDataAdapter<PostResponse, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as PostViewHolder).bind(item)
        }
    }
}

class PostViewHolder(
    private val onInteractionListener: PostOnInteractionListener,
    private val binding: PostCardBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: PostResponse) {

        post.authorAvatar?.let { binding.avatar.load(it) }
        post.attachment?.url?.let { binding.attachment.load(it) }
        //TODO FIX ATTACHMENT CUZ IT IS ALSO CAN BE VIDEO OR AUDIO LOL

        binding.author.text = post.author
        binding.published.text = post.published
        binding.content.text = post.content


        itemView.setOnClickListener {
            onInteractionListener.onClick(post)
        }

        binding.author.setOnClickListener {
            onInteractionListener.onAuthor(post)
        }

        binding.avatar.setOnClickListener {
            onInteractionListener.onAuthor(post)
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<PostResponse>() {
    override fun areItemsTheSame(
        oldItem: PostResponse,
        newItem: PostResponse
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PostResponse,
        newItem: PostResponse
    ): Boolean {
        return oldItem == newItem
    }
}
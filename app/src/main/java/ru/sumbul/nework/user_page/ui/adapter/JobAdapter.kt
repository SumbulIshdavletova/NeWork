package ru.sumbul.nework.user_page.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.nework.databinding.JobCardBinding
import ru.sumbul.nework.user_page.domain.model.Job


interface JobOnInteractionListener {
    fun onClick(job: Job) {}
}

class JobAdapter(
    private val onInteractionListener: JobOnInteractionListener,
) : ListAdapter<Job,
        JobViewHolder>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding =
            JobCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }


}

class JobViewHolder(
    private val onInteractionListener: JobOnInteractionListener,
    val binding: JobCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(job: Job) {
        binding.apply {
            binding.position.text = job.position
            binding.name.text = job.name
            binding.start.text = job.start
            binding.finish.text = job.finish
            binding.link.text = job.link

            itemView.setOnClickListener {
                onInteractionListener.onClick(job)
            }
            //TODO MENU
        }
    }
}


class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}

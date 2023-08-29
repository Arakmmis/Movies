package yassir.moviesapp.presentation.details.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yassir.moviesapp.data.pojos.MovieDetails
import yassir.moviesapp.databinding.ViewActorBinding
import yassir.moviesapp.domain.api.ApiConfig
import yassir.moviesapp.util.loadImage

class ActorsAdapter : RecyclerView.Adapter<ActorsAdapter.ViewHolder>() {

    private var actors: List<MovieDetails.Actor> = listOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewActorBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun getItemCount(): Int = actors.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actor = actors[position]
        holder.bind(actor)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(actors: List<MovieDetails.Actor>) {
        this.actors = actors
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ViewActorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            actor: MovieDetails.Actor?
        ) {
            if (actor != null) {
                binding.tvActor.text = actor.name

                binding.ivActor
                    .loadImage(ApiConfig.Constants.BASE_IMAGE_URL_w500_API + actor.profilePath)
            }
        }
    }
}
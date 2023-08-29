package yassir.moviesapp.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import yassir.moviesapp.R

fun ImageView.loadImage(
    url: String?,
    @DrawableRes placeholderRes: Int = R.drawable.ic_movie_poster_placeholder
) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .placeholder(placeholderRes)
        .into(this)
}
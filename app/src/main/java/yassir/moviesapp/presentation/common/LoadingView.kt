package yassir.moviesapp.presentation.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import yassir.moviesapp.databinding.ViewLoadingBinding

class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewLoadingBinding

    init {
        binding =
            ViewLoadingBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(stateType: StateType) {
        with(binding) {
            when (stateType) {
                StateType.LOADING -> {
                    root.visibility = View.VISIBLE
                    progressBar.visibility = View.VISIBLE
                    ivIcon.visibility = View.GONE
                }

                StateType.ERROR -> {
                    root.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    ivIcon.visibility = View.VISIBLE
                }

                StateType.DONE -> {
                    root.visibility = View.GONE
                }
            }
        }
    }

    enum class StateType {
        ERROR, LOADING, DONE
    }
}
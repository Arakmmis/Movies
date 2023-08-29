package yassir.moviesapp.presentation.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import timber.log.Timber
import yassir.moviesapp.R
import yassir.moviesapp.databinding.ViewErrorBinding

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewErrorBinding

    init {
        binding = ViewErrorBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(
        stateType: StateType,
        desc: String? = null,
        e: Throwable? = null,
        listener: OnClickListener? = null
    ) {
        with(binding) {
            when (stateType) {
                StateType.NO_ERROR -> {
                    container.visibility = GONE
                }

                StateType.CONNECTION -> {
                    container.visibility = VISIBLE
                    btnRetry.visibility = VISIBLE
                    btnRetry.setOnClickListener(listener)
                    tvTitle.text = resources.getString(R.string.err_title_common)
                    tvDesc.text = desc ?: resources.getString(R.string.err_connection)

                    Timber.e(e?.message)
                }

                StateType.OPERATIONAL -> {
                    container.visibility = VISIBLE
                    btnRetry.visibility = VISIBLE
                    btnRetry.setOnClickListener(listener)
                    tvTitle.text = resources.getString(R.string.err_title_common)
                    tvDesc.text = desc ?: resources.getString(R.string.err_operational_msg)

                    Timber.e(e?.message)
                }

                StateType.EMPTY -> {
                    container.visibility = VISIBLE
                    btnRetry.visibility = GONE
                    tvDesc.visibility = GONE
                    tvTitle.text = resources.getString(R.string.err_title_empty)

                    Timber.e(e?.message)
                }
            }
        }
    }

    enum class StateType {
        NO_ERROR, CONNECTION, OPERATIONAL, EMPTY
    }
}
package yassir.moviesapp.presentation.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import yassir.moviesapp.R

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.frag_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController()
                .navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        }, 2000)
    }
}
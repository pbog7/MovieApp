package com.example.movieapp

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Transition
import com.example.common.SharedViewModel
import com.example.common.base.BaseNavigationActivity
import com.example.common.domain.models.AppEvent
import com.example.common.domain.models.SharedData
import com.example.common.utils.EventBus
import com.example.common.utils.TransitionListener
import com.example.common.utils.animateSlide
import com.example.movieapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : BaseNavigationActivity() {
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var navHostContainerLayoutParams: ViewGroup.MarginLayoutParams
    private var bottomNavigationSize by Delegates.notNull<Int>()

    @Inject
    lateinit var eventBus: EventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navHostContainerLayoutParams =
            binding.navHostFragment.layoutParams as ConstraintLayout.LayoutParams
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        bottomNavigationSize = resources.getDimension(R.dimen.bottomNavigationSize).toInt()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventBus.event.collectLatest {
                    when (it) {
                        is AppEvent.GoToMovieReviews -> {
                            sharedViewModel.sharedData.tryEmit(SharedData(movieId = it.movieId))
                            navController.navigate(R.id.action_global_movie_reviews_fragment)
                        }
                    }
                }
            }
        }
    }

    override fun hideBottomNavigation() {
        binding.apply {
            bottomNav.animateSlide(
                viewGroup = mainConstraintLayout,
                gravity = Gravity.BOTTOM,
                visibility = View.GONE,
                transitionListener = TransitionListener(onEndBlock = { updateMargins(0) })
            )
        }
    }

    override fun showBottomNavigation() {
        binding.apply {
            bottomNav.animateSlide(
                viewGroup = mainConstraintLayout,
                gravity = Gravity.BOTTOM,
                visibility = View.VISIBLE,
                transitionListener = TransitionListener(onEndBlock = {
                    updateMargins(
                        bottomNavigationSize
                    )
                })
            )
        }
    }

    private fun updateMargins(margin: Int) {
        navHostContainerLayoutParams.bottomMargin = margin
        binding.navHostFragment.requestLayout()
    }
}

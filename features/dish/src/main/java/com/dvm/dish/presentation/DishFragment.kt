package com.dvm.dish.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dvm.dish.dish_impl.Dish
import com.dvm.dish.dish_impl.DishViewModel
import com.dvm.navigation.Navigator
import com.dvm.ui.themes.YammyDeliveryTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import javax.inject.Inject

@AndroidEntryPoint
internal class DishFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: DishViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            YammyDeliveryTheme(
                requireActivity().window
            ) {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    viewModel.state?.let { state ->
                        Dish(
                            state = state,
                            navigator = navigator,
                            onEvent = { viewModel.dispatchEvent(it) }
                        )
                    }
                }
            }
        }
    }
}
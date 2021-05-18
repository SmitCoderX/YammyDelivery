package com.dvm.order.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.dvm.ui.FragmentInsetsComposeView
import com.dvm.ui.YammyDeliveryScreen
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import javax.inject.Inject

@AndroidEntryPoint
internal class OrderFragment: Fragment() {

    @Inject
    lateinit var factory: OrderViewModelAssistedFactory

    private val args: OrderFragmentArgs by navArgs()

    private val viewModel: OrderViewModel by viewModels {
        factory.create(args.orderId, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentInsetsComposeView (requireContext()).apply {
        setContent {
            YammyDeliveryScreen(requireActivity()) {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    Order(
                        state = viewModel.state,
                        onEvent = { viewModel.dispatchEvent(it) }
                    )
                }
            }
        }
    }
}
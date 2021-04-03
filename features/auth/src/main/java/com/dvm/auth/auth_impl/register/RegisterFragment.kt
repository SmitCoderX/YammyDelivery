package com.dvm.auth.auth_impl.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dvm.appmenu.Navigator
import com.dvm.auth.auth_impl.di.AuthComponentHolder
import com.dvm.ui.themes.YammyDeliveryTheme
import dev.chrisbanes.accompanist.insets.ExperimentalAnimatedInsets
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import javax.inject.Inject

internal class RegisterFragment : Fragment() {

    @Inject
    lateinit var factory: RegisterViewModelAssistedFactory

    private val model: RegisterViewModel by viewModels {
        factory.create(findNavController())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AuthComponentHolder.getComponent().inject(this)
    }

    @OptIn(ExperimentalAnimatedInsets::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            YammyDeliveryTheme(
                requireActivity().window
            ) {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    Registration(
                        state = model.state,
                        onEvent = { model.dispatch(it) },
                        navigator = requireActivity() as Navigator
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
    }
}
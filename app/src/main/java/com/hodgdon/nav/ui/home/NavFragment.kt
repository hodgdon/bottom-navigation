package com.hodgdon.nav.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hodgdon.nav.R
import com.hodgdon.nav.ui.home.NavFragmentDestination.*

enum class NavFragmentDestination {
    A1, A2, A3, B1, B2, B3, C1, C2, C3,
}

enum class ButtonEnum {
    NEXT,

    DEEP_A1, DEEP_A2, DEEP_A3,
    DEEP_B1, DEEP_B2, DEEP_B3,
    DEEP_C1, DEEP_C2, DEEP_C3,

    GLOBAL_A1, GLOBAL_A2, GLOBAL_A3,
    GLOBAL_B1, GLOBAL_B2, GLOBAL_B3,
    GLOBAL_C1, GLOBAL_C2, GLOBAL_C3,

    RECREATE,
}

class NavFragment : Fragment() {
    private val navFragmentArgs by navArgs<NavFragmentArgs>()
    private val destination: NavFragmentDestination?
        get() {
            val destinationString = navFragmentArgs.destination
            return values().firstOrNull { it.name == destinationString }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val destination = destination
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Screen(destination, ::onButtonPress)
            }
        }
    }

    fun onButtonPress(buttonEnum: ButtonEnum) {
        val navController = findNavController()
        when(buttonEnum) {
            ButtonEnum.NEXT -> when(destination) {
                    A1 -> navController.navigate(R.id.action_a1_to_a2)
                    A2 -> navController.navigate(R.id.action_a2_to_a3)
                    B1 -> navController.navigate(R.id.action_b1_to_b2)
                    B2 -> navController.navigate(R.id.action_b2_to_b3)
                    C1 -> navController.navigate(R.id.action_c1_to_c2)
                    C2 -> navController.navigate(R.id.action_c2_to_c3)
                    A3 -> {}
                    B3 -> {}
                    C3 -> {}
                    null -> {}
                }
            ButtonEnum.DEEP_A1,
            ButtonEnum.DEEP_A2,
            ButtonEnum.DEEP_A3,
            ButtonEnum.DEEP_B1,
            ButtonEnum.DEEP_B2,
            ButtonEnum.DEEP_B3,
            ButtonEnum.DEEP_C1,
            ButtonEnum.DEEP_C2,
            ButtonEnum.DEEP_C3 -> {
                val destinationResourceId = buttonEnum.toDestination()?.destinationResId()
                if(destinationResourceId != null) {
                    val navDeepLinkBuilder = navController.createDeepLink()
                        .setDestination(destinationResourceId)
                    val stackBuilder = navDeepLinkBuilder
                        .createTaskStackBuilder()
                    stackBuilder.startActivities()
                }
            }
            ButtonEnum.GLOBAL_A1,
            ButtonEnum.GLOBAL_A2,
            ButtonEnum.GLOBAL_A3,
            ButtonEnum.GLOBAL_B1,
            ButtonEnum.GLOBAL_B2,
            ButtonEnum.GLOBAL_B3,
            ButtonEnum.GLOBAL_C1,
            ButtonEnum.GLOBAL_C2,
            ButtonEnum.GLOBAL_C3 -> {
                val globalAction = buttonEnum.toGlobalAction()
                if(globalAction != null) {
                    navController.navigate(globalAction)
                }
            }
            ButtonEnum.RECREATE -> {
                requireActivity().recreate()
            }
        }
    }
}


@Composable
private fun Screen(destination: NavFragmentDestination?, onClick: (ButtonEnum) -> Unit) {
    val showNext = when(destination) {
        A1 -> true
        A2 -> true
        B1 -> true
        B2 -> true
        C1 -> true
        C2 -> true
        A3 -> false
        B3 -> false
        C3 -> false
        null -> false
    }
    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(16.dp)) {
        if(showNext) {
            item(span= {
                GridItemSpan(maxCurrentLineSpan)
            }) {
                Button(
                    onClick = { onClick(ButtonEnum.NEXT) },
                    modifier = Modifier.defaultMinSize(minWidth = 300.dp, minHeight = 72.dp).padding(bottom = 8.dp)
                ) {
                    Text("Next")
                }
            }
        }
        item(span= {
            GridItemSpan(maxCurrentLineSpan)
        }) {
            Button(
                onClick = { onClick(ButtonEnum.RECREATE) },
                modifier = Modifier.defaultMinSize(minWidth = 300.dp, minHeight = 64.dp)
            ) {
                Text("Recreate")
            }
        }
        item(span= {
            GridItemSpan(maxCurrentLineSpan)
        }) {
            Text("Deep Links")
        }
        val rotatedDestinations = listOf(A1,B1,C1,A2,B2,C2,A3, B3, C3,)
        items(rotatedDestinations, key = {"deep" + it.ordinal}) {
            Button(onClick = {onClick(it.toDeepLinkButtonEnum())}, modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                Text(it.name)
            }
        }
        item(span= {
            GridItemSpan(maxCurrentLineSpan)
        }) {
            Text("Global Actions")
        }
        val tabDestinations = when(destination) {
            A1,
            A2,
            A3 -> listOf(A1,A2,A3)
            B1,
            B2,
            B3 -> listOf(B1,B2,B3)
            C1,
            C2,
            C3 -> listOf(C1,C2,C3)
            null -> emptyList()
        }
        items(tabDestinations, key = {"global" + it.ordinal}) {
            Button(onClick = {onClick(it.toGlobalButtonEnum())}, modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                Text(it.name)
            }
        }
    }
}

private fun NavFragmentDestination.toDeepLinkButtonEnum() = when(this) {
    A1 -> ButtonEnum.DEEP_A1
    A2 -> ButtonEnum.DEEP_A2
    A3 -> ButtonEnum.DEEP_A3
    B1 -> ButtonEnum.DEEP_B1
    B2 -> ButtonEnum.DEEP_B2
    B3 -> ButtonEnum.DEEP_B3
    C1 -> ButtonEnum.DEEP_C1
    C2 -> ButtonEnum.DEEP_C2
    C3 -> ButtonEnum.DEEP_C3
}

private fun NavFragmentDestination.toGlobalButtonEnum() = when(this) {
    A1 -> ButtonEnum.GLOBAL_A1
    A2 -> ButtonEnum.GLOBAL_A2
    A3 -> ButtonEnum.GLOBAL_A3
    B1 -> ButtonEnum.GLOBAL_B1
    B2 -> ButtonEnum.GLOBAL_B2
    B3 -> ButtonEnum.GLOBAL_B3
    C1 -> ButtonEnum.GLOBAL_C1
    C2 -> ButtonEnum.GLOBAL_C2
    C3 -> ButtonEnum.GLOBAL_C3
}

fun ButtonEnum.toDestination() = NavFragmentDestination.values().firstOrNull { destination ->
    this == destination.toDeepLinkButtonEnum()
}

@IdRes
fun NavFragmentDestination.destinationResId() = when(this) {
    A1 -> R.id.a1
    A2 -> R.id.a2
    A3 -> R.id.a3
    B1 -> R.id.b1
    B2 -> R.id.b2
    B3 -> R.id.b3
    C1 -> R.id.c1
    C2 -> R.id.c2
    C3 -> R.id.c3
}

@IdRes
fun ButtonEnum.toGlobalAction() = when(this) {
    ButtonEnum.GLOBAL_A1 -> R.id.action_global_a1
    ButtonEnum.GLOBAL_A2 -> R.id.action_global_a2
    ButtonEnum.GLOBAL_A3 -> R.id.action_global_a3
    ButtonEnum.GLOBAL_B1 -> R.id.action_global_b1
    ButtonEnum.GLOBAL_B2 -> R.id.action_global_b2
    ButtonEnum.GLOBAL_B3 -> R.id.action_global_b3
    ButtonEnum.GLOBAL_C1 -> R.id.action_global_c1
    ButtonEnum.GLOBAL_C2 -> R.id.action_global_c2
    ButtonEnum.GLOBAL_C3 -> R.id.action_global_c3
    else -> null
}

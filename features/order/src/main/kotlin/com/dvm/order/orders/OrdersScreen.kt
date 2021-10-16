package com.dvm.order.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dvm.appmenu_api.Drawer
import com.dvm.database.api.models.Order
import com.dvm.order.R
import com.dvm.order.orders.model.OrderStatus
import com.dvm.order.orders.model.OrdersEvent
import com.dvm.order.orders.model.OrdersState
import com.dvm.ui.components.*
import com.dvm.ui.themes.DecorColors
import com.dvm.utils.DrawerItem
import com.dvm.utils.extensions.format
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getStateViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun OrdersScreen(
    viewModel: OrdersViewModel = getStateViewModel()
) {
    val state: OrdersState = viewModel.state
    val onEvent: (OrdersEvent) -> Unit = { viewModel.dispatch(it) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Drawer(
        drawerState = drawerState,
        selected = DrawerItem.ORDERS
    ) {

        val color by rememberSaveable {
            mutableStateOf(DecorColors.values().random())
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalGradient(color.color.copy(alpha = 0.15f))
        ) {
            Spacer(modifier = Modifier.statusBarsHeight())
            DefaultAppBar(
                title = { Text(stringResource(R.string.orders_appbar_title)) },
                navigationIcon = {
                    AppBarIconMenu {
                        scope.launch { drawerState.open() }
                    }
                },
            )

            val pagerState = rememberPagerState(pageCount = 2)

            TabRow(
                selectedTabIndex = state.status.ordinal,
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = contentColorFor(MaterialTheme.colors.surface),
                indicator = { tabPositions ->
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.primary) {
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                        )
                    }

                }
            ) {
                Tab(
                    text = { Text(stringResource(R.string.orders_tab_actual)) },
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        scope.launch {
                            onEvent(OrdersEvent.StatusSelect(OrderStatus.ACTUAL))
                            pagerState.animateScrollToPage(0)
                        }
                    },
                )
                Tab(
                    text = { Text(stringResource(R.string.orders_tab_completed)) },
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        scope.launch {
                            onEvent(OrdersEvent.StatusSelect(OrderStatus.COMPLETED))
                            pagerState.animateScrollToPage(1)
                        }
                    },
                )
            }
            HorizontalPager(pagerState) {
                if (state.empty) {
                    EmptyPlaceholder(
                        resId = R.raw.empty_image,
                        text = stringResource(R.string.orders_empty_placeholder)
                    )
                } else {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(start = 30.dp)
                            .navigationBarsPadding()
                    ) {
                        items(state.orders) { order ->
                            OrderItem(
                                order = order,
                                onOrderClick = { onEvent(OrdersEvent.Order(order.id)) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.progress) {
        LoadingScrim()
    }
}

@OptIn(ExperimentalComposeApi::class)
@Composable
private fun OrderItem(
    order: Order,
    onOrderClick: () -> Unit
) {
    Column(
        Modifier.clickable(onClick = onOrderClick)
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
            Text(
                text = order.createdAt.format(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 15.dp, bottom = 8.dp)
            )
        }
        Row {
            Text(
                text = stringResource(
                    R.string.orders_order_number,
                    order.createdAt.time.toString().take(5)
                ),
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary
            )
            Text(
                text = stringResource(
                    R.string.dish_item_price,
                    order.total
                ),
                modifier = Modifier.padding(end = 8.dp),
                color = MaterialTheme.colors.primary
            )
        }
        Row {
            Text(
                text = order.address,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = order.status,
                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
            )
        }
        Divider()
    }
}
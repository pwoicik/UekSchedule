package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.database.Class
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleClassesColumn(
    classes: List<Class>,
    viewModel: ScheduleViewModel
) {
    var timeNow by remember { mutableStateOf(ZonedDateTime.now()) }
    LaunchedEffect(key1 = timeNow) {
        delay(15_000)
        timeNow = ZonedDateTime.now()
    }

    val swipeRefreshState by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(swipeRefreshState),
        onRefresh = { viewModel.refresh() },
        indicator = { s, trigger ->
            SwipeRefreshIndicator(s, trigger, shape = RoundedCornerShape(50))
        }
    ) {
        LazyColumn {
            stickyHeader {
                ScheduleColumnStickyHeader(classes[0].startDateTime.toLocalDate())
            }
            item {
                ScheduleColumnItem(clazz = classes[0], timeNow)
            }

            for (i in 1..classes.lastIndex) {
                val prevClass = classes[i - 1]
                val nextClass = classes[i]

                if (prevClass.startDate != nextClass.startDate) {
                    stickyHeader {
                        ScheduleColumnStickyHeader(nextClass.startDateTime.toLocalDate())
                    }
                    item {
                        ScheduleColumnItem(clazz = nextClass, timeNow)
                    }
                } else {
                    item {
                        Divider(modifier = Modifier.height(1.dp))
                        ScheduleColumnItem(clazz = nextClass, timeNow)
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleColumnStickyHeader(date: LocalDate) {
    Surface(
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            date.format(DateTimeFormatter.ofPattern("EE dd MMM yyyy")),
            modifier = Modifier.padding(8.dp)
        )
    }
}

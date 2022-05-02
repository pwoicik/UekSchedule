package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class MainNavGraph(val start: Boolean = false)

@MainNavGraph
@NavGraph
annotation class YourGroupsNavGraph(val start: Boolean = false)

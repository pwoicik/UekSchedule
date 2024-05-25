package uekschedule.home.di

import org.koin.dsl.module
import uekschedule.di.presenterFactory
import uekschedule.di.uiFactory
import uekschedule.home.ui.HomePresenter
import uekschedule.home.ui.HomeScreen
import uekschedule.home.ui.HomeState
import uekschedule.home.ui.HomeUi

val HomeModule = module {
    presenterFactory<HomeScreen> { _, navigator -> HomePresenter(navigator) }
    uiFactory<HomeScreen, HomeState> { state, modifier -> HomeUi(state, modifier) }
}

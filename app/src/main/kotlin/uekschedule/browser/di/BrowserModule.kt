package uekschedule.browser.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uekschedule.browser.data.usecase.GetSchedulablesImpl
import uekschedule.browser.data.usecase.SaveScheduleImpl
import uekschedule.browser.domain.usecase.GetSchedulables
import uekschedule.browser.domain.usecase.SaveSchedule
import uekschedule.browser.ui.BrowserPresenter
import uekschedule.browser.ui.BrowserScreen
import uekschedule.browser.ui.BrowserState
import uekschedule.browser.ui.BrowserUi
import uekschedule.di.presenterFactory
import uekschedule.di.uiFactory

val BrowserModule = module {
    presenterFactory<BrowserScreen> { _, navigator -> BrowserPresenter(navigator, get(), get()) }
    uiFactory<BrowserScreen, BrowserState> { state, modifier -> BrowserUi(state, modifier) }

    factoryOf(::GetSchedulablesImpl) bind GetSchedulables::class
    factoryOf(::SaveScheduleImpl) bind SaveSchedule::class
}

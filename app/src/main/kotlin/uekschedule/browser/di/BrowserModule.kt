package uekschedule.browser.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uekschedule.browser.data.usecase.GetSchedularsImpl
import uekschedule.browser.domain.usecase.GetSchedulars
import uekschedule.browser.ui.BrowserPresenter
import uekschedule.browser.ui.BrowserScreen
import uekschedule.browser.ui.BrowserState
import uekschedule.browser.ui.BrowserUi
import uekschedule.di.presenterFactory
import uekschedule.di.uiFactory

val BrowserModule = module {
    presenterFactory<BrowserScreen> { _, navigator -> BrowserPresenter(navigator, get()) }
    uiFactory<BrowserScreen, BrowserState> { state, modifier -> BrowserUi(state, modifier) }

    factoryOf(::GetSchedularsImpl) bind GetSchedulars::class
}

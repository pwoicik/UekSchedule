package uekschedule.schedule.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uekschedule.di.presenterFactory
import uekschedule.di.uiFactory
import uekschedule.schedule.data.usecase.GetScheduleImpl
import uekschedule.schedule.domain.usecase.GetSchedule
import uekschedule.schedule.ui.SchedulePresenter
import uekschedule.schedule.ui.ScheduleScreen
import uekschedule.schedule.ui.ScheduleState
import uekschedule.schedule.ui.ScheduleUi

val ScheduleModule = module {
    presenterFactory<ScheduleScreen> { screen, navigator ->
        SchedulePresenter(screen, navigator, get())
    }
    uiFactory<ScheduleScreen, ScheduleState> { state, modifier -> ScheduleUi(state, modifier) }

    factoryOf(::GetScheduleImpl) bind GetSchedule::class
}

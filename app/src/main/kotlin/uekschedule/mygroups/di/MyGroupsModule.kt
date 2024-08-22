package uekschedule.mygroups.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uekschedule.di.presenterFactory
import uekschedule.di.uiFactory
import uekschedule.mygroups.data.GetSchedulablesImpl
import uekschedule.mygroups.domain.usecase.GetSchedulables
import uekschedule.mygroups.ui.MyGroupsPresenter
import uekschedule.mygroups.ui.MyGroupsScreen
import uekschedule.mygroups.ui.MyGroupsState
import uekschedule.mygroups.ui.MyGroupsUi

val MyGroupsModule = module {
    presenterFactory<MyGroupsScreen> { _, navigator -> MyGroupsPresenter(navigator, get()) }
    uiFactory<MyGroupsScreen, MyGroupsState> { state, modifier -> MyGroupsUi(state, modifier) }

    factoryOf(::GetSchedulablesImpl) bind GetSchedulables::class
}

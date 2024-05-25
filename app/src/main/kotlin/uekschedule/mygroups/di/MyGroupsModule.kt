package uekschedule.mygroups.di

import org.koin.dsl.module
import uekschedule.di.presenterFactory
import uekschedule.di.uiFactory
import uekschedule.mygroups.ui.MyGroupsPresenter
import uekschedule.mygroups.ui.MyGroupsScreen
import uekschedule.mygroups.ui.MyGroupsState
import uekschedule.mygroups.ui.MyGroupsUi

val MyGroupsModule = module {
    presenterFactory<MyGroupsScreen> { _, navigator -> MyGroupsPresenter(navigator) }
    uiFactory<MyGroupsScreen, MyGroupsState> { state, modifier -> MyGroupsUi(state, modifier) }
}

package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

data class ScheduleUseCases(
    val addActivity: AddActivity,
    val addGroup: AddGroup,
    val deleteActivity: DeleteActivity,
    val deleteGroup: DeleteGroup,
    val getActivity: GetActivity,
    val getAllActivities: GetAllActivities,
    val getAllGroups: GetAllGroups,
    val getAllScheduleEntries: GetAllScheduleEntries,
    val getSavedGroups: GetSavedGroups,
    val getSavedGroupsCount: GetSavedGroupsCount,
    val getScheduleForGroup: GetScheduleForGroup,
    val refreshClasses: RefreshClasses,
) {

    constructor(repository: ScheduleRepository) : this(
        AddActivity(repository),
        AddGroup(repository),
        DeleteActivity(repository),
        DeleteGroup(repository),
        GetActivity(repository),
        GetAllActivities(repository),
        GetAllGroups(repository),
        GetAllScheduleEntries(repository),
        GetSavedGroups(repository),
        GetSavedGroupsCount(repository),
        GetScheduleForGroup(repository),
        RefreshClasses(repository),
    )
}

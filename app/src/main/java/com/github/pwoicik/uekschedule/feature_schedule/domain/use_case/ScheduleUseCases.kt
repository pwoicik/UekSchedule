package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

data class ScheduleUseCases(
    val deleteActivity: DeleteActivity,
    val deleteGroup: DeleteGroup,
    val getActivity: GetActivity,
    val getAllActivities: GetAllActivities,
    val getAllGroups: GetAllGroups,
    val getAllScheduleEntries: GetAllScheduleEntries,
    val getSavedGroups: GetSavedGroups,
    val getSavedGroupsCount: GetSavedGroupsCount,
    val getScheduleForGroup: GetScheduleForGroup,
    val saveActivity: SaveActivity,
    val saveGroup: SaveGroup,
    val updateClasses: UpdateClasses,
) {

    constructor(repository: ScheduleRepository) : this(
        DeleteActivity(repository),
        DeleteGroup(repository),
        GetActivity(repository),
        GetAllActivities(repository),
        GetAllGroups(repository),
        GetAllScheduleEntries(repository),
        GetSavedGroups(repository),
        GetSavedGroupsCount(repository),
        GetScheduleForGroup(repository),
        SaveActivity(repository),
        SaveGroup(repository),
        UpdateClasses(repository),
    )
}

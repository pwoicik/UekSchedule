package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

data class ScheduleUseCases(
    val getAllGroups: GetAllGroups,
    val getSavedGroups: GetSavedGroups,
    val getSavedGroupsCount: GetSavedGroupsCount,
    val deleteGroup: DeleteGroup,
    val addGroups: AddGroups,
    val updateClasses: UpdateClasses,
    val getActivity: GetActivity,
    val getAllActivities: GetAllActivities,
    val addActivity: AddActivity,
    val deleteActivity: DeleteActivity,
    val getAllScheduleEntries: GetAllScheduleEntries
) {

    constructor(repository: ScheduleRepository) : this(
        GetAllGroups(repository),
        GetSavedGroups(repository),
        GetSavedGroupsCount(repository),
        DeleteGroup(repository),
        AddGroups(repository),
        UpdateClasses(repository),
        GetActivity(repository),
        GetAllActivities(repository),
        AddActivity(repository),
        DeleteActivity(repository),
        GetAllScheduleEntries(repository)
    )
}

package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

data class ScheduleUseCases(
    val getAllGroups: GetAllGroups,
    val getSavedGroups: GetSavedGroups,
    val getSavedGroupsCount: GetSavedGroupsCount,
    val deleteGroup: DeleteGroup,
    val getAllClasses: GetAllClasses,
    val addGroups: AddGroups,
    val updateClasses: UpdateClasses,
    val getActivity: GetActivity,
    val getAllActivities: GetAllActivities,
    val getAllActivitiesFlow: GetAllActivitiesFlow,
    val addActivity: AddActivity,
    val deleteActivity: DeleteActivity,
    val getAllScheduleEntries: GetAllScheduleEntries
) {

    constructor(repository: ScheduleRepository) : this(
        GetAllGroups(repository),
        GetSavedGroups(repository),
        GetSavedGroupsCount(repository),
        DeleteGroup(repository),
        GetAllClasses(repository),
        AddGroups(repository),
        UpdateClasses(repository),
        GetActivity(repository),
        GetAllActivities(repository),
        GetAllActivitiesFlow(repository),
        AddActivity(repository),
        DeleteActivity(repository),
        GetAllScheduleEntries(repository)
    )
}

package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

data class ScheduleUseCases(
    val getAllGroups: GetAllGroups,
    val getSavedGroups: GetSavedGroups,
    val getSavedGroupsFlow: GetSavedGroupsFlow,
    val deleteGroup: DeleteGroup,
    val getAllClasses: GetAllClasses,
    val addGroups: AddGroups,
    val updateClasses: UpdateClasses
) {

    constructor(repository: ScheduleRepository) : this(
        GetAllGroups(repository),
        GetSavedGroups(repository),
        GetSavedGroupsFlow(repository),
        DeleteGroup(repository),
        GetAllClasses(repository),
        AddGroups(repository),
        UpdateClasses(repository)
    )
}

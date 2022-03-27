package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

class ScheduleUseCases(
    val addSubjectToIgnored: AddSubjectToIgnored,
    val deleteActivity: DeleteActivity,
    val deleteGroup: DeleteGroup,
    val deleteSubjectFromIgnored: DeleteSubjectFromIgnored,
    val getActivity: GetActivity,
    val getAllActivities: GetAllActivities,
    val getAllGroups: GetAllGroups,
    val getAllScheduleEntries: GetAllScheduleEntries,
    val getAllSubjectsForGroup: GetAllSubjectsForGroup,
    val getGroupWithClasses: GetGroupWithClasses,
    val getSavedGroups: GetSavedGroups,
    val getSavedGroupsCount: GetSavedGroupsCount,
    val getScheduleForGroup: GetScheduleForGroup,
    val saveActivity: SaveActivity,
    val saveGroup: SaveGroup,
    val saveGroupWithClasses: SaveGroupWithClasses,
    val updateGroup: UpdateGroup,
    val updateClasses: UpdateClasses,
) {

    constructor(repository: ScheduleRepository) : this(
        AddSubjectToIgnored(repository),
        DeleteActivity(repository),
        DeleteGroup(repository),
        DeleteSubjectFromIgnored(repository),
        GetActivity(repository),
        GetAllActivities(repository),
        GetAllGroups(repository),
        GetAllScheduleEntries(repository),
        GetAllSubjectsForGroup(repository),
        GetGroupWithClasses(repository),
        GetSavedGroups(repository),
        GetSavedGroupsCount(repository),
        GetScheduleForGroup(repository),
        SaveActivity(repository),
        SaveGroup(repository),
        SaveGroupWithClasses(repository),
        UpdateGroup(repository),
        UpdateClasses(repository),
    )
}

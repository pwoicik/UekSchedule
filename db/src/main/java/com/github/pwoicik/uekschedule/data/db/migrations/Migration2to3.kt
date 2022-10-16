package com.github.pwoicik.uekschedule.data.db.migrations

import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec

@RenameTable(
    fromTableName = "groups",
    toTableName = "schedulables"
)
@RenameColumn(
    tableName = "classes",
    fromColumnName = "group_id",
    toColumnName = "schedulable_id"
)
@RenameColumn(
    tableName = "ignored_subjects",
    fromColumnName = "group_id",
    toColumnName = "schedulable_id"
)
@RenameColumn(
    tableName = "ignored_subjects",
    fromColumnName = "group_name",
    toColumnName = "schedulable_name"
)
internal class Migration2to3 : AutoMigrationSpec

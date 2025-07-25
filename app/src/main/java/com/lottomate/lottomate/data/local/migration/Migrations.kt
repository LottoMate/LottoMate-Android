package com.lottomate.lottomate.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `RandomMyNumbers` (
                `key` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `numbers` TEXT NOT NULL,
                `createAt` TEXT NOT NULL
            )
        """.trimIndent())
    }
}
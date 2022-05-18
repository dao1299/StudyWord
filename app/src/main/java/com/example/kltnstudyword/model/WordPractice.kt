package com.example.kltnstudyword.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity(tableName = "word_practice")
class WordPractice constructor(
    @PrimaryKey(autoGenerate = true)
    var wordPracticeId: Long = 0,

    @ColumnInfo(name = "wordId")
    var wordId : Long,

    @ColumnInfo(name = "level")
    var level: Long = 0,

//    @ColumnInfo(name = "timePractice")
//    var timePractice: Time
)
    {
}
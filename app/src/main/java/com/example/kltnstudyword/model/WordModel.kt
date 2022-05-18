package com.example.kltnstudyword.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "word")
class WordModel(
    @PrimaryKey(autoGenerate = true)
    var wordId: Long=0,

    @ColumnInfo(name = "word")
    var word : String="",

    @ColumnInfo(name = "idTopic")
    var topicId: Long = 0,

    @ColumnInfo(name = "partOfSpeech")
    var partOfSpeech : String="",

    @ColumnInfo(name = "exampleEN")
    var exampleEN : String="",

    @ColumnInfo(name = "exampleVN")
    var exampleVN : String="",

    @ColumnInfo(name = "srcImg")
    var srcImg : String="",

    @ColumnInfo(name = "srcAudio")
    var srcAudio: String="",

    @ColumnInfo(name = "phonetic")
    var phonetic: String="",

    @ColumnInfo(name = "synonyms")
    var synonyms: String="",

    @ColumnInfo(name = "level", defaultValue = "0")
    var level : Int = 0,

    @ColumnInfo(name = "practiceTime")
    @TypeConverters(Converters::class)
    var practiceTime : java.util.Date? = null,

    @ColumnInfo(name = "srcImgFb", defaultValue = "")
    var srcImgFb : String?="",

    @ColumnInfo(name = "srcAudioFb", defaultValue = "")
    var srcAudioFb : String?="",

):Serializable {



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WordModel

        if (word != other.word) return false
        if (partOfSpeech != other.partOfSpeech) return false
        if (exampleEN != other.exampleEN) return false
        if (exampleVN != other.exampleVN) return false

        return true
    }

    override fun hashCode(): Int {
        var result = word.hashCode()
        result = 31 * result + partOfSpeech.hashCode()
        result = 31 * result + exampleEN.hashCode()
        result = 31 * result + exampleVN.hashCode()
        return result
    }

    override fun toString(): String {
        return "WordModel(word='$word', partOfSpeech='$partOfSpeech', exampleEN='$exampleEN', exampleVN='$exampleVN', srcImg='$srcImg', srcAudio='$srcAudio', phonetic='$phonetic', synonyms='$synonyms')"
    }


}
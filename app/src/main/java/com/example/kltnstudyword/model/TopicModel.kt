package com.example.kltnstudyword.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Topic")
class TopicModel (

    @PrimaryKey(autoGenerate = true)
    var topicId: Long = 0,

    @ColumnInfo(name = "nameTopic")
    var nameTopic: String="",

    @ColumnInfo(name = "imgTopic")
    var imgTopic: String="",

    @ColumnInfo(name = "isDownloaded")
    var isDownloaded : Int?

): Serializable{
    //    override fun toString(): String {
//        return "TopicModel(topicId=$topicId, nameTopic='$nameTopic', imgTopic='$imgTopic', isDownloaded=$isDownloaded)"
//    }
    override fun toString(): String {
        return "TopicModel(topicId=$topicId, nameTopic='$nameTopic', imgTopic='$imgTopic')"
    }
}
package com.example.kltnstudyword.roomdatabase

import androidx.room.*
import com.example.kltnstudyword.model.TopicModel
import com.example.kltnstudyword.model.WordModel
import java.util.*
import com.example.kltnstudyword.model.Converters

@Dao
interface TopicDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopic(topic: TopicModel?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrReplaceTopic(vararg topic: TopicModel?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTopic(topic: TopicModel?)

    @Query("DELETE FROM Topic")
    fun deleteAll()

    @Query("delete from word")
    fun deleteAllWord()

    @Query("SELECT * FROM Topic")
    fun findAllTopicSync(): List<TopicModel?>?

    @Query("SELECT * FROM word where idTopic = :topicId")
    fun findAllWordSync(topicId: Int): List<WordModel?>?



    fun insertList(list: List<TopicModel?>): Long{
        for (element in list){
            insertTopic(element)
        }
        return 1;
    }
    @Query("SELECT * FROM topic ORDER BY topicId ASC")
    fun getAllTopicInfo(): List<TopicModel>?

    @Query("SELECT COUNT(*) from word where idTopic = :topicId")
    fun getNumber(topicId: Int): Int

    @Query("select count(*) from word")
    fun countWord(): Int

    @Query("select * from word")
    fun findAllWord():List<WordModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWordModel(wordModel: WordModel?)

    @Query("update topic set isDownloaded= :isDownloaded where topicId= :topicId ")
    fun updateDownloadTopic(topicId: Int, isDownloaded : Int): Int

    @Query("select * from topic where topicId= :topicId")
    fun findTopic(topicId: Int): TopicModel

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWordModel(wordModel: WordModel)

    @Query("select  count(DISTINCT idTopic) from word where level>0 ")
    fun countTopicLearned(): Int;

    @Query("select  count(DISTINCT wordId) from word where level>0")
    fun countWordLearned(): Int;

    @Query("select  count(*) from word where level= :level")
    fun countNumLevel(level : Int): Int;

    @Query("select count(*) from word where practiceTime < :now and level>0")
    @TypeConverters(Converters::class)
    fun countWordPractice(now : Date):Int

    @Query("select * from word where practiceTime< :now and level>0 limit 30")
    @TypeConverters(Converters::class)
    fun findListPractice(now : Date):List<WordModel>

    @Query("select idTopic from word where level > 0 group by idTopic ")
    fun findTopicLearned():List<Int>
}
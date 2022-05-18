package com.example.kltnstudyword.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.kltnstudyword.model.TopicModel
import com.example.kltnstudyword.roomdatabase.AppDatabase

class FragmentStudyViewModel(app: Application): AndroidViewModel(app) {
    var allTopic : MutableLiveData<List<TopicModel>> = MutableLiveData()
    init{
        allTopic = MutableLiveData()
        getAllTopic()
    }

    fun getAllUsersObservers(): MutableLiveData<List<TopicModel>> {
        return allTopic
    }

     fun getAllTopic() {
        val topicDao = AppDatabase.getInMemoryDatabase((getApplication()))?.roomDao()
        val list = topicDao?.getAllTopicInfo()
        allTopic.postValue(list)
    }

    fun insertTopicInfo(entity: TopicModel){
        val topicDao = AppDatabase.getInMemoryDatabase((getApplication()))?.roomDao()
        topicDao?.insertTopic(entity)
        getAllTopic()
    }

    fun updateTopicInfo(entity: TopicModel){
        val topicDao = AppDatabase.getInMemoryDatabase(getApplication())?.roomDao()
        topicDao?.updateTopic(entity)
        getAllTopic()
    }


}
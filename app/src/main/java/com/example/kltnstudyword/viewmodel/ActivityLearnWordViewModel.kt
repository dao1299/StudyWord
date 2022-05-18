package com.example.kltnstudyword.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.roomdatabase.AppDatabase

class ActivityLearnWordViewModel(app: Application): AndroidViewModel(app) {
//    var allWord : MutableLiveData<List<WordModel>> = MutableLiveData()
//    init{
//        allWord = MutableLiveData()
//        getAllWord()
//    }
//
//    fun getAllWordObservers(): MutableLiveData<List<WordModel>> {
//        return allWord
//    }
//
//     fun getAllWord() {
//        val wordDao = AppDatabase.getInMemoryDatabase((getApplication()))?.roomDao()
//        val list = wordDao?.findAllWordSync()
//        allWord.postValue(list)
//    }
//
//    fun insertTopicInfo(entity: WordModel){
//        val wordDao = AppDatabase.getInMemoryDatabase((getApplication()))?.roomDao()
//        wordDao?.insertTopic(entity)
//        getAllWord()
//    }
//
//    fun updateTopicInfo(entity: WordModel){
//        val wordDao = AppDatabase.getInMemoryDatabase(getApplication())?.roomDao()
//        wordDao?.updateTopic(entity)
//        getAllWord()
//    }


}
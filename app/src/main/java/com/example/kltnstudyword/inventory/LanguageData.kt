package com.example.kltnstudyword.inventory

import com.example.kltnstudyword.R


class LanguageData {
    fun getLanguageModelList(): List<LanguageModel> {
        val LanguageModelList: MutableList<LanguageModel> = ArrayList()
        LanguageModelList.add(LanguageModel("English", R.drawable.anh))
        LanguageModelList.add(LanguageModel("Việt Nam", R.drawable.vn))
        return LanguageModelList
    }
}
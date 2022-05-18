package com.example.kltnstudyword.model

import java.io.Serializable

class WordResultModel constructor(
    var wordModel: WordModel,
    var result : Boolean
): Serializable {}
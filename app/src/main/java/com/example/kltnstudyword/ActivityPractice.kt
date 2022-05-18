package com.example.kltnstudyword

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.fragment.learn.FragmentQuestionStudyFill
import com.example.kltnstudyword.fragment.learn.FragmentQuestionStudyListen
import com.example.kltnstudyword.fragment.quiz.FragmentQuestionChoosePicture
import com.example.kltnstudyword.fragment.quiz.FragmentQuestionKind3
import com.example.kltnstudyword.fragment.quiz.FragmentQuestionKindObjectiveFillSentence
import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.model.WordResultModel
import com.example.kltnstudyword.roomdatabase.AppDatabase
import kotlinx.android.synthetic.main.activity_study_word.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class ActivityPractice : AppCompatActivity() {

    var listWordNeedToPractice = ArrayList<WordModel>()

    var listCopy = ArrayList<WordModel>()
    var listResult = ArrayList<WordResultModel>()
    var scoreFinal = 0
    var sizeOfList = 0
    var maxProgress = 0
    var previousQuiz = 0;
    lateinit var wordCurrent: WordModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_word)
        getListToPractice()
        addEventButton()
        practice()
    }

    fun addEventButton() {
        btnNext.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_shadow_button_touched)
                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_shadow_button)
            }
            false
        })
        btnNextInResult.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.setBackgroundResource(R.drawable.background_button_next_result_touched)
                MotionEvent.ACTION_UP -> v.setBackgroundResource(R.drawable.background_button_next_result)
            }
            false
        })
        btnCloseStudy.setOnClickListener {
            saveData()
        }
    }



    private fun practice() {
        btnNext.isEnabled=false
        btnNext.setBackgroundResource(R.drawable.background_shadow_button_disable)
        wordCurrent=listWordNeedToPractice[0]
        Log.d("demoPra","hien tai: $wordCurrent")
        var kindOfQuiz = Random.nextInt(1,6)
        do {
            kindOfQuiz = Random.nextInt(1,6)
        } while (kindOfQuiz==previousQuiz)
        previousQuiz=kindOfQuiz
        when (kindOfQuiz){
            1-> practiceQuestionKindObjectiveFillSentence(wordCurrent)
            2-> practiceQuestionKindListen(wordCurrent)
            3-> setFragment(FragmentQuestionStudyFill.newInstance(wordCurrent),"")
            4-> practiceQuestionKind3(wordCurrent)
            5-> practiceQuestionChoosePicture(wordCurrent)
        }
        btnNext.setOnClickListener {
            if (kindOfQuiz==4){
                if (sizeOfList>0){
                    listResult.add(WordResultModel(wordCurrent,true))

                }else{
                    listWordNeedToPractice.shuffle()
                }
                nextWord()
            }else{
                txtWordInResult.text = listWordNeedToPractice[0].word
                txtDefinitionInResult.text = listWordNeedToPractice[0].partOfSpeech
                if (txtResult.text.toString() == "true") {
                    Log.d("objectiveTest", "true")
                    layoutResult.apply {
//                                animation = animatedSlideInFromBottom
                        visibility = View.VISIBLE
                        setBackgroundResource(R.drawable.background_layout_result_true)
                    }
                    btnNextInResult.setTextColor(android.graphics.Color.parseColor("#59d18c"))

                    if (sizeOfList>0){
                        listResult.add(WordResultModel(wordCurrent,true))

                    }else{
                        listWordNeedToPractice.shuffle()
                    }
                } else {
                    Log.d("objectiveTest", "false")
                    layoutResult.apply {
//                                animation = animatedSlideInFromBottom
                        visibility = View.VISIBLE
                        setBackgroundResource(R.drawable.background_layout_result_false)
                    }
                    btnNextInResult.setTextColor(android.graphics.Color.parseColor("#D15959"))
                    listWordNeedToPractice.add(listWordNeedToPractice[0])

                    if (sizeOfList>0){

                        listResult.add(WordResultModel(wordCurrent,false))
                    }else
                        listWordNeedToPractice.shuffle()


//                list.add(wordCurrent)

                    maxProgress++
                }

                btnNextInResult.setOnClickListener {
//                layoutResult.animation = animatedFadeIn
                    nextWord()

                }
            }



        }
    }

    private fun practiceQuestionChoosePicture(wordCurrent: WordModel) {
        val listRandom = randomList(wordCurrent)
        if (listRandom.size == 4) {
            setFragment(FragmentQuestionChoosePicture.newInstance(listRandom), "")
        }else{
            practice()
        }

    }

    private fun nextWord() {
        sizeOfList--;
        if (sizeOfList==0) showFragmentNotiAgain()
        listWordNeedToPractice.removeFirst()
        if (!upgradeProcessbar()){
            layoutResult.apply {
//                    animation = animatedSlideOutToBottom
                layoutResult.visibility = View.GONE
            }
            btnNext.text = "Confirm"
            txtResult.text = ""
            practice()
        }else{

        }
    }

    private fun practiceQuestionKind3(wordCurrent: WordModel) {
        val listRandom = randomList(wordCurrent)
        if (listRandom.size==4) listRandom.removeLast()
        if (listRandom.size==1) practice()
        else setFragment(FragmentQuestionKind3.newInstance(listRandom),"")
    }

    private fun practiceQuestionKindListen(wordCurrent: WordModel) {
        setFragment(FragmentQuestionStudyListen.newInstance(wordCurrent),"")
    }

    private fun practiceQuestionKindObjectiveFillSentence(wordCurrent: WordModel) {
        val listRandom = randomList(listWordNeedToPractice[0])
        if (listRandom.size == 4) {
            setFragment(FragmentQuestionKindObjectiveFillSentence.newInstance(listRandom), "")
        }else{
            practice()
        }
    }

    private fun randomList(wordPractice: WordModel): ArrayList<WordModel> {
        var listRandom = ArrayList<WordModel>(listCopy)
        var listResultRandom = ArrayList<WordModel>()
        listResultRandom.add(wordPractice)
        listRandom.removeIf { wordEle -> wordEle == wordPractice }
        listRandom.shuffle()
        if (listRandom.size>=3){
            listResultRandom.addAll(listRandom.subList(0, 3))
        }else{
            listResultRandom.addAll(listRandom)
        }
        return listResultRandom
    }

    private fun getListToPractice() {
//        listWordNeedToPractice.add(
//            WordModel(
//                "admit",
//                "thừa nhận, cho vào",
//                "She admits being strict with her children.",
//                "Cô ấy thừa nhận đã nghiêm khắc với con cái.",
//                "https://600tuvungtoeic.com/template/english/images/study/admit.jpg"
//            )
//        )
//        listWordNeedToPractice.add(
//            WordModel(
//                "mission",
//                "sứ mệnh, nhiệm vụ",
//                "The nurse explained that the mission of everyone in the unit was to make sure the patients got well as soon as possible.",
//                "Người y tá đã giải thích rằng nhiệm vụ của mọi người trong khoa là phải đảm bảo rằng các bệnh nhân khỏi bệnh càng sớm càng tốt.",
//                "https://600tuvungtoeic.com/template/english/images/study/mission.jpg"
//            )
//        )
//        listWordNeedToPractice.add(
//            WordModel(
//                "result",
//                "kết quả, đáp số",
//                "The scientific results prove that the new procedure is not significantly safer than the traditional one.",
//                "Các kết quả khoa học đã chứng minh rằng thủ tục mới không an toàn hơn đáng kể so với thủ tục truyền thống.",
//                "https://600tuvungtoeic.com/template/english/images/study/result.jpg"
//            )
//        )
//        listWordNeedToPractice.add(
//            WordModel(
//                "usually",
//                "thường thường",
//                "I'm usually home by 6 o'clock.",
//                "Tôi thường về nhà lúc 6 giờ.",
//                "https://600tuvungtoeic.com/template/english/images/study/usually.jpg"
//            )
//        )
//        listWordNeedToPractice.add(
//            WordModel(
//                "aspect",
//                "khía cạnh",
//                "She felt she had looked at the problem from every aspect.",
//                "Cô ấy cảm thấy rằng cô ấy đã nhìn nhận vấn đề từ mọi khía cạnh.",
//                "https://600tuvungtoeic.com/template/english/images/study/aspect.jpg"
//            )
//        )
//        listWordNeedToPractice.add(
//            WordModel(
//                "salary",
//                "lương hàng tháng",
//                "The technician was pleased to have a raise in salary after only six months on the job.",
//                "Người kỹ thuật viên đã hài lòng khi có sự tăng lương chỉ sau 6 tháng làm việc.",
//                "https://600tuvungtoeic.com/template/english/images/study/salary.jpg"
//            )
//        )


        val intent = intent
        if (intent.hasExtra("listPractice")){
            listWordNeedToPractice = intent.getSerializableExtra("listPractice") as ArrayList<WordModel>
        }

        listWordNeedToPractice.shuffle()
        listCopy.addAll(listWordNeedToPractice)
        sizeOfList=listWordNeedToPractice.size
        maxProgress=sizeOfList
        pgbStudy.max=maxProgress
    }

    private fun setFragment(fragment: Fragment, tag: String) =
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.fab_slide_in_from_right, R.anim.fab_slide_out_to_left)
            replace(R.id.containerStudy, fragment, tag)
            commit()
        }

    private fun upgradeProcessbar(): Boolean {
        val calendar = Calendar.getInstance()
        pgbStudy.apply {
            progress = maxProgress-listWordNeedToPractice.size
            max = maxProgress
        }
        return if (listWordNeedToPractice.size==0) {
            saveData()
            true
        } else {
            false
        }
    }
    private fun showFragmentNotiAgain() {
        val tf: Typeface = Typeface.createFromAsset(
            assets,
            "fonts/UVNVan_R.TTF"
        )
        txtScreenAgain.setTypeface(tf)
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator() //add this
        fadeIn.duration = 500

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator() //and this
        fadeOut.startOffset = 2000
        fadeOut.duration = 1000

        val animation = AnimationSet(false) //change to false
        animation.addAnimation(fadeIn)
        animation.addAnimation(fadeOut)
        screenAgain.animation=animation

        val rotation: Animation = AnimationUtils.loadAnimation(this, R.anim.rotate_image_again)
        imgAgainScreenAgain.startAnimation(rotation)
    }

    fun saveData(){
        val mDb = AppDatabase.getInMemoryDatabase(this)
        val intent = Intent(this, ActivityResult::class.java)
        intent.putExtra("listResult", listResult)
        for (wordResult in listResult){
            val calendar = Calendar.getInstance()
            if (wordResult.result){
                when (wordResult.wordModel.level){
                    1-> {
                        wordResult.wordModel.level=2
                        calendar.add(Calendar.MINUTE,2) // change to hour
                        wordResult.wordModel.practiceTime = calendar.time
                    }
                    2->{
                        wordResult.wordModel.level=3
                        calendar.add(Calendar.MINUTE,4) // change to hour
                        wordResult.wordModel.practiceTime = calendar.time
                    }
                    3->{
                        wordResult.wordModel.level=4
                        calendar.add(Calendar.MINUTE,7) // change to hour
                        wordResult.wordModel.practiceTime = calendar.time
                    }
                    4->{
                        wordResult.wordModel.level=4
                        calendar.add(Calendar.MINUTE,10) // change to hour
                        wordResult.wordModel.practiceTime = calendar.time
                    }
                }
            }else{
                calendar.add(Calendar.MINUTE,2) // change to hour
                wordResult.wordModel.practiceTime = calendar.time
            }
            mDb?.roomDao()?.updateWordModel(wordResult.wordModel)
        }
        scoreFinal = 0
        for (wordResult in listResult) {
            if (wordResult.result) scoreFinal++
        }
        val scorePercent: Float = scoreFinal * 100f / listResult.size
//            Log.d("result","$scorePercent")
        intent.putExtra("score", scorePercent)
        startActivity(intent)
    }

}
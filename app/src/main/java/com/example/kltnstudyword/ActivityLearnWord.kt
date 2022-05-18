package com.example.kltnstudyword

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.fragment.learn.FragmentLearnByCard
import com.example.kltnstudyword.fragment.learn.FragmentQuestionStudyFill
import com.example.kltnstudyword.fragment.learn.FragmentQuestionStudyListen

import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.model.WordResultModel
import com.example.kltnstudyword.roomdatabase.AppDatabase
import kotlinx.android.synthetic.main.activity_study_word.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class ActivityLearnWord : AppCompatActivity() {

    var listWordNeedToLearn = ArrayList<WordModel>()
    var listIncorrect = ArrayList<WordModel>()
    var listWordResult = ArrayList<WordResultModel>()
    lateinit var wordAgain: WordModel
    var positionAgain = 0
    var indexList = 0
    lateinit var wordCurrent: WordModel
    var indexListLearnAgain = 0
    var isWordAppear = 0
    var state = 1
    var scoreElement = 0
    var scoreFinal = 0
    var kind = 0 //0: first learn / 1: learn again


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_word)
        addEventButton()
        getListWordNeedToLearn()
        indexList = 0
        if (indexList < listWordNeedToLearn.size) {
            var kindOfQuiz = 0
            wordCurrent = listWordNeedToLearn[indexList]
            setFragment(
                FragmentLearnByCard.newInstance(wordCurrent),
                "learn_by_card"
            )
            btnNext.setOnClickListener {
                when (state) {
                    1 -> {
                        btnNext.text = "Confirm"
                        isWordAppear = 0
                        state++
                        setFragment(
                            FragmentQuestionStudyListen.newInstance(wordCurrent),
                            ""
                        )

                    }
                    2 -> {
//                        state=1;
                        // kiem tra dien tu
                        txtWordInResult.text = wordCurrent.word
                        txtDefinitionInResult.text = wordCurrent.partOfSpeech
                        if (txtResult.text == "true") {
                            layoutResult.apply {
//                                animation = animatedSlideInFromBottom
                                visibility = View.VISIBLE
                                setBackgroundResource(R.drawable.background_layout_result_true)
                            }
                            btnNextInResult.setTextColor(android.graphics.Color.parseColor("#59d18c"))
                            scoreElement = 1
                        } else {
                            layoutResult.apply {
//                                animation = animatedSlideInFromBottom
                                visibility = View.VISIBLE
                                setBackgroundResource(R.drawable.background_layout_result_false)
                            }
                            btnNextInResult.setTextColor(android.graphics.Color.parseColor("#D15959"))
                            listIncorrect.add(wordCurrent)
                            isWordAppear = 1
                            // cau tra loi la sai
                        }
                        btnNextInResult.setOnClickListener {
                            state++
//                            layoutResult.animation = animatedFadeIn
                            layoutResult.apply {
//                                animation = animatedSlideOutToBottom
                                layoutResult.visibility = View.GONE
                            }
                            btnNext.text = "Confirm"
                            txtResult.text = ""
                            setFragment(
                                FragmentQuestionStudyFill.newInstance(wordCurrent),
                                ""
                            )
                        }

                    }
                    3 -> {
                        txtWordInResult.text = wordCurrent.word
                        txtDefinitionInResult.text = wordCurrent.partOfSpeech
                        if (txtResult.text == "true") {
                            layoutResult.apply {
//                                animation = animatedSlideInFromBottom
                                visibility = View.VISIBLE
                                setBackgroundResource(R.drawable.background_layout_result_true)
                            }
                            btnNextInResult.setTextColor(android.graphics.Color.parseColor("#59d18c"))
                            scoreElement++
                        } else {

                            layoutResult.apply {
//                                animation = animatedSlideInFromBottom
                                visibility = View.VISIBLE
                                setBackgroundResource(R.drawable.background_layout_result_false)
                            }
                            btnNextInResult.setTextColor(android.graphics.Color.parseColor("#D15959"))
                            if (isWordAppear == 0) listIncorrect.add(wordCurrent)
                            isWordAppear=0
                            // cau tra loi la sai

                        }

                        if (kind == 1) {
                            if (scoreElement == 2) {
                                listWordResult[positionAgain] = WordResultModel(wordCurrent, true)
                            } else {
                                listWordResult[positionAgain] = WordResultModel(wordCurrent, false)
                            }
                        }else{
                            if (indexList < listWordNeedToLearn.size)
                                if (scoreElement == 2)
                                    listWordResult.add(WordResultModel(wordCurrent, true))
                                else
                                    listWordResult.add(WordResultModel(wordCurrent, false))

                        }
                        scoreElement = 0
                        btnNextInResult.setOnClickListener {
                            state = 1
                            txtResult.text = ""
//                                layoutResult.animation = animatedFadeIn
                            layoutResult.apply {
//                                    animation = animatedSlideOutToBottom
                                layoutResult.visibility = View.GONE
                            }
                            indexList++
                            if (!upgradeProcessbar()) {
                                if (indexList < listWordNeedToLearn.size) {
                                    btnNext.text = "Next"
                                    wordCurrent = listWordNeedToLearn[indexList]
                                    setFragment(
                                        FragmentLearnByCard.newInstance(wordCurrent),
                                        "learn_by_card"
                                    )
                                } else if (indexListLearnAgain < listIncorrect.size) {
                                    if (indexListLearnAgain==0){
                                        showFragmentNotiAgain()
                                    }
                                    btnNext.text = "Next"
                                    wordCurrent = listIncorrect[indexListLearnAgain]
                                    setFragment(
                                        FragmentQuestionStudyFill.newInstance(wordCurrent),
                                        ""
                                    )
                                    state = 3
                                    indexListLearnAgain++
                                }
                            } else {

                            }

                        }

                    }
                }
            }
        }
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
            val intent = Intent(this, ActivityResult::class.java)
            intent.putExtra("score", 0)
            intent.putExtra("listResult",ArrayList<WordResultModel>())
            startActivity(intent)
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

    private fun upgradeProcessbar(): Boolean {
        val calendar = Calendar.getInstance()
        pgbStudy.apply {
            progress = indexList
            max = listWordNeedToLearn.size + listIncorrect.size
        }
        return if (indexList == pgbStudy.max) {
            val mDb = AppDatabase.getInMemoryDatabase(this)
            if (kind==0){
                for (wordResult in listWordResult){
                    wordResult.wordModel.level=1
                    calendar.add(Calendar.MINUTE,2) // change to hour
                    wordResult.wordModel.practiceTime=calendar.time
                    mDb?.roomDao()?.updateWordModel(wordResult.wordModel)
                }
            }
            val intent = Intent(this, ActivityResult::class.java)
            intent.putExtra("listResult", listWordResult)
            scoreFinal = 0
            for (wordResult in listWordResult) {
                if (wordResult.result) scoreFinal++
            }
            val scorePercent: Float = scoreFinal * 100f / listWordResult.size
            intent.putExtra("score", scorePercent)
            startActivity(intent)
            true
        } else {
            false
        }
    }

    private fun setFragment(fragment: Fragment, tag: String) =
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.fab_slide_in_from_right, R.anim.fab_slide_out_to_left)
            replace(R.id.containerStudy, fragment, tag)
            commit()
        }

    private fun getListWordNeedToLearn() {
        val intent = intent
        if (intent.hasExtra("positionAgain")) {
//            val wordResultAgain = intent.getSerializableExtra("wordAgain") as WordResultModel
//            wordAgain = wordResultAgain.wordModel
//            listWordNeedToLearn.add(wordAgain)
            listWordResult = intent.getSerializableExtra("listWordAgain") as ArrayList<WordResultModel>
            positionAgain = intent.getIntExtra("positionAgain", 0)
            listWordNeedToLearn.add(listWordResult[positionAgain].wordModel)
            kind = 1
        } else if(intent.hasExtra("topicId")){
            listWordNeedToLearn = intent.getSerializableExtra("topicId") as ArrayList<WordModel>
//            val index = intent.getIntExtra("topicId",-1)
//            val mDb = AppDatabase.getInMemoryDatabase(this)
//            if (index!=-1) {
//                getListWord(index){
//                    listWordNeedToLearn=it
//                }
//            }
//                listWordNeedToLearn = mDb?.roomDao()?.findAllWordSync(index) as ArrayList<WordModel>
        }
        else
        {
//            listWordNeedToLearn.add(
//                WordModel(
//                    "admit",
//                    "thừa nhận, cho vào",
//                    "She admits being strict with her children.",
//                    "Cô ấy thừa nhận đã nghiêm khắc với con cái.",
//                    "https://600tuvungtoeic.com/template/english/images/study/admit.jpg"
//                )
//            )
//            listWordNeedToLearn.add(
//                WordModel(
//                    "mission",
//                    "sứ mệnh, nhiệm vụ",
//                    "The nurse explained that the mission of everyone in the unit was to make sure the patients got well as soon as possible.",
//                    "Người y tá đã giải thích rằng nhiệm vụ của mọi người trong khoa là phải đảm bảo rằng các bệnh nhân khỏi bệnh càng sớm càng tốt.",
//                    "https://600tuvungtoeic.com/template/english/images/study/mission.jpg"
//                )
//            )
//            listWordNeedToLearn.add(
//                WordModel(
//                    "result",
//                    "kết quả, đáp số",
//                    "The scientific results prove that the new procedure is not significantly safer than the traditional one.",
//                    "Các kết quả khoa học đã chứng minh rằng thủ tục mới không an toàn hơn đáng kể so với thủ tục truyền thống.",
//                    "https://600tuvungtoeic.com/template/english/images/study/result.jpg"
//                )
//            )
//            listWordNeedToLearn.add(
//                WordModel(
//                    "usually",
//                    "thường thường",
//                    "I'm usually home by 6 o'clock.",
//                    "Tôi thường về nhà lúc 6 giờ.",
//                    "https://600tuvungtoeic.com/template/english/images/study/usually.jpg"
//                )
//            )
//            listWordNeedToLearn.add(
//                WordModel(
//                    "aspect",
//                    "khía cạnh",
//                    "She felt she had looked at the problem from every aspect.",
//                    "Cô ấy cảm thấy rằng cô ấy đã nhìn nhận vấn đề từ mọi khía cạnh.",
//                    "https://600tuvungtoeic.com/template/english/images/study/aspect.jpg"
//                )
//            )
//            listWordNeedToLearn.add(
//                WordModel(
//                    "salary",
//                    "lương hàng tháng",
//                    "The technician was pleased to have a raise in salary after only six months on the job.",
//                    "Người kỹ thuật viên đã hài lòng khi có sự tăng lương chỉ sau 6 tháng làm việc.",
//                    "https://600tuvungtoeic.com/template/english/images/study/salary.jpg"
//                )
//            )
        }
    }


}
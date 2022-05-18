package com.example.kltnstudyword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kltnstudyword.fragment.learn.FragmentLearnByCard

import com.example.kltnstudyword.fragment.quiz.FragmentQuestionKindObjectiveFillSentence
import com.example.kltnstudyword.model.WordModel
import kotlinx.android.synthetic.main.activity_study_word.*
import kotlin.random.Random

class StudyActivity : AppCompatActivity() {
    var currentIndex = 0

    // 0:learn 1:practice
    companion object {
        var state = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_word)

        var listWord = ArrayList<WordModel>()
//        listWord.add(
//            WordModel(
//                "admit",
//                "thừa nhận, cho vào",
//                "She admits being strict with her children.",
//                "Cô ấy thừa nhận đã nghiêm khắc với con cái.",
//                "https://600tuvungtoeic.com/template/english/images/study/admit.jpg"
//            )
//        )
//        listWord.add(
//            WordModel(
//                "mission",
//                "sứ mệnh, nhiệm vụ",
//                "The nurse explained that the mission of everyone in the unit was to make sure the patients got well as soon as possible.",
//                "Người y tá đã giải thích rằng nhiệm vụ của mọi người trong khoa là phải đảm bảo rằng các bệnh nhân khỏi bệnh càng sớm càng tốt.",
//                "https://600tuvungtoeic.com/template/english/images/study/mission.jpg"
//            )
//        )
//        listWord.add(
//            WordModel(
//                "result",
//                "kết quả, đáp số",
//                "The scientific results prove that the new procedure is not significantly safer than the traditional one.",
//                "Các kết quả khoa học đã chứng minh rằng thủ tục mới không an toàn hơn đáng kể so với thủ tục truyền thống.",
//                "https://600tuvungtoeic.com/template/english/images/study/result.jpg"
//            )
//        )
//        listWord.add(
//            WordModel(
//                "usually",
//                "thường thường",
//                "I'm usually home by 6 o'clock.",
//                "Tôi thường về nhà lúc 6 giờ.",
//                "https://600tuvungtoeic.com/template/english/images/study/usually.jpg"
//            )
//        )
//        listWord.add(
//            WordModel(
//                "aspect",
//                "khía cạnh",
//                "She felt she had looked at the problem from every aspect.",
//                "Cô ấy cảm thấy rằng cô ấy đã nhìn nhận vấn đề từ mọi khía cạnh.",
//                "https://600tuvungtoeic.com/template/english/images/study/aspect.jpg"
//            )
//        )
//        listWord.add(
//            WordModel(
//                "salary",
//                "lương hàng tháng",
//                "The technician was pleased to have a raise in salary after only six months on the job.",
//                "Người kỹ thuật viên đã hài lòng khi có sự tăng lương chỉ sau 6 tháng làm việc.",
//                "https://600tuvungtoeic.com/template/english/images/study/salary.jpg"
//            )
//        )

        val animatedFadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in)
        val animatedSlideInFromRight: Animation =
            AnimationUtils.loadAnimation(this, R.anim.fab_slide_in_from_right)
        val animatedSlideOut: Animation =
            AnimationUtils.loadAnimation(this, R.anim.fab_slide_out_to_left)
        val animatedSlideInFromBottom: Animation =
            AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom)
        val animatedSlideOutToBottom: Animation =
            AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom)


        animatedFadeIn.duration = 1000L
        animatedSlideInFromRight.duration = 1000L
        animatedSlideOut.duration = 1000L
        animatedSlideInFromBottom.duration = 1000L
        animatedSlideOutToBottom.duration = 1000L

        var kindOfQuiz = 0;
        setFragment(FragmentLearnByCard.newInstance(listWord[currentIndex / 2]), "learn_by_card")
        btnNext.setOnClickListener {
            when (state) {
                1 -> {
                    btnNext.isEnabled = false
                    kindOfQuiz = Random.nextInt(2, 4)
//                    kindOfQuiz = 2
                    when (kindOfQuiz) {
                        1 -> {
                            btnNext.text = "Xác nhận"
//                            setFragment(FragmentQuestionKind1.newInstance(listWord[currentIndex/2]), "")
                        }
                        2 -> {
                            btnNext.text = "Xác nhận"
                            var listSend = ArrayList<WordModel>()
                            for (i in (currentIndex / 2)..(currentIndex + 3)) listSend.add(listWord[i])
                            setFragment(FragmentQuestionKindObjectiveFillSentence.newInstance(listSend), "")
                        }
                        3 -> {
                            btnNext.text = "Tiếp theo"
//                            setFragment(FragmentQuestionKind3.newInstance("h"), "")
                        }
                    }
                    state = 2
                }
                2 -> {
                    when (kindOfQuiz) {
                        1 -> {

                        }
                        2 -> {
                            if (txtResult.text == "true") {
                                layoutResult.apply {
                                    animation = animatedSlideInFromBottom
                                    visibility = View.VISIBLE
                                    setBackgroundResource(R.drawable.background_layout_result_true)
                                }
                                btnNextInResult.setTextColor(android.graphics.Color.parseColor("#59d18c"))
                            } else {
                                layoutResult.apply {
                                    animation = animatedSlideInFromBottom
                                    visibility = View.VISIBLE
                                    setBackgroundResource(R.drawable.background_layout_result_false)
                                }
                                btnNextInResult.setTextColor(android.graphics.Color.parseColor("#D15959"))
                                // cau tra loi la sai
                            }
                            // popup

                            txtDefinitionInResult.text = listWord[currentIndex / 2].partOfSpeech
                            txtWordInResult.text = listWord[currentIndex / 2].word
                            btnNextInResult.setOnClickListener {
                                state = 1
                                layoutResult.animation = animatedFadeIn
                                layoutResult.apply {
                                    animation = animatedSlideOutToBottom
                                    layoutResult.visibility = View.GONE
                                }
                                btnNext.text = "Tiếp theo"
                                setFragment(
                                    FragmentLearnByCard.newInstance(listWord[currentIndex / 2]),
                                    "learn_by_card"
                                )
                            }
                        }
                        3 -> {
                            state = 1
                            btnNext.text = "Tiếp theo"
                            setFragment(
                                FragmentLearnByCard.newInstance(listWord[currentIndex / 2]),
                                "learn_by_card"
                            )
                        }
                    }
                }
            }
            currentIndex += 1
            pgbStudy.apply {
                progress = currentIndex
                max = 10
            }
            if (currentIndex == pgbStudy.max) {
                val intent = Intent(this, ActivityResult::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setFragment(fragment: Fragment, tag: String) =
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.fab_slide_in_from_right, R.anim.fab_slide_out_to_left)
            replace(R.id.containerStudy, fragment, tag)
            commit()
        }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
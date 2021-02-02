package com.example.myawesomequiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE="extraScore";
    private static final long COUNTDOWN_IN_MILLIS=30000;

    private static final String KEY_SCORE="keyScore";
    private static final String KEY_QUESTION_COUNT="keyQuestionCount";
    private static final String KEY_MILLIS_LEFT="keyMillisLeft";
    private static final String KEY_QUESTION_LIST="keyQuestionList";
    private static final String KEY_ANSWERED="keyAnswered";

    private TextView tvScore;
    private TextView tvQuestionCount;
    private TextView tvCountDown;
    private TextView tvQuestion;
    private RadioGroup radioGroup;
    private RadioButton rbOption1;
    private RadioButton rbOption2;
    private RadioButton rbOption3;
    private Button btnConfirm;
    private TextView tvCategory;
    private TextView tvDifficulty;

    private ColorStateList  textColorDefaultRb;
    private ColorStateList TextColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private boolean answered;
    private int score;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        tvScore=(TextView)findViewById(R.id.activity_quiz_tv_score);
        tvQuestionCount=(TextView)findViewById(R.id.activity_quiz_tv_question_count);
        tvCountDown=(TextView)findViewById(R.id.activity_quiz_tv_countdown);
        tvQuestion=(TextView)findViewById(R.id.activity_quiz_tv_question);
        radioGroup=(RadioGroup)findViewById(R.id.activity_quiz_radio_group);
        rbOption1=(RadioButton)findViewById(R.id.activity_quiz_rb_option1);
        rbOption2=(RadioButton)findViewById(R.id.activity_quiz_rb_option2);
        rbOption3=(RadioButton)findViewById(R.id.activity_quiz_rb_option3);
        btnConfirm=(Button)findViewById(R.id.activity_quiz_btn_confirm);
        tvCategory=(TextView)findViewById(R.id.activity_quiz_tv_Category);
        tvDifficulty=(TextView)findViewById(R.id.activity_quiz_tv_difficulty);

        textColorDefaultRb=rbOption1.getTextColors();
        TextColorDefaultCd=tvCountDown.getTextColors();

        Intent intent=getIntent();
        int categoryID=intent.getIntExtra(StartScreenActivity.EXTRA_CATEGORY_ID,0);
        String categoryName=intent.getStringExtra(StartScreenActivity.EXTRA_CATEGORY_NAME);
        String  difficulty=intent.getStringExtra(StartScreenActivity.EXTRA_DIFFICULTY);
        tvCategory.setText("Category: "+categoryName);
        tvDifficulty.setText("Difficulty: "+difficulty);

        if (savedInstanceState==null) {
            QuizDbHelper dbHelper =QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID,difficulty);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);
            showNextQuestion();
        }else {
            questionList=savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            if (questionList==null){
                finish();
            }
            questionCountTotal=questionList.size();
            questionCounter=savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion=questionList.get(questionCounter - 1);
            score=savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis=savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered=savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered){
                startCountDown();
            }else {
                updateCountDownText();
                showSolution();
            }
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered){
                    if (rbOption1.isChecked() || rbOption2.isChecked() || rbOption3.isChecked()){
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizActivity.this,"Please select an answer",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        rbOption1.setTextColor(textColorDefaultRb);
        rbOption2.setTextColor(textColorDefaultRb);
        rbOption3.setTextColor(textColorDefaultRb);
        radioGroup.clearCheck();

        if (questionCounter<questionCountTotal){
            currentQuestion=questionList.get(questionCounter);
            tvQuestion.setText(currentQuestion.getQuestion());
            rbOption1.setText(currentQuestion.getOption1());
            rbOption2.setText(currentQuestion.getOption2());
            rbOption3.setText(currentQuestion.getOption3());
            questionCounter++;
            tvQuestionCount.setText("Question: "+questionCounter+"/"+questionCountTotal);
            answered=false;
            btnConfirm.setText("Cofirm");
            timeLeftInMillis=COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else {
            finishQuiz();
        }
    }

    private void startCountDown(){
        countDownTimer= new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText(){
        int miutes= (int) ((timeLeftInMillis/1000)/60);
        int seconds= (int) ((timeLeftInMillis/1000)%60);
        String  timeFormatted=String.format(Locale.getDefault(),"%02d:%02d",miutes,seconds);
        tvCountDown.setText(timeFormatted);

        if (timeLeftInMillis<10000){
            tvCountDown.setTextColor(Color.RED);
        }else {
            tvCountDown.setTextColor(TextColorDefaultCd);
        }
    }


    private void checkAnswer(){
        answered=true;
        countDownTimer.cancel();
        RadioButton rbSelectedId=findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNr=radioGroup.indexOfChild(rbSelectedId) + 1;
        if (answerNr==currentQuestion.getAnswerNr()){
        score++;
        tvScore.setText("Score: "+score);
        }
        showSolution();
    }

    private void showSolution() {
        rbOption1.setTextColor(Color.RED);
        rbOption2.setTextColor(Color.RED);
        rbOption3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()){
            case 1: rbOption1.setTextColor(Color.GREEN);
                    tvQuestion.setText("Answer 1 is Correct");
                    break;
            case 2: rbOption2.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 2 is Correct");
                break;
            case 3: rbOption3.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 3 is Correct");
                break;
        }
        if (questionCounter<questionCountTotal){
            btnConfirm.setText("Next");
        }else{
            btnConfirm.setText("Finish");
        }
    }

    private void finishQuiz() {
        Intent resultIntent=new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }else {
            Toast.makeText(this," Press back again to finish",Toast.LENGTH_SHORT).show();
        }
        backPressedTime=System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SCORE,score);
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putLong(KEY_MILLIS_LEFT,timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED,answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,questionList);

    }
}
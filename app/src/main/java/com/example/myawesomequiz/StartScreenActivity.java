package com.example.myawesomequiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StartScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ=1;
    public static final String SHARED_PREFS="sharedPrefs";
    public static final String KEY_HIGHSCORE="keyHighscore";
    public static final String EXTRA_DIFFICULTY="extraDifficulty";
    public static final String EXTRA_CATEGORY_ID="extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME="extraCategoryName";

    private TextView tvHighscore;
    private Spinner spinnerDifficulty;
    private Spinner spinnerCategory;
    private int highscore;
    Button startBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
        startBtn=(Button)findViewById(R.id.activity_starting_screen_btn_start);
        tvHighscore=(TextView)findViewById(R.id.activity_starting_screen_tv_highScore);
        spinnerDifficulty=(Spinner)findViewById(R.id.activity_starting_screen_spinner_difficulty);
        spinnerCategory=(Spinner)findViewById(R.id.activity_starting_screen_spinner_category);

        loadCategories();
        loadDifficultyLevels();
        loadHighscore();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        /*Question q7=new Question("Programming Hard: what is the component of computer?",
                "cpu","keyboard","mouse",1,Question.DIFFICULTY_HARD,Category.PROGRAMMING);
        QuizDbHelper.getInstance(this).addQuestion(q7);*/

    }

    private void startQuiz(){
        Category selectedCategory= (Category) spinnerCategory.getSelectedItem();
        int categoryID=selectedCategory.getId();
        String categoryName=selectedCategory.getName();
        String difficulty=spinnerDifficulty.getSelectedItem().toString();
        Intent intent=new Intent(StartScreenActivity.this,QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID,categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME,categoryName);
        intent.putExtra(EXTRA_DIFFICULTY,difficulty);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE_QUIZ){
            if (resultCode== RESULT_OK){
                int score=data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
                if (score>highscore){
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadCategories(){
        QuizDbHelper dbHelper= QuizDbHelper.getInstance(this);
        List<Category> categories=dbHelper.getAllCategories();
        ArrayAdapter<Category> adapterCategories=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }

    private void loadDifficultyLevels(){
        String [] difficultyLevels=Question.getAllDifficultyLevels();
        ArrayAdapter<String> adapterDifficulty=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }

    private void loadHighscore(){
        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highscore=prefs.getInt(KEY_HIGHSCORE,0);
        tvHighscore.setText("Highscore: "+highscore);
    }

    private void updateHighscore(int highscoreNew) {
        highscore=highscoreNew;
        tvHighscore.setText("Highscore: "+ highscore);

        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putInt(KEY_HIGHSCORE,highscore);
        editor.apply();
    }

}
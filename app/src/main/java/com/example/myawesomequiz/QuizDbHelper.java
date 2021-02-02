package com.example.myawesomequiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myawesomequiz.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " + CategoriesTable.TABLE_NAME + " ( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " + QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT,  " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ")REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";


        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Programming");
        insertCategory(c1);
        Category c2 = new Category("Geography");
        insertCategory(c2);
        Category c3 = new Category("Math");
        insertCategory(c3);
    }

    public void addCategory(Category category) {
        db = getWritableDatabase();
        insertCategory(category);
    }

    public void addCategories(List<Category> categories) {
        db = getWritableDatabase();
        for (Category category : categories) {
            insertCategory(category);
        }
    }

    private void insertCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, values);
    }

    private void fillQuestionsTable() {
        //Easy Programming Questions
        Question q1 = new Question("What does the expression float a = 35 / 0 return?", "0", "Infinity",
                "Run time exception", 2, Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q1);
        Question q2 = new Question("Which of the following for loop declaration is not valid?",
                "for ( int i = 99; i >= 0; i / 9 )", "for ( int i = 7; i <= 77; i += 7 )",
                "for ( int i = 2; i <= 20; i = 2* i )", 1, Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q2);
        Question q3=new Question("Which keyword is used for accessing the features of a package?","package",
                "import","export",2,Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        insertQuestion(q3);
        Question q4=new Question("Which one of the following is not a primitive datatype?","short",
                "long","class",3, Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        insertQuestion(q4);
        Question q5=new Question("Which of the following operators has more precedence in Java?","*",
                "+","-",1, Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        insertQuestion(q5);

        //Medium Programming Questions
        Question q6 = new Question("Evaluate the following Java expression, if x=3, y=5, and z=10:\n" +
                "\n" + "++z + y - y + z + x++", "24", "23", "20",
                1, Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q6);
        Question q7=new Question("What do you mean by nameless objects?",
                "An object of a superclass created in the subclass.",
                "An object without having any name but having a reference.","An object that has no reference.",
                3,Question.DIFFICULTY_MEDIUM,Category.PROGRAMMING);
        insertQuestion(q7);
        Question q8=new Question("Which of these classes are the direct subclasses of the Throwable class?",
                "RuntimeException and Error class", "Exception and VirtualMachineError class",
                "Error and Exception class",3,Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q8);
        Question q9=new Question("What is the initial capacity of the ArrayList?","5","10","0",
                2,Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q9);
        Question q10=new Question("Which of the following is not an operator in Java?"," ^","<->",
                " ~",2, Question.DIFFICULTY_MEDIUM,Category.PROGRAMMING);
        insertQuestion(q10);

        //Hard Programming Questions
        Question q11=new Question("In which memory a String is stored, when we create a string using new operator?",
                "Stack", "String memory","Heap memory",3,
                Question.DIFFICULTY_HARD,Category.PROGRAMMING);
        insertQuestion(q11);
        Question q12=new Question("How many threads can be executed at a time?","Only one thread",
                "Multiple threads","Only main (main() method) thread",2,
                Question.DIFFICULTY_HARD,Category.PROGRAMMING);
        insertQuestion(q12);
        Question q13=new Question("In character stream I/O, a single read/write operation performs",
                "Two bytes read/write at a time.","Eight bytes read/write at a time.",
                "One byte read/write at a time.",1,Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q13);
        Question q14=new Question("Select Odd one out from these about local variables",
                " Local variables are created when the method, constructor or block is entered",
                "the variable will be destroyed once it exits the method, constructor, or block",
                "We can't create reference variables of Local variables",3,
                Question.DIFFICULTY_HARD,Category.PROGRAMMING);
        insertQuestion(q14);
        Question q15=new Question("What do you mean by >>> operator in Java?","Right Shift Operator",
                "Zero Fill Right Shift", "Zero Fill Left Shift",2,
                Question.DIFFICULTY_HARD,Category.PROGRAMMING);
        insertQuestion(q15);

        //Easy Geography Questions
        Question q16=new Question("What is the largest country in the world (by area)?","Russia",
                "China","Canda",1, Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        insertQuestion(q16);
        Question q17=new Question("Muscat is the capital of which country?","Yemen",
                "Jordan", "Oman",3, Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        insertQuestion(q17);
        Question q18=new Question(" How many countries are in Africa?","47","54", "51",
                2,Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        insertQuestion(q18);
        Question q19=new Question("Language of Angola","Spanish","French","Portuguese",
                3,Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(q19);
        Question q20=new Question(" Which African country has the largest population?","Nigeria",
                "Egypt","Congo DR",1, Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        insertQuestion(q20);

        //Medium Geography Questions
        Question q21=new Question("What is the 'Radius' of the Earth (in kilometres)?","6,371 Km",
                "8,284 Km","4,772 Km",1, Question.DIFFICULTY_MEDIUM,Category.GEOGRAPHY);
        insertQuestion(q21);
        Question q22=new Question("How long is the border between Belgium and Germany?","160 Km",
                "147 Km","133 Km",3, Question.DIFFICULTY_MEDIUM,Category.GEOGRAPHY);
        insertQuestion(q22);
        Question q23=new Question("What is the largest country without a river?","Mangolia",
                "Saudi Arabia","Spain",2, Question.DIFFICULTY_MEDIUM,Category.GEOGRAPHY);
        insertQuestion(q23);
        Question q24=new Question("Which US State has the most 'Active Volcanoes'?","Alaska",
                "Hawaii","California",1, Question.DIFFICULTY_MEDIUM,Category.GEOGRAPHY);
        insertQuestion(q24);
        Question q25=new Question("What is the fastest flowing river in the World?","Congo",
                "Mississippi","Amazon", 3,Question.DIFFICULTY_MEDIUM,Category.GEOGRAPHY);
        insertQuestion(q25);

        //Hard Geography Questions
        Question q26=new Question("Which of the following countries does NOT have a population exceeding 200 million?",
                "Russia","indonesia", "Pakistan",1,Question.DIFFICULTY_HARD,Category.GEOGRAPHY);
        insertQuestion(q26);
        Question q27=new Question("Which of the following countries is NOT a member state of the European Union?",
                "Finland", "Norway","Denmark",2,Question.DIFFICULTY_HARD,Category.GEOGRAPHY);
        insertQuestion(q27);
        Question q28=new Question("The largest lake in the UK is?","Lake Windermere","Loch Lomond",
                "Lough Neagh",3,Question.DIFFICULTY_HARD,Category.GEOGRAPHY);
        insertQuestion(q28);
        Question q29=new Question("The sea between Australia and New Zealand is named?","Tasman sea",
                "Hartog sea","Dampier sea",1, Question.DIFFICULTY_HARD,Category.GEOGRAPHY);
        insertQuestion(q29);
        Question q30=new Question("What is the maximum depth of Lake Victoria?","190 meters",
                "81 meters","122 meters",2, Question.DIFFICULTY_HARD,Category.GEOGRAPHY);
        insertQuestion(q30);

        //Easy Math Questions
        Question q31=new Question("If David’s age is 27 years old in 2011. What was his age in 2003?","17 years",
                "19 years"," 20 years", 2,Question.DIFFICULTY_EASY,Category.MATH);
        insertQuestion(q31);
        Question q32=new Question("What is 7% equal to?","0.007","0.7","0.07",
                3,Question.DIFFICULTY_EASY,Category.MATH);
        insertQuestion(q32);
        Question q33=new Question("What is the square of 15?","225","252","30",
                1,Question.DIFFICULTY_EASY,Category.MATH);
        insertQuestion(q33);
        Question q34=new Question("In a century how many months are there?","120","1200",
                "12000",2,Question.DIFFICULTY_EASY,Category.MATH);
        insertQuestion(q34);
        Question q35=new Question(" What is 1004 divided by 2?","520","52","502",
                3,Question.DIFFICULTY_EASY,Category.MATH);
        insertQuestion(q35);

        //Medium Math Questions
        Question q36=new Question("The number of 3-digit numbers divisible by 6","150","151",
                "166",1,Question.DIFFICULTY_MEDIUM,Category.MATH);
        insertQuestion(q36);
        Question q37=new Question("A clock strikes once at 1 o’clock, twice at 2 o’clock, thrice at 3 o’clock and so on." +
                " How many times will it strike in 24 hours?", "78","156","196",
                2,Question.DIFFICULTY_MEDIUM,Category.MATH);
        insertQuestion(q37);
        Question q38=new Question("106 × 106 – 94 × 94 = ?","1906","2004","2400",
                3,Question.DIFFICULTY_MEDIUM,Category.MATH);
        insertQuestion(q38);
        Question q39=new Question("Which of the following numbers gives 240 when added to its own square?",
                "15","16","18",1,Question.DIFFICULTY_MEDIUM,Category.MATH);
        insertQuestion(q39);
        Question q40=new Question("4505 ÷ 5 = ","190","901","910",
                2,Question.DIFFICULTY_MEDIUM,Category.MATH);
        insertQuestion(q40);

        //Hard Math Questions
        Question q41=new Question("The average of first 50 natural numbers is","25.00","25.30",
                "25.50",3,Question.DIFFICULTY_HARD,Category.MATH);
        insertQuestion(q41);
        Question q42=new Question("The simplest form of 1.5 : 2.5 is","3 : 5","6 : 10",
                "15 : 25",1,Question.DIFFICULTY_HARD,Category.MATH);
        insertQuestion(q42);
        Question q43=new Question("The square root of 0.0081 is","0.9","0.09",
                "0.009",2,Question.DIFFICULTY_HARD,Category.MATH);
        insertQuestion(q43);
        Question q44=new Question("The cube root of 1331 is","19","13","11",
                3,Question.DIFFICULTY_HARD,Category.MATH);
        insertQuestion(q44);
        Question q45=new Question("The wages of 10 workers for a six-day week is $ 1200." +
                " What are the one day’s wages of 4 workers?", "$ 40","$ 80","$ 32",
                2,Question.DIFFICULTY_HARD,Category.MATH);
        insertQuestion(q45);


    }

    public void addQuestion(Question question) {
        db = getWritableDatabase();
        insertQuestion(question);
    }

    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();
        for (Question question : questions) {
            insertQuestion(question);
        }
    }

    private void insertQuestion(Question question) {
        ContentValues values = new ContentValues();
        values.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        values.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        values.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        values.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        values.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        values.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        values.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryId());
        db.insert(QuestionsTable.TABLE_NAME, null, values);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(CategoriesTable._ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryId(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " + " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectioArgs = new String[]{String.valueOf(categoryID), difficulty};
        Cursor cursor = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectioArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryId(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }
}

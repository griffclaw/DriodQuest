package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mBackButton;
    private Button mDeceitButton;

    private TextView mQuestionTextView;
    private Question[] mQuestionsBank = new Question[] {
            new Question(R.string.question_android, true),
            new Question(R.string.question_linear, false),
            new Question(R.string.question_servise, false),
            new Question(R.string.question_res, true),
            new Question(R.string.question_manifest, true),
            new Question(R.string.question_q1, false),
            new Question(R.string.question_q2, false),
            new Question(R.string.question_q3, true),
            new Question(R.string.question_q4, true),
            new Question(R.string.question_q5, true),

    };
    private int mCurrentIndex = 0;
    private boolean mIsDeceiter;

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() вызван");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() вызван");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() вызван");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() вызван");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() вызван");
    }


    @SuppressLint("MissingInflatedId")
    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsDeceiter) {
            messageResId = R.string.judment_toast; }
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
    }
    private static final int REQUEST_CODE_DECEIT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) вызван");
        setContentView(R.layout.activity_main);
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(true);
            }
        });
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(false);
            }
        });
        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                mIsDeceiter = false;
                updateQuestion();
            }
        });

            mBackButton = (Button)findViewById(R.id.back_button);
            mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentIndex == 0) {
                        mBackButton.setVisibility(View.INVISIBLE);
                    } else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionsBank.length;
                    mBackButton.setVisibility(View.VISIBLE);
                    updateQuestion();
                    }
                }
            });
            mDeceitButton = (Button)findViewById(R.id.deceit_button);
            mDeceitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean answerIsTrue = mQuestionsBank[mCurrentIndex] .isAnswerTrue();
                    Intent i = DeceitActivity.newIntent(MainActivity.this, answerIsTrue);
                    startActivityForResult(i, REQUEST_CODE_DECEIT);
                }
            });
            if (savedInstanceState != null) {
                mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            }
        updateQuestion();
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
        return;
    } if (requestCode == REQUEST_CODE_DECEIT) {
        if (data == null) {
            return;
        }
        mIsDeceiter = DeceitActivity.wasAnswerShown(data);
    }
    }
        @Override
        public void onSaveInstanceState(Bundle saveInstanceState) {
            super.onSaveInstanceState(saveInstanceState);
            Log.i(TAG, "onSaveInstanceState");
            saveInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        }

    }
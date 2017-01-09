package com.example.q.wifitest;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by q on 2017-01-08.
 */

public class VoiceFollower extends AppCompatActivity {
    static final int PERMISSIONS_REQUEST_INTERNET = 77;
    static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 78;
    private final Integer[] hard_sentences = {
            R.string.hard_sentence1, R.string.hard_sentence2, R.string.hard_sentence3,
            R.string.hard_sentence4, R.string.hard_sentence5, R.string.hard_sentence6,
            R.string.hard_sentence7, R.string.hard_sentence8, R.string.hard_sentence9,
            R.string.hard_sentence10, R.string.hard_sentence11, R.string.hard_sentence12
    };
    private ArrayList<Integer> questionList;
    private Intent i;
    private SpeechRecognizer mRecognizer;
    private TextView question;
    private TextView answer;
    private Button button;
    private int score;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicefollower);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);

        question = (TextView) findViewById(R.id.voicefollower_question);
        answer = (TextView) findViewById(R.id.voicefollower_text);

        button = (Button) findViewById(R.id.voicefollower_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setClickable(false);
                if (questionList.size() == 0)
                    questionList = new ArrayList<Integer>(Arrays.asList(hard_sentences));
                int questionIndex = new Random().nextInt(questionList.size());
                question.setText(questionList.get(questionIndex));
                questionList.remove(questionIndex);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getVoice();
            }
        });

        score = 0;
        questionList = new ArrayList<Integer>(Arrays.asList(hard_sentences));
    }

    private void getVoice() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.INTERNET}, PERMISSIONS_REQUEST_INTERNET);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            mRecognizer.startListening(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_INTERNET || requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getVoice();
            } else {
                Toast.makeText(VoiceFollower.this, "Until you grant the permission, we cannot recognize your voice", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            answer.setText("준비하시고...");
        }

        @Override
        public void onBeginningOfSpeech() {
            answer.setText("지금!");
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            answer.setText("인식 중입니다...");
        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String[] rs = new String[result.size()];
            result.toArray(rs);
            answer.setText("결과 : [" + rs[0] + "]");

            new CheckIfCorrect().execute(rs[0], question.getText().toString());
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    private class CheckIfCorrect extends AsyncTask<String, Boolean, Boolean> {
        @Override
        public Boolean doInBackground(String... params) {
            Boolean success = null;
            String answerStr = params[0];
            String questionStr = params[1];
            questionStr = questionStr.replaceAll(" ", "");
            answerStr = answerStr.replaceAll(" ", "");
            answerStr = answerStr.replaceAll("6", "육");
            answerStr = answerStr.replaceAll("8", "팔");
            if (questionStr.compareTo(answerStr) == 0) {
                success = true;
                publishProgress(success);
                score += 50;
            } else {
                success = false;
                publishProgress(success);
                if (mRecognizer != null)
                    mRecognizer.destroy();
            }

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onProgressUpdate(Boolean... params) {
            if (params[0])
                question.setText("정답입니다!");
            else {
                question.setText("오답입니다...");
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Intent i = new Intent(VoiceFollower.this, ClickFollowerGameOver.class);
                i.putExtra("score", score);
                startActivity(i);
                finish();
            } else {
                question.setText("Follow this!");
                answer.setText("위 문장을 따라서 말하세요.");
                button.setClickable(true);
            }
        }
    }
}

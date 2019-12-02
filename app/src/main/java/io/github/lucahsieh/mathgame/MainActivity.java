package io.github.lucahsieh.mathgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int right;
    private int left;
    private int operation; // +:0 -:1 *:2 /:3
    private int ans;
    private int score;
    private int questionNum;
    private boolean gameContinue;
    TextView questionTV;
    TextView statusTV;
    Button[] options;
    Button resetBtn;

    public static final String CHANNEL_1_ID = "channel1";
    private NotificationManagerCompat notificationMgmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTV = findViewById(R.id.questionTV);
        statusTV = findViewById(R.id.statusTV);
        resetBtn = findViewById(R.id.resetBtn);
        options= new Button[3];
        options[0] = findViewById(R.id.option1TV);
        options[1] = findViewById(R.id.option2TV);
        options[2] = findViewById(R.id.option3TV);

        reset();

        createNotificationChannels();
        notificationMgmt = NotificationManagerCompat.from(this);

    }

    private void renderQuestionOptions(){
        //render status
        String status = score + "/" + questionNum;
        statusTV.setText(status);
        //render question.
        String question = "" +( questionNum + 1) +".  " + left;
        switch (operation){
            case 0 :
                question += (" + ");break;
            case 1 :
                question += (" - ");break;
            case 2 :
                question += (" * ");break;
            case 3 :
                question += (" / ");break;
        }
        question += right;
        questionTV.setText(question);

        // set/render options
        Random ran = new Random();
        int rightAnsIndex = ran.nextInt(3);
        for(int i = 0; i < options.length; i++){
            if(i == rightAnsIndex){
                options[i].setText(ans+"");
                options[i].setOnClickListener(new View.OnClickListener() {
                    // clicks right ans
                    @Override
                    public void onClick(View v) {
                        questionNum++;
                        score++;
                        nextQuestion();
                    }
                });
                continue;
            }
            options[i].setText(ran.nextInt(400)+"");
            options[i].setOnClickListener(new View.OnClickListener() {
                //clicks wrong ans
                @Override
                public void onClick(View v) {
                    questionNum++;
                    nextQuestion();
                }
            });
        }
    }

    public void reset(View view){
        reset();
    }

    public void reset(){
        score = 0;
        questionNum = 0;
        generateQuestion();
        renderQuestionOptions();
    }

    public void nextQuestion(){
        if(questionNum >= 5) {
            //game ends;
            //TODO: notification.
            makeNotice();
//            Toast.makeText(MainActivity.this,"GameEnds," + score +"/"+questionNum, Toast.LENGTH_LONG);
            reset();
            return;
        }
        generateQuestion();
        renderQuestionOptions();
    }


    private void generateQuestion(){
        Random random = new Random();
        this.right = random.nextInt(101);
        this.left = random.nextInt(101);
        this.operation = random.nextInt(4);
        switch (operation){
            case 0:
                ans = (right + left);
                break;
            case 1:
                if((left - right) < 0){
                    int temp = left;
                    left = right;
                    right = temp;
                }
                ans = left - right;
                break;
            case 2:
                ans = (left * right);
                break;
            case 3:
                if(right == 0)
                    right = 1;
                while (left % right != 0){
                    right--;
                }
                ans = left / right;
                break;
        }
    }

    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("to show your score");

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel1);
        }
    }

    private void makeNotice(){
        String appName = getResources().getString(R.string.app_name);
        String scoreTx = getResources().getString(R.string.yourScore);
        Notification n = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(appName)
                .setContentText(scoreTx+" "+score+" / "+questionNum)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationMgmt.notify(1,n);

    }
}

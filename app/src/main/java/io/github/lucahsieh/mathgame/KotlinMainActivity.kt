package io.github.lucahsieh.mathgame


import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import java.util.Random

class KotlinMainActivity : AppCompatActivity() {

    private var right: Int = 0
    private var left: Int = 0
    private var operation: Int = 0 // +:0 -:1 *:2 /:3
    private var ans: Int = 0
    private var score: Int = 0
    private var questionNum: Int = 0
    private val gameContinue: Boolean = false
    internal var questionTV: TextView? = null
    internal var statusTV: TextView? = null
    internal var options: Array<Button> = arrayOf<Button>()
    internal var resetBtn: Button? = null
    private var notificationMgmt: NotificationManagerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_main)


        questionTV = findViewById(R.id.questionTV)
        statusTV = findViewById(R.id.statusTV)
        options = arrayOf(findViewById(R.id.option1TV), findViewById(R.id.option2TV),findViewById(R.id.option3TV))
        resetBtn = findViewById(R.id.resetBtn)

        reset()

        createNotificationChannels()
        notificationMgmt = NotificationManagerCompat.from(this)

    }

    private fun renderQuestionOptions() {
        //render status
        val status = "$score/$questionNum"
        statusTV?.text = status
        //render question.
        var question = "" + (questionNum + 1) + ".  " + left
        when (operation) {
            0 -> question += " + "
            1 -> question += " - "
            2 -> question += " * "
            3 -> question += " / "
        }
        question += right
        questionTV?.text = question

        // set/render options
        val ran = Random()
        val rightAnsIndex = ran.nextInt(3)
        for (i in options.indices) {
            if (i == rightAnsIndex) {
                options[i].text = ans.toString() + ""
                options[i].setOnClickListener {
                    questionNum++
                    score++
                    nextQuestion()
                }
                continue
            }
            options[i].text = ran.nextInt(400).toString() + ""
            options[i].setOnClickListener {
                questionNum++
                nextQuestion()
            }
        }
    }

    fun reset(view: View) {
        reset()
    }

    fun reset() {
        score = 0
        questionNum = 0
        generateQuestion()
        renderQuestionOptions()
    }

    fun nextQuestion() {
        if (questionNum >= 5) {
            //game ends;
            //TODO: notification.
            makeNotice()
            //            Toast.makeText(MainActivity.this,"GameEnds," + score +"/"+questionNum, Toast.LENGTH_LONG);
            reset()
            return
        }
        generateQuestion()
        renderQuestionOptions()
    }


    private fun generateQuestion() {
        val random = Random()
        this.right = random.nextInt(101)
        this.left = random.nextInt(101)
        this.operation = random.nextInt(4)
        when (operation) {
            0 -> ans = right + left
            1 -> {
                if (left - right < 0) {
                    val temp = left
                    left = right
                    right = temp
                }
                ans = left - right
            }
            2 -> ans = left * right
            3 -> {
                if (right == 0)
                    right = 1
                while (left % right != 0) {
                    right--
                }
                ans = left / right
            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "to show your score"

            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel1)
        }
    }

    private fun makeNotice() {
        val appName = resources.getString(R.string.app_name)
        val scoreTx = resources.getString(R.string.yourScore)
        val n = NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(appName)
                .setContentText("$scoreTx $score / $questionNum")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build()
        notificationMgmt!!.notify(1, n)

    }

    companion object {

        val CHANNEL_1_ID = "channel1"
    }
}
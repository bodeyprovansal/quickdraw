package com.example.quickdraw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button ideaButton;
    private ImageButton colorBtn1, colorBtn2, colorBtn3;
    private TextView ideaText, timerText;
    private DrawView drawView;
    private int[] ideaArr;
    private int newColor, time;
    private Drawable btnBackground;
    private CountDownTimer timer;
    private boolean firstRound = true;
    private Random rand;
    //Storing our drawing prompts in order
    {
        ideaArr = new int[]{R.string.string1, R.string.string2, R.string.string3,
                R.string.string4, R.string.string5, R.string.string6, R.string.string7,
                R.string.string8, R.string.string8, R.string.string9, R.string.string10,
                R.string.string11, R.string.string12, R.string.string13, R.string.string14,
                R.string.string15, R.string.string16, R.string.string17, R.string.string18,
                R.string.string19, R.string.string20, R.string.string21, R.string.string22,
                R.string.string23, R.string.string24, R.string.string25, R.string.string26};
    }
    //Storing possible colors for each button (in this case, colorBtn1 = primary,
    //colorBtn2 = secondary, and colorBtn3 = shading)
    private int[] primColorArr = {R.color.red, R.color.blue, R.color.yellow};
    private int[] secdColorArr = {R.color.green, R.color.orange, R.color.purple};
    private int[] shadeColorArr = {R.color.white, R.color.black, R.color.light_slate_gray, R.color.dark_gray};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Start our Board, UI and Timer
        init();
        makeTimer();
    }

    public void init()
    {
        drawView = findViewById(R.id.drawing_board);
        rand = new Random();
        //Match Components with Objects
        ideaButton = findViewById(R.id.idea_btn);
        colorBtn1 = findViewById(R.id.color_1);
        colorBtn2 = findViewById(R.id.color_2);
        colorBtn3 = findViewById(R.id.color_3);
        ideaText = findViewById(R.id.idea_text);
        timerText = findViewById(R.id.timerText);
        //Assign Listeners
        ideaButton.setOnClickListener(this);
        colorBtn1.setOnClickListener(this);
        colorBtn2.setOnClickListener(this);
        colorBtn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.idea_btn)
        {
            //New round started: get prompt, colors, clear board
            newIdea();
            newColors();
            drawView.clearCanvas();
            //After grabbing colors, take the color of our first color button and
            // assign it to the user. This way they don't keep an old color
            btnBackground = colorBtn1.getBackground();
            newColor = ((ColorDrawable) btnBackground).getColor();
            drawView.setUserPaint(newColor);
            //Make sure we already have a timer running before trying to stop it.
            if (!firstRound)
            {
                stopTimer();
            }
            makeTimer();
            timer.start();
            firstRound = false;
        }
        else if(v.getId() == R.id.color_1)
        {
            //When clicked, grabs color from button, assigns it to user.
            btnBackground = colorBtn1.getBackground();
            newColor = ((ColorDrawable) btnBackground).getColor();
            drawView.setUserPaint(newColor);
        }
        else if(v.getId() == R.id.color_2)
        {
            btnBackground = colorBtn2.getBackground();
            newColor = ((ColorDrawable) btnBackground).getColor();
            drawView.setUserPaint(newColor);
        }
        else if(v.getId() == R.id.color_3)
        {
            btnBackground = colorBtn3.getBackground();
            newColor = ((ColorDrawable) btnBackground).getColor();
            drawView.setUserPaint(newColor);

        }
    }
    //Choose Random Drawing Prompt
    public void newIdea()
    {
        int randIdea = rand.nextInt(25);
        ideaText.setText(getResources().getString(ideaArr[randIdea]));
    }
    //Choose random color for each button
    public void newColors()
    {
        int randColor = rand.nextInt(primColorArr.length);
        int newColor = getResources().getColor(primColorArr[randColor]);
        colorBtn1.setBackgroundColor(newColor);
        randColor = rand.nextInt(secdColorArr.length);
        colorBtn2.setBackgroundColor(getResources().getColor(secdColorArr[randColor]));
        randColor = rand.nextInt(shadeColorArr.length);
        colorBtn3.setBackgroundColor(getResources().getColor(shadeColorArr[randColor]));
    }
    //Create a new timer
    public void makeTimer()
    {
        time = 60;
        timer = new CountDownTimer(60000, 1000)
        {
            public void onTick(long milliseconds)
            {
                //Workaround to Nougat 7.0 bug that doesn't display last second of CountDownTimer
                if(time == 60)
                {
                    timerText.setText("1:00");
                    time--;
                }
                else if((time < 60) && (time > 9))
                {
                    timerText.setText("0:" + time);
                    time--;
                }
                else if((time < 10) && (time > 1))
                {
                    timerText.setText("0:0" + time);
                    time--;
                }
                else
                {
                    timerText.setText("0:01");
                }
            }
            public void onFinish()
            {
               timesUpDialog();
            }
        };
    }
    //Kill currently running timer
    public void stopTimer()
    {
        time = 60;
        timer.cancel();
        timerText.setText("0:" + time);
    }
    //Display a dialog when the timer runs out
    public void timesUpDialog()
    {
        final AlertDialog.Builder timeDialog = new AlertDialog.Builder(this);
        timeDialog.setTitle(R.string.dialog_title);
        timeDialog.setMessage(R.string.dialog_msg);
        //Yes: Starts new round
        timeDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopTimer();
                makeTimer();
                timer.start();
                drawView.clearCanvas();
                newIdea();
                newColors();
                dialog.dismiss();
            }
        });
        //No: Dismiss dialog, keep canvas until New Idea button is pressed
        timeDialog.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timerText.setText(R.string.timer_text);
                dialog.dismiss();
            }
        });
        timeDialog.show();
    }
}

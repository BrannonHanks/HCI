package com.example.brannon.ia08;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    //make new paint object that store paths of every draw.
         /*
            (new SeekBar.OnSeekBarChangeListener() {

    });*/
    DoodleView can;
    SeekBar brush;
    SeekBar op;
    SeekBar red;
    SeekBar green;
    SeekBar blue;
    Button un;
    Button cl;
    Space redT;
    Space blueT;
    Space greenT;
    Space finT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        can = (DoodleView) findViewById(R.id.canvas);
        brush = (SeekBar) findViewById(R.id.brushSize);
        op = (SeekBar) findViewById(R.id.opacity);
        red = (SeekBar) findViewById(R.id.seekBarRed);
        green = (SeekBar) findViewById(R.id.seekBarGreen);
        blue = (SeekBar) findViewById(R.id.seekBarBlue);
        un = (Button) findViewById(R.id.undo);
        cl = (Button) findViewById(R.id.clear);
        redT = (Space) findViewById(R.id.redBox);
        greenT = (Space) findViewById(R.id.greenBox);
        blueT = (Space) findViewById(R.id.blueBox);
        finT = (Space) findViewById(R.id.finalBox);

        brush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

                                         {
                                             @Override
                                             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                 can.changeBrushSize(progress);
                                             }

                                             @Override
                                             public void onStartTrackingTouch(SeekBar seekBar) {

                                             }

                                             @Override
                                             public void onStopTrackingTouch(SeekBar seekBar) {

                                             }
                                         }

        );
        op.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

                                      {
                                          @Override
                                          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                              can.setAlpha(progress, finT);

                                          }

                                          @Override
                                          public void onStartTrackingTouch(SeekBar seekBar) {

                                          }

                                          @Override
                                          public void onStopTrackingTouch(SeekBar seekBar) {

                                          }
                                      }

        );
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

                                       {
                                           @Override
                                           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                               can.setRed(progress, redT, finT);
                                           }

                                           @Override
                                           public void onStartTrackingTouch(SeekBar seekBar) {

                                           }

                                           @Override
                                           public void onStopTrackingTouch(SeekBar seekBar) {

                                           }
                                       }

        );
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

                                         {
                                             @Override
                                             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                 can.setGreen(progress, greenT, finT);
                                             }

                                             @Override
                                             public void onStartTrackingTouch(SeekBar seekBar) {

                                             }

                                             @Override
                                             public void onStopTrackingTouch(SeekBar seekBar) {

                                             }
                                         }

        );
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

                                        {
                                            @Override
                                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                can.setBlue(progress, blueT, finT);
                                            }

                                            @Override
                                            public void onStartTrackingTouch(SeekBar seekBar) {

                                            }

                                            @Override
                                            public void onStopTrackingTouch(SeekBar seekBar) {

                                            }
                                        }

        );
        un.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      can.undo();
                                  }
                              }

        );
        cl.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      can.clear();
                                  }
                              }

        );
    }
}

package com.example.clm;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView mStatusView;
    MediaRecorder mRecorder;
    Thread runner;

    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.8;

    boolean calibrating = false;
    boolean measuring = false;

    double ampl = 30;
    int calibratingCycles;

    Runnable measureUpdater = new Runnable(){
        public void run(){
            mStatusView.setText(Math.round(soundDb(ampl)*100)/100. + " dB");
        };
    };
    Runnable calibrater = new Runnable() {
        @Override
        public void run() {
            double val = getAmplitudeEMA();
            Log.d("[MAIN]", ""+val);
            ampl += val;
            calibratingCycles++;
        }
    };
    final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusView = (TextView) findViewById(R.id.status);
    }
/*
    public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }
*/
    private void runThread(Runnable action)
    {
        if (runner == null)
        {
            runner = new Thread(new Runnable() {
                private Runnable action;
                public Runnable init(Runnable action)
                {
                    this.action = action;
                    return this;
                }
                @Override
                public void run() {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(300);
                            Log.d("[MAIN]","TOCK");
                        } catch (InterruptedException e) {
                            Log.d("[MAIN]","interupted");
                            runner = null;
                            return;
                        };
                        mHandler.post(action);
                    }
                }
            }.init(action));
            runner.start();
        }
    }

    public void startRecorder(){
        if (mRecorder == null)
        {
            Log.d("[MAIN]","startRecorder()");
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[MAIN]", "IOException: " + android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[MAIN]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[MAIN]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            Log.d("[MAIN]","stopRecorder()");
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double soundDb(double ampl){
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    public void toggleMeasure(View view)
    {
        if(!calibrating) {
            if (measuring) {
                stopRecorder();
                runner.interrupt();
            } else {
                startRecorder();
                runThread(measureUpdater);
            }
            measuring = !measuring;
        }

    }

    public void calibrate(View view)
    {
        if(!measuring)
        {
            if(calibrating) {
                stopRecorder();
                ampl /= calibratingCycles;
                Toast.makeText(this, "Calibrated to: "+Math.round(ampl*100)/100., Toast.LENGTH_LONG).show();
                runner.interrupt();
            }
            else {
                startRecorder();
                calibratingCycles = 0;
                ampl = 0;
                runThread(calibrater);
            }
            calibrating = !calibrating;
        }
    }

}

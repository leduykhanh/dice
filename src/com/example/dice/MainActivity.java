package com.example.dice;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

    SensorManager mSensorManager;
    Sensor accSensor;
    Sensor magnetSensor;
    private static final int MIN_FORCE = 20;

    float gravity[];
    float geoMagnetic[];
    float azimut;
    float pitch;
    float roll;
    private static final int MIN_DIRECTION_CHANGE = 3;

    /** Maximum pause between movements. */
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;

    /** Maximum allowed time for shake gesture. */
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;

    /** Time when the gesture started. */
    private long mFirstDirectionChangeTime = 0;

    /** Time when the last movement started. */
    private long mLastDirectionChangeTime;

    /** How many movements are considered so far. */
    private int mDirectionChangeCount = 0;
    /** The last x position. */
    private float lastX = 0;

    /** The last y position. */
    private float lastY = 0;

    /** The last z position. */
    private float lastZ = 0;
    
	ImageView dice1_picture,dice2_picture,dice3_picture;		//reference to dice picture 
	Random rng=new Random();	//generate random numbers
	SoundPool dice_sound = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
	int sound_id;		//Used to control sound stream return by SoundPool
	Handler handler;	//Post message to start roll
	Timer timer=new Timer();	//Used to implement feedback to user
	boolean rolling=false;		//Is die rolling?
	boolean bowlClosed = false;
	int diceValue1 = 0;
	int diceValue2 = 0;
	int diceValue3 = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_view);
		/*View view = (View) findViewById(R.id.imageLayout); //the layout you set in `setContentView()`
		LinearLayout picLL = new LinearLayout(this);
		ImageView myImage = new ImageView(this);
		picLL.addView(myImage);
		myImage.setBackgroundResource(R.drawable.ic_launcher);
		picLL.layout(0, 0, 100, 0);
		picLL.setLayoutParams(new LayoutParams(1000, 60));
		picLL.setOrientation(LinearLayout.HORIZONTAL);
		((ViewGroup) view).addView(picLL);*/
		//load dice sound
		//View view = (View) findViewById(R.id.plate);
		//view.bringToFront();
		sound_id=dice_sound.load(this,R.raw.shake_dice,1);
		//get reference to image widget
        dice1_picture = (ImageView) findViewById(R.id.imageView1);
        dice2_picture = (ImageView) findViewById(R.id.imageView2);
        dice3_picture = (ImageView) findViewById(R.id.imageView3);
        //link handler to callback
        handler=new Handler(callback);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
 	}

	//User pressed dice, lets start
	public void HandleClick(View arg0) {
		if(!rolling) {
			rolling=true;
			//Show rolling image
			dice1_picture.setImageResource(R.drawable.dice3droll);
			dice2_picture.setImageResource(R.drawable.dice3droll);
			dice3_picture.setImageResource(R.drawable.dice3droll);
			//Start rolling sound
			dice_sound.play(sound_id,1.0f,1.0f,0,0,1.0f);
			//Pause to allow image to update
			timer.schedule(new Roll(), 1000);
		}
	}
	public void Shake(SensorEvent arg0) {
	if(!rolling) {
		rolling=true;
		//Show rolling image
		dice1_picture.setImageResource(R.drawable.dice3droll);
		dice2_picture.setImageResource(R.drawable.dice3droll);
		dice3_picture.setImageResource(R.drawable.dice3droll);
		//Start rolling sound
		dice_sound.play(sound_id,1.0f,1.0f,0,0,1.0f);
		//Pause to allow image to update
		timer.schedule(new Roll(), 1000);
	}
}
	//When pause completed message sent to callback
	class Roll extends TimerTask {
	    public void run() {
		    handler.sendEmptyMessage(0);
	    }
	}

	//Receives message from timer to start dice roll
	Callback callback = new Callback() {
	    public boolean handleMessage(Message msg) {
	    	//Get roll result
	    	//Remember nextInt returns 0 to 5 for argument of 6
	    	//hence + 1
	    	diceValue1 = rng.nextInt(6)+1;
			switch(diceValue1) {
	    	case 1:
	    		dice1_picture.setImageResource(R.drawable.one);
	    		break;
	    	case 2:
	    		dice1_picture.setImageResource(R.drawable.two);
	    		break;
	    	case 3:
	    		dice1_picture.setImageResource(R.drawable.three);
	    		break;
	    	case 4:
	    		dice1_picture.setImageResource(R.drawable.four);
	    		break;
	    	case 5:
	    		dice1_picture.setImageResource(R.drawable.five);
	    		break;
	    	case 6:
	    		dice1_picture.setImageResource(R.drawable.six);
	    		break;
	    	default:
			}
			
			diceValue2 = rng.nextInt(6)+1;
			switch(diceValue2) {
	    	case 1:
	    		dice2_picture.setImageResource(R.drawable.one);
	    		break;
	    	case 2:
	    		dice2_picture.setImageResource(R.drawable.two);
	    		break;
	    	case 3:
	    		dice2_picture.setImageResource(R.drawable.three);
	    		break;
	    	case 4:
	    		dice2_picture.setImageResource(R.drawable.four);
	    		break;
	    	case 5:
	    		dice2_picture.setImageResource(R.drawable.five);
	    		break;
	    	case 6:
	    		dice2_picture.setImageResource(R.drawable.six);
	    		break;
	    	default:
			}
			diceValue3 = rng.nextInt(6)+1;
			switch(diceValue3) {
	    	case 1:
	    		dice3_picture.setImageResource(R.drawable.one);
	    		break;
	    	case 2:
	    		dice3_picture.setImageResource(R.drawable.two);
	    		break;
	    	case 3:
	    		dice3_picture.setImageResource(R.drawable.three);
	    		break;
	    	case 4:
	    		dice3_picture.setImageResource(R.drawable.four);
	    		break;
	    	case 5:
	    		dice3_picture.setImageResource(R.drawable.five);
	    		break;
	    	case 6:
	    		dice3_picture.setImageResource(R.drawable.six);
	    		break;
	    	default:
			}
    	    rolling=false;	//user can press again
    	    return true;
	    }
	};

	//Clean up
	@Override
	protected void onPause() {
		super.onPause();
		dice_sound.pause(sound_id);
	}
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		   float x = event.values[SensorManager.DATA_X];
		    float y = event.values[SensorManager.DATA_Y];
		    float z = event.values[SensorManager.DATA_Z];

		    // calculate movement
		    float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

	 
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = event.values.clone();
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geoMagnetic = event.values.clone();

        if (gravity != null && geoMagnetic != null) {
        	
        	
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geoMagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = 57.29578F * orientation[0];
                pitch = 57.29578F * orientation[1];
                roll = 57.29578F * orientation[2];

                float dist = Math.abs((float) (1.4f * Math.tan(pitch * Math.PI / 180)));

                //Log.e("log", "orientation values: " + azimut + " / " + pitch + " / " + roll + " dist = " + dist);
               if(dist > 1.5){
            	      // get time
            	      long now = System.currentTimeMillis();

            	      // store first movement time
            	      if (mFirstDirectionChangeTime == 0) {
            	        mFirstDirectionChangeTime = now;
            	        mLastDirectionChangeTime = now;
            	      }
            	      long lastChangeWasAgo = now - mLastDirectionChangeTime;
            	      if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

            	        // store movement data
            	        mLastDirectionChangeTime = now;
            	        mDirectionChangeCount++;

            	        // store last sensor data 
            	        lastX = x;
            	        lastY = y;
            	        lastZ = z;

            	        // check how many movements are so far
            	        if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

	            	          // check total duration
	            	          long totalDuration = now - mFirstDirectionChangeTime;
	            	          if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE && bowlClosed) {
	            	        	  Shake(event);
	            	            resetShakeParameters();
	            	          }
            	        }

            	      } else {
            	        resetShakeParameters();
            	      }
            	   Log.e("log","dist = " + dist);
            	   
                }
            }
        }
    }
	
	private void resetShakeParameters() {
	    mFirstDirectionChangeTime = 0;
	    mDirectionChangeCount = 0;
	    mLastDirectionChangeTime = 0;
	    lastX = 0;
	    lastY = 0;
	    lastZ = 0;
	  }
	public void closeBowl(View v){
		TextView result = (TextView) findViewById(R.id.result);
		//openning
		if(!bowlClosed){
			bowlClosed = true;
			result.setText("0");
			Button closeBtn = (Button) findViewById(R.id.close);
			closeBtn.setText("Open");
		}
		else{
			bowlClosed = false;
			
			result.setText(diceValue1 + diceValue2 +diceValue3 + "");
			Button closeBtn = (Button) findViewById(R.id.close);
			closeBtn.setText("Close");
		}
	}	
	
	}



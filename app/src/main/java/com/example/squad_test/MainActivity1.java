package com.example.squad_test;

import java.util.Timer;

import java.util.concurrent.Delayed;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;





public class MainActivity1 extends Activity {
	ImageView logo1,logo2;
	boolean flag_img=true;
	MediaPlayer mp;
	long ti=3;
	Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity1);
		logo1=(ImageView)findViewById(R.id.imgnrml);
		logo2=(ImageView)findViewById(R.id.imgglow);
		final AnimatorSet nazalh = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
				R.animator.naza1);
		final AnimatorSet nazahl=(AnimatorSet)AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.naza2);
		final AnimatorSet nazalh1 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
				R.animator.naza1);
		final AnimatorSet nazahl1=(AnimatorSet)AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.naza2);
		nazalh.setTarget(logo2);

		nazahl1.setTarget(logo2);

		nazalh.start();
		nazalh.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {

			}

			@Override
			public void onAnimationEnd(Animator animator) {

				if(flag_img) {
					nazahl1.start();
				}
				nazahl1.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {

					}

					@Override
					public void onAnimationEnd(Animator animator) {


						if(flag_img) {
							nazalh.start();
						}

					}

					@Override
					public void onAnimationCancel(Animator animator) {

					}

					@Override
					public void onAnimationRepeat(Animator animator) {

					}
				});

			}

			@Override
			public void onAnimationCancel(Animator animator) {

			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		});
		
					i=new Intent(MainActivity1.this, MainActivity.class);
					mp=MediaPlayer.create(getApplicationContext(), R.raw.final111);

					mp.start();
					
					Thread t=new Thread()
					 {
						public void run(){
						try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mp.stop();
							flag_img=false;
						MainActivity1.this.startActivity(i);
						finish();
						}
						
					};
					t.start();
					/*try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/

					
					
					
				
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity1, menu);
		return true;
	}

}

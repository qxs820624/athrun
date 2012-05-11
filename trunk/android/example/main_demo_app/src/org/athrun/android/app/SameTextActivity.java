package org.athrun.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class SameTextActivity extends Activity {
	private Button sameButtonOne;
	private Button sameButtonTwo;
	private Button sameButtonThree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sametext_layout);
		init();
	}

	private void init() {
		sameButtonOne = (Button) findViewById(R.id.same_one);
		sameButtonTwo = (Button) findViewById(R.id.same_two);
		sameButtonThree = (Button) findViewById(R.id.same_three);

		sameButtonOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(SameTextActivity.this, "Button One Clicked.", Toast.LENGTH_SHORT).show();
			}
		});

		sameButtonTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(SameTextActivity.this, "Button Two Clicked.", Toast.LENGTH_SHORT).show();
			}
		});

		sameButtonThree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(SameTextActivity.this, "Button Three Clicked.", Toast.LENGTH_SHORT).show();
			}
		});
	}
}

package com.bingoogol.mymoment.ui;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bingoogol.mymoment.R;

public class WelcomeActivity extends GenericActivity {
	private ImageView logoIv = null;
	@Override
	public void onClick(View v) {
	}

	@Override
	protected void loadViewLayout() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
	}

	@Override
	protected void findViewById() {
		logoIv = (ImageView) this.findViewById(R.id.logo_iv);
	}

	@Override
	protected void setListener() {
	}

	@Override
	protected void processLogic() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent homeIntent = new Intent(app,HomeActivity.class);
				finish();
				startActivity(homeIntent);
				overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			}
		}, 1500);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1500);
		logoIv.startAnimation(animation);
		
	}

}

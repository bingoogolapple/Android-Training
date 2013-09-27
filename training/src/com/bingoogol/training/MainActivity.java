package com.bingoogol.training;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.bingoogol.training.ui.CalculatorActivity;
import com.bingoogol.training.ui.FirstActivity;
import com.bingoogol.training.ui.HtmlViewActivity;

public class MainActivity extends GenericActivity {

	private Button helloworldBtn = null;
	private Button calculatorBtn = null;
	private Button htmlViewBtn = null;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void findViewById() {
		helloworldBtn = (Button) this.findViewById(R.id.main_helloworld_btn);
		calculatorBtn = (Button) this.findViewById(R.id.main_calculator_btn);
		htmlViewBtn = (Button) this.findViewById(R.id.main_html_wiew_btn);
	}

	@Override
	protected void setListener() {
		helloworldBtn.setOnClickListener(this);
		calculatorBtn.setOnClickListener(this);
		htmlViewBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_helloworld_btn:
			Intent helloworldIntent = new Intent(context,FirstActivity.class);
			startActivity(helloworldIntent);
			break;
		case R.id.main_calculator_btn:
			Intent calculatorIntent = new Intent(context,CalculatorActivity.class);
			startActivity(calculatorIntent);
			break;
		case R.id.main_html_wiew_btn:
			Intent htmlViewIntent = new Intent(context,HtmlViewActivity.class);
			startActivity(htmlViewIntent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void processLogic() {
		
	}
}

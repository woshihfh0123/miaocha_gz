package com.mc.phonelive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.mc.phonelive.R;

/**
 * 显示信息的dialog
 * @author wwl
 *
 */
public class CancelORderDialog extends Dialog implements OnClickListener {

	private LayoutInflater factory;

	private TextView dialogtitle, showmsg;
	private TextView confirm, cancel;

	private String title, msg; // 显示的信息

	public CancelORderDialog(Context context, String title, String msg) {
		super(context);
		factory = LayoutInflater.from(context);
		this.title = title;
		this.msg = msg;
	}

	public CancelORderDialog(Context context, int theme) {
		super(context, theme);
		factory = LayoutInflater.from(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题
		this.setContentView(factory.inflate(R.layout.food_cancelorderdialog, null));
		dialogtitle = (TextView) this.findViewById(R.id.dialogtitle);
		showmsg = (TextView) this.findViewById(R.id.showmsg);
		confirm =  this.findViewById(R.id.confirm);
		cancel =this.findViewById(R.id.cancel);
		if(title!=null && !title.equals("")) {
			dialogtitle.setText(title);
		}
		if(msg!=null && !msg.equals("")) {
			showmsg.setText(msg);
		}
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.confirm) { // 确定
			this.dismiss();
			this.cancel();
			doConfirmUp();
		} else if (id == R.id.cancel) {
			this.dismiss();
			this.cancel();
		}
	}
	public  void dis(){
		this.dismiss();
	}
	public void doConfirmUp() {
	}
	public void doCancel(View v){

	}

}

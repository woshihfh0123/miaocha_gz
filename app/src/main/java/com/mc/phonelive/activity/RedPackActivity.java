package com.mc.phonelive.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.utils.DataSafeUtils;

public class RedPackActivity extends AbsActivity {
    private EditText price;
    private EditText mContent;
    private TextView btn_send;
    private RelativeLayout title;
    private ImageView back_imgs;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_redpack_layout;


    }

    @Override
    protected void main() {
        setTitle("发红包");
        title =findViewById(R.id.title);
        price =findViewById(R.id.price);
        back_imgs =findViewById(R.id.back_imgs);
        mContent =findViewById(R.id.content);
        btn_send =(TextView) findViewById(R.id.btn_send);

        back_imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               RedPackActivity.this.finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataSafeUtils.isEmpty(price.getText().toString().trim())){
                    return;
                }
                Intent intent = new Intent();
               // title  content  money  content_text
                intent.putExtra("money", price.getText().toString());
                intent.putExtra("title", mContent.getText().toString());
                intent.putExtra("content", "视频红包");
                intent.putExtra("content_text", "[红包]");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

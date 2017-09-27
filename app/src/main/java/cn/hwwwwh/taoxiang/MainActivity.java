package cn.hwwwwh.taoxiang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button=(Button)findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        Button button2=(Button)findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trade(v);
            }
        });

        Button button3=(Button)findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trade(v);
            }
        });
    }

    /**
     * 登录
     */
    public void login(View view) {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                Toast.makeText(MainActivity.this, "登录成功 ",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(MainActivity.this, "登录失败 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 电商交易
     */
    public void trade(View view) {
        Intent transactionIntent = new Intent(MainActivity.this, AliSdkTransactionActivity.class);
        startActivity(transactionIntent);
    }
}

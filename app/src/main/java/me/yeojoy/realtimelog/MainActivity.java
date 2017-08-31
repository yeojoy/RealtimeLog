package me.yeojoy.realtimelog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = Arrays.asList("hi.");
                String text = null;
                Toast.makeText(MainActivity.this, text.indexOf("hi"), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.button).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard.hasPrimaryClip()) {
                    CharSequence text = clipboard.getPrimaryClip().getItemAt(0).getText();
                    Log.d("MAINACTIVITY", "text : " + text);
                    textView.setText(text);
                }
                return true;
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                // Get the stack trace.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);

                // Add it to the clip board and close the app
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Stack trace", sw.toString());
                clipboard.setPrimaryClip(clip);

                sendMail();

                System.exit(1);
            }
        });

        textView = (TextView) findViewById(R.id.text_view);
        textView.setText("Hello, world!\n\n");

        /*
        try {
            Process process = Runtime.getRuntime().exec("logcat -g 1000000");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                textView.append(line);
                textView.append("\n");
            }
        } catch (IOException e) {
            // Handle Exception
        }
        */
    }

    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("plain/text");

        String[] address = {"fofyj@naver.com"};    //이메일 주소 입력

        intent.putExtra(Intent.EXTRA_EMAIL, address);

        intent.putExtra(Intent.EXTRA_SUBJECT, "아만다 버그");

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            CharSequence text = clipboard.getPrimaryClip().getItemAt(0).getText();
            intent.putExtra(Intent.EXTRA_TEXT, text);
        }

        // intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/mnt/sdcard/image.jpg"));    //파일 첨부

        startActivity(intent);

    }
}

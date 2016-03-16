package com.news.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by taoxiaoyan on 16-3-16.
 */
public class MainActivity extends Activity implements SummaryView.ShowWebViewListener, ParseRSS.onCompleteParseListener {

    LinearLayout m_layout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        m_layout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        m_layout.setLayoutParams(layoutParams);

        setContentView(m_layout);
        ParseRSS parseRSS = new ParseRSS(this);
        parseRSS.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startWebActivity(String url){
        Intent intent = new Intent();
        intent.setClass(this, WebActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);

        /*Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);*/
    }

    @Override
    public void onShowWebView(String url) {
        startWebActivity(url);
    }

    @Override
    public void complete(ArrayList<ParseRSS.RssItem> items) {
        SummaryView summaryView = new SummaryView(this, this, items);
        m_layout.addView(summaryView);
    }
}

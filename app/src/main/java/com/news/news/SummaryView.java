package com.news.news;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by taoxiaoyan on 16-3-16.
 */
public class SummaryView extends ListView{

    interface ShowWebViewListener {
        void onShowWebView(String url);
    }

    private Context m_context;
    private ShowWebViewListener m_listener;
    private ArrayList<ParseRSS.RssItem> m_infos = null;

    public SummaryView(Context context, ShowWebViewListener showWebViewListener, ArrayList<ParseRSS.RssItem> items) {
        super(context);
        m_context = context;
        m_listener = showWebViewListener;
        if(items != null){
            m_infos = items;
        }else{
            m_infos = new ArrayList<ParseRSS.RssItem>();
        }
        init();
    }

    private void init(){
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundColor(Color.WHITE);

        SummaryAdapter arrayAdapter = new SummaryAdapter(m_context,android.R.layout.simple_list_item_1, m_infos);

        this.setAdapter(arrayAdapter);

        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String link = m_infos.get(i).getItem_link();
                showWebView(link);
            }
        });
    }

    public void showWebView(String link){
        m_listener.onShowWebView(link);
    }

    class SummaryAdapter extends ArrayAdapter<ParseRSS.RssItem>{

        public SummaryAdapter(Context context, int resourceId, ArrayList<ParseRSS.RssItem> infos){
            super(context, resourceId, infos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                textView = (TextView) layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
                textView.setTextColor(Color.BLACK);
            }else{
                textView = (TextView)convertView;
            }

            textView.setText(m_infos.get(position).getItem_title());

            return textView;
        }


    }
}

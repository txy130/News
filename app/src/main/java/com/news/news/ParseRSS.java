package com.news.news;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by taoxiaoyan on 16-3-16.
 */
public class ParseRSS {

    interface onCompleteParseListener{
        void complete(ArrayList<RssItem> items);
    }

    private final int PARSE_FINISH_MESSAGE = 0;
    private Handler m_handler = null;
    private onCompleteParseListener m_listener = null;
    private Thread m_thread = null;

    public ParseRSS(onCompleteParseListener onCompleteParseListener){
        m_listener = onCompleteParseListener;
        init();
    }

    private void init(){
        m_handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case PARSE_FINISH_MESSAGE:
                        Bundle bundle = msg.getData();
                        ArrayList<RssItem> items = bundle.getParcelableArrayList("items");
                        if(m_listener != null){
                            m_listener.complete(items);
                        }
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        m_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<RssItem> items = Parse();
                Message message = m_handler.obtainMessage();
                message.what = PARSE_FINISH_MESSAGE;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("items", items);
                message.setData(bundle);
                m_handler.sendMessage(message);
            }
        });
    }

    public void start(){
        m_thread.start();
    }

    private ArrayList<RssItem> Parse(){
        try {
            URL url = new URL("http://news.qq.com/newsgn/rss_newsgn.xml");
            InputStream inputStream = url.openStream();
            return ParseRss(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<RssItem> ParseRss(InputStream in) throws IOException, XmlPullParserException {
        String title = "";
        String pubDate = "";
        String link = "";
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(in, null); //null 为编码格式，如utf-8，null为所有的。
        int eventType = xpp.getEventType();
        String tag = xpp.getName();
        ArrayList<RssItem> m_items = new ArrayList<RssItem>();

        //解析文件的头
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if(tag.equals("item")) {
                    break;
                } else if(tag.equals("title")) {
                    xpp.next();
                    title = xpp.getText().toString();
                } else if(tag.equals("pubDate")) {
                    xpp.next();
                    pubDate = xpp.getText().toString();
                } else if(tag.equals("link")) {
                    xpp.next();
                    link = xpp.getText().toString();
                }
            } else if(eventType == XmlPullParser.END_TAG) {
                if(tag.equals("link")) {
                    RssHead head = new RssHead(title, pubDate, link);
                }
            }
            eventType = xpp.next();
            tag = xpp.getName();
        }


        //解析文件的Item项
        String item_title = null;
        String item_pubtime = null;
        String item_link = null;
        String description = null;
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if (tag == null){
                    xpp.next();
                    continue;
                }
                if(tag.equals("item")) {
                    xpp.next();
                    item_title = item_pubtime = item_link = description = "";
                } else if(tag.equals("title")) {
                    xpp.next();
                    item_title = xpp.getText().toString();
                } else if(tag.equals("link")) {
                    xpp.next();
                    item_link = xpp.getText().toString();
                } else if(tag.equals("pubTime")) {
                    xpp.next();
                    item_pubtime = xpp.getText().toString();
                } else if(tag.equals("description")) {
                    xpp.next();
                    description = xpp.getText().toString();
                }
            } else if(eventType == XmlPullParser.END_TAG) {
                if(tag.equals("item")) {
                    RssItem ri = new RssItem(item_title,item_pubtime,item_link,description);
                    m_items.add(ri);
                }
            }
            eventType = xpp.next();
            tag = xpp.getName();
        }

        return m_items;

    }

    public class RssHead {

        private String title;
        private String pubDate;
        private String link;

        public RssHead(String title, String pubDate, String link) {
            // TODO Auto-generated constructor stub
            this.link = link;
            this.pubDate = pubDate;
            this.title = title;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getPubDate() {
            return pubDate;
        }
        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }
        public String getLink() {
            return link;
        }
        public void setLink(String link) {
            this.link = link;
        }
    }

    public static class RssItem implements Parcelable{

        private String item_title;
        private String item_pubDate;
        private String item_link;
        private String description;

        public RssItem(String title, String putDate, String link, String description) {
            // TODO Auto-generated constructor stub
            item_title = title;
            item_pubDate = putDate;
            item_link = link;
            this.description = description;
        }

        public RssItem(Parcel in){
            item_title = in.readString();
            item_pubDate = in.readString();
            item_link = in.readString();
            description = in.readString();
        }

        public String getItem_title() {
            return item_title;
        }
        public void setItem_title(String item_title) {
            this.item_title = item_title;
        }
        public String getItem_pubDate() {
            return item_pubDate;
        }
        public void setItem_pubDate(String item_pubDate) {
            this.item_pubDate = item_pubDate;
        }
        public String getItem_link() {
            return item_link;
        }
        public void setItem_link(String item_link) {
            this.item_link = item_link;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(item_title);
            parcel.writeString(item_pubDate);
            parcel.writeString(item_link);
            parcel.writeString(description);
        }

        public static final Parcelable.Creator<RssItem> CREATOR = new Creator<RssItem>()
        {
            @Override
            public RssItem[] newArray(int size)
            {
                return new RssItem[size];
            }

            @Override
            public RssItem createFromParcel(Parcel in)
            {
                return new RssItem(in);
            }
        };
    }
}

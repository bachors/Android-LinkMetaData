package com.bachors.linkmetadata;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.webkit.URLUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Bachors on 1/21/2018.
 * https://github.com/bachors/Android-LinkMetaData
 */


public class LinkMetaData {

    private String url;
    private Model metaData;
    private Listener metaDataListener;
    private String msg;

    private Boolean st;

    public LinkMetaData(){
    }

    public void url(String url){
        this.url = url;
        this.metaData = new Model();
        this.st = true;
        new GetMetaData().execute();
    }

    public void setMetaDataListener(Listener metaDataListener) {
        this.metaDataListener = metaDataListener;
    }

    /**
     * Async task class to get html by making HTTP call
     */
    @SuppressLint("StaticFieldLeak")
    private class GetMetaData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Document html = null;
            try {
                st = true;
                // dom
                html = Jsoup.connect(url).get();

                // url
                metaData.setUrl(url);

                // domain
                metaData.setDomain(findDomain(url));

                // title
                String title = html.getElementsByTag("title").text();
                metaData.setTitle(title);

                // description
                String description = html.select("meta[name=description]").attr("content");
                if(!description.isEmpty())
                    metaData.setDescription(description);
                String ogdescription = html.select("meta[property=og:description]").attr("content");
                if(!ogdescription.isEmpty())
                    metaData.setDescription(ogdescription);

                // image
                String ogimage = html.select("meta[property=og:image]").attr("content");
                metaData.setImage(findURL(url, ogimage));

                // favicon
                String icon = html.select("link[rel=icon]").attr("href");
                if(!icon.isEmpty())
                    metaData.setIcon(findURL(url, icon));
                String icon2 = html.select("link[rel=apple-touch-icon]").attr("href");
                if(!icon2.isEmpty())
                    metaData.setIcon(findURL(url, icon2));
                String icon3 = html.select("link[rel=shortcut icon]").attr("href");
                if(!icon3.isEmpty())
                    metaData.setIcon(findURL(url, icon3));

            } catch (IOException e) {
                e.printStackTrace();
                st = false;
                msg = e.getLocalizedMessage();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(st)
                metaDataListener.onResponse(metaData);
            else
                metaDataListener.onFailure(msg);
        }
    }

    private String findDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private String findURL(String url, String part) {
        if(URLUtil.isValidUrl(part)) {
            return part;
        } else {
            URI base_uri = null;
            try {
                base_uri = new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            assert base_uri != null;
            base_uri = base_uri.resolve(part);
            return base_uri.toString();
        }
    }

    // Model
    public class Model {
        private String url;
        private String icon;
        private String image;
        private String title;
        private String domain;
        private String description;

        public String getUrl() {
            return url;
        }

        private void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        private void setIcon(String icon) {
            this.icon = icon;
        }

        public String getImage() {
            return image;
        }

        private void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        private void setTitle(String title) {
            this.title = title;
        }

        public String getDomain() {
            return domain;
        }

        private void setDomain(String domain) {
            this.domain = domain;
        }

        public String getDescription() {
            return description;
        }

        private void setDescription(String description) {
            this.description = description;
        }

    }

    // interface
    public interface Listener {
        void onResponse(Model metaData);
        void onFailure(String message);
    }

}
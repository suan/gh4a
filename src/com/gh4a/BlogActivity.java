/*
 * Copyright 2011 Azwan Adli Abdullah
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gh4a;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.gh4a.holder.BreadCrumbHolder;

public class BlogActivity extends BaseActivity {

    private LoadingDialog mLoadingDialog;
    private String mTitle;
    private String mContent;
    private String mLink;
    
    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_viewer);
        setUpActionBar();
        
        mTitle = getIntent().getStringExtra(Constants.Blog.TITLE);
        mContent = getIntent().getStringExtra(Constants.Blog.CONTENT);
        mLink = getIntent().getStringExtra(Constants.Blog.LINK);

        TextView tvHistoryFile = (TextView) findViewById(R.id.tv_view);
        tvHistoryFile.setVisibility(View.GONE);
        
        TextView tvViewRaw = (TextView) findViewById(R.id.tv_view_raw);
        tvViewRaw.setVisibility(View.GONE);

        TextView tvDownload = (TextView) findViewById(R.id.tv_download);
        tvDownload.setVisibility(View.GONE);
        
        TextView tvViewInBrowser = (TextView) findViewById(R.id.tv_in_browser);
        tvViewInBrowser.setVisibility(View.VISIBLE);
        tvViewInBrowser.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                getApplicationContext().openBrowser(BlogActivity.this, mLink);
            }
        });
        
        setBreadCrumb();

        mLoadingDialog = LoadingDialog.show(this, true, true);
        
        fillData();
    }
    
    protected void setBreadCrumb() {
        BreadCrumbHolder[] breadCrumbHolders = new BreadCrumbHolder[2];

        BreadCrumbHolder b = new BreadCrumbHolder();
        b.setLabel(getResources().getString(R.string.explore));
        b.setTag(Constants.EXPLORE);
        breadCrumbHolders[0] = b;
        
        b = new BreadCrumbHolder();
        b.setLabel(getResources().getString(R.string.blog));
        b.setTag(Constants.Blog.BLOG);
        breadCrumbHolders[1] = b;
        
        createBreadcrumb(mTitle, breadCrumbHolders);
    }

    private void fillData() {
        
        WebView webView = (WebView) findViewById(R.id.web_view);

        WebSettings s = webView.getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        s.setUseWideViewPort(false);
        s.setAllowFileAccess(true);
        s.setBuiltInZoomControls(false);
        s.setLightTouchEnabled(true);
        s.setLoadsImagesAutomatically(true);
        s.setPluginsEnabled(false);
        s.setSupportZoom(true);
        s.setSupportMultipleWindows(true);
        s.setJavaScriptEnabled(true);

        webView.setWebViewClient(webViewClient);
        webView.loadDataWithBaseURL("https://github.com", mContent, "text/html", "utf-8", null);
    }

    /** The web view client. */
    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView webView, String url) {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    };

}

package zerogerc.com.artistinfo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import zerogerc.com.artistinfo.R;

/**
 * Simple WebView Activity. It loads url from Intent key {@link #URL_KEY}.
 */
public class WebViewActivity extends AppCompatActivity {
    public static final String URL_KEY = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = ((WebView) findViewById(R.id.web_view));

        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(WebViewActivity.this, description, Toast.LENGTH_SHORT).show();
                }
            });
            String url = getIntent().getStringExtra(URL_KEY);
            webView.loadUrl(url);
        }
    }
}

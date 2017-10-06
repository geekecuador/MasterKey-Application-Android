package aplicacion.masterkey;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(coneccionWifi() || coneccionDatos()){
            setContentView(R.layout.activity_main);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            webView=(WebView) findViewById(R.id.navegador);
            WebSettings webSettings = webView.getSettings();
            webSettings.setAppCachePath("/data/data/"+ getPackageName() +"/cache");
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setCacheMode(webSettings.LOAD_DEFAULT);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://horarios.masterkey.com.ec/");
            if(!verificarConexion(this)){
                Toast.makeText(MainActivity.this,"Comprueba tu conexion a Internet Saliendo.....",Toast.LENGTH_SHORT).show();
                this.finish();
            }
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int progress)
                {
                    if(!verificarConexion(MainActivity.this)){
                        Toast.makeText(MainActivity.this,"Comprueba tu conexion a Internet Saliendo.....",Toast.LENGTH_SHORT).show();
                        MainActivity.this.finish();
                    }
                }
            });
        }
        if(!verificarConexion(this)){
            Toast.makeText(MainActivity.this,"Comprueba tu conexion a Internet Saliendo.....",Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){
        webView=(WebView) findViewById(R.id.navegador);
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            switch (KeyCode){
                case KeyEvent.KEYCODE_BACK:
                    if(webView.canGoBack()){
                        webView.goBack();
                    }else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(KeyCode,event);
    }


    public Boolean coneccionWifi(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            NetworkInfo info=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(info!=null && info.isAvailable()){
                if (info.isConnected()){
                    return true;
                }
            }
        }
        return false;
    }

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
        Toast.makeText(MainActivity.this, "Tu conexión a Internet podría no estar activa o " + error , Toast.LENGTH_LONG).show();
    }

    public Boolean coneccionDatos(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager !=null){
            NetworkInfo info=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(info!=null && info.isAvailable()){
                if(info.isConnected()){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean verificarConexion(Context ctx){
        ConnectivityManager connectivityManager=(ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes=connectivityManager.getAllNetworkInfo();
        for (int i=0;i<2;i++){
            if(redes[i].getState()==NetworkInfo.State.CONNECTED){
                return true;
            }
        }
        return false;
    }
}
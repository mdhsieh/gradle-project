package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.michaelhsieh.jokedisplay.JokeActivity;
import com.udacity.gradle.builditbigger.IdlingResource.SimpleIdlingResource;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.lang.ref.WeakReference;

import static com.michaelhsieh.jokedisplay.JokeActivity.KEY_JOKE;

/** The MainActivity specific to the free version.
 *  This class will display an interstitial ad when the joke button is clicked, which requires
 *  the ad dependencies of the free product flavor.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // true if testing endpoints on emulator, false if testing on real device
    // default true, change this if needed
    private boolean testWithEmulator = true;

    // the root URL used to test Google Cloud endpoints
    // this needs to be used in class EndpointsAsyncTask
    static String rootUrl;

    // loading indicator to let user know joke is being retrieved through Google Cloud Endpoints module
    private ProgressBar loadingIndicator;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource idlingResource;

    // free version displays a full-screen ad on button click
    private InterstitialAd interstitialAd;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if (idlingResource == null)
        {
            idlingResource = new SimpleIdlingResource();
        }

        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize full-screen ad
        interstitialAd = new InterstitialAd(this);
        // set ad unit ID
        // using the dedicated test ad unit ID for Android interstitials
        interstitialAd.setAdUnitId(getString(R.string.test_ad_unit_id));
        // create ad request
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        // load the ad
        interstitialAd.loadAd(adRequest);

        // Get the IdlingResource instance
        getIdlingResource();

        // 10.0.2.2 is localhost's IP address in Android emulator
        final String EMULATOR_IP_ADDRESS = "10.0.2.2";
        /* To test on a real device:
        Create res/values/secrets.xml with string resource parse_local_computer_ip_address
        containing your computer's IP address.
        You need to turn off your computer's firewall to connect! */
        final String LOCAL_COMPUTER_IP_ADDRESS = getString(R.string.parse_local_computer_ip_address);

        // get loading indicator
        loadingIndicator = findViewById(R.id.pb_loading);

        // change root URL depending on whether testing on emulator or real device
        if (testWithEmulator) {
            // .setRootUrl("http://10.0.2.2:8080/_ah/api/")
            rootUrl = "http://" + EMULATOR_IP_ADDRESS + ":8080/_ah/api/";
        }
        else {
            rootUrl = "http://" + LOCAL_COMPUTER_IP_ADDRESS + ":8080/_ah/api/";
        }
        // Log.d(TAG, "root url: " + rootUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        String defaultText = getString(R.string.default_joke);

        // show the interstitial ad
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Log.d(TAG, "The interstitial wasn't loaded yet.");
        }

        /* Make loading indicator visible here, instead of in AsyncTask's
        onPreExecute, because the context that AsyncTask will use is from
        the parameters of doInBackground.
         */
        loadingIndicator.setVisibility(View.VISIBLE);
        new EndpointsAsyncTask().execute(new Pair<Context, String>(this, defaultText));
    }

}

/* Retrieves the joke from the joke source in a background task, or
shows default text and Toast if an error occurs. */
class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

    private static final String TAG = EndpointsAsyncTask.class.getSimpleName();

    private static MyApi myApiService = null;

    // using Context object directly, ex. private Context context, will leak the Activity context

    // use a weak reference to avoid leaking a context object
    private WeakReference<Context> weakContext;

    // was connection successful or not
    // default assume true
    private boolean isConnectionSuccessful = true;

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - turn off compression when running against local devappserver
                    .setRootUrl(MainActivity.rootUrl)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        weakContext = new WeakReference<>(params[0].first);
        String defaultText = params[0].second;

        try {
            // this endpoint method gets a joke from the joke source library
            // through the Google Cloud Endpoints module
            return myApiService.getJokeFromSource().execute().getData();
        } catch (IOException e) {
            /* Connection can timeout and fail because
            1. No Internet connection
            2. Firewall blocks connection to your local computer
            3. Gradle task appEngineStart in backend->appengine standard environment was not run
             */
            Log.e(TAG, e.getMessage());
            isConnectionSuccessful = false;
            return defaultText;
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if (weakContext != null) {
            // get Activity context without memory leak
            Context context = weakContext.get();

            // hide the loading indicator
            if (context instanceof MainActivity) {
                ProgressBar bar = ((MainActivity) context).findViewById(R.id.pb_loading);
                bar.setVisibility(View.GONE);
            } else {
                // should never happen
                Log.e(TAG, "couldn't hide loading indicator");
                Log.e(TAG, "context is not an instance of MainActivity: " + context);
            }

            // display toast if connection failed
            if (!isConnectionSuccessful) {
                Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show();
            }

            Intent jokeIntent = new Intent(context, JokeActivity.class);
            jokeIntent.putExtra(KEY_JOKE, result);
            context.startActivity(jokeIntent);
        } else {
            Log.e(TAG, "weak context is null");
        }
    }
}

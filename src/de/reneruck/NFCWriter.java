package de.reneruck;

import de.reneruck.nfcExperiment.R;
import android.app.Activity;
import android.os.Bundle;

public class NFCWriter extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
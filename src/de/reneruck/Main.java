package de.reneruck;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.util.Log;

public class Main extends Activity {

	private static final String TAG = "Main";
	private IntentFilter[] intentFiltersArray;
	private NfcAdapter mAdapter;
	private PendingIntent pendingIntent;
	private String[][] techListsArray;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		this.mAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
	    try {
	        ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
	                                       You should specify only the ones that you need. */
	        tag.addDataType("*/*");
	    }
	    catch (MalformedMimeTypeException e) {
	        throw new RuntimeException("fail", e);
	    }
	   intentFiltersArray = new IntentFilter[] {ndef, tag };
	   techListsArray = new String[][] { new String[] { NfcF.class.getName() },
			   new String[] { MifareClassic.class.getName() },
			   new String[] { MifareUltralight.class.getName() },
			   new String[] { NfcA.class.getName() },
			   new String[] { NfcB.class.getName() },
			   new String[] { NfcV.class.getName() },
			   new String[] { Ndef.class.getName() },
			   new String[] { NdefFormatable.class.getName() }
			   };
	}
	
	
	public void onPause() {
	    super.onPause();
	    this.mAdapter.disableForegroundDispatch(this);
	}

	public void onResume() {
	    super.onResume();
	    mAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
	}

	public void onNewIntent(Intent intent) {
		Uri data = intent.getData();
		Log.d(TAG, "MimeType: " + intent.getType());
		NdefMessage[] ndefMessages = NfcUtils.getNdefMessages(intent);
		if(ndefMessages != null) {
			for (NdefMessage ndefMessage : ndefMessages) {
				NdefRecord[] records = ndefMessage.getRecords();
				for (NdefRecord ndefRecord : records) {
					Log.d(TAG, new String(ndefRecord.getPayload()));
				}
			}
		}
	}
}



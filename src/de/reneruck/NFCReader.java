package de.reneruck;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.reneruck.nfcExperiment.R;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NFCReader extends Activity {
	
    private NdefMessage[] ndefMessages;

    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_layout);
        readTagFromIntent(getIntent());
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	readTagFromIntent(intent);
    }

	private void readTagFromIntent(Intent intent) {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) | NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Log.d("NFC Reader", "New NFC Element detected");
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

			if (rawMsgs != null) {
				this.ndefMessages = new NdefMessage[rawMsgs.length];				
				Log.d("NFC Reader", "NDEF Message count: " + rawMsgs.length);				
				for (int i = 0; i < rawMsgs.length; i++) {
					this.ndefMessages[i] = (NdefMessage) rawMsgs[i];
				}
				readMessages();
			}
		}
	}

	private void readMessages() {
		for (int i = 0; i < this.ndefMessages.length; i++) {
			NdefRecord[] records = this.ndefMessages[i].getRecords();
			StringBuilder builder = new StringBuilder();
			for (int j = 0; j < records.length; j++) {
				builder.append(makeRecordEntry(records[i]));
			}
			fillGui(this.ndefMessages[i].toByteArray(), builder.toString(), records.length);
		}
	}
	
	private void fillGui(byte[] message, String records, int recordCount){
	    TextView messageCount = (TextView) findViewById(R.id.message_count);
	    TextView recordCountfield = (TextView) findViewById(R.id.record_count);
	    TextView recordsText = (TextView) findViewById(R.id.records);
	    TextView completeMessage = (TextView) findViewById(R.id.complete_message);
	    
	    completeMessage.setText(Arrays.toString(message));
	    
	    recordCountfield.setText("Records: " + recordCount);
	    
	    recordsText.setText(records);					
	    messageCount.setText("NDEF Message Count " + this.ndefMessages.length);
		
	}
	private String makeRecordEntry(NdefRecord record) {
		byte[] byteArray = record.toByteArray();
		byte[] id = record.getId();
		short tnf = record.getTnf();
		byte[] type = record.getType();
		
		String output = "Record Entry: \n" +
				"ID: " + Arrays.toString(id) + "\n" +
				"Type: " + Arrays.toString(type) + "\n" +
				"TNF: " + tnf + "\n" +
				"Full Record as byte[] \n" +
				Arrays.toString(byteArray) + "\n";
		return output;
	}
}
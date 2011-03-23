package br.com.thecodebakers.hppfree.activities;

import br.com.thecodebakers.hppfree.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class Ajuda extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ajuda);
	}
	
	public void fechar(View view) {
		finish();
	}
	
	public void web(View view) {
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com"));
		startActivity(viewIntent); 
	}
	
}

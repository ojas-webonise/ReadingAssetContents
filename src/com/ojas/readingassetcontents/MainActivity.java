package com.ojas.readingassetcontents;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView editText;
	private TextView textView;
	AssetManager asm;
//	private Whitelist whitelist;
	String [] list;
	private String strFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView)findViewById(R.id.textView);
		editText = (TextView)findViewById(R.id.editText);
		asm = getAssets();
//		whitelist = Whitelist.none();
		try {
			list = getAssets().list("html"); // "html" is the name of subfolder in assets.. change it accordingly
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean listAssetFiles() {
		System.out.println(">>>>>"+ System.currentTimeMillis());
		String strTextToSearch = editText.getText().toString();
		strFiles = "Searched text "+strTextToSearch + "\n" ;
		textView.setText("Searched text "+strTextToSearch + "\n");
		if (list.length > 0) {
			// This is a folder
			for (String file : list) {
				/** Uncomment if want to search in subfolders also */ 
				//if (!listAssetFiles(path + "/" + file))
				//	return false;
				//else {
				String strFileContents = readTxt(file);

				// takes 13- 24 miliseconds
				// boolean isPresent = strFileContents.toLowerCase(Locale.getDefault()).contains(strTextToSearch);

				// takes 1-2 miliseconds
				boolean isPresent = Pattern.compile(strTextToSearch,Pattern.CASE_INSENSITIVE).matcher(strFileContents).find();
				if (isPresent) {
					strFiles += " Found in file : "+ file + "\n";
				}
				// }
			}
		} else {
			// This is a file
			// TODO: add file name to an array list
		}
		textView.setText(strFiles);
		System.out.println(">>>>>"+ System.currentTimeMillis());
		return true; 
	} 

	private String readTxt(String strFileName){

		InputStream inputStream = null; 
		try {
			inputStream = asm.open("html/"+strFileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// InputStreamReader takes 2-3 miliseconds to read a file
		InputStreamReader reader = new InputStreamReader(inputStream);
		StringBuilder sb = new StringBuilder();

		final char[] buf = new char[1024];
		int len;
		try {
			while ((len = reader.read(buf)) > 0) {
				sb.append(buf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 


		//  InputStream inputStream = getResources().openRawResource(R.raw.internals);

		// ByteArrayOutputStream takes 24-41 miliseconds to read a file
		/**		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int i;
		try {
			i = inputStream.read();
			while (i != -1)
			{
				byteArrayOutputStream.write(i);
				i = inputStream.read();
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/


		// takes 600-800 miliseconds
		//		String cleanStr = Jsoup.clean(sb.toString(), whitelist);
		
		// takes 10-17 miliseconds
		String cleanStr = sb.toString().replaceAll("<[^>]*>", "");
		//		System.out.println("string >>>> "+ cleanStr);
		return cleanStr;
	}

	public void onClickSearchText(View view) {
		listAssetFiles();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

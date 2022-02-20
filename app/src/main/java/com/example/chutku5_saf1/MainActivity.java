package com.example.chutku5_saf1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private static final int CREATE_CODE=40;
    private static final int WRITE_CODE=42;
    private static final int READ_CODE=41;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnCreatePressed(View v) {
        Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE,"myRemoteFile.txt");
        startActivityForResult(intent,CREATE_CODE);

    }

    private void startActivityForResult() {
    }

    public void btnWritePressed(View v) {
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent,WRITE_CODE);

    }

    public void btnReadPressed(View v) {
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent,READ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri fileURL=null;
        if(resultCode== Activity.RESULT_OK){
            if(resultCode==CREATE_CODE){
                if(data!=null){
                    Log.i("STORAGE_TAG","File is created successfully");
                }

            }
            else if(requestCode==WRITE_CODE){
                if(data!=null){
                    fileURL=data.getData();
                    writeRemotefile(fileURL);
                }

            }
            else if(requestCode==READ_CODE){
                if(data!=null){
                    fileURL=data.getData();
                   // fileURL="content://com.android.providers.downloads.documents/document/50";
                    try {
                        String fileData=readRemoteFile(fileURL);
                        Log.i("STORAGE_TAG","File Data ::: "+fileURL);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void writeRemotefile(Uri uri){
        try {
            ParcelFileDescriptor parcelFileDescriptor=this.getContentResolver().openFileDescriptor(uri,"w");
            FileOutputStream fileOutputStream=new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            EditText editText=(EditText) findViewById(R.id.editContent);
            String dataToWrite=editText.getText().toString();
            fileOutputStream.write(dataToWrite.getBytes());
            fileOutputStream.close();
            parcelFileDescriptor.close();

            Log.i("STORAGE_TAG","File is written successfully");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private String readRemoteFile(Uri uri) throws IOException{
        InputStream inputStream=getContentResolver().openInputStream(uri);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder=new StringBuilder();
        String line="";

        while((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line+"\n");

        }
        inputStream.close();
        return stringBuilder.toString();

    }
}
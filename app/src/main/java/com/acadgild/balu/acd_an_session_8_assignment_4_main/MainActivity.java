package com.acadgild.balu.acd_an_session_8_assignment_4_main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    static String file_name = "testfile.txt";
    File file = new File(Environment.getExternalStorageDirectory(), file_name);

    EditText    editText_write_data;
    TextView    textView_read_data_label, textView_read_data;
    Button      button_write_data,button_clear_data, button_delete_file;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_write_data = (EditText) findViewById(R.id.editText_write_data);
        textView_read_data_label = (TextView) findViewById(R.id.textView_read_data_label);
        textView_read_data = (TextView) findViewById(R.id.textView_read_data);
        button_write_data = (Button) findViewById(R.id.button_write_data);
        button_clear_data = (Button) findViewById(R.id.button_clear_data);
        button_delete_file = (Button) findViewById(R.id.button_delete_file);

        button_write_data.setOnClickListener(this);
        button_clear_data.setOnClickListener(this);
        button_delete_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.button_write_data)
        {
            if (isStorageReadable()) {
                new WriteData().execute(editText_write_data.getText().toString());
                Log.e("write data", editText_write_data.getText().toString());
                editText_write_data.setText("");
            }
        }

        else if (v.getId() == R.id.button_clear_data)
        {
            editText_write_data.setText("");
            textView_read_data.setText("");
        }

        else if (v.getId() == R.id.button_delete_file)
        {
            file.delete();
            editText_write_data.setText("");
            textView_read_data.setText("");
            Toast.makeText(getApplicationContext(),
                    String.format(getResources().getString(R.string.info), file),
                    Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isStorageReadable()
    {
        boolean mExternalStorageAvailable = false;
        try
        {
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state))
            {
                mExternalStorageAvailable = true;
                Log.i("isStorageReadable", "External storage card is readable.");
            }
            else
            {
                mExternalStorageAvailable = false;
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return mExternalStorageAvailable;
    }

    class WriteData extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            Log.e("file", String.valueOf(file));
            FileWriter fileWriter = null;
            try
            {
                fileWriter = new FileWriter(file, true);
                fileWriter.append(params[0].toString());
                fileWriter.append("\n");
                fileWriter.flush();
                fileWriter.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data)
        {
            super.onPostExecute(data);

            String stringToReturn = "";
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            FileReader fileReader = null;
            try
            {
                fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((receiveString = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append("\n");
                    stringBuilder.append(receiveString);
                }
                bufferedReader.close();
                fileReader.close();
                stringToReturn = stringBuilder.toString();
            }
            catch (FileNotFoundException e)
            {
                Log.e("TAG", "File not found: " + e.toString());
            }
            catch (IOException e)
            {
                Log.e("TAG", "Can not read file: " + e.toString());
            }

            textView_read_data.setText(stringToReturn);
        }
    }
}

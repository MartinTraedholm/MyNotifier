package com.example.martin.mynotifier;

import android.content.Context;
import android.os.Message;
import android.util.JsonWriter;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Martin on 25/02/2016.
 */
public class MyJsonFileHelpers
{
    private static final String MY_FILE_NAME = "thisIsAwesomeFile.json";
    private JsonWriter writer;
    public MyJsonFileHelpers() {
        Log.d(this.getClass().getSimpleName(), "MyJsonFileHelpers");
    }

    public boolean writeToFile(byte[] data, Context context)
    {
        boolean returnVal = true;
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(MY_FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            returnVal = false;
        }

        return returnVal;
    }

    public void writeJsonToFile(Context context)
    {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(MY_FILE_NAME, Context.MODE_APPEND);
            writeJson(outputStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void writeJson(OutputStream out) throws IOException {
        writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("    ");
        jsonFinal(writer);
        Log.d(this.getClass().getSimpleName(), out.toString());
    }

    public void jsonFinal(JsonWriter writer) throws IOException{
        writer.beginObject();
        writer.name("status").value("OK");
        writer.name("num_results").value("");
        writer.endObject();
        writer.close();
    }


}

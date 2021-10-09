package com.example.newassignment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class Talk extends Thread implements TextToSpeech.OnInitListener {


    Context con;
    public Handler handler;
    public static Talk t;
    TextToSpeech tts;
    String last = "duh";

    public static Talk getInstance(Context c){
        if (t == null){
            t = new Talk(c);
        }
        else {
            t.con = c;
        }
        return t;
    }

    private Talk(Context con){
        this.con = con;
        tts = new TextToSpeech(con, this);
    }
    //@Override
    public void run(String string){

        Toast.makeText(con, "Talking " + string, Toast.LENGTH_SHORT).show();
        speakOut("There is " + string);

    }


    public void onInit(int status){
        if (status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.US);
            Toast.makeText(con, "Speech Init Success", Toast.LENGTH_LONG).show();


            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(con, "language is not supported", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(con, "Initialization Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void speakOut(String text){
        Log.v("***SPEECH***", text);

        if(last != text){
            last = text;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            while (tts.isSpeaking()){
                try {
                    Thread.sleep(200);
                }
                catch  (Exception e){
                }
            }
        }
    }
}

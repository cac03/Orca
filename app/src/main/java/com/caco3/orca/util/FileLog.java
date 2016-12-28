package com.caco3.orca.util;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import timber.log.Timber;

/**
 * Rough logger which logs messages to the file in private app storage.
 */
public class FileLog {

    private final String fileName;
    private final Context context;

    public FileLog(Context context, String fileName) {
        this.fileName = Preconditions.checkNotNull(fileName, "fileName == null");
        this.context = Preconditions.checkNotNull(context, "context == null");
    }


    public synchronized void log(String msg) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            fos.write(formatMsg(msg).getBytes());
        } catch (IOException e) {
            Timber.e(e, "Unable to write message: '%s' to file(%s)", msg, fileName);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Timber.e(e, "Unable to close file(%s) after message written", fileName);
                }
            }
        }
    }

    public void log(String msg, Throwable error) {
        log(msg + "\n" + Log.getStackTraceString(error));
    }

    private String formatMsg(String msg) {
        return new Date().toString() + ": " + msg + "\n";
    }
}

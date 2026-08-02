/*
 * Decompiled with CFR 0.152.
 */
package errors;

import errors.ErrorPopUp;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

public class ErrorManager {
    public static final String STANDARD_TITLE = "Error!";
    public static String defaultMessage = "An error has occurred!";
    private static File logFolder = new File("DefaultErrorLogs");

    public static void init(File folder, String standardMessage) {
        logFolder = folder;
        defaultMessage = standardMessage;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                ErrorManager.crashWithUserAlert(e);
            }
        });
    }

    public static void crashWithUserAlert(Throwable error) {
        error.printStackTrace();
        ErrorManager.crashWithUserAlert(STANDARD_TITLE, defaultMessage, error);
    }

    public static void crashWithUserAlert(String title, String message, String error) {
        ErrorPopUp.showPopUp(title, message, error);
        try {
            ErrorManager.createErrorLog(title, String.valueOf(message) + " - " + error);
            Thread.sleep(100000000L);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void crashWithUserAlert(String title, String message, Throwable error) {
        error.printStackTrace();
        ErrorManager.crashWithUserAlert(title, message, ErrorManager.errorToString(error));
    }

    public static void createErrorLog(String name, Throwable error) {
        ErrorManager.createErrorLog(name, ErrorManager.errorToString(error));
    }

    public static void createErrorLog(String errorType, String message) {
        try {
            File textFile = ErrorManager.createErrorLogFile();
            ErrorManager.writeErrorLog(textFile, errorType, message);
        }
        catch (Exception e) {
            System.err.println("Failed to create error log.");
            e.printStackTrace();
        }
    }

    public static String errorToString(Throwable error) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        return sw.toString();
    }

    private static File createErrorLogFile() throws Exception {
        File textFile = new File(logFolder, "ERROR_" + Calendar.getInstance().getTime().getTime() + ".txt");
        if (!logFolder.exists()) {
            logFolder.mkdir();
        }
        textFile.createNewFile();
        return textFile;
    }

    private static void writeErrorLog(File textFile, String name, String contents) throws Exception {
        PrintWriter printWriter = new PrintWriter(textFile);
        printWriter.println(name);
        printWriter.println(contents);
        printWriter.close();
    }
}


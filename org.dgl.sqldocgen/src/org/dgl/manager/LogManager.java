package org.dgl.manager;

import java.io.*;

public class LogManager {

    private static final String LOG_PATH = "log_path";
    private static final String INFO_TAG = " - INF - ";
    private static final String ERROR_TAG = " - ERR - ";

    public static synchronized void info(String line) {
        PrintWriter printer = null;
        try {
            String path = "";
            if (true) {
                ConfigurationManager config = new ConfigurationManager();
                path = config.get(LOG_PATH);
            }
            printer = new PrintWriter(new java.io.FileWriter(path + today() + ".txt", true));
            printer.println(now() + INFO_TAG + line);
        } catch (Exception ex) {
        } finally {
            if (printer != null) {
                printer.flush();
                printer.close();
            }
        }
    }

    public static synchronized void info(String line, String utente) {
        PrintWriter printer = null;
        try {
            String path = "";
            if (true) {
                ConfigurationManager config = new ConfigurationManager();
                path = config.get(LOG_PATH);
            }
            printer = new PrintWriter(new java.io.FileWriter(path + today() + ".txt", true));
            printer.println(now() + INFO_TAG + " (" + utente + ") " + line);
        } catch (Exception ex) {
        } finally {
            if (printer != null) {
                printer.flush();
                printer.close();
            }
        }
    }

    public static synchronized void error(String line) {
        PrintWriter printer = null;
        try {
            String path = "";
            if (true) {
                ConfigurationManager config = new ConfigurationManager();
                path = config.get(LOG_PATH);
            }
            printer = new PrintWriter(new java.io.FileWriter(path + today() + ".txt", true));
            printer.println(now() + ERROR_TAG + line);
        } catch (Exception ex) {
        } finally {
            if (printer != null) {
                printer.flush();
                printer.close();
            }
        }
    }

    public static synchronized void error(String line, String utente) {
        PrintWriter printer = null;
        try {
            String path = "";
            if (true) {
                ConfigurationManager config = new ConfigurationManager();
                path = config.get(LOG_PATH);
            }
            printer = new PrintWriter(new java.io.FileWriter(path + today() + ".txt", true));
            printer.println(now() + ERROR_TAG + " (" + utente + ") " + line);
        } catch (Exception ex) {
        } finally {
            if (printer != null) {
                printer.flush();
                printer.close();
            }
        }
    }

    public static String today() {
        java.util.Calendar date = new java.util.GregorianCalendar();
        String year = "" + date.get(java.util.GregorianCalendar.YEAR);
        String month = "" + (date.get(java.util.GregorianCalendar.MONTH) + 1);
        if (month.length() < 2) {
            month = "0" + month;
        }
        String day = "" + date.get(java.util.GregorianCalendar.DAY_OF_MONTH);
        if (day.length() < 2) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    public static String now() {
        java.util.Calendar date = new java.util.GregorianCalendar();
        String hour = "" + date.get(java.util.GregorianCalendar.HOUR_OF_DAY);
        if (hour.length() < 2) {
            hour = "0" + hour;
        }
        String minute = "" + date.get(java.util.GregorianCalendar.MINUTE);
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        String second = "" + date.get(java.util.GregorianCalendar.SECOND);
        if (second.length() < 2) {
            second = "0" + second;
        }
        return hour + ":" + minute + ":" + second;
    }

    public static String describeException(Exception ex) {
        String exception = ex.toString() + "\n";
        exception = exception + ex.getMessage() + "\n";
        for (int i = 0; (i < ex.getStackTrace().length) && (i < 20); i++) {
            exception = exception + ex.getStackTrace()[i].toString() + "\n";
        }
        return exception;
    }
}

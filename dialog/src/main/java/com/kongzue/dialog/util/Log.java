/*
 * Copyright (C) 2010 Michael Pardo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kongzue.dialog.util;

import java.util.Locale;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * Log Class
 */
public final class Log {
    private static boolean sEnabled = false;
    private static final String TAG_LOG = "[dialog]";
    private static final int DOMAIN_ID = 0xAD00F00;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, DOMAIN_ID, Log.TAG_LOG);
    private static final String LOG_FORMAT = "%s: %s";

    private Log() {
        /* Do nothing */
    }

    /**
     * Print debug log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void d(String tag, String msg) {
        HiLog.debug(LABEL_LOG, String.format(Locale.ROOT, LOG_FORMAT, tag, msg));
    }

    /**
     * Print info log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void i(String tag, String msg) {
        HiLog.info(LABEL_LOG, String.format(Locale.ROOT, LOG_FORMAT, tag, msg));
    }

    /**
     * Print warn log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void w(String tag, String msg) {
        HiLog.warn(LABEL_LOG, String.format(Locale.ROOT, LOG_FORMAT, tag, msg));
    }

    /**
     * Print error log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void e(String tag, String msg) {
        HiLog.error(LABEL_LOG, String.format(Locale.ROOT, LOG_FORMAT, tag, msg));
    }

    /**
     * isEnabled
     *
     * @return enabled boolean
     */
    public static boolean isEnabled() {
        return sEnabled;
    }

    /**
     * setEnabled sets the logging state
     *
     * @param enabled enabled
     */
    public static void setEnabled(boolean enabled) {
        sEnabled = enabled;
    }

    /**
     * Chekc if logging is enabled
     *
     * @return logging status
     */
    public static boolean isLoggingEnabled() {
        return sEnabled;
    }

    /**
     * Print info log
     *
     * @param msg log message
     * @return zero
     */
    public static int i(String msg) {
        if (sEnabled) {
            i(TAG_LOG, msg);
        }
        return 0;
    }

    /**
     * Print warning log
     * @param msg message
     */
    public static void w(String msg) {
        if (sEnabled) {
            w(TAG_LOG, msg);
        }
    }

    /**
     * Print warning log
     *
     * @param msg message
     * @param tr throwable
     */
    public static void w(String msg, Throwable tr) {
        if (sEnabled) {
            w(TAG_LOG, msg + tr.getLocalizedMessage());
        }
    }

    /**
     * Print error log
     *
     * @param msg log message
     */
    public static void e(String msg) {
        if (sEnabled) {
            e(TAG_LOG, msg);
        }
    }

    /**
     * Print error log
     *
     * @param msg msg
     * @param tr throwable
     */
    public static void e(String msg, Throwable tr) {
        if (sEnabled) {
            e(TAG_LOG, msg + tr.getLocalizedMessage());
        }
    }
}
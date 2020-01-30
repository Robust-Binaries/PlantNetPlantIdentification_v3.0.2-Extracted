package com.facebook.common.logging;

public interface LoggingDelegate {
    /* renamed from: d */
    void mo9928d(String str, String str2);

    /* renamed from: d */
    void mo9929d(String str, String str2, Throwable th);

    /* renamed from: e */
    void mo9930e(String str, String str2);

    /* renamed from: e */
    void mo9931e(String str, String str2, Throwable th);

    int getMinimumLoggingLevel();

    /* renamed from: i */
    void mo9933i(String str, String str2);

    /* renamed from: i */
    void mo9934i(String str, String str2, Throwable th);

    boolean isLoggable(int i);

    void log(int i, String str, String str2);

    void setMinimumLoggingLevel(int i);

    /* renamed from: v */
    void mo9939v(String str, String str2);

    /* renamed from: v */
    void mo9940v(String str, String str2, Throwable th);

    /* renamed from: w */
    void mo9941w(String str, String str2);

    /* renamed from: w */
    void mo9942w(String str, String str2, Throwable th);

    void wtf(String str, String str2);

    void wtf(String str, String str2, Throwable th);
}

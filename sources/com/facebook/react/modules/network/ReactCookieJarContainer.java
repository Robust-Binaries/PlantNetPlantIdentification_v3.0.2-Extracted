package com.facebook.react.modules.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers.Builder;
import okhttp3.HttpUrl;

public class ReactCookieJarContainer implements CookieJarContainer {
    @Nullable
    private CookieJar cookieJar = null;

    public void setCookieJar(CookieJar cookieJar2) {
        this.cookieJar = cookieJar2;
    }

    public void removeCookieJar() {
        this.cookieJar = null;
    }

    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        CookieJar cookieJar2 = this.cookieJar;
        if (cookieJar2 != null) {
            cookieJar2.saveFromResponse(httpUrl, list);
        }
    }

    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        CookieJar cookieJar2 = this.cookieJar;
        if (cookieJar2 == null) {
            return Collections.emptyList();
        }
        List<Cookie> loadForRequest = cookieJar2.loadForRequest(httpUrl);
        ArrayList arrayList = new ArrayList();
        for (Cookie cookie : loadForRequest) {
            try {
                new Builder().add(cookie.name(), cookie.value());
                arrayList.add(cookie);
            } catch (IllegalArgumentException unused) {
            }
        }
        return arrayList;
    }
}

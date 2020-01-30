package p007io.invertase.firebase.admob;

import android.support.p000v4.p002os.EnvironmentCompat;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.VideoOptions;
import p007io.invertase.firebase.C1350Utils;

/* renamed from: io.invertase.firebase.admob.RNFirebaseAdMobUtils */
class RNFirebaseAdMobUtils {
    RNFirebaseAdMobUtils() {
    }

    static WritableMap errorCodeToMap(int i) {
        WritableMap createMap = Arguments.createMap();
        switch (i) {
            case 0:
                createMap.putString("code", "admob/error-code-internal-error");
                createMap.putString("message", "Something happened internally; for instance, an invalid response was received from the ad server.");
                break;
            case 1:
                createMap.putString("code", "admob/error-code-invalid-request");
                createMap.putString("message", "The ad request was invalid; for instance, the ad unit ID was incorrect.");
                break;
            case 2:
                createMap.putString("code", "admob/error-code-network-error");
                createMap.putString("message", "The ad request was unsuccessful due to network connectivity.");
                break;
            case 3:
                createMap.putString("code", "admob/error-code-no-fill");
                createMap.putString("message", "The ad request was successful, but no ad was returned due to lack of ad inventory.");
                break;
        }
        return createMap;
    }

    static Builder buildRequest(ReadableMap readableMap) {
        Builder builder = new Builder();
        if (readableMap.hasKey("isDesignedForFamilies")) {
            builder.setIsDesignedForFamilies(readableMap.getBoolean("isDesignedForFamilies"));
        }
        if (readableMap.hasKey("tagForChildDirectedTreatment")) {
            builder.tagForChildDirectedTreatment(readableMap.getBoolean("tagForChildDirectedTreatment"));
        }
        if (readableMap.hasKey("contentUrl")) {
            builder.setContentUrl(readableMap.getString("contentUrl"));
        }
        if (readableMap.hasKey("requestAgent")) {
            builder.setRequestAgent(readableMap.getString("requestAgent"));
        }
        if (readableMap.hasKey("gender")) {
            String string = readableMap.getString("gender");
            char c = 65535;
            int hashCode = string.hashCode();
            if (hashCode != -1278174388) {
                if (hashCode != -284840886) {
                    if (hashCode == 3343885 && string.equals("male")) {
                        c = 0;
                    }
                } else if (string.equals(EnvironmentCompat.MEDIA_UNKNOWN)) {
                    c = 2;
                }
            } else if (string.equals("female")) {
                c = 1;
            }
            switch (c) {
                case 0:
                    builder.setGender(1);
                    break;
                case 1:
                    builder.setGender(2);
                    break;
                case 2:
                    builder.setGender(0);
                    break;
            }
        }
        for (Object next : C1350Utils.recursivelyDeconstructReadableArray(readableMap.getArray("testDevices"))) {
            if (next == "DEVICE_ID_EMULATOR") {
                builder.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
            } else {
                builder.addTestDevice((String) next);
            }
        }
        for (String addKeyword : C1350Utils.recursivelyDeconstructReadableArray(readableMap.getArray("keywords"))) {
            builder.addKeyword(addKeyword);
        }
        return builder;
    }

    static VideoOptions.Builder buildVideoOptions(ReadableMap readableMap) {
        VideoOptions.Builder builder = new VideoOptions.Builder();
        builder.setStartMuted(readableMap.getBoolean("startMuted"));
        return builder;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0077, code lost:
        if (r4.equals("LARGE_BANNER") != false) goto L_0x007b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.google.android.gms.ads.AdSize stringToAdSize(java.lang.String r4) {
        /*
            java.lang.String r0 = "([0-9]+)x([0-9]+)"
            java.util.regex.Pattern r0 = java.util.regex.Pattern.compile(r0)
            java.util.regex.Matcher r0 = r0.matcher(r4)
            boolean r1 = r0.find()
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L_0x0028
            java.lang.String r4 = r0.group(r3)
            int r4 = java.lang.Integer.parseInt(r4)
            java.lang.String r0 = r0.group(r2)
            int r0 = java.lang.Integer.parseInt(r0)
            com.google.android.gms.ads.AdSize r1 = new com.google.android.gms.ads.AdSize
            r1.<init>(r4, r0)
            return r1
        L_0x0028:
            java.lang.String r4 = r4.toUpperCase()
            r0 = -1
            int r1 = r4.hashCode()
            switch(r1) {
                case -1966536496: goto L_0x0071;
                case -1008851236: goto L_0x0067;
                case -140586366: goto L_0x005d;
                case -96588539: goto L_0x0053;
                case 446888797: goto L_0x0049;
                case 1669469470: goto L_0x003f;
                case 1951953708: goto L_0x0035;
                default: goto L_0x0034;
            }
        L_0x0034:
            goto L_0x007a
        L_0x0035:
            java.lang.String r1 = "BANNER"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x007a
            r2 = 1
            goto L_0x007b
        L_0x003f:
            java.lang.String r1 = "SMART_BANNER_LANDSCAPE"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x007a
            r2 = 7
            goto L_0x007b
        L_0x0049:
            java.lang.String r1 = "LEADERBOARD"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x007a
            r2 = 5
            goto L_0x007b
        L_0x0053:
            java.lang.String r1 = "MEDIUM_RECTANGLE"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x007a
            r2 = 3
            goto L_0x007b
        L_0x005d:
            java.lang.String r1 = "SMART_BANNER"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x007a
            r2 = 6
            goto L_0x007b
        L_0x0067:
            java.lang.String r1 = "FULL_BANNER"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x007a
            r2 = 4
            goto L_0x007b
        L_0x0071:
            java.lang.String r1 = "LARGE_BANNER"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x007a
            goto L_0x007b
        L_0x007a:
            r2 = -1
        L_0x007b:
            switch(r2) {
                case 2: goto L_0x0090;
                case 3: goto L_0x008d;
                case 4: goto L_0x008a;
                case 5: goto L_0x0087;
                case 6: goto L_0x0084;
                case 7: goto L_0x0081;
                default: goto L_0x007e;
            }
        L_0x007e:
            com.google.android.gms.ads.AdSize r4 = com.google.android.gms.ads.AdSize.BANNER
            return r4
        L_0x0081:
            com.google.android.gms.ads.AdSize r4 = com.google.android.gms.ads.AdSize.SMART_BANNER
            return r4
        L_0x0084:
            com.google.android.gms.ads.AdSize r4 = com.google.android.gms.ads.AdSize.SMART_BANNER
            return r4
        L_0x0087:
            com.google.android.gms.ads.AdSize r4 = com.google.android.gms.ads.AdSize.LEADERBOARD
            return r4
        L_0x008a:
            com.google.android.gms.ads.AdSize r4 = com.google.android.gms.ads.AdSize.FULL_BANNER
            return r4
        L_0x008d:
            com.google.android.gms.ads.AdSize r4 = com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE
            return r4
        L_0x0090:
            com.google.android.gms.ads.AdSize r4 = com.google.android.gms.ads.AdSize.LARGE_BANNER
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: p007io.invertase.firebase.admob.RNFirebaseAdMobUtils.stringToAdSize(java.lang.String):com.google.android.gms.ads.AdSize");
    }
}

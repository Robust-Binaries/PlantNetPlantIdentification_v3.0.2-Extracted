package p007io.invertase.firebase;

/* renamed from: io.invertase.firebase.ErrorUtils */
public class ErrorUtils {
    public static String getMessageWithService(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(": ");
        sb.append(str);
        sb.append(" (");
        sb.append(str3.toLowerCase());
        sb.append(").");
        return sb.toString();
    }

    public static String getCodeWithService(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.toLowerCase());
        sb.append("/");
        sb.append(str2.toLowerCase());
        return sb.toString();
    }
}

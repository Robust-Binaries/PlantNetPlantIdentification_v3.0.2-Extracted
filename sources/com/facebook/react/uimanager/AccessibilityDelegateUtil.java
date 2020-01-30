package com.facebook.react.uimanager;

import android.content.Context;
import android.support.p000v4.view.AccessibilityDelegateCompat;
import android.support.p000v4.view.ViewCompat;
import android.support.p000v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.p000v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.view.View;
import com.facebook.react.C0788R;
import java.util.Locale;
import javax.annotation.Nullable;

public class AccessibilityDelegateUtil {

    public enum AccessibilityRole {
        NONE,
        BUTTON,
        LINK,
        SEARCH,
        IMAGE,
        IMAGEBUTTON,
        KEYBOARDKEY,
        TEXT,
        ADJUSTABLE,
        SUMMARY,
        HEADER;

        public static String getValue(AccessibilityRole accessibilityRole) {
            switch (accessibilityRole) {
                case NONE:
                    return null;
                case BUTTON:
                    return "android.widget.Button";
                case LINK:
                    return "android.widget.ViewGroup";
                case SEARCH:
                    return "android.widget.EditText";
                case IMAGE:
                    return "android.widget.ImageView";
                case IMAGEBUTTON:
                    return "android.widget.ImageView";
                case KEYBOARDKEY:
                    return "android.inputmethodservice.Keyboard$Key";
                case TEXT:
                    return "android.widget.ViewGroup";
                case ADJUSTABLE:
                    return "android.widget.SeekBar";
                case SUMMARY:
                    return "android.widget.ViewGroup";
                case HEADER:
                    return "android.widget.ViewGroup";
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid accessibility role value: ");
                    sb.append(accessibilityRole);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public static AccessibilityRole fromValue(@Nullable String str) {
            AccessibilityRole[] values;
            for (AccessibilityRole accessibilityRole : values()) {
                if (accessibilityRole.name().equalsIgnoreCase(str)) {
                    return accessibilityRole;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid accessibility role value: ");
            sb.append(str);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    private AccessibilityDelegateUtil() {
    }

    public static void setDelegate(final View view) {
        final String str = (String) view.getTag(C0788R.C0791id.accessibility_hint);
        final AccessibilityRole accessibilityRole = (AccessibilityRole) view.getTag(C0788R.C0791id.accessibility_role);
        if (ViewCompat.hasAccessibilityDelegate(view)) {
            return;
        }
        if (str != null || accessibilityRole != null) {
            ViewCompat.setAccessibilityDelegate(view, new AccessibilityDelegateCompat() {
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                    if (str != null) {
                        String str = (String) accessibilityNodeInfoCompat.getContentDescription();
                        if (str != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str);
                            sb.append(", ");
                            sb.append(str);
                            accessibilityNodeInfoCompat.setContentDescription(sb.toString());
                        } else {
                            accessibilityNodeInfoCompat.setContentDescription(str);
                        }
                    }
                    AccessibilityDelegateUtil.setRole(accessibilityNodeInfoCompat, accessibilityRole, view.getContext());
                }
            });
        }
    }

    public static void setRole(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityRole accessibilityRole, Context context) {
        if (accessibilityRole == null) {
            accessibilityRole = AccessibilityRole.NONE;
        }
        accessibilityNodeInfoCompat.setClassName(AccessibilityRole.getValue(accessibilityRole));
        if (Locale.getDefault().getLanguage().equals(new Locale("en").getLanguage())) {
            if (accessibilityRole.equals(AccessibilityRole.LINK)) {
                accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0788R.string.link_description));
                if (accessibilityNodeInfoCompat.getContentDescription() != null) {
                    SpannableString spannableString = new SpannableString(accessibilityNodeInfoCompat.getContentDescription());
                    spannableString.setSpan(new URLSpan(""), 0, spannableString.length(), 0);
                    accessibilityNodeInfoCompat.setContentDescription(spannableString);
                }
                if (accessibilityNodeInfoCompat.getText() != null) {
                    SpannableString spannableString2 = new SpannableString(accessibilityNodeInfoCompat.getText());
                    spannableString2.setSpan(new URLSpan(""), 0, spannableString2.length(), 0);
                    accessibilityNodeInfoCompat.setText(spannableString2);
                }
            }
            if (accessibilityRole.equals(AccessibilityRole.SEARCH)) {
                accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0788R.string.search_description));
            }
            if (accessibilityRole.equals(AccessibilityRole.IMAGE)) {
                accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0788R.string.image_description));
            }
            if (accessibilityRole.equals(AccessibilityRole.IMAGEBUTTON)) {
                accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0788R.string.image_button_description));
            }
            if (accessibilityRole.equals(AccessibilityRole.ADJUSTABLE)) {
                accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0788R.string.adjustable_description));
            }
            if (accessibilityRole.equals(AccessibilityRole.HEADER)) {
                accessibilityNodeInfoCompat.setRoleDescription(context.getString(C0788R.string.header_description));
                accessibilityNodeInfoCompat.setCollectionItemInfo(CollectionItemInfoCompat.obtain(0, 1, 0, 1, true));
            }
        }
        if (accessibilityRole.equals(AccessibilityRole.IMAGEBUTTON)) {
            accessibilityNodeInfoCompat.setClickable(true);
        }
    }
}

package com.facebook.react.modules.timepicker;

import android.app.Dialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.p000v4.app.DialogFragment;
import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.annotation.Nullable;

public class TimePickerDialogFragment extends DialogFragment {
    @Nullable
    private OnDismissListener mOnDismissListener;
    @Nullable
    private OnTimeSetListener mOnTimeSetListener;

    public Dialog onCreateDialog(Bundle bundle) {
        return createDialog(getArguments(), getActivity(), this.mOnTimeSetListener);
    }

    static Dialog createDialog(Bundle bundle, Context context, @Nullable OnTimeSetListener onTimeSetListener) {
        Bundle bundle2 = bundle;
        Calendar instance = Calendar.getInstance();
        int i = instance.get(11);
        int i2 = instance.get(12);
        boolean is24HourFormat = DateFormat.is24HourFormat(context);
        TimePickerMode timePickerMode = TimePickerMode.DEFAULT;
        if (!(bundle2 == null || bundle.getString("mode", null) == null)) {
            timePickerMode = TimePickerMode.valueOf(bundle.getString("mode").toUpperCase(Locale.US));
        }
        if (bundle2 != null) {
            i = bundle.getInt("hour", instance.get(11));
            i2 = bundle.getInt("minute", instance.get(12));
            is24HourFormat = bundle.getBoolean("is24Hour", DateFormat.is24HourFormat(context));
        }
        if (VERSION.SDK_INT >= 21) {
            if (timePickerMode == TimePickerMode.CLOCK) {
                DismissableTimePickerDialog dismissableTimePickerDialog = new DismissableTimePickerDialog(context, context.getResources().getIdentifier("ClockTimePickerDialog", "style", context.getPackageName()), onTimeSetListener, i, i2, is24HourFormat);
                return dismissableTimePickerDialog;
            } else if (timePickerMode == TimePickerMode.SPINNER) {
                DismissableTimePickerDialog dismissableTimePickerDialog2 = new DismissableTimePickerDialog(context, context.getResources().getIdentifier("SpinnerTimePickerDialog", "style", context.getPackageName()), onTimeSetListener, i, i2, is24HourFormat);
                return dismissableTimePickerDialog2;
            }
        }
        DismissableTimePickerDialog dismissableTimePickerDialog3 = new DismissableTimePickerDialog(context, onTimeSetListener, i, i2, is24HourFormat);
        return dismissableTimePickerDialog3;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        }
    }

    public void setOnDismissListener(@Nullable OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setOnTimeSetListener(@Nullable OnTimeSetListener onTimeSetListener) {
        this.mOnTimeSetListener = onTimeSetListener;
    }
}

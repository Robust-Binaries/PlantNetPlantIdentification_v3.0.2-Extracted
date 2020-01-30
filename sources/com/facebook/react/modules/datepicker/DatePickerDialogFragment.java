package com.facebook.react.modules.datepicker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.p000v4.app.DialogFragment;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Locale;
import javax.annotation.Nullable;

@SuppressLint({"ValidFragment"})
public class DatePickerDialogFragment extends DialogFragment {
    private static final long DEFAULT_MIN_DATE = -2208988800001L;
    @Nullable
    private OnDateSetListener mOnDateSetListener;
    @Nullable
    private OnDismissListener mOnDismissListener;

    public Dialog onCreateDialog(Bundle bundle) {
        return createDialog(getArguments(), getActivity(), this.mOnDateSetListener);
    }

    static Dialog createDialog(Bundle bundle, Context context, @Nullable OnDateSetListener onDateSetListener) {
        DismissableDatePickerDialog dismissableDatePickerDialog;
        Calendar instance = Calendar.getInstance();
        if (bundle != null && bundle.containsKey("date")) {
            instance.setTimeInMillis(bundle.getLong("date"));
        }
        int i = instance.get(1);
        int i2 = instance.get(2);
        int i3 = instance.get(5);
        DatePickerMode valueOf = (bundle == null || bundle.getString("mode", null) == null) ? DatePickerMode.DEFAULT : DatePickerMode.valueOf(bundle.getString("mode").toUpperCase(Locale.US));
        if (VERSION.SDK_INT >= 21) {
            switch (valueOf) {
                case CALENDAR:
                    dismissableDatePickerDialog = new DismissableDatePickerDialog(context, context.getResources().getIdentifier("CalendarDatePickerDialog", "style", context.getPackageName()), onDateSetListener, i, i2, i3);
                    break;
                case SPINNER:
                    dismissableDatePickerDialog = new DismissableDatePickerDialog(context, context.getResources().getIdentifier("SpinnerDatePickerDialog", "style", context.getPackageName()), onDateSetListener, i, i2, i3);
                    break;
                case DEFAULT:
                    dismissableDatePickerDialog = new DismissableDatePickerDialog(context, onDateSetListener, i, i2, i3);
                    break;
                default:
                    dismissableDatePickerDialog = null;
                    break;
            }
        } else {
            DismissableDatePickerDialog dismissableDatePickerDialog2 = new DismissableDatePickerDialog(context, onDateSetListener, i, i2, i3);
            switch (valueOf) {
                case CALENDAR:
                    dismissableDatePickerDialog2.getDatePicker().setCalendarViewShown(true);
                    dismissableDatePickerDialog2.getDatePicker().setSpinnersShown(false);
                    break;
                case SPINNER:
                    dismissableDatePickerDialog2.getDatePicker().setCalendarViewShown(false);
                    break;
            }
            dismissableDatePickerDialog = dismissableDatePickerDialog2;
        }
        DatePicker datePicker = dismissableDatePickerDialog.getDatePicker();
        if (bundle == null || !bundle.containsKey("minDate")) {
            datePicker.setMinDate(DEFAULT_MIN_DATE);
        } else {
            instance.setTimeInMillis(bundle.getLong("minDate"));
            instance.set(11, 0);
            instance.set(12, 0);
            instance.set(13, 0);
            instance.set(14, 0);
            datePicker.setMinDate(instance.getTimeInMillis());
        }
        if (bundle != null && bundle.containsKey("maxDate")) {
            instance.setTimeInMillis(bundle.getLong("maxDate"));
            instance.set(11, 23);
            instance.set(12, 59);
            instance.set(13, 59);
            instance.set(14, 999);
            datePicker.setMaxDate(instance.getTimeInMillis());
        }
        return dismissableDatePickerDialog;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setOnDateSetListener(@Nullable OnDateSetListener onDateSetListener) {
        this.mOnDateSetListener = onDateSetListener;
    }

    /* access modifiers changed from: 0000 */
    public void setOnDismissListener(@Nullable OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }
}

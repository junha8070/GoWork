package com.example.gowork.views.work;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.example.gowork.R;
import com.google.android.material.shape.RelativeCornerSize;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

public class Calendar implements DayViewDecorator {

    private CalendarDay today;

    public Calendar() {
        today = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(today);
    }


    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
//        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new BackgroundColorSpan(Color.rgb(162, 160, 255)));
    }

    public void setToday(Date date) {
        this.today = CalendarDay.from(date);
    }

    public CalendarDay getToday() {
        return today;
    }
}

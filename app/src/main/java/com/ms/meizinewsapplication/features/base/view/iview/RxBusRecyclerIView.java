package com.ms.meizinewsapplication.features.base.view.iview;

import android.app.Activity;

import com.ms.meizinewsapplication.R;
import com.ms.meizinewsapplication.features.base.event.ColorfulEvent;
import com.ms.mythemelibrary.lib.Colorful;
import com.ms.mythemelibrary.lib.setter.RecyclerViewSetter;
import com.ms.retrofitlibrary.util.rx.RxBus;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by 啟成 on 2016/6/6.
 */
public class RxBusRecyclerIView extends RecyclerIView {


    protected Colorful mColorful;
    protected RecyclerViewSetter recyclerViewSetter;

    protected boolean isDay = true;


    public void initColorful(Activity activity) {

        initRecyclerViewSetter();
        mColorful = new Colorful.Builder(activity)
                .setter(recyclerViewSetter) // 手动设置setter
                .create();
    }

    protected void initRecyclerViewSetter()
    {
        recyclerViewSetter = new RecyclerViewSetter(
                recycler_list,
                0
        );
        recyclerViewSetter.childViewTextColor(
                R.id.tv_type,
                R.attr.text_item_color
        );
        recyclerViewSetter.childViewTextColor(
                R.id.story_item_title,
                R.attr.text_item_color
        );
        recyclerViewSetter.childViewBgColor(
                R.id.tv_type,
                R.attr.root_view_bg
        );
        recyclerViewSetter.childViewBgColor(
                R.id.story_item,
                R.attr.root_view_bg
        );

    }


    public void rxColorfulEvent(ColorfulEvent colorfulEvent) {
        RxBus.getInstance().post4HasObservers(colorfulEvent);
    }

    public Subscription rxBusEvent() {
        return RxBus.getInstance().toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {

                if (o instanceof ColorfulEvent) {
                    eventColorful((ColorfulEvent) o);
                }

            }
        });
    }

    public void eventColorful(ColorfulEvent colorfulEvent) {

        if (!isDay) {
            mColorful.setTheme(R.style.NightTheme);
        } else {
            mColorful.setTheme(R.style.DayTheme);
        }

        isDay = !isDay;

    }
}

package com.bleadingsmile.utildemo.demoitem;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Hsiao on 2016/12/18.
 */

public class DemoItemFactory {
    public static List<DemoItem> obtainDemoItems(Context context){
        List<DemoItem> demoItems = new ArrayList<>();
        demoItems.add(new OneShotPermissionGranter(context));
        demoItems.add(new SpeechRecognize(context));
        return demoItems;
    }
}

package com.app.collow.commondialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.app.collow.baseviews.BaseTextview;

import java.util.List;

/**
 * Created by chinkal on 1/23/17.
 */

public class MyListViewDialog {
    Context context=null;
    String title=null;
    BaseTextview baseTextview_set_item=null;

  public  MyListViewDialog(Context context, String title, List<String> stringList, BaseTextview baseTextview_set_item)
    {
        this.context=context;
        this.title=title;
        this.baseTextview_set_item=baseTextview_set_item;

        showDialogListing(stringList);
    }

    public void showDialogListing(List<String> stringList)
    {
        final CharSequence[] items = stringList.toArray(new CharSequence[stringList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                baseTextview_set_item.setText(String.valueOf(items[item]));
            }
        });
        builder.show();
    }
}

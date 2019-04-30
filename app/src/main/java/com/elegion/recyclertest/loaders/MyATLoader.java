package com.elegion.recyclertest.loaders;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;

public class MyATLoader extends AsyncTaskLoader<String> {

    private String id;

    public MyATLoader(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    public String loadInBackground() {

        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                new String[]{id, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursor.close();

//            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + number)));
            return number;
        }

        return null;
    }
}

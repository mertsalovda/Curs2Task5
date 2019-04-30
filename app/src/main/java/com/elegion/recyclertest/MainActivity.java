package com.elegion.recyclertest;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.elegion.recyclertest.loaders.MyATLoader;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.OnItemClickListener,
        LoaderManager.LoaderCallbacks<String> {
    private static final int LOADER_ID = 0;
    private String id_contact;
    private LoaderManager loaderManager;

    // добавить фрагмент с recyclerView ---
    // добавить адаптер, холдер и генератор заглушечных данных ---
    // добавить обновление данных и состояние ошибки ---
    // добавить загрузку данных с телефонной книги ---
    // добавить обработку нажатий ---
    // добавить декораторы ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RecyclerFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                try {
                    if(loaderManager.getLoader(LOADER_ID) != null) {
                        loaderManager.destroyLoader(LOADER_ID);
                        Toast.makeText(this, R.string.txt_request_canceled, Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(String id) {
        id_contact = id;
        loaderManager = getLoaderManager();
        loaderManager.restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new MyATLoader(this, id_contact);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String number) {
        if (number != null) {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + number)));
        } else {
            Toast.makeText(this, R.string.txt_error_number_empty, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    @Override
    protected void onPause() {
        loaderManager.destroyLoader(LOADER_ID);
        super.onPause();
    }
}

package com.herma.apps.textbooks.settings;

import static com.herma.apps.textbooks.settings.LanguageHelper.LANGUAGE_CODE_KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.herma.apps.textbooks.R;
import com.herma.apps.textbooks.ReadActivity;
import com.herma.apps.textbooks.ui.about.About_us;

import java.util.HashMap;
import java.util.Map;

// SettingsActivity.java
public class SettingsActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private Spinner themeModeSpinner;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LanguageHelper.updateLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Add back button to the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        languageSpinner = findViewById(R.id.language_spinner);
        themeModeSpinner = findViewById(R.id.theme_spinner);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> themeModeAdapter = ArrayAdapter.createFromResource(this,
                R.array.themeMode, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        themeModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeModeSpinner.setAdapter(themeModeAdapter);

        String languageCode = LanguageHelper.getLanguage(this);
        if(languageCode.equals("am"))
            languageSpinner.setSelection(1);
        else if(languageCode.equals("or"))
            languageSpinner.setSelection(2);
        else if(languageCode.equals("ar"))
            languageSpinner.setSelection(3);

        if(prefs.getString("themeMode","light").equals("dark"))
            themeModeSpinner.setSelection(1);


        // Save language settings when the user clicks the save button
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = languageSpinner.getSelectedItem().toString();
                String languageCode = "en";

                if(language == getString(R.string.lang_am))
                    languageCode = "am";
                else if(language == getString(R.string.lang_or))
                    languageCode = "or";
                else if(language == getString(R.string.lang_ar))
                    languageCode = "ar";

                LanguageHelper.setLanguage(getApplicationContext(), languageCode);
                LanguageHelper.updateLanguage(SettingsActivity.this);

                String themeMode = themeModeSpinner.getSelectedItem().toString();
                SharedPreferences.Editor editor = prefs.edit();

                if(themeMode == getString(R.string.theme_mode_light)) {
                    editor.putString("themeMode", "light");
                    editor.apply();
                }else {
                    editor.putString("themeMode", "dark");
                    editor.apply();
                }

                Toast.makeText(SettingsActivity.this, R.string.restart_the_app, Toast.LENGTH_LONG).show();

                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

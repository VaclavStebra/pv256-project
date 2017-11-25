package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(getSavedTheme());
        setContentView(R.layout.activity_main);

        Button changeThemeBtn = (Button) findViewById(R.id.change_theme_button);

        changeThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleTheme();
            }
        });
    }

    private int getSavedTheme() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(getString(R.string.saved_theme), R.style.PrimaryTheme);
    }

    private void toggleTheme() {
        int theme = getSavedTheme();
        saveTheme(getNewTheme(theme));
        restartActivity();
    }

    private int getNewTheme(int theme) {
        return theme == R.style.PrimaryTheme
                ? R.style.SecondaryTheme
                : R.style.PrimaryTheme;
    }

    private void saveTheme(int newTheme) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_theme), newTheme);
        editor.commit();
    }

    private void restartActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

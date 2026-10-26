package dev.indy.formularz;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private Button btnPickDate, btnSubmit;
    private Spinner spCourse;
    private RadioGroup rgMode;
    private SeekBar sbLevel;
    private TextView tvLevelValue, tvSummary;
    private MaterialSwitch swNewsletter;
    private CheckBox cbTerms;

    private String birthDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.etName = this.findViewById(R.id.etName);
        this.etEmail = this.findViewById(R.id.etEmail);
        this.btnPickDate = this.findViewById(R.id.btnBirthDate);
        this.btnSubmit = this.findViewById(R.id.btnSubmit);
        this.spCourse = this.findViewById(R.id.spCourse);
        this.rgMode = this.findViewById(R.id.rgMode);
        this.sbLevel = this.findViewById(R.id.sbLevel);
        this.tvLevelValue = this.findViewById(R.id.tvLevelValue);
        this.tvSummary = this.findViewById(R.id.tvSummary);
        this.swNewsletter = this.findViewById(R.id.swNewsletter);
        this.cbTerms = this.findViewById(R.id.cbTerms);

        this.cbTerms.setOnCheckedChangeListener((v, checked) -> this.btnSubmit.setEnabled(checked));
        this.btnPickDate.setOnClickListener(v -> this.openDatePicker());
        this.btnSubmit.setOnClickListener(v-> {
            if(this.validateForm()) this.askForConfirmation();
        });

        this.sbLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLevelValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        this.spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position > 0) tvSummary.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        this.setupCourseSpinner();
    }

    private void setupCourseSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.courses, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spCourse.setAdapter(adapter);
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(this,
            (view, year, month, dayOfMonth) -> {
                this.birthDate = String.format(Locale.US, "%04d-%02d-%02d",
                    year, month + 1, dayOfMonth
                );
                this.btnPickDate.setText(this.birthDate);
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private boolean validateForm() {
        String name = this.etName.getText().toString().trim();
        if(name.isBlank()) {
            this.etName.setError(this.getString(R.string.errorName));
            this.etName.requestFocus();

            return false;
        }

        String email = this.etEmail.getText().toString().trim();
        if(email.isBlank()) {
            this.etEmail.setError(this.getString(R.string.errorEmail));
            this.etEmail.requestFocus();

            return false;
        }

        if(this.birthDate.isBlank()) {
            Toast.makeText(this, this.getString(R.string.errorDate), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(this.spCourse.getSelectedItemPosition() == 0) {
            Toast.makeText(this, this.getString(R.string.errorCourse), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(this.rgMode.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, this.getString(R.string.errorMode), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getModeText() {
        Integer id = this.rgMode.getCheckedRadioButtonId();

        if(id.equals(R.id.rbOnsite)) return this.getString(R.string.modeOnSite);
        else if(id.equals(R.id.rbRemote)) return this.getString(R.string.modeRemote);

        return this.getString(R.string.modeRemote);
    }

    private String getNewsletterText() {
        return this.swNewsletter.isChecked()
            ? this.getString(R.string.yesShort)
            : this.getString(R.string.noShort);
    }

    private void askForConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.confirmTitle)
            .setMessage(R.string.confirmMessage)
            .setPositiveButton(R.string.yes, (dialog, which) -> this.submitForm())
            .setNegativeButton(R.string.no, null)
            .show();
    }

    private void submitForm() {
        this.buildSummary();

        Snackbar.make(this.findViewById(R.id.main), R.string.sent, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo, v -> this.undoSubmit())
            .show();
    }

    private void undoSubmit() {
        this.tvSummary.setText("");
    }

    private void buildSummary() {
        this.tvSummary.setText(this.getString(R.string.summaryFormat,
            this.etName.getText().toString().trim(),
            this.etEmail.getText().toString().trim(),
            this.birthDate,
            this.spCourse.getSelectedItem().toString(),
            this.getModeText(),
            this.sbLevel.getProgress(),
            this.getNewsletterText()
        ));
    }
}
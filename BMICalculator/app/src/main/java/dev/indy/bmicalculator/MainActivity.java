package dev.indy.bmicalculator;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText _etWeight, _etHeight;
    private RadioGroup _rgGender;
    private TextView _tvResult;

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

        this._etWeight = this.findViewById(R.id.etWeight);
        this._etHeight = this.findViewById(R.id.etHeight);
        this._rgGender = this.findViewById(R.id.rgGender);
        this._tvResult = this.findViewById(R.id.tvResult);

        this.findViewById(R.id.btnCalculate).setOnClickListener(v -> this.validate());
    }

    private void validate() {
        String weightStr = this._etWeight.getText().toString();
        if(weightStr.isBlank()) {
            this._etWeight.setError(this.getString(R.string.errorWeightEmpty));
            return;
        }

        double weight = Double.parseDouble(weightStr);
        if(weight <= 0) {
            this._etWeight.setError(this.getString(R.string.errorWeightInvalid));
            return;
        }

        String heightStr = this._etHeight.getText().toString();
        if(heightStr.isBlank()) {
            this._etHeight.setError(this.getString(R.string.errorHeightEmpty));
            return;
        }

        double height = Double.parseDouble(heightStr);
        if(height <= 0 || height > 300) {
            this._etHeight.setError(this.getString(R.string.errorHeightInvalid));
            return;
        }

        if(this._rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, this.getString(R.string.errorGenderEmpty), Toast.LENGTH_SHORT).show();
            return;
        }

        this.calculate(weight, height / 100);
    }

    private void calculate(double weight, double height) {
        double bmi = weight / (height * height);

        this._tvResult.setText(this.getString(R.string.formatResult, bmi, this.getCategory(bmi)));
        this._tvResult.setVisibility(TextView.VISIBLE);
    }

    private String getCategory(double bmi) {
        if(bmi < 18.5) return this.getString(R.string.bmiUnderweight);
        else if(bmi < 25) return this.getString(R.string.bmiNormal);
        else if(bmi < 30) return this.getString(R.string.bmiOverweight);
        else if(bmi < 35) return this.getString(R.string.bmiObesity1);
        else if(bmi < 40) return this.getString(R.string.bmiObesity2);
        else return this.getString(R.string.bmiObesity3);
    }
}
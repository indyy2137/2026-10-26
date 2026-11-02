package dev.indy.pizzaorderer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RadioGroup _rgPizzaSize;
    private SeekBar _sbPizzaAmount;
    private TextView _tvPizzaAmountValue, _tvResult;
    private CheckBox _cbPizzaForKruk;
    private EditText _etAddress;
    private Button _btnOrder;

    private int[] _toppings;

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

        this._rgPizzaSize = this.findViewById(R.id.rgPizzaSize);
        this._sbPizzaAmount = this.findViewById(R.id.sbPizzaAmount);
        this._tvPizzaAmountValue = this.findViewById(R.id.tvPizzaAmountValue);
        this._cbPizzaForKruk = this.findViewById(R.id.cbPizzaForKruk);
        this._etAddress = this.findViewById(R.id.etAddress);
        this._btnOrder = this.findViewById(R.id.btnOrder);
        this._tvResult = this.findViewById(R.id.tvResult);

        this._toppings = new int[]{
            R.id.cbToppingTomatoSauce,
            R.id.cbToppingCheese,

            // Meat
            R.id.cbToppingHam,
            R.id.cbToppingSalami,
            R.id.cbToppingBacon,
            R.id.cbToppingChicken,

            // Vegetable
            R.id.cbToppingCorn,
            R.id.cbToppingOnion,
            R.id.cbToppingShrooms,
            R.id.cbToppingHawking,
        };

        this.addListeners();
    }

    private void addListeners() {
        this._sbPizzaAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                _tvPizzaAmountValue.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        this._cbPizzaForKruk.setOnCheckedChangeListener((v, b) -> this._btnOrder.setEnabled(b));
        this._btnOrder.setOnClickListener(v -> this.validate());
    }

    private void validate() {
        if(this._rgPizzaSize.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, this.getString(R.string.errorPizzaSize), Toast.LENGTH_SHORT).show();
            return;
        }

        if(this._sbPizzaAmount.getProgress() == 0) {
            Toast.makeText(this, this.getString(R.string.errorAmount), Toast.LENGTH_SHORT).show();
            return;
        }

        if(this._etAddress.getText().toString().isBlank()) {
            this._etAddress.setError(this.getString(R.string.errorAddress));
            this._etAddress.requestFocus();

            return;
        }

        this.askForConfirmation();
    }

    private void askForConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.confirmTitle)
            .setMessage(R.string.confirmMessage)
            .setPositiveButton(R.string.yes, (dialog, which) -> this.order())
            .setNegativeButton(R.string.no, null)
            .show();
    }

    private void order() {
        List<String> selectedToppings = new ArrayList<>();

        for(int id : this._toppings) {
            CheckBox checkbox = this.findViewById(id);
            if(checkbox.isChecked()) selectedToppings.add(checkbox.getText().toString());
        }

        this._tvResult.setText(this.getString(R.string.formatResult,
            String.valueOf(this._sbPizzaAmount.getProgress() + (this._cbPizzaForKruk.isChecked() ? 1 : 0)),
            ((RadioButton) this.findViewById(this._rgPizzaSize.getCheckedRadioButtonId())).getText().toString(),
            String.join(", ", selectedToppings),
            this._etAddress.getText()
        ));

        Snackbar.make(this.findViewById(R.id.main), R.string.orderPlaced, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo, v -> this.undo())
            .show();
    }

    private void undo() {
        this._tvResult.setText("");
    }
}
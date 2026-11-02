package dev.indy.pizzaorderer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private Button _btnOrder, _btnClear;

    private Map<Integer, Boolean> _toppings;

    private int _prevPizzaSizeId, _prevPizzaAmount;
    private boolean _prevPizzaForKrukChecked;
    private String _prevAddress;
    private Map<Integer, Boolean> _prevToppingsStates;

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
        this._btnClear = this.findViewById(R.id.btnClear);
        this._tvResult = this.findViewById(R.id.tvResult);

        this._toppings = new HashMap<>(Map.of(
            R.id.cbToppingTomatoSauce, true,
            R.id.cbToppingCheese, true,

            // Meat
            R.id.cbToppingHam, false,
            R.id.cbToppingSalami, false,
            R.id.cbToppingBacon, false,
            R.id.cbToppingChicken, false,

            // Vegetable
            R.id.cbToppingCorn, false,
            R.id.cbToppingOnion, false,
            R.id.cbToppingShrooms, false,
            R.id.cbToppingHawking, false
        ));

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
        this._btnClear.setOnClickListener(v -> this.askForClearConfirmation());
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

        this.askForSubmitConfirmation();
    }

    private void askForSubmitConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.submitConfirmTitle)
            .setMessage(R.string.submitConfirmMessage)
            .setPositiveButton(R.string.yesSubmit, (dialog, which) -> this.order())
            .setNegativeButton(R.string.no, null)
            .show();
    }

    private void askForClearConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.clearConfirmTitle)
            .setMessage(R.string.clearConfirmMessage)
            .setPositiveButton(R.string.yesClear, (dialog, which) -> this.clear())
            .setNegativeButton(R.string.no, null)
            .show();
    }

    private void order() {
        List<String> selectedToppings = new ArrayList<>();

        for(int id : this._toppings.keySet()) {
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
            .setAction(R.string.undo, v -> this.undoSubmit())
            .show();
    }

    private void clear() {
        // Save current state
        this._prevPizzaSizeId = this._rgPizzaSize.getCheckedRadioButtonId();

        this._prevToppingsStates = new HashMap<>();
        for(int id : this._toppings.keySet()) {
            CheckBox cb = this.findViewById(id);
            this._prevToppingsStates.put(id, cb.isChecked());
        }

        this._prevPizzaAmount = this._sbPizzaAmount.getProgress();
        this._prevPizzaForKrukChecked = this._cbPizzaForKruk.isChecked();
        this._prevAddress = this._etAddress.getText().toString();

        // Clear
        this._rgPizzaSize.clearCheck();

        for(Map.Entry<Integer, Boolean> topping : this._toppings.entrySet())
            ((CheckBox) this.findViewById(topping.getKey())).setChecked(topping.getValue());

        this._sbPizzaAmount.setProgress(0);
        this._cbPizzaForKruk.setChecked(true);
        this._etAddress.setText("");

        Snackbar.make(this.findViewById(R.id.main), R.string.formCleared, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo, v -> this.undoClear())
            .show();
    }

    private void undoSubmit() {
        this._tvResult.setText("");
    }

    private void undoClear() {
        this._rgPizzaSize.check(this._prevPizzaSizeId);

        for(Map.Entry<Integer, Boolean> topping : this._prevToppingsStates.entrySet())
            ((CheckBox) this.findViewById(topping.getKey())).setChecked(topping.getValue());

        this._sbPizzaAmount.setProgress(this._prevPizzaAmount);
        this._cbPizzaForKruk.setChecked(this._prevPizzaForKrukChecked);
        this._etAddress.setText(this._prevAddress);
    }
}
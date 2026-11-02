package dev.indy.pizzaorderer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private RadioGroup _rgPizzaSize;
    private SeekBar _sbPizzaAmount;
    private TextView _tvPizzaAmountValue;
    private CheckBox _cbPizzaForKruk;
    private EditText _etAddress;
    private Button _btnOrder;

    private int[] _availableToppings, _selectedToppings;

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

        this._availableToppings = new int[]{
            R.id.cbToppingTomatoSauce, R.id.cbToppingCheese,

            // Meat
            R.id.cbToppingHam, R.id.cbToppingSalami, R.id.cbToppingBacon, R.id.cbToppingChicken,

            // Vegetable
            R.id.cbToppingCorn, R.id.cbToppingOnion, R.id.cbToppingShrooms, R.id.cbToppingHawking
        };
        this._selectedToppings = new int[]{};
    }
}
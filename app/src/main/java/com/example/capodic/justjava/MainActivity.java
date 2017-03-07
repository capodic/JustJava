package com.example.capodic.justjava;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.microsoft.azure.mobile.MobileCenter;
        import com.microsoft.azure.mobile.analytics.Analytics;
        import com.microsoft.azure.mobile.crashes.Crashes;

        import static android.R.attr.name;
        import static android.R.id.message;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    int price =0;
    boolean hasWhippedCream = false;
    boolean hasChocolate = false;
    private static final int priceOfCoffee  = 5;
    private static final int priceOfToppingChocolate  = 2;
    private static final int priceOfToppingCream  = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileCenter.start(getApplication(), "932bc5aa-29e0-4fc4-b17f-e0efd585668d", Analytics.class, Crashes.class);
        setContentView(R.layout.activity_main);
        // aggiunto io legge valore inziale impostato all'xml di quantity
        // int num = Integer.valueOf(tv.getText().toString());
        // bisogna verificare che siano numeri..
        TextView tv = (TextView) findViewById(R.id.quantity_text_view);
        String tvValue = tv.getText().toString();
        // if (!tvValue.equals("") && !tvValue.equals(......)) {
        if (tvValue.matches("\\d+")) {
            quantity = Integer.parseInt(tvValue);
        } else {
            Log.v("MainActivity", "Errore quantity_text_value, initial value not a number");
        }
        // display price in iniziale
        displayPrice();
    }

    /**
     * This method is called when the plus button is clicked.
     */

    public void increment(View view) {
        if (quantity==100) {
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
        displayPrice();
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "Non puo' essere inferiore  1", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    public void onCheckboxChocolateClicked (View view) {
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        hasChocolate = chocolateCheckBox.isChecked();
        Log.v("MainActivity", "hasChocolate" + hasChocolate);
        displayPrice();
    }
    public void onCheckboxWhippedClicked (View view) {
        CheckBox whippedCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        hasWhippedCream = whippedCheckBox.isChecked();
        Log.v("MainActivity", "hasWhippedCream" + hasWhippedCream);
        displayPrice();
    }
    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Figure out if the user wants whipped cream topping
//        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
//        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the user wants whipped cream topping
//        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
//        boolean hasChocolate = chocolateCheckBox.isChecked();

        // Calculate the price; calcolato in variabile globale...
        // int price = calculatePrice();

        // Display the order summary on the screen
//        String message = createOrderSummary(price, hasWhippedCream, hasChocolate);
//        displayMessage(message);
        // send intent to gmail

        EditText etName = (EditText) findViewById(R.id.Name_edit_text);
        String name = etName.getText().toString();


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
//        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.eMailSubjectTitle));
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(name, price, hasWhippedCream, hasChocolate));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
//    private int calculatePrice() {
//        return (quantity * 5);
//    }

    /**
     * Create summary of the order.
     *
     * @param lprice           of the order
     * @param addWhippedCream is whether or not to add whipped cream to the coffee
     * @param addChocolate    is whether or not to add chocolate to the coffee
     * @return text summary
     */
    private String createOrderSummary(String name, int lprice, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage ="";
        priceMessage += getString(R.string.ordinante, name);
        Log.v("MainActivity", "Name field : " + priceMessage);
        priceMessage += "\n" + getString(R.string.msgAggiungiCrema) + " " + addWhippedCream;
        priceMessage += "\n" + getString(R.string.msgAggiungiCioccolata) +" " +  addChocolate;
        priceMessage += "\n" + getString(R.string.quantita) + " " + quantity;
        priceMessage += "\n" + getString(R.string.totale)+ lprice;
        priceMessage += "\n" + getString(R.string.ringraziamenti);
        return priceMessage;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("geo:47.6, -122.5"));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//        startActivity(intent);
//        }
//        return "";
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
        displayPrice();
    }
    private void displayPrice() {
        price = quantity*priceOfCoffee;
        if (hasChocolate) {
            price += priceOfToppingChocolate;
            Log.v("MainActivity", "price $" + price);
        }
        if (hasWhippedCream) {
            price += priceOfToppingCream;
            Log.v("MainActivity", "price$" + price);
        }

        TextView priceTv = (TextView) findViewById(R.id.price_text_view);
        priceTv.setText("$"+price);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

}
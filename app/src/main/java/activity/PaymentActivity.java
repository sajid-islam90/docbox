package activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.elune.sajid.myapplication.R;
import com.loopj.android.http.RequestParams;
import com.razorpay.Checkout;

import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import objects.personal_obj;
import utilityClasses.DatabaseHandler;
import utilityClasses.utility;

public class PaymentActivity extends Activity
{
    personal_obj personalObj;
    int amountPayable;
    public PaymentActivity(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
DatabaseHandler databaseHandler = new DatabaseHandler(PaymentActivity.this);
        personalObj = databaseHandler.getPersonalInfo();
        // payment button created by you in xml layout
        View button = (View) findViewById(R.id.pay_btn);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startPayment();
            };
        });
    }

    public void startPayment(){
        /**
         * Replace with your public key
         */

        RadioButton radioButton = (RadioButton)findViewById(R.id.radioButton);
        RadioButton radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
        RadioButton radioButton3 = (RadioButton)findViewById(R.id.radioButton3);
        if(radioButton.isChecked())
        {
            amountPayable = 50000;
        }
        else if(radioButton2.isChecked())
        {
            amountPayable = 250000;
        }
        else if(radioButton3.isChecked())
        {
            amountPayable = 500000;
        }
        /**
         * You need to pass current activity in order to let razorpay create CheckoutActivity
         */
        final Activity activity = this;


        final Checkout co = new Checkout();
        //co.setPublicKey(public_key);
        co.setImage(R.drawable.icon4);
        try{
            JSONObject options = new JSONObject("{" +
                    "description: 'Charges'," +
                    "image: 'www.docbox.co.in/sajid/icon4.png'," +
                    "currency: 'INR'}"
            );

            options.put("amount", String.valueOf(amountPayable));
            options.put("name", "Elune Tech.");
            options.put("prefill", new JSONObject("{email: '"+personalObj.get_email()+"', contact: '"+personalObj.get_password()+"'}"));

            co.open(activity, options);

        } catch(Exception e){
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     *   onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    public void onPaymentSuccess(String razorpayPaymentID){
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            final String  address =  getResources().getString(R.string.action_server_ip_address);
            final RequestParams requestParams = new RequestParams();
            ArrayList<String> data = new ArrayList<>();
            data.add(razorpayPaymentID);
            data.add(personalObj.get_email());
            data.add(String.valueOf(amountPayable/100));

            String s1 ="";
            StringWriter out = new StringWriter();
            try {
                JSONValue.writeJSONString(data, out);
                s1 = out.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            requestParams.put("payment_data", s1);
            utility.sync("http://" + address + "/paymentCaptureRazorPay.php",requestParams,PaymentActivity.this);
        }
        catch (Exception e){
            Log.e("com.merchant", e.getMessage(), e);
        }
    }

    /**
     * The name of the function has to be
     *   onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    public void onPaymentError(int code, String response){
        try {
            Toast.makeText(this, "Payment failed: " + Integer.toString(code) + " " + response, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Log.e("com.merchant", e.getMessage(), e);
        }
    }
}
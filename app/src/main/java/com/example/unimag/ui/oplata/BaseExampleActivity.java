package com.example.unimag.ui.oplata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cloudipsp.android.Card;
import com.cloudipsp.android.Cloudipsp;
import com.cloudipsp.android.CloudipspWebView;
import com.cloudipsp.android.Currency;
import com.cloudipsp.android.GooglePayCall;
import com.cloudipsp.android.Order;
import com.cloudipsp.android.Receipt;
import com.example.unimag.R;
import com.example.unimag.ui.Request.AddRequest;
import com.example.unimag.ui.ThreadCheckingConnection;

import java.security.SecureRandom;

/**
 * Created by vberegovoy on 6/20/17.
 */

abstract public class BaseExampleActivity extends Activity implements
        View.OnClickListener,
        Cloudipsp.PayCallback,
        Cloudipsp.GooglePayCallback {
    private static final int RC_GOOGLE_PAY = 100500;
    private static final String K_GOOGLE_PAY_CALL = "google_pay_call";

    private TextView editAmount;
    private Spinner spinnerCcy;
    private EditText editEmail;
    private EditText editDescription;
    private CloudipspWebView webView;

    private String idProductList;
    private String secureKod;
    private String orderId;
    private String totalMoney;
    private String pickUpPoint;

    private Cloudipsp cloudipsp;
    private GooglePayCall googlePayCall;// <- this should be serialized on saving instance state

    protected abstract int getLayoutResId();

    protected abstract Card getCard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        findViewById(R.id.btn_amount).setOnClickListener(this);
        editAmount = findViewById(R.id.edit_amount);
        spinnerCcy = findViewById(R.id.spinner_ccy);
        editEmail = findViewById(R.id.edit_email);
        editDescription = findViewById(R.id.edit_description);
        findViewById(R.id.btn_pay_card).setOnClickListener(this);
        findViewById(R.id.btn_pay_google).setOnClickListener(this);


        totalMoney = getIntent().getStringExtra("Amount");
        editAmount.setText(getIntent().getStringExtra("Amount"));
        idProductList = getIntent().getStringExtra("IdProductList");
        secureKod = getIntent().getStringExtra("secureKod");
        pickUpPoint = getIntent().getStringExtra("pickUpPoint");
        String email = getIntent().getStringExtra("email");

        editEmail.setText(email);

        webView = findViewById(R.id.web_view);
        cloudipsp = new Cloudipsp(1396424, webView);

        spinnerCcy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Currency.values()));
        spinnerCcy.setSelection(1);
        spinnerCcy.setEnabled(false);

        if (savedInstanceState != null) {
            googlePayCall = savedInstanceState.getParcelable(K_GOOGLE_PAY_CALL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(K_GOOGLE_PAY_CALL, googlePayCall);
    }

    @Override
    public void onBackPressed() {
        if (webView.waitingForConfirm()) {
            webView.skipConfirm();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_GOOGLE_PAY:
                if (!cloudipsp.googlePayComplete(resultCode, data, googlePayCall, this)) {
                    Toast.makeText(this, R.string.e_google_pay_canceled, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_amount:
                fillTest();
                break;
            case R.id.btn_pay_card:
                processPayCard();
                break;
            case R.id.btn_pay_google:
                processGooglePay();
                break;
        }
    }

    private void fillTest() {
        editAmount.setText("1");
        editEmail.setText("test@example.com");
        editDescription.setText("test payment");
    }

    private void processPayCard() {
        final Order order = createOrder();
        if (order != null) {
            final Card card = getCard();
            if (card != null) {
                //System.out.println("Тут да  "+order.id);
                cloudipsp.pay(card, order, this);
            }
        }
    }

    private void processGooglePay() {
        if (Cloudipsp.supportsGooglePay(this)) {
            final Order googlePayOrder = createOrder();
            if (googlePayOrder != null) {
                cloudipsp.googlePayInitialize(googlePayOrder, this, RC_GOOGLE_PAY, this);
            }
        } else {
            Toast.makeText(this, R.string.e_google_pay_unsupported, Toast.LENGTH_LONG).show();
        }
    }

    private Order createOrder() {
        SecureRandom random = new SecureRandom();
        editEmail.setError(null);
        editDescription.setError(null);

        final int amount = Integer.parseInt(editAmount.getText().toString());

        final String email = editEmail.getText().toString();
        final String description = editDescription.getText().toString();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError(getString(R.string.e_invalid_email));
            return null;
        } else if (TextUtils.isEmpty(description)) {
            editDescription.setError(getString(R.string.e_invalid_description));
            return null;
        }
        final Currency currency = (Currency) spinnerCcy.getSelectedItem();
        //orderId = String.valueOf(("vb_" + System.currentTimeMillis()).hashCode());
        orderId = String.valueOf(Math.abs(random.nextInt()));

        final Order order = new Order(amount, currency, orderId, description, email);
        order.setLang(Order.Lang.ru);
        return order;
    }

    @Override
    public void onPaidProcessed(Receipt receipt) {
        try {
            AddRequest addRequest = new AddRequest(getApplicationContext(), idProductList, secureKod, orderId, totalMoney, pickUpPoint, "addToOrders");
            addRequest.execute();
            Toast.makeText(this, "Paid " + receipt.status.name() + "\nPaymentId:" + receipt.paymentId, Toast.LENGTH_LONG).show();
            //this.onDestroy();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void onPaidFailure(Cloudipsp.Exception e) {
        if (e instanceof Cloudipsp.Exception.Failure) {
            Cloudipsp.Exception.Failure f = (Cloudipsp.Exception.Failure) e;

            Toast.makeText(this, "Failure\nErrorCode: " +
                    f.errorCode + "\nMessage: " + f.getMessage() + "\nRequestId: " + f.requestId, Toast.LENGTH_LONG).show();
        } else if (e instanceof Cloudipsp.Exception.NetworkSecurity) {
            Toast.makeText(this, "Network security error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else if (e instanceof Cloudipsp.Exception.ServerInternalError) {
            Toast.makeText(this, "Internal server error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else if (e instanceof Cloudipsp.Exception.NetworkAccess) {
            Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
        }
        e.printStackTrace();
    }

    @Override
    public void onGooglePayInitialized(GooglePayCall result) {
        this.googlePayCall = result;
    }
}

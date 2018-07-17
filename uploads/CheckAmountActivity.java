package com.examp.three.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.examp.three.R;
import com.examp.three.dao.AccessCodeContract;
import com.examp.three.dao.BankContract;
import com.examp.three.dao.CCAvenueContract;
import com.examp.three.dao.PreferedBankContract;
import com.examp.three.entity.AccessCodeEntity;
import com.examp.three.entity.CCAvenueRatesEntity;
import com.examp.three.entity.GetDemandEntity;
import com.examp.three.entity.PreferedBankEntity;
import com.examp.three.paymentgateway.activity.WebViewActivity;
import com.examp.three.paymentgateway.utility.AvenuesParams;
import com.examp.three.utility.Constant;
import com.examp.three.utility.NetworkConnectivity;
import com.examp.three.utility.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CheckAmountActivity extends AppCompatActivity {

    // Sharedpref file name
    private static final String PREF_NAME = "EtownPreference";

    //    JSONObject jsonObject;
    public static Typeface TamilFont;
    final String[] creditCard = {"Amex", "Amex eZeClick", "Diners Club", "MasterCard", "Visa"};

    //    EditText etCardNumber, etMonth, etYear, etCVV;
    final String[] debitCard = {"Andhra Bank", "Canara Bank Debit card", "Citibank", "IOB Debit card",
            "Maestro Debit Card", "MasterCard Debit Card", "Punjab National Bank", "RuPay",
            "State Bank of India", "Union Bank Of India", "Visa Debit Card"};
    final String[] netBanking = {"Andhra Bank", "Axis Bank", "Bank of Baharin and Kuwait",
            "Bank of Baroda Corporate", "Bank of Baroda Retail", "Bank of India", "Bank of Maharashtra",
            "Canara Bank", "Catholic Syrian Bank", "Central Bank of India", "Citi Bank India", "City Union Bank",
            "Corporation Bank", "DBS Bank Ltd", "Deutsche Bank", "Development Bank Singapore",
            "Development Credit Bank", "Dhanlaxmi Bank", "Federal Bank", "HDFC Bank", "ICICI Bank", "IDBI Bank",
            "Indian Bank", "Indian Overseas Bank", "IndusInd Bank", "ING Vysya Bank", "Jammu and kashmir Bank",
            "Karnataka Bank", "Karur Vysya Bank", "Kotak Mahindra Bank", "Lakshmi Vilas Bank",
            "Oriental Bank Of Commerce", "Punjab National Bank [Corporate]", "Punjab National Bank [Retail]",
            "Royal Bank Of Scotland", "Saraswat Bank", "Shamrao Vithal Co-op. Bank Ltd",
            "South Indian Bank", "Standard Chartered Bank", "State Bank Of Bikaner and Jaipur",
            "State Bank Of Hyderabad", "State Bank of India", "State Bank Of Mysore", "State Bank of Patiala",
            "State Bank of Travancore", "Syndicate Bank", "Tamilnad Mercantile Bank", "UCO Bank", "Union Bank of India",
            "United Bank of India", "Vijaya Bank", "YES Bank"};
    final String[] cashCard = {"Done Card", "ICash Card", "ITZ Cash Card", "Oxicash", "PayCash Card"};
    final String[] mobilePayment = {"PayMate"};
    final String[] wallet = {"Paytm", "Mobikwik"};
    String orderId, sDistrictName, sPanchayatName, sTaxType, sAssessmentNumber, sAssessmentName, sBankName, sPaymentMode,
            PAYMENT_OPTION, CARD_TYPE, sCardNumber, sExpMonth, sExpYear, sCVV, sDoorNo, sStreetName, sWardNo,
            sUserId, sDataAccept, sContactNum, sEmail, sFinancialYear = "", sSno = "", sTerm = "",
            sCessAmount = "", sMaintenanceAmount = "", sWaterCharges = "", sPenaltyAmount = "";
    TextView tvTotalDemand, tvPenalty, tvTotalDemandAmount, tvTransactionCost, tvServiceCharge, tvBankCharge,
            tvTotalAmtToBePaid, tvTaxType, tvAssessmentNo, tvDistrict, tvAssessmentName, tvPanchayat;  // tvCardTitle
    double totalDemand = 0, totalDemandAmount = 0, transactionCost = 0, serviceCharge = 0,
            bankCharges = 0, amountToBePaid = 0, penaltyAmount = 0; // penalty = 0,
    //    Table design declaration
    TableLayout tableLayoutTerm;
    LayoutParams layoutParams;

    //    final String[] netBanking = {"Andhra Bank", "AXIS Bank", "Bank of Bahrain & Kuwait",
//            "Bank of Baroda Corporate Accounts", "Bank of Baroda Retail Accounts", "Bank of India",
//            "Bank of Maharashtra", "Canara Bank", "Catholic Syrian Bank", "Central Bank of India",
//            "Citibank Bank Account Online", "City Union Bank", "Corporation Bank", "DBS Bank Ltd",
//            "DCB Bank (Development Credit Bank)", "Deutsche Bank", "Federal Bank", "HDFC Bank", "ICICI Bank",
//            "IDBI Bank", "Indian Bank", "Indian Overseas Bank", "IndusInd Bank", "ING Vysya Bank",
//            "Jammu & Kashmir Bank", "Karnataka Bank", "Karur Vysya Bank", "Kotak Mahindra Bank",
//            "Lakshmi Vilas Bank NetBanking", "Oriental Bank of Commerce",
//            "Punjab National Bank Corporate Accounts", "Punjab National Bank Retail Accounts",
//            "South Indian Bank", "Standard Chartered Bank", "State Bank of Bikaner and Jaipur",
//            "State Bank of Hyderabad", "State Bank of India", "State Bank of Mysore", "State Bank of Patiala",
//            "State Bank of Travancore", "Syndicate Bank", "Tamilnad Mercantile Bank", "Union Bank of India",
//            "United Bank of India", "Vijaya Bank", "YES Bank"};
    float totalBalance = 0;
    CheckBox[] checkBoxes;
    CheckBox checkBoxAll;

    //    Integer[] imageArray =
//            {R.drawable.visa,
//                    R.drawable.master,
//                    R.drawable.discover,
//                    R.drawable.amex,
//                    R.drawable.dinersclub};
//
//    String cardtype;
    int iBox;
    Spinner spinnerPaymentMode, spinnerBankName;
    int positionPaymentMode, positionBankName;
    //    ArrayList<PropProfBalEntity> propProfBalList = new ArrayList<>();
    ArrayList<GetDemandEntity> demandEntityList = new ArrayList<>();

//    RelativeLayout relativeLayout;

    //    Avvenues
//    private ProgressDialog pDialog;
//    private JSONObject jsonRespObj;
//    String selectedPaymentOption, selectedCardType;
//
//    Map<String, ArrayList<CardTypeDTO>> cardsList = new LinkedHashMap<String, ArrayList<CardTypeDTO>>();
//    ArrayList<PaymentOptionDTO> payOptionList = new ArrayList<PaymentOptionDTO>();
//    ArrayList<EMIOptionDTO> emiOptionList = new ArrayList<EMIOptionDTO>();
//    private Map<String, String> paymentOptions = new LinkedHashMap<String, String>();
//    int counter;
//    private String emiPlanId, emiTenureId, amount, currency, cardName, allowedBins;
//    Calendar calendar = Calendar.getInstance();
//    int currentYear, currentMonth, year, month;
    //    private CustomListAdapter customListAdapter;
//    ListView listView;
    View header, footer;
    TableRow tableRow;
    // Shared Preferences
    SharedPreferences preferences;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    NetworkConnectivity networkConnectivity;

    ProgressDialog dialog;
     String lAccessCode,lmerchantId,lcurrency;

    Double Master_Creditcard;
    Double Amex_Creditcard;
    Double Master_DebitCard_2KBelow;
    Double Master_DebitCard_2KAbove;
    Double HDFC_NetBank;
    Double Others_NetBank;
    Double CashCard;
    Double ServiceTax;
    Double BankCharges;

    TextView tv1;
    TextView tv2;

    String srEmail,srMobil;

    int DebitAmount;

    CheckBox pymode;
    AccessCodeContract accessCodeContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_amount);

        Bundle bundle = getIntent().getExtras();
        sDistrictName = bundle.getString(Params.districtName);
        sPanchayatName = bundle.getString(Params.panchayatName);
        sTaxType = bundle.getString(Params.taxType);
        sAssessmentNumber = bundle.getString(Params.assessmentNumber);

        sAssessmentName = bundle.getString(Params.assessmentName);
        sDoorNo = bundle.getString(Params.doorNo);
        sStreetName = bundle.getString(Params.streetName);
        sWardNo = bundle.getString(Params.wardNo);
        sUserId = bundle.getString(Params.userId);

        sContactNum = bundle.getString(Params.contactNo);
        sEmail = bundle.getString(Params.emailId);

        System.out.println("mobileNo"+sContactNum);
        System.out.println("EmailId"+sEmail);

        spinnerPaymentMode = (Spinner) findViewById(R.id.spinner_paymentMode);
        spinnerBankName = (Spinner) findViewById(R.id.spinner_bankName);

        pymode = (CheckBox) findViewById(R.id.check_paymode);


        tvTotalDemand = (TextView) findViewById(R.id.textView_totalDemand);

        tvAssessmentNo = (TextView) findViewById(R.id.textView_assessmentNo);
        tvAssessmentName = (TextView) findViewById(R.id.textView_assessmentName);
        tvDistrict = (TextView) findViewById(R.id.textView_district);
        tvPanchayat = (TextView) findViewById(R.id.textView_panchayat);

        TamilFont = Typeface.createFromAsset(getAssets(), "fonts/Avvaiyar.ttf");


        tvAssessmentNo.setText(sAssessmentNumber);
//        tvAssessmentName.setText(sAssessmentName);
        tvDistrict.setText(sDistrictName);
        tvPanchayat.setText(sPanchayatName);
//        tvAssessmentName.setTypeface(TamilFont);

        accessCodeContract=new AccessCodeContract(this);
        tvPenalty = (TextView) findViewById(R.id.textView_penalty);
        tvTotalDemandAmount = (TextView) findViewById(R.id.textView_totalDemand_withPenalty);
        tvTransactionCost = (TextView) findViewById(R.id.textView_Transaction_Cost);
        tvServiceCharge = (TextView) findViewById(R.id.textView_service_charge);
        tvBankCharge = (TextView) findViewById(R.id.textView_bank_charges);
        tvTotalAmtToBePaid = (TextView) findViewById(R.id.textView_totalAmt_tobe_paid);
        tvTaxType = (TextView) findViewById(R.id.textView_taxType);

        networkConnectivity = new NetworkConnectivity(this);

        preferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();

      srEmail =preferences.getString("email", null);
      srMobil = preferences.getString("mobile", null);
//        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutCard);
//
//        tvCardTitle = (TextView) findViewById(R.id.textView_cardType_title);
//
//        etCardNumber = (EditText) findViewById(R.id.editText_card);
//        etMonth = (EditText) findViewById(R.id.editText_month);
//        etYear = (EditText) findViewById(R.id.editText_year);
//        etCVV = (EditText) findViewById(R.id.editText_cvv);

//        listView = (ListView)findViewById(R.id.listView);

        header = getLayoutInflater().inflate(R.layout.list_view, null);
        footer = getLayoutInflater().inflate(R.layout.list_view_footer, null);

//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    return true; // Indicates that this has been handled by you and will not be forwarded further.
//                }
//                return false;
//            }
//        });


        tv1 = (TextView) findViewById(R.id.footerText1);
        tv2 = (TextView) findViewById(R.id.footerText3);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.etownpanchayat.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.prematix.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


//        loadccavenuerate
        getCCAvenueDetails();
        getAccessCod();
//        getCCAvenueRates();
//        customListAdapter = new CustomListAdapter(this, propProfBalList);
//        listView.addHeaderView(header);
//        listView.setAdapter(customListAdapter);

//        Table design
        tableLayoutTerm = (TableLayout) findViewById(R.id.tableLayout_term);

        tableRow = (TableRow) findViewById(R.id.row);
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, -7, 15, 15);
//        currentYear = calendar.get(Calendar.YEAR) % 100;
//        currentMonth = calendar.get(Calendar.MONTH);

//        cardTextChangeListener();
        loadPaymentMode();
//        cardFocusChangeListener();
//        theme();
        Resources resources = getResources();

        switch (sTaxType) {
            case "P":
                tvTaxType.setText(Params.property_tax);
                tvTaxType.setBackgroundColor(resources.getColor(R.color.property));
                if (networkConnectivity.isConnected()) {
                    getPropProfBalanceDetails();
                } else {
                    alertBox1();
                }
                break;
            case "PR":
                tvTaxType.setBackgroundColor(resources.getColor(R.color.profession));
                tvTaxType.setText(Params.profession_tax);
                if (networkConnectivity.isConnected()) {
                    getPropProfBalanceDetails();
                } else {
                    alertBox1();
                }
                break;
            case "W":
                tvTaxType.setBackgroundColor(resources.getColor(R.color.water));
                tvTaxType.setText(Params.water_tax);
                if (networkConnectivity.isConnected()) {
                    getWaterBalanceDetails();
                } else {
                    alertBox1();
                }
                break;
            case "N":
                tvTaxType.setBackgroundColor(resources.getColor(R.color.nonTax));
                tvTaxType.setText(Params.miscellaneous_tax);
                if (networkConnectivity.isConnected()) {
                    getNonTaxBalanceDetails();
                } else {
                    alertBox1();
                }
                break;
            default:
                break;
        }


//        get_BankDetails("netBanking");


    }


    private void getNonTaxBalanceDetails() {
        String URL_ASSESS_DETAILS = Constant.URL_JSON_DOMAIN + Constant.METHOD_NAME_GET_NONTAXBAL_DETAILS;

        final ProgressDialog dialog = new ProgressDialog(CheckAmountActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(sPanchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonArrayRequest request = new JsonArrayRequest(URL_ASSESS_DETAILS
                + "?" + "district=" + sDistrictName + Constant.PARAMETER_SEPERATOR_AMP + "panchayat=" + tempPanchayat
                + Constant.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            System.out.println("jsonArray = " + jsonArray);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    try {
                                        GetDemandEntity getDemandEntity = new GetDemandEntity();
                                        getDemandEntity.setFinancialYear(jsonObject.getString(Params.financialYear));
                                        getDemandEntity.setBalance(jsonObject.getString(Params.balance));
                                        getDemandEntity.setTerm(jsonObject.getString(Params.term));
                                        getDemandEntity.setsNo(jsonObject.getString(Params.sNo));
                                        demandEntityList.add(getDemandEntity);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                loadDataInTable();
                            } else {
                                alertBox();
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);
    }

    public void alertBox1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckAmountActivity.this);
//        builder.setTitle("Registration successful");
        builder.setMessage("Please connect to Internet and try again, do not go back.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (sTaxType) {
                            case "P":
                                if (networkConnectivity.isConnected()) {
                                    getPropProfBalanceDetails();
                                } else {
                                    alertBox1();
                                }
                                break;
                            case "PR":
                                if (networkConnectivity.isConnected()) {
                                    getPropProfBalanceDetails();
                                } else {
                                    alertBox1();
                                }
                                break;
                            case "W":
                                if (networkConnectivity.isConnected()) {
                                    getWaterBalanceDetails();
                                } else {
                                    alertBox1();
                                }
                                break;
                            case "N":
                                if (networkConnectivity.isConnected()) {
                                    getNonTaxBalanceDetails();
                                    break;
                                } else {
                                    alertBox1();
                                }
                            default:

                                break;
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


//    public void get_BankDetails(String sPaymentMode) {
//
//        final ProgressDialog dialog = new ProgressDialog(CheckAmountActivity.this);
//        dialog.setMessage("Loading...");
//        dialog.show();
//        dialog.setCancelable(false);
//        String tempsPaymentMode = "";
//        try {
//            tempsPaymentMode = URLEncoder.encode(sPaymentMode, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String URL_USER_ID = Constant.URL_JSON_DOMAIN + Constant.METHOD_NAME_GET_BANKLIST + "?PaymentType=" + tempsPaymentMode;
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        final JsonArrayRequest request = new JsonArrayRequest(URL_USER_ID,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray jsonArray) {
//                        if (dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        try {
//                            System.out.println("jsonArray = " + jsonArray);
//
//                            String[] stringArray = new String[jsonArray.length()];
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                stringArray[i] = jsonArray.getJSONObject(i).getString("bankName");
//
//                            }
////                            loadBankNames(stringArray);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        requestQueue.add(request);
//    }


    private void getWaterBalanceDetails() {
        String URL_ASSESS_DETAILS = Constant.URL_JSON_DOMAIN + Constant.METHOD_NAME_GET_WATERBAL_DETAILS;

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(sPanchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressDialog dialog = new ProgressDialog(CheckAmountActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonArrayRequest request = new JsonArrayRequest(URL_ASSESS_DETAILS + "?"
                + "district=" + sDistrictName + "&" + "panchayat=" + tempPanchayat
                + Constant.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            System.out.println("jsonArray = " + jsonArray);
//                            if (!jsonArray.equals("[]")) {
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    try {
                                        GetDemandEntity getDemandEntity = new GetDemandEntity();
                                        getDemandEntity.setFinancialYear(jsonObject.getString("finYear"));
                                        getDemandEntity.setBalance(jsonObject.getString(Params.balance));
                                        getDemandEntity.setTerm(jsonObject.getString(Params.term));
                                        getDemandEntity.setsNo(jsonObject.getString(Params.sNo));
                                        getDemandEntity.setCessAmount(jsonObject.getString(Params.cessAmount));
                                        getDemandEntity.setWaterCharges(jsonObject.getString(Params.waterCharges));
                                        getDemandEntity.setMaintenanceCharge(jsonObject.getString(Params.maintenanceCharge));
                                        getDemandEntity.setPenaltyAmount(jsonObject.getString(Params.penaltyAmount));
                                        demandEntityList.add(getDemandEntity);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                loadDataInTable();
                            } else {
                                alertBox();
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);
    }

    public void getPropProfBalanceDetails() {
        String URL_ASSESS_DETAILS = Constant.URL_JSON_DOMAIN + Constant.METHOD_NAME_GET_PPBAL_DETAILS;

        final ProgressDialog dialog = new ProgressDialog(CheckAmountActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        String tempPanchayat = "";
        try {
            tempPanchayat = URLEncoder.encode(sPanchayatName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonArrayRequest request = new JsonArrayRequest(URL_ASSESS_DETAILS + "?"
                + "district=" + sDistrictName + "&" + "panchayat=" + tempPanchayat
                + Constant.PARAMETER_SEPERATOR_AMP + "taxNo=" + sAssessmentNumber + Constant.PARAMETER_SEPERATOR_AMP
                + "taxType=" + sTaxType,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            System.out.println("jsonArray = " + jsonArray);
//                            if (!jsonArray.equals("[]")) {
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    try {
                                        GetDemandEntity getDemandEntity = new GetDemandEntity();
                                        getDemandEntity.setFinancialYear(jsonObject.getString(Params.financialYear));
                                        getDemandEntity.setBalance(jsonObject.getString(Params.balance));
                                        getDemandEntity.setTerm(jsonObject.getString(Params.term));
                                        getDemandEntity.setsNo(jsonObject.getString(Params.sNo));
                                        demandEntityList.add(getDemandEntity);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                    String financialYear = jsonObject.getString(Params.financialYear);
//                                    String term = jsonObject.getString(Params.term);
//                                    String balance = jsonObject.getString(Params.balance);
                                }
                                loadDataInTable();
//                                customListAdapter = new CustomListAdapter(CheckAmountActivity.this, propProfBalList);
//                                listView.addHeaderView(header);
//                                listView.addFooterView(footer);
//                                listView.setAdapter(customListAdapter);
                            } else {
                                alertBox();
//                                showToast("No data found");
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);
    }

    public void loadDataInTable() {
        tableTitle();
    }

    public void tableTitle() {
        TableRow tableRowTitle = new TableRow(this);
        tableRowTitle.setLayoutParams(layoutParams);

        checkBoxAll = new CheckBox(this);
        checkBoxAll.setLayoutParams(layoutParams);
        checkBoxAll.setText("All");

        TextView tvFinancialYear = new TextView(this);
        tvFinancialYear.setLayoutParams(layoutParams);
        tvFinancialYear.setText("FinancialYear");
        tvFinancialYear.setTextColor(getResources().getColor(R.color.black));

        TextView tvTerm = new TextView(this);
        tvTerm.setLayoutParams(layoutParams);
        tvTerm.setText("Term");
        tvTerm.setTextColor(getResources().getColor(R.color.black));

        TextView tvBalance = new TextView(this);
        tvBalance.setLayoutParams(layoutParams);
        tvBalance.setText("Balance(INR)");
        tvBalance.setTextColor(getResources().getColor(R.color.black));

        tableRowTitle.addView(checkBoxAll);
        tableRowTitle.addView(tvFinancialYear);
        tableRowTitle.addView(tvTerm);
        tableRowTitle.addView(tvBalance);
        tableLayoutTerm.addView(tableRowTitle, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        call table values method
        tableValues();
    }

    public void tableValues() {
        checkBoxes = new CheckBox[demandEntityList.size()];
        try {
            for (int i = 0; i < demandEntityList.size(); i++) {
                final TableRow tableRowValue = new TableRow(this);

                tableRowValue.setId(i);
                tableRowValue.setLayoutParams(layoutParams);

                checkBoxes[i] = new CheckBox(this);
                checkBoxes[i].setLayoutParams(layoutParams);
//                checkBoxes[i].setButtonDrawable(id);

                final TextView fin = new TextView(this);
                fin.setLayoutParams(layoutParams);
                fin.setText(demandEntityList.get(i).getFinancialYear());
//                fin.setTextColor(Color.WHITE);

                final TextView term = new TextView(this);
                term.setLayoutParams(layoutParams);
                term.setText(demandEntityList.get(i).getTerm());
//                term.setTextColor(Color.WHITE);

                final TextView taxamoun = new TextView(this);
                taxamoun.setLayoutParams(layoutParams);
                taxamoun.setText(demandEntityList.get(i).getBalance());
//                taxamoun.setTextColor(Color.WHITE);

                totalBalance = totalBalance + Float.parseFloat(demandEntityList.get(i).getBalance());

                tableRowValue.addView(checkBoxes[i]);
                tableRowValue.addView(fin);
                tableRowValue.addView(term);
                tableRowValue.addView(taxamoun);

                tableLayoutTerm.addView(tableRowValue, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkBoxValidation();
        tableTotal();
    }

    public void tableTotal() {
        TableRow tableRowTotal = new TableRow(this);
        tableRowTotal.setLayoutParams(layoutParams);

        TextView tvEmpty = new TextView(this);
        tvEmpty.setLayoutParams(layoutParams);
        tvEmpty.setText("");

        TextView tvEmpty1 = new TextView(this);
        tvEmpty1.setLayoutParams(layoutParams);
        tvEmpty1.setText("");

        TextView tvTotalKey = new TextView(this);
        tvTotalKey.setLayoutParams(layoutParams);
        tvTotalKey.setText("Total");
        tvTotalKey.setTextColor(getResources().getColor(R.color.black));

        TextView tvTotal = new TextView(this);
        tvTotal.setLayoutParams(layoutParams);
        tvTotal.setText(String.valueOf(String.format("%.2f", totalBalance)));
        tvTotal.setTextColor(getResources().getColor(R.color.black));


        tableRowTotal.addView(tvEmpty);
        tableRowTotal.addView(tvEmpty1);
        tableRowTotal.addView(tvTotalKey);
        tableRowTotal.addView(tvTotal);
        tableLayoutTerm.addView(tableRowTotal, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void checkBoxValidation() {
        checkBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxAll.isChecked()) {
                    for (int i = 0; i < demandEntityList.size(); i++) {
                        checkBoxes[i].setEnabled(true);
                        checkBoxes[i].setChecked(true);
                    }
                } else {
                    for (int i = 0; i < demandEntityList.size(); i++) {
                        checkBoxes[i].setChecked(false);
                        checkBoxes[i].setId(i);
                        if (checkBoxes[i].getId() == 0) {
                            checkBoxes[i].setEnabled(true);
                        } else {
                            checkBoxes[i].setEnabled(false);
                        }
                    }
                }
            }
        });

        for (iBox = 0; iBox < demandEntityList.size(); iBox++) {

            checkBoxes[iBox].setId(iBox);
            if (checkBoxes[iBox].getId() == 0) {
                checkBoxes[iBox].setEnabled(true);
            } else {
                checkBoxes[iBox].setEnabled(false);
            }

//            Check Change Listener
            checkBoxes[iBox].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (int j = 0; j < demandEntityList.size() - 1; j++) {
                            if (checkBoxes[j].isChecked()) {
                                checkBoxes[j + 1].setEnabled(true);
                            }

                            if (checkBoxes[demandEntityList.size() - 1].isChecked()) {
                                checkBoxAll.setChecked(true);
                            }
                        }
//                    Slno , financialYear, term

                        sFinancialYear = sFinancialYear + "," + demandEntityList.get(buttonView.getId()).getFinancialYear();
                        sSno = sSno + "," + demandEntityList.get(buttonView.getId()).getsNo();
                        sTerm = sTerm + "," + demandEntityList.get(buttonView.getId()).getTerm();

                        sCessAmount = sCessAmount + "," + demandEntityList.get(buttonView.getId()).getCessAmount();
                        sMaintenanceAmount = sMaintenanceAmount + "," + demandEntityList.get(buttonView.getId()).getMaintenanceCharge();
                        sPenaltyAmount = sPenaltyAmount + "," + demandEntityList.get(buttonView.getId()).getPenaltyAmount();
                        sWaterCharges = sWaterCharges + "," + demandEntityList.get(buttonView.getId()).getWaterCharges();

                        System.out.println("sFinancialYear = " + sFinancialYear);
                        System.out.println("sSno = " + sSno);
                        System.out.println("sTerm = " + sTerm);
                        System.out.println("sCessAmount = " + sCessAmount);
                        System.out.println("sMaintenanceAmount = " + sMaintenanceAmount);
                        System.out.println("sPenaltyAmount = " + sPenaltyAmount);
                        System.out.println("sWaterCharges = " + sWaterCharges);

//                      Calculation of amount

                        totalDemand = totalDemand + Double.parseDouble(demandEntityList.get(buttonView.getId()).getBalance());
                        tvTotalDemand.setText(String.valueOf(String.format("%.2f", totalDemand)));
                        if (sTaxType.equals("W")) {
                            penaltyAmount = penaltyAmount + Double.parseDouble(demandEntityList.get(buttonView.getId()).getPenaltyAmount());
                            tvPenalty.setText(String.valueOf(String.format("%.2f", penaltyAmount)));
                            System.out.println("penaltyAmount = " + penaltyAmount);
                        } else {
                            tvPenalty.setText(String.valueOf(String.format("%.2f", penaltyAmount)));
                        }
                        plusCharges(totalDemand);
//                        Un check chekBox
                    } else {
                        for (int k = 0; k < demandEntityList.size() - 1; k++) {
                            if (!checkBoxes[k].isChecked()) {
                                checkBoxes[k + 1].setEnabled(false);
                                checkBoxes[k + 1].setChecked(false);
                            }
                            if (!checkBoxes[demandEntityList.size() - 1].isChecked()) {
                                checkBoxAll.setChecked(false);
                            }
                        }
                        //                    Slno , financialYear, term

                        sFinancialYear = sFinancialYear.substring(0, sFinancialYear.lastIndexOf(","));
                        sSno = sSno.substring(0, sSno.lastIndexOf(","));
                        sTerm = sTerm.substring(0, sTerm.lastIndexOf(","));

                        sCessAmount = sCessAmount.substring(0, sCessAmount.lastIndexOf(","));
                        sMaintenanceAmount = sMaintenanceAmount.substring(0, sMaintenanceAmount.lastIndexOf(","));
                        sPenaltyAmount = sPenaltyAmount.substring(0, sPenaltyAmount.lastIndexOf(","));
                        sWaterCharges = sWaterCharges.substring(0, sWaterCharges.lastIndexOf(","));

                        System.out.println("sFinancialYear = " + sFinancialYear);
                        System.out.println("sSno = " + sSno);
                        System.out.println("sTerm = " + sTerm);
                        System.out.println("sCessAmount = " + sCessAmount);
                        System.out.println("sMaintenanceAmount = " + sMaintenanceAmount);
                        System.out.println("sPenaltyAmount = " + sPenaltyAmount);
                        System.out.println("sWaterCharges = " + sWaterCharges);

                        // Calculation of amount

                        totalDemand = totalDemand - Double.parseDouble(demandEntityList.get(buttonView.getId()).getBalance());
                        tvTotalDemand.setText(String.valueOf(totalDemand));

                        if (sTaxType.equals("W")) {
                            penaltyAmount = penaltyAmount - Double.parseDouble(demandEntityList.get(buttonView.getId()).getPenaltyAmount());
                            tvPenalty.setText(String.valueOf(String.format("%.2f", penaltyAmount)));
                        } else {
                            tvPenalty.setText(String.valueOf(String.format("%.2f", penaltyAmount)));
                        }
                        plusCharges(totalDemand);
                    }
                }
            });
        }
    }


    public void getCCAvenueDetails() {

        try {
            CCAvenueContract contract = new CCAvenueContract(this);
            List<CCAvenueRatesEntity> ccavenue = contract.getCCAvenue();
            for (CCAvenueRatesEntity objcc : ccavenue) {

                Master_Creditcard = objcc.getMaster_Creditcard();
                Amex_Creditcard = objcc.getAmex_Creditcard();
                DebitAmount = objcc.getDebitAmount();
                Master_DebitCard_2KBelow = objcc.getMaster_DebitCard_2KBelow();
                Master_DebitCard_2KAbove = objcc.getMaster_DebitCard_2KAbove();
                HDFC_NetBank = objcc.getHDFC_NetBank();
                Others_NetBank = objcc.getOthers_NetBank();
                CashCard = objcc.getCashCard();
                ServiceTax = objcc.getServiceTax();
                BankCharges = objcc.getBankCharges();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Please check Details", Toast.LENGTH_SHORT).show();
        }
    }


//    public void getCCAvenueRates() {
//        String URL_CCAVENUE = Constant.URL_JSON_DOMAIN + Constant.METHOD_NAME_CCAVENUE_RATE;
//
//        final ProgressDialog dialog = new ProgressDialog(CheckAmountActivity.this);
//        dialog.setMessage("Loading...");
//        dialog.show();
//        dialog.setCancelable(false);
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        final JsonArrayRequest request = new JsonArrayRequest(URL_CCAVENUE,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray jsonArray) {
//                        try {
//                            System.out.println("jsonArray = " + jsonArray);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//
//
//                                Master_Creditcard = jsonObject.getDouble(Params.nMaster_Creditcard);
//                                Amex_Creditcard = jsonObject.getDouble(Params.nAmex_Creditcard);
//                                DebitAmount = jsonObject.getInt(Params.nDebitAmount);
//                                Master_DebitCard_2KBelow = jsonObject.getDouble(Params.nMaster_DebitCard_2KBelow);
//                                Master_DebitCard_2KAbove = jsonObject.getDouble(Params.nMaster_DebitCard_2KAbove);
//                                HDFC_NetBank = jsonObject.getDouble(Params.nHDFC_NetBank);
//                                Others_NetBank = jsonObject.getDouble(Params.nOthers_NetBank);
//                                CashCard = jsonObject.getDouble(Params.nCashCard);
//                                ServiceTax = jsonObject.getDouble(Params.nServiceTax);
//                                BankCharges = jsonObject.getDouble(Params.nBankCharges);
//
////                                ccAvenueContract.addCCAvenueDetails(new CCAvenueRatesEntity(Double.parseDouble(Master_Creditcard), Double.parseDouble(Amex_Creditcard),Integer.parseInt(DebitcardAmount), Double.parseDouble(Master_DebitCard_2KBelow), Double.parseDouble(Master_DebitCard_2KAbove),
////                                        Double.parseDouble(HDFC_NetBank), Double.parseDouble(Others_NetBank), Double.parseDouble(CashCard), Double.parseDouble(ServiceTax), Double.parseDouble(BankCharges)));
//
//                                System.out.println("12Master_Creditcard" + Master_Creditcard);
//                                System.out.println("12Amex_Creditcard" + Amex_Creditcard);
//                                System.out.println("12Master_DebitCard_2KBelow" + Master_DebitCard_2KBelow);
//                                System.out.println("12Master_DebitCard_2KAbove" + Master_DebitCard_2KAbove);
//                                System.out.println("12HDFC_NetBank" + HDFC_NetBank);
//                                System.out.println("12Others_NetBank" + Others_NetBank);
//                                System.out.println("12CashCard" + CashCard);
//                                System.out.println("12ServiceTax" + ServiceTax);
//                                System.out.println("12BankCharges" + BankCharges);
//
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if (dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        requestQueue.add(request);
//    }

    public void plusCharges(double sTotalDemand) {

//        penalty = 0;
//        tvPenalty.setText(String.format("%.2f", penalty));

        totalDemandAmount = sTotalDemand + penaltyAmount;
        tvTotalDemandAmount.setText(String.format("%.2f", totalDemandAmount));


        switch (sPaymentMode) {
            case "---select Payment mode---":
                transactionCost = 0;
                break;
            case "CreditCard":
                if (sBankName.equals("Amex") || sBankName.equals("Amex ezeClick")) {
//                if (sBankName.equals("Amex") || sBankName.equals("Amex eZeClick")) {
                    transactionCost = (totalDemandAmount * Amex_Creditcard) / 100;
                } else {
                    transactionCost = (totalDemandAmount * Master_Creditcard) / 100;
                }
//                tvCardTitle.setText("Credit Card");
                break;
            case "DebitCard":
                if (totalDemandAmount <= DebitAmount) {
                    transactionCost = (totalDemandAmount * Master_DebitCard_2KBelow) / 100;
                } else {
                    transactionCost = (totalDemandAmount * Master_DebitCard_2KAbove) / 100;
                }
//                tvCardTitle.setText("Debit Card");
                break;
            case "NetBanking":
                if (sBankName.equals("HDFC Bank")) {
                    transactionCost = (totalDemandAmount * HDFC_NetBank) / 100;
                } else {
                    transactionCost = (totalDemandAmount * Others_NetBank) / 100;
                }
                break;
            default:
                transactionCost = (totalDemandAmount * CashCard) / 100;
                break;
        }

        tvTransactionCost.setText(String.format("%.2f", transactionCost));

        serviceCharge = (transactionCost * ServiceTax) / 100;
        tvServiceCharge.setText(String.format("%.2f", serviceCharge));

        if (totalDemandAmount == 0) {
            bankCharges = 0;
        } else {
            bankCharges = BankCharges;
        }
        tvBankCharge.setText(String.format("%.2f", bankCharges));

        amountToBePaid = totalDemandAmount + transactionCost + serviceCharge + bankCharges;
        tvTotalAmtToBePaid.setText(String.format("%.2f", amountToBePaid));

        System.out.println("amountToBePaid = " + amountToBePaid);
    }

//    backup
//    public void plusCharges(double sTotalDemand) {
//
////        penalty = 0;
////        tvPenalty.setText(String.format("%.2f", penalty));
//
//        totalDemandAmount = sTotalDemand + penaltyAmount;
//        tvTotalDemandAmount.setText(String.format("%.2f", totalDemandAmount));
//
//        switch (sPaymentMode) {
//            case "Credit Card":
//                if (sBankName.equals("Amex") || sBankName.equals("Amex ezeClick")) {
//                    transactionCost = (totalDemandAmount * 2.1) / 100;
//                } else {
//                    transactionCost = (totalDemandAmount * 1.5) / 100;
//                }
////                tvCardTitle.setText("Credit Card");
//                break;
//            case "Debit Card":
//                if (totalDemandAmount <= 2000) {
//                    transactionCost = (totalDemandAmount * 0.75) / 100;
//                } else {
//                    transactionCost = (totalDemandAmount * 1) / 100;
//                }
////                tvCardTitle.setText("Debit Card");
//                break;
//            case "Net Banking":
//                if (sBankName.equals("HDFC Bank")) {
//                    transactionCost = (totalDemandAmount * 1.6) / 100;
//                } else {
//                    transactionCost = (totalDemandAmount * 1.2) / 100;
//                }
//                break;
//            default:
//                transactionCost = (totalDemandAmount * 2.1) / 100;
//                break;
//        }
//
//        tvTransactionCost.setText(String.format("%.2f", transactionCost));
//
//        serviceCharge = (transactionCost * 14.50) / 100;
//        tvServiceCharge.setText(String.format("%.2f", serviceCharge));
//
//        if (totalDemandAmount == 0) {
//            bankCharges = 0;
//        } else {
//            bankCharges = 5;
//        }
//        tvBankCharge.setText(String.format("%.2f", bankCharges));
//
//        amountToBePaid = totalDemandAmount + transactionCost + serviceCharge + bankCharges;
//        tvTotalAmtToBePaid.setText(String.format("%.2f", amountToBePaid));
//
//        System.out.println("amountToBePaid = " + amountToBePaid);
//    }

    public void loadPaymentMode() {

        spinnerPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPaymentMode = spinnerPaymentMode.getSelectedItem().toString().trim();
                positionPaymentMode = position;


                BankContract bank = new BankContract(getApplicationContext());

//                List<String> districtList = bank.getBankList();

                PreferedBankContract pbank = new PreferedBankContract(getApplicationContext());
//                pbank.addPreferedmode(new PreferedBankEntity(sUserId, String.valueOf(positionPaymentMode), String.valueOf(positionBankName)));
                String userid = null, paymode = null, bankname = null;
                List<PreferedBankEntity> pbanks = pbank.getallPaymodeList(sUserId);
                for (PreferedBankEntity eee : pbanks) {
                    userid = eee.getUserId();
                    paymode = eee.getPaymode();
                    bankname = eee.getBankname();

                }
                if (sUserId.equals(userid)) {


                    spinnerPaymentMode.setSelection(Integer.parseInt(paymode));
                    String name = spinnerPaymentMode.getSelectedItem().toString();
                    System.out.println("bank" + name);
//                    get_BankDetails("NetBanking");
                    positionPaymentMode = position;


                    List<String> paymentMode = null;
                    if (position == 1) {
//                    paymentMode = creditCard;
                        paymentMode = bank.getBankList("CreditCard");
                        loadBankNames(paymentMode);

//                        get_BankDetails("CreditCard");
                        CARD_TYPE = "CRDC";
                        PAYMENT_OPTION = "OPTCRDC";
                    } else if (position == 2) {
//                    paymentMode = debitCard;
                        paymentMode = bank.getBankList("DebitCard");
                        loadBankNames(paymentMode);

//                        get_BankDetails("DebitCard");
                        CARD_TYPE = "DBCRD";
                        PAYMENT_OPTION = "OPTDBCRD";
                    } else if (position == 3) {
//                        get_BankDetails("NetBanking");

                        paymentMode = bank.getBankList("NetBanking");
                        loadBankNames(paymentMode);
//                    paymentMode = netBanking;
                        CARD_TYPE = "NBK";
                        PAYMENT_OPTION = "OPTNBK";
                    } else if (position == 4) {
//                    paymentMode = cashCard;
//                        get_BankDetails("CashCard");
                        paymentMode = bank.getBankList("CashCard");
                        loadBankNames(paymentMode);

                        CARD_TYPE = "CASHC";
                        PAYMENT_OPTION = "OPTCASHC";
                    } else if (position == 5) {
//                    paymentMode = mobilePayment;
//                        get_BankDetails("MobilePayment");

                        paymentMode = bank.getBankList("MobilePayment");
                        loadBankNames(paymentMode);
                        CARD_TYPE = "MOBP";

                        PAYMENT_OPTION = "OPTMOBP";
                    } else if (position == 6) {
//                    paymentMode = wallet;
//                        get_BankDetails("Wallet");
                        paymentMode = bank.getBankList("Wallet");
                        loadBankNames(paymentMode);
                        CARD_TYPE = "WLT";
                        PAYMENT_OPTION = "OPTWLT";
                    }


                    spinnerBankName.setSelection(Integer.parseInt(bankname));

                    pymode.setChecked(true);

                } else {
                    if (position > 0) {

//                spinnerPaymentMode.setPrompt("-----select Payment Mode-----");
                        List<String> paymentMode = null;
                        if (position == 1) {
//                    paymentMode = creditCard;
                            paymentMode = bank.getBankList("CreditCard");
                            loadBankNames(paymentMode);
//                            get_BankDetails("CreditCard");
                            CARD_TYPE = "CRDC";
                            PAYMENT_OPTION = "OPTCRDC";
                        } else if (position == 2) {
//                    paymentMode = debitCard;
                            paymentMode = bank.getBankList("DebitCard");
                            loadBankNames(paymentMode);
//                            get_BankDetails("DebitCard");
                            CARD_TYPE = "DBCRD";
                            PAYMENT_OPTION = "OPTDBCRD";
                        } else if (position == 3) {
//                            get_BankDetails("NetBanking");
                            paymentMode = bank.getBankList("NetBanking");
                            loadBankNames(paymentMode);
//                    paymentMode = netBanking;
                            CARD_TYPE = "NBK";
                            PAYMENT_OPTION = "OPTNBK";
                        } else if (position == 4) {
//                    paymentMode = cashCard;
//                            get_BankDetails("CashCard");

                            paymentMode = bank.getBankList("CashCard");
                            loadBankNames(paymentMode);

                            CARD_TYPE = "CASHC";
                            PAYMENT_OPTION = "OPTCASHC";
                        } else if (position == 5) {
//                    paymentMode = mobilePayment;
//                            get_BankDetails("MobilePayment");
                            paymentMode = bank.getBankList("MobilePayment");
                            loadBankNames(paymentMode);

                            CARD_TYPE = "MOBP";
                            PAYMENT_OPTION = "OPTMOBP";
                        } else if (position == 6) {
//                    paymentMode = wallet;
//                            get_BankDetails("Wallet");

                            paymentMode = bank.getBankList("Wallet");
                            loadBankNames(paymentMode);

                            CARD_TYPE = "WLT";
                            PAYMENT_OPTION = "OPTWLT";
                        }
//                loadBankNames(paymentMode);
//                if (sPaymentMode.equals("Credit Card") || sPaymentMode.equals("Debit Card")) {
//                    relativeLayout.setVisibility(View.VISIBLE);
//                } else {
//                    relativeLayout.setVisibility(View.GONE);
//                }


                    } else {
                        loadDefaultBankName();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox) v;
        PreferedBankContract pbank = new PreferedBankContract(getApplicationContext());

        if (checkBox.isChecked()) {


            spinnerPaymentMode.setEnabled(false);
            spinnerBankName.setEnabled(false);

            if (positionPaymentMode == 0 || positionBankName == 0) {
                showToast("Please select payment mode and cardName ");
                pymode.setChecked(false);

                spinnerPaymentMode.setEnabled(true);
                spinnerBankName.setEnabled(true);
            } else {
//               boolean paymodebank = pbank.getPositionCount();
//                if(paymodebank){
//                    pbank.updatePreferedmode(new PreferedBankEntity(sUserId, String.valueOf(positionPaymentMode), String.valueOf(positionBankName)));

//                }else {
                pbank.addPreferedmode(new PreferedBankEntity(sUserId, String.valueOf(positionPaymentMode), String.valueOf(positionBankName)));

//                }
                String userid = null, paymode = null, bankname = null;
                List<PreferedBankEntity> pbanks = pbank.getallPaymodeList(sUserId);
                for (PreferedBankEntity eee : pbanks) {
                    userid = eee.getUserId();
                    paymode = eee.getPaymode();
                    bankname = eee.getBankname();

                }
                if (sUserId.equals(userid)) {
                    String name = spinnerPaymentMode.getSelectedItem().toString();


                    spinnerPaymentMode.setSelection(Integer.parseInt(paymode));
                    spinnerBankName.setSelection(Integer.parseInt(bankname));

                    pymode.setChecked(true);
                }
            }


        } else {


            spinnerPaymentMode.setEnabled(true);
            spinnerBankName.setEnabled(true);

            spinnerPaymentMode.setSelection(0);
            pbank.deletepaymodeDetails(sUserId);

//            spinnerBankName.setSelection(0);

        }
    }


    public void getDataAccept(String tempPaymentMode,String tempbankName) {


        String bankName = "";
        String paymentMode = "";
        try {
            bankName = URLEncoder.encode(tempbankName, "UTF-8");
            paymentMode= URLEncoder.encode(tempPaymentMode, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String URL_ADD = Constant.URL_JSON_DOMAIN + Constant.METHOD_NAME_DATA_ACCEPT + "?" + "PaymentMode=" + paymentMode+ "&BankName=" + bankName ;

//        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
//        dialog.setMessage("Loading...");
//        dialog.show();
//        dialog.setCancelable(false);


        try {


            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(URL_ADD, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }

                    String dataAccept = s;

                    System.out.println("dataAccept........... = " + dataAccept);




                    if (positionPaymentMode == 1) {
                        sDataAccept = dataAccept;
                    } else if (positionPaymentMode == 2) {
                        sDataAccept = dataAccept;

//                        if (positionBankName == 4 || positionBankName == 5 || positionBankName == 8 || positionBankName == 10) {
//                            sDataAccept = dataAccept;
//                        } else {
//                            sDataAccept = dataAccept;
//                        }

                    } else if (positionPaymentMode == 3) {
//                        if (positionBankName == 42) {
//                            sDataAccept = "Y";
//                        } else {
//                            sDataAccept = "N";
//                        }
                        sDataAccept = dataAccept;

                    } else {
                        sDataAccept = dataAccept;
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
                }
            });
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadBankNames(List<String> paymentMode) {

        ArrayAdapter<String> adapterBankName = new ArrayAdapter<>
                (CheckAmountActivity.this, android.R.layout.simple_spinner_item, paymentMode);
        adapterBankName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBankName.setAdapter(adapterBankName);
//        spinnerBankName.setPrompt("-----select Card/Bank Name----");
//        spinnerBankName.setAdapter(
//                new NDSpinner(
//                        adapterBankName,
//                        R.layout.spinnertitle,
//                        R.layout.layout, // Optional
//                        getApplicationContext()
//                ));

        spinnerBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sBankName = spinnerBankName.getSelectedItem().toString().trim();
                positionBankName = position;


                PreferedBankContract pbank = new PreferedBankContract(getApplicationContext());

                String userid = null, paymode = null, bankname = null;

                List<PreferedBankEntity> pbanks = pbank.getallPaymodeList(sUserId);
                for (PreferedBankEntity eee : pbanks) {
                    userid = eee.getUserId();
                    paymode = eee.getPaymode();
                    bankname = eee.getBankname();

                }
                if (sUserId.equals(userid)) {
                    String name = spinnerPaymentMode.getSelectedItem().toString();

//                    spinnerPaymentMode.setSelection(Integer.parseInt(paymode));
                    spinnerBankName.setSelection(Integer.parseInt(bankname));
                    pymode.setChecked(true);
                }


                String paymentMode = spinnerPaymentMode.getSelectedItem().toString();

                System.out.println("paymentmode"+paymentMode);


                plusCharges(totalDemand);
//                dataAccept();
                String bankName = spinnerBankName.getSelectedItem().toString();
                getDataAccept(paymentMode,bankName);

                System.out.println("bankName" + bankName);

//               System.out.println("select"+name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void loadDefaultBankName() {
        List<String> loadDefaultPanchayat = new LinkedList<>();
        loadDefaultPanchayat.add(0, "--- Card/Bank Name ---");
        ArrayAdapter<String> panchayatAdapter = new ArrayAdapter<>
                (getApplicationContext(), R.layout.spinnertitle, loadDefaultPanchayat);
        panchayatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBankName.setAdapter(panchayatAdapter);

        spinnerBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sBankName = spinnerBankName.getSelectedItem().toString().trim();

                positionBankName = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void dataAccept() {
        if (positionPaymentMode == 0) {
            sDataAccept = "Y";
        } else if (positionPaymentMode == 1) {

            if (positionBankName == 4 || positionBankName == 5 || positionBankName == 8 || positionBankName == 10) {
                sDataAccept = "Y";
            } else {
                sDataAccept = "N";
            }

        } else if (positionPaymentMode == 2) {
            if (positionBankName == 42) {
                sDataAccept = "Y";
            } else {
                sDataAccept = "N";
            }
        } else {
            sDataAccept = "N";
        }
    }
    public void getAccessCod(){
        List<AccessCodeEntity> list =accessCodeContract.getAccessCodeList();

        for(AccessCodeEntity acc :list){
            lAccessCode =acc.getAccessCode();
            lmerchantId=acc.getMerchantId();
            lcurrency=acc.getCurrency();
       System.out.println("2222222222222222222222222"+list);
        }
    }
//    public static ArrayList<String> listOfPattern() {
//        ArrayList<String> listOfPattern = new ArrayList<>();
//
//        String ptVisa = "^4[0-9]$";
//        listOfPattern.add(ptVisa);
//
//        String ptMasterCard = "^5[1-5]$";
//        listOfPattern.add(ptMasterCard);
//
//        String ptDiscover = "^6(?:011|5[0-9]{2})$";
//        listOfPattern.add(ptDiscover);
//
//        String ptAmeExp = "^3[47]$";
//        listOfPattern.add(ptAmeExp);
//
//        String ptDinersClub = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
//        listOfPattern.add(ptDinersClub);
//
//        return listOfPattern;
//    }

//    public void cardTextChangeListener() {
//        etCardNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String ccNum = s.toString();
//
//                if (ccNum.length() >= 2) {
//                    for (int i = 0; i < listOfPattern().size(); i++) {
//                        if (ccNum.substring(0, 2).matches(listOfPattern().get(i))) {
//                            etCardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageArray[i], 0);
//                            cardtype = String.valueOf(i);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (!etCardNumber.getText().toString().equalsIgnoreCase("")) {
//                    for (int i = 0; i < listOfPattern().size(); i++) {
//                        if (etCardNumber.getText().toString().matches(listOfPattern().get(i))) {
//                            etCardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageArray[i], 0);
//
//                            cardtype = String.valueOf(i);
//                        }
//                    }
//                } else {
//                    etCardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_card, 0);
//                }
//
//                if (etCardNumber.length() == 16) {
//                    etMonth.requestFocus();
//                }
//            }
//        });
//
//        etMonth.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (etMonth.length() == 2) {
//                    etYear.requestFocus();
//                }
//            }
//        });
//
//        etYear.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (etYear.length() == 2) {
//                    etCVV.requestFocus();
//                }
//            }
//        });
//    }

//    public void cardFocusChangeListener() {
//        etCardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
////                if(!hasFocus){
//                if (etCardNumber.getText().toString().length() < 16) {
//                    etCardNumber.setBackgroundResource(R.drawable.custom_edittext_wrong);
//                } else {
//                    etCardNumber.setBackgroundResource(R.drawable.custom_edittext);
//                }
////                }
//            }
//        });
//        etYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                try {
////                if(!hasFocus) {
//                    String year1 = etYear.getText().toString();
//                    String month1 = etMonth.getText().toString();
//                    if (year1.length() < 2 || Integer.parseInt(year1) < currentYear) {
//                        etYear.setBackgroundResource(R.drawable.custom_edittext_wrong);
//                    } else {
//                        etYear.setBackgroundResource(R.drawable.custom_edittext);
//                    }
//
//                    if (Integer.parseInt(year1) == currentYear && Integer.parseInt(month1) < currentMonth) {
//                        etMonth.setBackgroundResource(R.drawable.custom_edittext_wrong);
//                    } else {
//                        etMonth.setBackgroundResource(R.drawable.custom_edittext);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
////                }
//            }
//        });
//        etMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                try {
//                    String month1 = etMonth.getText().toString();
//                    String year1 = etYear.getText().toString();
////                    if (!hasFocus) {
//                    if (String.valueOf(month1).length() < 2 || Integer.parseInt(month1) > 12 ||
//                            (Integer.parseInt(year1) == currentYear && Integer.parseInt(month1) < currentMonth)) {
//                        etMonth.setBackgroundResource(R.drawable.custom_edittext_wrong);
//                    } else {
//                        etMonth.setBackgroundResource(R.drawable.custom_edittext);
//                    }
////                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        etCVV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (etCVV.getText().toString().length() < 3) {
//                    etCVV.setBackgroundResource(R.drawable.custom_edittext_wrong);
//                } else {
//                    etCVV.setBackgroundResource(R.drawable.custom_edittext);
//                }
//            }
//        });
//    }

    public void payment(View view) {

        if (networkConnectivity.isConnected()) {
//            if (sPaymentMode.equals("Credit Card") || sPaymentMode.equals("Debit Card")) {
//                sCardNumber = etCardNumber.getText().toString();
//                sExpMonth = etMonth.getText().toString();
//                sExpYear = etYear.getText().toString();
//                sCVV = etCVV.getText().toString();
//
//                if (sCardNumber.equals("") || sExpMonth.equals("") || sExpYear.equals("") || sCVV.equals("")) {
//                    Toast.makeText(getApplicationContext(), "Please enter card details", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (amountToBePaid <= 0) {
//                        showToast("Please select atleast one demand");
//                    } else {
//                        getOrderId();
//                    }
//                }
//            } else {
            sCardNumber = "";
            sExpMonth = "";
            sExpYear = "";
            sCVV = "";
            if (amountToBePaid <= 0) {
                showToast("Please select atleast one demand");
            } else if (positionPaymentMode == 0) {
                showToast("Please select payment mode");

            } else if (positionBankName == 0) {
                showToast("Please select  BankName");

            } else {

                getOrderId();

            }
//            }
        } else {
            showToast("Please check your Internet connection");
        }
    }

    public void getOrderId() {
        String URL_ASSESS_DETAILS = Constant.URL_JSON_DOMAIN + Constant.METHOD_NAME_GET_ORDERID;

        final ProgressDialog dialog = new ProgressDialog(CheckAmountActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(URL_ASSESS_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                orderId = s;
                System.out.println("mobileNo"+sContactNum);
                System.out.println("EmailId"+sEmail);
                if (preferences.contains("firstName")) {
                    saveOnlineBeforeTransDetails(sUserId, orderId, sTaxType, sAssessmentNumber, sDistrictName, sPanchayatName, String.valueOf(amountToBePaid), sPaymentMode, srMobil ,srEmail, "SRUser", "", "", "", "", sAssessmentName, sWardNo, "", sSno, sFinancialYear, sTerm, "", sCessAmount, sMaintenanceAmount, sPenaltyAmount, String.valueOf(totalDemandAmount), "", "", "");
                }else{
                    saveOnlineBeforeTransDetails("", orderId, sTaxType, sAssessmentNumber, sDistrictName, sPanchayatName, String.valueOf(amountToBePaid), sPaymentMode, sContactNum, sEmail, "SUUser", "", "", "", "", sAssessmentName, sWardNo, "", sSno, sFinancialYear, sTerm, "", sCessAmount, sMaintenanceAmount, sPenaltyAmount, String.valueOf(totalDemandAmount), "", "", "");

                }
                    postToAvenues();
                System.out.println("orderId........... = " + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        requestQueue.add(request);
    }


    public void saveOnlineBeforeTransDetails(String UserId, String OrderId, String TaxType, String TaxNo,
                                             String District, String Panchayat, String Amount, String PayMode, String MobileNo, String MailId,
                                             String LoginType, String DistrictTamil, String PanchayatTamil, String DistrictId, String PanchayatId,
                                             String TaxName, String TaxWardNo, String TaxStreetName, String TaxDemandSno, String TaxFinYear,
                                             String TaxHalfYear, String TaxQuarterAmount, String TaxCessAmount, String TaxMtnCharge,
                                             String TaxPenaltyAmount, String TaxTotalAmount,  String TaxOrgName, String TaxDesignation,
                                             String TaxLeaseTypeCode) {
        String tempDistrict, tempPayMode, tempCardName, tempMailId,
                tempPanchayat, tempTaxType,tempLoginType,tempDistrictTamil,tempPanchayatTamil,tempTaxName,tempTaxStreetName;
        String URL_SAVE_ONLINE_TRANS = "";

        try {
            tempTaxType = URLEncoder.encode(TaxType, "UTF-8");
            tempDistrict = URLEncoder.encode(District, "UTF-8");
            tempPanchayat = URLEncoder.encode(Panchayat, "UTF-8");
            tempPayMode = URLEncoder.encode(PayMode, "UTF-8");
//            tempMailId = URLEncoder.encode(MailId, "UTF-8");
            tempLoginType = URLEncoder.encode(LoginType, "UTF-8");
            tempDistrictTamil = URLEncoder.encode(DistrictTamil, "UTF-8");
            tempPanchayatTamil = URLEncoder.encode(PanchayatTamil, "UTF-8");
            tempTaxName = URLEncoder.encode(TaxName, "UTF-8");
//            tempTaxStreetName = URLEncoder.encode(TaxStreetName, "UTF-8");

            URL_SAVE_ONLINE_TRANS = Constant.URL_JSON_DOMAIN + Constant.METHOD_NAME_SAVE_ONLINE_BEFORE_TRANS + "?" +
                    "UserId=" + UserId + "&OrderId=" + OrderId + "&TaxType=" + tempTaxType +
                    "&TaxNo=" + TaxNo + "&District=" + tempDistrict + "&Panchayat=" + tempPanchayat +
                    "&Amount=" + Amount + "&PayMode=" + tempPayMode + "&MobileNo=" + MobileNo +
                    "&MailId=" + MailId + "&LoginType=" + tempLoginType + "&DistrictTamil=" + tempDistrictTamil +
                    "&PanchayatTamil=" + tempPanchayatTamil + "&DistrictId=" + DistrictId + "&PanchayatId=" + PanchayatId +
                    "&TaxName=" + tempTaxName + "&TaxWardNo=" + TaxWardNo + "&TaxStreetName=" + TaxStreetName +
                    "&TaxDemandSno=" + TaxDemandSno + "&TaxDemandSno=" + TaxDemandSno + "&TaxFinYear=" + TaxFinYear +
                    "&TaxHalfYear=" + TaxHalfYear + "&TaxQuarterAmount=" + TaxQuarterAmount + "&TaxCessAmount=" + TaxCessAmount +
                    "&TaxMtnCharge=" + TaxMtnCharge + "&TaxPenaltyAmount=" + TaxPenaltyAmount + "&TaxTotalAmount=" + TaxTotalAmount +
                    "&TaxOrgName=" + TaxOrgName + "&TaxDesignation=" + TaxDesignation + "&TaxLeaseTypeCode=" + TaxLeaseTypeCode;


        System.out.println("URL_SAVE = " + URL_SAVE_ONLINE_TRANS);

        final ProgressDialog dialog = new ProgressDialog(CheckAmountActivity.this);

        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JsonObjectRequest request = new JsonObjectRequest(URL_SAVE_ONLINE_TRANS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        System.out.println("jsonArray = " + jsonObject);
                        try {

                            System.out.println(jsonObject.getString("code"));
//                            Toast.makeText(getApplicationContext(), jsonObject.getString("code"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                volleyError.printStackTrace();
            }
        });
        requestQueue.add(request);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void postToAvenues() {

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(AvenuesParams.ORDER_ID, orderId);
        intent.putExtra(AvenuesParams.ACCESS_CODE, lAccessCode);
        intent.putExtra(AvenuesParams.MERCHANT_ID,lmerchantId);
        if (preferences.contains("firstName")) {
            intent.putExtra(AvenuesParams.BILLING_NAME, preferences.getString("firstName", null));
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, preferences.getString("address", null));
            intent.putExtra(AvenuesParams.BILLING_COUNTRY, preferences.getString("country", null));
            intent.putExtra(AvenuesParams.BILLING_STATE, preferences.getString("state", null));
            intent.putExtra(AvenuesParams.BILLING_CITY, preferences.getString("city", null));
            intent.putExtra(AvenuesParams.BILLING_ZIP, preferences.getString("pincode", null));
            intent.putExtra(AvenuesParams.BILLING_TEL, preferences.getString("mobile", null));
            intent.putExtra(AvenuesParams.BILLING_EMAIL, preferences.getString("email", null));
            intent.putExtra(AvenuesParams.DELIVERY_NAME, preferences.getString("firstName", null));
            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, preferences.getString("address", null));
            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, preferences.getString("country", null));
            intent.putExtra(AvenuesParams.DELIVERY_STATE, preferences.getString("state", null));
            intent.putExtra(AvenuesParams.DELIVERY_CITY, preferences.getString("city", null));
            intent.putExtra(AvenuesParams.DELIVERY_ZIP, preferences.getString("pincode", null));
            intent.putExtra(AvenuesParams.DELIVERY_TEL, preferences.getString("mobile", null));
//            intent.putExtra(AvenuesParams.BILLING_NAME, HomeActivity.userDetailsEntity.getFirstName());
//            intent.putExtra(AvenuesParams.BILLING_ADDRESS, HomeActivity.userDetailsEntity.getAddress());
//            intent.putExtra(AvenuesParams.BILLING_COUNTRY, HomeActivity.userDetailsEntity.getCountry());
//            intent.putExtra(AvenuesParams.BILLING_STATE, HomeActivity.userDetailsEntity.getState());
//            intent.putExtra(AvenuesParams.BILLING_CITY, HomeActivity.userDetailsEntity.getCity());
//            intent.putExtra(AvenuesParams.BILLING_ZIP, HomeActivity.userDetailsEntity.getPinCode());
//            intent.putExtra(AvenuesParams.BILLING_TEL, HomeActivity.userDetailsEntity.getContactNumber());
//            intent.putExtra(AvenuesParams.BILLING_EMAIL, HomeActivity.userDetailsEntity.getEmailId());
//            intent.putExtra(AvenuesParams.DELIVERY_NAME, HomeActivity.userDetailsEntity.getFirstName());
//            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, HomeActivity.userDetailsEntity.getAddress());
//            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, HomeActivity.userDetailsEntity.getCountry());
//            intent.putExtra(AvenuesParams.DELIVERY_STATE, HomeActivity.userDetailsEntity.getState());
//            intent.putExtra(AvenuesParams.DELIVERY_CITY, HomeActivity.userDetailsEntity.getCity());
//            intent.putExtra(AvenuesParams.DELIVERY_ZIP, HomeActivity.userDetailsEntity.getPinCode());
//            intent.putExtra(AvenuesParams.DELIVERY_TEL, HomeActivity.userDetailsEntity.getContactNumber());

        } else {
            intent.putExtra(AvenuesParams.BILLING_NAME, "UnRegistered");
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, "UnRegistered");
            intent.putExtra(AvenuesParams.BILLING_COUNTRY, "India");
            intent.putExtra(AvenuesParams.BILLING_STATE, "UnRegistered");
            intent.putExtra(AvenuesParams.BILLING_CITY, "UnRegistered");
            intent.putExtra(AvenuesParams.BILLING_ZIP, "UnRegistered");
            intent.putExtra(AvenuesParams.BILLING_TEL, sContactNum);
            intent.putExtra(AvenuesParams.BILLING_EMAIL, sEmail);
            intent.putExtra(AvenuesParams.DELIVERY_NAME, "UnRegistered");
            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, "UnRegistered");
            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, "UnRegistered");
            intent.putExtra(AvenuesParams.DELIVERY_STATE, "UnRegistered");
            intent.putExtra(AvenuesParams.DELIVERY_CITY, "UnRegistered");
            intent.putExtra(AvenuesParams.DELIVERY_ZIP, "UnRegistered");
            intent.putExtra(AvenuesParams.DELIVERY_TEL, sContactNum);

        }


        intent.putExtra(AvenuesParams.CARD_NUMBER, sCardNumber);
        intent.putExtra(AvenuesParams.EXPIRY_YEAR, sExpYear);
        intent.putExtra(AvenuesParams.EXPIRY_MONTH, sExpMonth);
        intent.putExtra(AvenuesParams.CVV, sCVV);

        intent.putExtra(AvenuesParams.REDIRECT_URL, Constant.URL_REDIRECT);
        intent.putExtra(AvenuesParams.CANCEL_URL, Constant.URL_CANCEL);
        intent.putExtra(AvenuesParams.RSA_KEY_URL, Constant.URL_RSA_KEY);
        intent.putExtra(AvenuesParams.PAYMENT_OPTION, PAYMENT_OPTION.trim());
        intent.putExtra(AvenuesParams.ISSUING_BANK, sBankName);
        intent.putExtra(AvenuesParams.CARD_TYPE, CARD_TYPE);
        intent.putExtra(AvenuesParams.CARD_NAME, sBankName);
        intent.putExtra(AvenuesParams.DATA_ACCEPTED_AT, sDataAccept);
        intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "Nil");
        intent.putExtra(AvenuesParams.CURRENCY, lcurrency);
        intent.putExtra(AvenuesParams.AMOUNT, String.valueOf(amountToBePaid));
        intent.putExtra(AvenuesParams.SAVE_CARD, "N");

        intent.putExtra(Params.assessmentNumber, sAssessmentNumber);
        intent.putExtra(Params.assessmentName, sAssessmentName);
        intent.putExtra(Params.districtName, sDistrictName);
        intent.putExtra(Params.panchayatName, sPanchayatName);
        intent.putExtra(Params.taxType, sTaxType);
        intent.putExtra(Params.doorNo, sDoorNo);
        intent.putExtra(Params.wardNo, sWardNo);
        intent.putExtra(Params.streetName, sStreetName);
        intent.putExtra(Params.sNo, sSno);
        intent.putExtra(Params.financialYear, sFinancialYear);
        intent.putExtra(Params.term, sTerm);
        intent.putExtra(Params.userId, sUserId);

        intent.putExtra(Params.cessAmount, sCessAmount);
        intent.putExtra(Params.maintenanceCharge, sMaintenanceAmount);
        intent.putExtra(Params.penaltyAmount, String.valueOf(penaltyAmount));
        intent.putExtra(Params.waterCharges, sWaterCharges);

        intent.putExtra(Params.contactNo, sContactNum);
        intent.putExtra(Params.emailId, sEmail);
        intent.putExtra("totalDemandAmount", String.valueOf(totalDemandAmount));
        intent.putExtra("demandAmount", String.valueOf(totalDemand));

        startActivity(intent);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }





    public void alertBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckAmountActivity.this);
//        builder.setTitle("Registration successful");
        builder.setMessage("No balance amount to be paid");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_amount, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
package com.pataa.sdk;

import static com.pataa.sdk.AppConstants.ON_ACT_RSLT_PATAA_DATA;
import static com.pataa.sdk.AppConstants.REFRESS_INTERVAL_FOR_VALIDATION_CHECK;
import static com.pataa.sdk.AppConstants.metaClientKey;
import static com.pataa.sdk.AppConstants.metaEnableDebugKey;
import static com.pataa.sdk.AppConstants.metaEnableDevelopmentKey;
import static com.pataa.sdk.Utill.getAppHash;
import static com.pataa.sdk.Utill.getMeta;
import static com.pataa.sdk.Validation.SEARCH_PATAA_CODE_MAXIMUM_THRESHOLD;
import static com.pataa.sdk.Validation.isPataaCodeValid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PataaAutoFillView extends FrameLayout {
    public static boolean enableLogger;
    public static String sha1;
    private OnAddress address;
    private Activity activity;
    private View tvCreateNow;
    private TextView tvErrorMessage;

    private View vCreateNow;
    private View vContainer;
    private View vValidPataa;
    private View vClickHere;
    private View btnGreenTickPataaFound;
    private View btnCrossPataaNotFound;
    private View btnAddAddress;
    private TextView tvShipTo;
    private AlertDialog whatIsPataaDialog;
    private EditText editText;
    private View edtHint2, edtHint;
    private View btnAutoFill;
    private Context context;
    private String apikey = "";

    public PataaAutoFillView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public PataaAutoFillView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public PataaAutoFillView(Context context, OnAddress onAddress) {
        super(context);
        this.address = onAddress;
        this.context = context;
        initView();
    }

    public PataaAutoFillView setAddressCallBack(OnAddress onAddress) {
        this.address = onAddress;
        return this;
    }

    public PataaAutoFillView setCurrentActivity(Activity activity, String apikey) {
        this.activity = activity;
        this.apikey = apikey;
        return this;
    }

    public PataaAutoFillView setCurrentActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public Activity getCurrentActivity() {
        if (activity == null) {
            Logger.e("Current activity object is required please se the data on \nsetCurrentActivity(ACTIVITY);");
            return null;
        }
        return activity;
    }

    public void getPataadetail(String pataaCode) {
        if (Utill.isNotNullOrEmpty(pataaCode) && Validation.isPataaCodeValid(pataaCode)) {
            if (editText != null && editText != null) {
                edtHint2.setVisibility(GONE);
                edtHint.setVisibility(GONE);
                editText.setText(pataaCode);
                handler.removeCallbacks(runnable);
                btnAutoFill.performClick();
                btnAddAddress.setVisibility(GONE);

            }
            refreshViewChildrenLayout();
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String s = editText.getText().toString().trim();

                vValidPataa.setVisibility(GONE);
                vCreateNow.setVisibility(GONE);

                if (isPataaCodeValid(s)) {
                    Logger.e("is valid pataa code : " + s);
                    setToInitialViewState(false);
                    btnAutoFill.setVisibility(VISIBLE);
                    btnAddAddress.setVisibility(GONE);
                } else {
                    Logger.e("is not a valid pataa : " + s);
                    setToInitialViewState(false);
                }
                refreshViewChildrenLayout();
            } catch (Exception e) {
                Logger.e(e.getMessage());
                e.printStackTrace();
            }
        }
    };

    String strings[] = {"^KUMAR100", "^786ALIF", "^SINGH007", "^123JOHN"};
    int messageCount = strings.length;
    int currentIndex = -1;

    private void initView() {
        enableLogger = Utill.getMetaBoolean(getContext(), metaEnableDebugKey());
        View inflatedView = inflate(getContext(), R.layout.pataa_auto_fill_cell, this);
        editText = inflatedView.findViewById(R.id.edtPataaEntry);
        View edtCaret = inflatedView.findViewById(R.id.edtCaret);
        View tvClickHere = inflatedView.findViewById(R.id.tvClickHere);
        edtHint = inflatedView.findViewById(R.id.edtHint);
        edtHint2 = inflatedView.findViewById(R.id.edtHint2);
        tvCreateNow = inflatedView.findViewById(R.id.tvCreateNow);
        tvErrorMessage = inflatedView.findViewById(R.id.tvErrorMessage);

        btnAddAddress = inflatedView.findViewById(R.id.btnAddAddress);
        btnCrossPataaNotFound = inflatedView.findViewById(R.id.btnCrossPataaNotFound);
        btnGreenTickPataaFound = inflatedView.findViewById(R.id.btnGreenTickPataaFound);
        vContainer = inflatedView.findViewById(R.id.vContainer);
        btnAutoFill = inflatedView.findViewById(R.id.btnAutoFill);
        vClickHere = inflatedView.findViewById(R.id.vClickHere);
        vCreateNow = inflatedView.findViewById(R.id.vCreateNow);
        vValidPataa = inflatedView.findViewById(R.id.vValidPataa);
        tvShipTo = inflatedView.findViewById(R.id.tvShipTo);

        tvClickHere.setOnClickListener(v -> {
            callCreatePataa();
        });
        btnAddAddress.setOnClickListener(v -> {
            callCreatePataa();
        });
        tvCreateNow.setOnClickListener(v -> {
            callCreatePataa();
        });

        btnAutoFill.setOnClickListener(v -> {
            if (Utill.isNetworkConnected(v.getContext())) getPataadetail(editText);
            else if (address != null) address.onNetworkIsNotAvailable();
        });

        btnCrossPataaNotFound.setOnClickListener(v -> {
            setToInitialViewState(true);
        });

        inflatedView.findViewById(R.id.vWhatIsPataa).setOnClickListener(v -> {
            openWhatIsPataa(this);
        });

        addFiltersToEditText(editText);
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    if (edtHint != null) {
                        edtHint.setVisibility(GONE);
                    }
                    if (edtHint2 != null && editText.getText().length() == 0) {
                        edtHint2.setVisibility(VISIBLE);
                    }
                    refreshViewChildrenLayout();
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (edtHint2 != null) {
                        edtHint2.setVisibility(charSequence.length() > 0 ? GONE : VISIBLE);
                    }

                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, REFRESS_INTERVAL_FOR_VALIDATION_CHECK);
                    edtCaret.setVisibility(charSequence.length() > 0 ? VISIBLE : GONE);
                    refreshViewChildrenLayout();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (Validation.isPataaCodeValid(editText.getText().toString().trim())) {
                        btnAutoFill.performClick();
                        return false;
                    }
                    return true;
                }
                return false;
            }
        });

        //########## animation ##############//
        TextSwitcher simpleTextSwitcher = inflatedView.findViewById(R.id.simpleTextSwitcher);
        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        simpleTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create a TextView
                TextView t = new TextView(context);
                t.setTextColor(Color.parseColor("#989898"));
                // set the gravity of text to top and center horizontal
//                t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                // set displayed text size
                t.setTextSize(15);
                return t;
            }
        });

        // Declare in and out animations and load them using AnimationUtils class
        Animation in = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
        Animation out = AnimationUtils.loadAnimation(context, R.anim.bottom_up2);

        // set the animation type to TextSwitcher
        simpleTextSwitcher.setInAnimation(in);
        simpleTextSwitcher.setOutAnimation(out);
        final Handler handler = new Handler();
        final int delay = 1500; // 1000 milliseconds == 1 second
//        simpleTextSwitcher.setCurrentText("Enter Pataa");
        handler.postDelayed(new Runnable() {
            public void run() {
                handler.postDelayed(this, delay);
                currentIndex++;
                // If index reaches maximum then reset it
                if (currentIndex == messageCount)
                    currentIndex = 0;
                simpleTextSwitcher.setText(strings[currentIndex]);
            }
        }, delay);

    }

    public String getSha1() {
        if (!Utill.isNotNullOrEmpty(sha1)) {
            sha1 = getAppHash(getCurrentActivity());
        }
        Log.e("SHA1", sha1);
        return sha1;
    }

    private void callCreatePataa() {
        if (getCurrentActivity() != null) {
            String url = vContainer.getContext().getString(Utill.getMetaBoolean(vContainer.getContext(), metaEnableDevelopmentKey()) ? R.string.create_pataa_web_url_development : R.string.create_pataa_web_url);
            Intent intent = CreatePataaActivity.createPataa(vContainer.getContext(), url, getMeta(getContext(), metaClientKey()), new DialogCallback() {
                @Override
                public void Response(String pataa) {

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            getPataadetail(pataa);
                            // Stuff that updates the UI

                        }
                    });


                }
            });
            activity.startActivityForResult(intent, 200);

        }
    }

    private void openWhatIsPataa(View view) {
        if (whatIsPataaDialog != null) {
            whatIsPataaDialog.show();
        } else
            whatIsPataaDialog = Utill.showWhatIsPataaDialog(view.getContext(), new WhatIsPataaDialogCallBack() {
                @Override
                public void clickOnCreatePataa() {
                    callCreatePataa();
                }

                @Override
                public void onDismiss() {

                }

                @Override
                public void onPataaClick(String pataaCode) {

                }
            });
    }

    private void addFiltersToEditText(EditText editText) {
        try {
            ArrayList<InputFilter> curInputFilters = new ArrayList<InputFilter>(Arrays.asList(editText.getFilters()));
            curInputFilters.add(0, new AlphaNumericAndDashInputOnlyA2ZFilter());
            curInputFilters.add(1, new InputFilter.AllCaps());
            curInputFilters.add(2, new InputFilter.LengthFilter(SEARCH_PATAA_CODE_MAXIMUM_THRESHOLD));
            InputFilter[] newInputFilters = curInputFilters.toArray(new InputFilter[curInputFilters.size()]);
            editText.setFilters(newInputFilters);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        } catch (Exception e) {
            Logger.e(e.getMessage());
            e.printStackTrace();
        }
    }

    public String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public void getPataadetail(EditText editText) {
        if (getCurrentActivity() == null) return;
        String app_name = getApplicationName(context);
        Api.getApi(getContext()).getPataaDetail(
                apikey.length() == 0 ? getMeta(getContext(), metaClientKey()) : apikey,
                editText.getText().toString().trim().toUpperCase(), "android",
                app_name,
                context.getPackageName(),
                getSha1().toUpperCase()
        ).enqueue(new Callback<GetPataaDetailResponse>() {

            @Override
            public void onResponse(Call<GetPataaDetailResponse> call, Response<GetPataaDetailResponse> response) {
                try {
                    Logger.e("request : " + new Gson().toJson(call.request().body()));
                    Logger.e("api data");
//                    Logger.e(response.body().getMsg());
                    Logger.e(new Gson().toJson(response.body()));
                    if (response.body().getStatus() == PataaErrorCodes.PATAA_FOUND.getErrorCode()) {
                        setPataaDetail(response.body());
                    } else if (response.body().getStatus() == PataaErrorCodes.PATAA_NOT_FOUND.getErrorCode()) {
                        setPataaDetailNotFound(response.body());
                    } else if (response.body().getStatus() == PataaErrorCodes.PATAA_ORIGIN_MISMATCHED.getErrorCode()) {
                        setErrorWithoutAction(response.body().getMsg(), response.body().getStatus());
                    } else if (response.body().getStatus() == PataaErrorCodes.PATAA_LIMIT_EXIST.getErrorCode()) {
                        setErrorWithoutAction(response.body().getMsg(), response.body().getStatus());
                    } else if (response.body().getStatus() == PataaErrorCodes.PATAA_INVALID_KEY.getErrorCode()) {
                        setErrorWithoutAction(response.body().getMsg(), response.body().getStatus());
                    } else {
                        if (address != null)
                            address.onError(response.body().getStatus(), response.body().getMsg());
                        Logger.e(response.body().getMsg());
                        Logger.e(response.body());
                    }
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetPataaDetailResponse> call, Throwable t) {
                try {
                    Logger.e(call.request().body());
                    t.printStackTrace();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    e.printStackTrace();
                }
            }

        });
    }

    private void setPataaDetail(GetPataaDetailResponse pataaDetail) {
        try {
            Drawable drawableValidPataa = vContainer.getContext().getDrawable(R.drawable.bg_green_border);

            if (tvShipTo != null) {
                tvShipTo.setText(tvShipTo.getContext().getString(R.string.ship_to, pataaDetail.getResult().getPataa().getFormattedAddressShort()));
                vContainer.setBackground(drawableValidPataa);
                vCreateNow.setVisibility(GONE);
                btnCrossPataaNotFound.setVisibility(GONE);
                btnAutoFill.setVisibility(GONE);
                vClickHere.setVisibility(GONE);
                editText.setSelection(editText.getText().toString().length());
                btnGreenTickPataaFound.setVisibility(VISIBLE);
                vValidPataa.setVisibility(VISIBLE);
            }
            address.onPataaFound(pataaDetail.getResult().getUser(), pataaDetail.getResult().getPataa());
            refreshViewChildrenLayout();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setErrorWithoutAction(String message, int errorCode) {
        try {
            Drawable drawableNotFound = vContainer.getContext().getDrawable(R.drawable.bg_red_border);
            vContainer.setBackground(drawableNotFound);
            tvCreateNow.setVisibility(INVISIBLE);
            vCreateNow.setVisibility(VISIBLE);
            btnCrossPataaNotFound.setVisibility(VISIBLE);

            vClickHere.setVisibility(GONE);
            vValidPataa.setVisibility(GONE);
            btnAutoFill.setVisibility(GONE);
            btnGreenTickPataaFound.setVisibility(GONE);
            btnAddAddress.setVisibility(GONE);

            tvErrorMessage.setText(getResources().getString(R.string.pataa_is_disabled_please_fill_up_your_address_details_manually));
        } catch (Exception e) {
            Logger.e(e.getMessage());
            e.printStackTrace();
        }
    }


    private void setPataaDetailNotFound(GetPataaDetailResponse pataaDetail) {
        try {
            Drawable drawableNotFound = vContainer.getContext().getDrawable(R.drawable.bg_red_border);
            vContainer.setBackground(drawableNotFound);
            vCreateNow.setVisibility(VISIBLE);
            btnCrossPataaNotFound.setVisibility(VISIBLE);

            vClickHere.setVisibility(GONE);
            vValidPataa.setVisibility(GONE);
            btnAutoFill.setVisibility(GONE);
            btnGreenTickPataaFound.setVisibility(GONE);
            btnAddAddress.setVisibility(GONE);

            address.onPataaNotFound(pataaDetail.getMsg());
            refreshViewChildrenLayout();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setToInitialViewState(boolean clearTheField) {
        try {
            Drawable drawable = vContainer.getContext().getDrawable(R.drawable.bg_black_border);
            vContainer.setBackground(drawable);
            vClickHere.setVisibility(VISIBLE);

            vCreateNow.setVisibility(GONE);
            btnCrossPataaNotFound.setVisibility(GONE);
            vValidPataa.setVisibility(GONE);
            btnAutoFill.setVisibility(GONE);
            btnGreenTickPataaFound.setVisibility(GONE);
            btnAddAddress.setVisibility(VISIBLE);
            if (clearTheField) editText.setText("");
            refreshViewChildrenLayout();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            e.printStackTrace();
        }
    }


    private void refreshViewChildrenLayout() {
        View view = this;
        view.measure(
                View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }
}

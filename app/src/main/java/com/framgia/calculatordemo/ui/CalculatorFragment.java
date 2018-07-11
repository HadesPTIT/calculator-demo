package com.framgia.calculatordemo.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.framgia.calculatordemo.R;
import com.framgia.calculatordemo.util.Calculate;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private static final String PREF_NAME = "PREF_LAST_RESULT";
    private static final String KEY_SAVE = "KEY_SAVE";

    private TextView mTvResult;
    private TextView mTvExpress;
    private double mResult;
    private SharedPreferences mPref;

    private int[] mIds = {R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6,
            R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_div, R.id.button_mul, R.id.button_add, R.id.button_sub,
            R.id.button_dot, R.id.button_equal, R.id.button_change_sign, R.id.button_mod, R.id.button_ac};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTvResult = view.findViewById(R.id.text_result);
        mTvExpress = view.findViewById(R.id.text_express);
        mPref = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mTvResult.setText(mPref.getString(KEY_SAVE, getString(R.string.default_result)));
        setupCallback(view);
    }

    private void setupCallback(View view) {
        for (int id : mIds) {
            view.findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                handleClear();
                return true;
            case R.id.menu_save_last_result:
                handleSaveLastResult();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_change_sign:
                handleChangeSign();
                break;
            case R.id.button_ac:
                handleClear();
                break;
            case R.id.button_equal:
                handleCalculate();
                break;
            default:
                handleExpression(v);
        }
    }

    private void handleSaveLastResult() {
        mPref.edit().putString(KEY_SAVE, String.valueOf(mResult)).apply();
    }

    private void handleCalculate() {
        try {
            mResult = Calculate.eval(mTvExpress.getText().toString());
            mTvResult.setText(String.valueOf(mResult));
        } catch (RuntimeException e) {
            mTvResult.setText(R.string.error);
        }

    }

    private void handleExpression(View v) {
        Button b = (Button) v;
        String expression = b.getText().toString();
        mTvExpress.append(expression);
    }

    private void handleClear() {
        mTvExpress.setText("");
        mTvResult.setText("0");
    }

    private void handleChangeSign() {
        if (mResult == 0) return;
        mResult = mResult * -1;
        mTvResult.setText(String.valueOf(mResult));
    }
}



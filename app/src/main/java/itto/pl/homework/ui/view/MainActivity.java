package itto.pl.homework.ui.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import itto.pl.homework.R;
import itto.pl.homework.base.BaseActivity;
import itto.pl.homework.base.IActionCallback;
import itto.pl.homework.ui.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {

    private MainViewModel mViewModel;
    private Button mBtnStart;
    private Button mBtnStop;
    private TextView mTvResponse;
    private TextView mTvLocationState;
    private TextView mTvBatteryState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

    /**
     * Init event and data
     */
    private void init() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        setupViews();
        setupData();
    }

    private void setupViews() {
        mBtnStart = findViewById(R.id.btn_start);
        mBtnStop = findViewById(R.id.btn_stop);
        mTvResponse = findViewById(R.id.tv_response);
        mTvLocationState = findViewById(R.id.tv_location_state);
        mTvBatteryState = findViewById(R.id.tv_battery_state);

        mBtnStart.setOnClickListener((view) -> requestForStarting());
        mBtnStop.setOnClickListener((view) -> requestForStopping());
    }

    /**
     * Setup data from server
     */
    private void setupData() {
        mViewModel.getLoadingState().observe(this, isLoading -> {
                    if (isLoading) {
                        /* If is loading data, disable start button */
                        mTvLocationState.setText(R.string.loading);
                        mTvBatteryState.setText(R.string.loading);
                        mBtnStart.setEnabled(false);
                        mBtnStop.setEnabled(true);
                    } else {
                        /* If is NOT loading data, disable stop button */
                        mTvLocationState.setText(R.string.stopped);
                        mTvBatteryState.setText(R.string.stopped);
                        mBtnStart.setEnabled(true);
                        mBtnStop.setEnabled(false);
                    }
                }
        );
        /* Update state Label */
        mViewModel.getLocationState().observe(this, state -> mTvLocationState.setText(state));
        mViewModel.getBatteryState().observe(this, state -> mTvBatteryState.setText(state));
        /* Update Response label */
        mViewModel.getResult().observe(this, response -> mTvResponse.append(response + "\n---------------\n"));
    }

    /**
     * request stop collecting data
     */
    private void requestForStopping() {
        mViewModel.stopLoading();
    }

    /**
     * Request start collect data
     */
    private void requestForStarting() {
        if (isGrantPermisisons()) {
            mViewModel.startLoading();
        } else {
            requestPermissions(new IActionCallback() {
                @Override
                public void onSuccess(Object result) {
                    mViewModel.startLoading();
                }

                @Override
                public void onFailed(@Nullable Object error) {
                    showToast(R.string.permission_denied);
                }
            });
        }
    }

}

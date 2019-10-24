package itto.pl.homework.base;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import static itto.pl.homework.base.AppConstants.PERMISSION_REQUEST_CODE;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * The Callback to handle when requesting permission is succeed or failed
     */
    private IActionCallback mPermissionCallback = null;

    public boolean isGrantPermisisons() {
        boolean ok = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : AppConstants.PERMISSIONS_LIST)
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    ok = false;
                    break;
                }
        }
        return ok;
    }

    /**
     * Request Permissions
     *
     * @param callback pass this callback to handle when permission is granted or denied
     */
    public void requestPermissions(@NonNull IActionCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionCallback = callback;
            requestPermissions(AppConstants.PERMISSIONS_LIST, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                boolean ok = true;
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        ok = false;
                        break;
                    }
                }

                if (ok) {
                    if (mPermissionCallback != null)
                        mPermissionCallback.onSuccess(null);
                } else {
                    if (mPermissionCallback != null)
                        mPermissionCallback.onFailed(null);
                }
                mPermissionCallback = null;
                break;
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
    }

    /**
     * get the Layout ResourceId of this activity
     */
    protected abstract int getResId();

    /**
     * Show Toast from String resourceId
     * @param msgId
     */
    protected void showToast(@StringRes int msgId) {
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show Toast from String message
     * @param msg
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

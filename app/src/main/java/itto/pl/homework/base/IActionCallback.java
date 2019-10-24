package itto.pl.homework.base;

import androidx.annotation.Nullable;

/**
 * Created by PL_itto-PC on 10/24/2019
 **/
public interface IActionCallback {
    void onSuccess(Object result);

    void onFailed(@Nullable Object error);
}

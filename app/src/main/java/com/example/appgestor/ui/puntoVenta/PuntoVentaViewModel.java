package com.example.appgestor.ui.puntoVenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PuntoVentaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PuntoVentaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
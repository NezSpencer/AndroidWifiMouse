package com.nuhiara.nezspencer.androidwifimouse.viewmodel;

import android.util.Pair;

import com.nuhiara.nezspencer.androidwifimouse.view.interfaces.MouseMovementListener;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MouseActivityViewModel extends ViewModel implements MouseMovementListener {

    private MutableLiveData<Pair<Integer, Integer>> coordinatesObservable = new MutableLiveData<>();

    @Override
    public void onMouseMoved(int x, int y) {
        coordinatesObservable.setValue(Pair.create(x, y));
    }

    public MutableLiveData<Pair<Integer, Integer>> getCoordinatesObservable() {
        return coordinatesObservable;
    }
}

package PictureData;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Bitmap> data = new MutableLiveData<>();

    public void setData(Bitmap value) {
        data.setValue(value);
    }

    public LiveData<Bitmap> getData() {
        return data;
    }
}

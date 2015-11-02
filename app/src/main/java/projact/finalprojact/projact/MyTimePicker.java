package projact.finalprojact.projact;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 01/11/2015.
 */
public class MyTimePicker extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View timepicker= inflater.inflate(R.layout.timepickerfragment, container, false);
        return timepicker;
    }
}

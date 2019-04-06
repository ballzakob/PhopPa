package com.example.phobpa.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phobpa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sub1Fragment extends Fragment {
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sub1, container, false);
        textView = v.findViewById(R.id.textView);
        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}

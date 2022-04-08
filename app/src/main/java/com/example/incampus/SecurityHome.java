package com.example.incampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class SecurityHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstancesState){
        View root =  inflater.inflate(R.layout.fragment_home_security, container, false);

        CardView studentCard = root.findViewById(R.id.studentCard);

        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), StudentCard.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return root;
    }
}

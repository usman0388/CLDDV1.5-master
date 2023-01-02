package com.example.clddv13.Tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clddv13.Adapters.RecyclerviewAdapter;
import com.example.clddv13.R;
import com.example.clddv13.customClasses.leafDisease;

import java.util.ArrayList;
import java.util.List;


public class tab2 extends Fragment {
    List<leafDisease> types;
    public tab2() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        types = new ArrayList<>();
        types.add(new leafDisease("Citrus Cranker","Citrus canker is a disease affecting Citrus species caused by the bacterium Xanthomonas axonopodis." +
                " Infection causes lesions on the leaves, stems, and fruit of citrus trees, including lime, oranges, and grapefruit. While not harmful to humans, " +
                "canker significantly affects the vitality of citrus trees, causing leaves and fruit to drop prematurely; " +
                "a fruit infected with canker is safe to eat, but too unsightly to be sold." +
                "The disease, which is believed to have originated in Southeast Asia, is extremely persistent when it becomes established in an area. " +
                "Citrus groves have been destroyed in attempts to eradicate the disease. Brazil and the United States are currently suffering from canker outbreaks.",R.drawable.canker));
        types.add(new leafDisease("Alternaria","Alternaria rot is a fungal disease that affects mainly navel oranges and lemons. " +
                "Fruit infected with Alternaria change color prematurely. The decay is softer on lemons than on oranges. Infections typically occur in the grove; disease often " +
                "doesn't develop until after harvest, and most damage occurs during storage. On navel oranges, the disease is also called black rot, " +
                "and results in dark brown to black, firm spots or areas at the stylar (blossom) end or in the navel. If you cut the fruit in half, you can see the rot extending into the core.",R.drawable.alternaria));
        types.add(new leafDisease("Magnesium Deficiency","Magnesium deficiency has been a major problem in citrus " +
                "production. In Florida, Mg deficiency is commonly referred " +
                "to as “bronzing.” Trees with inadequate Mg may have no " +
                "symptoms in the spring growth flush, but leaf symptoms " +
                "develop as the leaves age and the fruit expand and mature " +
                "in the summer and fall. Magnesium deficiency symptoms " +
                "occur on mature leaves following the removal of Mg to " +
                "satisfy fruit requirements",R.drawable.magnesium));
        RecyclerView myrv = (RecyclerView) view.findViewById(R.id.recyclerview);
        RecyclerviewAdapter myAdapter = new RecyclerviewAdapter(getActivity(),types);
        myrv.setLayoutManager(new GridLayoutManager(getActivity(),3));

        myrv.setAdapter(myAdapter);

        return view;
    }
}
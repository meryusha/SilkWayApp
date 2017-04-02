package silkway.merey.silkwayapp.operator.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.operator.activities.MainActivityOperator;
import silkway.merey.silkwayapp.operator.activities.OperatorToursFeed;
import silkway.merey.silkwayapp.operator.adapters.InstrumentsListViewAdapter;

public class OperatorInstrumentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button createTourButton;
    private ListView listView;
    // private OnListViewClickedListener listener;
    private InstrumentsListViewAdapter adapter;

    public OperatorInstrumentsFragment() {
        // Required empty public constructor
    }


    public static OperatorInstrumentsFragment newInstance(String param1, String param2) {
        OperatorInstrumentsFragment fragment = new OperatorInstrumentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_operator_instruments, container, false);
        initToolbar();
        initListView(v);
        initButton(v);


        return v;
    }

    private void initButton(View v) {
        createTourButton = (Button) v.findViewById(R.id.createTourButton);
        createTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateClicked();
            }
        });
    }

    private void onCreateClicked() {
    }

    private void initListView(View v) {
        listView = (ListView) v.findViewById(R.id.listView);
        List<String> instruments = new ArrayList<>();
        instruments.add("Перелеты");
        instruments.add("Отели");
        instruments.add("Туристические услуги");

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.perelety);
        images.add(R.drawable.hotels);
        images.add(R.drawable.tours);
        adapter = new InstrumentsListViewAdapter(getActivity(), instruments, images);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    Intent intent = new Intent(getActivity(), OperatorToursFeed.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
     /*   if (context instanceof OnListViewClickedListener) {
            listener = (OnListViewClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //  listener = null;
    }

    private void initToolbar() {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Мои иструменты");
        ((MainActivityOperator) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}


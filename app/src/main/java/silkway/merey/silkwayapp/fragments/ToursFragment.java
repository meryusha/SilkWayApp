package silkway.merey.silkwayapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import silkway.merey.silkwayapp.activities.AddTourActivity;
import silkway.merey.silkwayapp.activities.MainActivity;
import silkway.merey.silkwayapp.adapters.LocationAdapter;
import silkway.merey.silkwayapp.adapters.TypeAdapter;
import silkway.merey.silkwayapp.data.DataHolder;
import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.adapters.ToursFeedAdapter;


public class ToursFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Tour> tours = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    private OnTourFragmentInteractionListener mListener;
    private ToursFeedAdapter adapter;
    private TabLayout tabLayout;
    private SearchView searchView;
    private Spinner locationSpinner;
    private Spinner typeSpinner;
    private FloatingActionButton floatingButton;

    public ToursFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ToursFragment newInstance(String param1, String param2) {
        ToursFragment fragment = new ToursFragment();
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
        View v = inflater.inflate(R.layout.fragment_tours, container, false);
        initViews(v);
        return v;
    }

    private void initViews(View v) {
        initListView(v);
        initToolbar();
        initTabs(v);
        initSearchView(v);
        initSpinners(v);
    }

    private void initToolbar() {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Туры");
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initSpinners(View v) {
        typeSpinner = (Spinner) v.findViewById(R.id.typeSpinner);
        locationSpinner = (Spinner) v.findViewById(R.id.locationSpinner);
        ArrayList<String> typeList = new ArrayList<>();
        ArrayList<String> locationList = new ArrayList<>();
        generateSpinnerArrays(typeList, locationList);
        typeSpinner.setAdapter(new TypeAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, typeList));
        locationSpinner.setAdapter(new LocationAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, locationList));
    }

    //will be changed
    private void generateSpinnerArrays(ArrayList<String> typeList, ArrayList<String> locationList) {
        typeList.add("Экстрим");
        typeList.add("Туризм");
        locationList.add("Казахстан");
        locationList.add("Россия");
        locationList.add("Кыргызстан");
        locationList.add("Китай");

    }


    private void initSearchView(View v) {
        searchView = (SearchView) v.findViewById(R.id.searchView);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                adapter.filter(searchQuery.toString().trim());
                listView.invalidate();
                return true;
            }
        });
    }

    private void initTabs(View v) {
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Все"));
        tabLayout.addTab(tabLayout.newTab().setText("Мои"));
        tabLayout.addTab(tabLayout.newTab().setText("Открытые"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void initListView(View v) {
        floatingButton = (FloatingActionButton) v.findViewById(R.id.floatingButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTourActivity.class);
                startActivity(intent);
            }
        });
        listView = (ListView) v.findViewById(R.id.listView);
        tryListView();
        adapter = new ToursFeedAdapter(getActivity(), tours);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewClicked(position);
            }
        });
        listView.setAdapter(adapter);

    }


    //will be changed when connected with Parse
    private void tryListView() {
        tours.add(new Tour("Первое знакомство с Казахстаном", "Незабываемое путешествие в самый центр Евразии, к восхитительной природе, красочным традициям и удивительному переплетению прошлого и современности"));
        tours.add(new Tour("Экскурсия в каньон Чарын", "Протяженность автомобильной части: 440 км; Протяженность пешеходной части: 5 км"));
        tours.add(new Tour("Экскурсия на горное озеро Иссык", "Экскурсия на горное озеро Иссык"));
        tours.add(new Tour("Экскурсия на водопад «Медвежий» в горном ущелье Тургень", "Протяженность автомобильной части: 200 км"));
        DataHolder.getInstance().setTours(tours);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onListViewClicked(int position) {
        if (mListener != null) {
            mListener.onTourFragmentInteraction(tours.get(position));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTourFragmentInteractionListener) {
            mListener = (OnTourFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTourFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnTourFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTourFragmentInteraction(Tour tour);
    }
}

package silkway.merey.silkwayapp.operator.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.DataManager;
import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.agent.adapters.CategoryAdapter;
import silkway.merey.silkwayapp.agent.adapters.LocationAdapter;
import silkway.merey.silkwayapp.agent.adapters.OperatorToursFeedAdapter;
import silkway.merey.silkwayapp.classes.Category;
import silkway.merey.silkwayapp.classes.Location;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.operator.activities.MainActivityOperator;

public class OperatorToursFragment extends Fragment {
    private List<Tour> tours = new ArrayList<>();
    //buttons
    private Button sortByPriceButton;
    private Button sortByTopButton;
    private Button sortByDateButton;

    private ListView listView;
    private OperatorToursFeedAdapter adapter;
    private TabLayout tabLayout;
    private SearchView searchView;
    private Spinner locationSpinner;
    private Spinner categorySpinner;
    private OnTourFragmentInteractionListener listener;
    private boolean topUp = true;
    private boolean priceUp = true;
    private boolean dateUp = true;
    private ProgressDialog dialog;
    private List<Category> categories;
    private List<Location> locations;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public OperatorToursFragment() {
        // Required empty public constructor
    }


    public static OperatorToursFragment newInstance(String param1, String param2) {
        OperatorToursFragment fragment = new OperatorToursFragment();
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
        View v = inflater.inflate(R.layout.fragment_operator_feed_tours, container, false);
        initViews(v);


        return v;
    }


    private void initViews(View v) {
        initListView(v);
        initToolbar();
        initTabs(v);
        initSearchView(v);
        initSpinners(v);
        initSort(v);
    }


    private void initSort(View v) {
        sortByDateButton = (Button) v.findViewById(R.id.sortByDate);
        sortByPriceButton = (Button) v.findViewById(R.id.sortByPrice);
        sortByTopButton = (Button) v.findViewById(R.id.sortByTop);

        sortByTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByTop();
            }
        });
        sortByDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByDate();
            }
        });
        sortByPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPrice();
            }
        });

    }

    private void sortByTop() {
        if (topUp) {
            // getTours("created ASC");
            sortByTopButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_sort_arrow_down, 0);

        } else {
            //   getTours("created ASC");
            sortByTopButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_sort_arrow, 0);

        }
        topUp = !topUp;
        sortByTopButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        sortByDateButton.setBackgroundColor(getResources().getColor(R.color.transparent));
        sortByPriceButton.setBackgroundColor(getResources().getColor(R.color.transparent));


    }

    private void sortByPrice() {
        if (priceUp) {
            sortByPriceButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_sort_arrow_down, 0);
            getTours("price DESC");

        } else {
            sortByPriceButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_sort_arrow, 0);
            getTours("price ASC");

        }
        priceUp = !priceUp;
        sortByPriceButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        sortByDateButton.setBackgroundColor(getResources().getColor(R.color.transparent));
        sortByTopButton.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void sortByDate() {
        if (dateUp) {
            sortByDateButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_sort_arrow_down, 0);
            getTours("created DESC");
        } else {
            sortByDateButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_sort_arrow, 0);
            getTours("created ASC");
        }
        dateUp = !dateUp;
        sortByDateButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        sortByTopButton.setBackgroundColor(getResources().getColor(R.color.transparent));
        sortByPriceButton.setBackgroundColor(getResources().getColor(R.color.transparent));

    }

    private void initToolbar() {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Туры");
        ((MainActivityOperator) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initSpinners(View v) {
        categorySpinner = (Spinner) v.findViewById(R.id.typeSpinner);
        locationSpinner = (Spinner) v.findViewById(R.id.locationSpinner);
        generateSpinnerArrays();

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
                if (adapter != null) {
                    adapter.filter(searchQuery.toString().trim());
                    listView.invalidate();
                }
                return true;
            }
        });
    }

    private void initTabs(View v) {
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Все"));
        tabLayout.addTab(tabLayout.newTab().setText("В перегововрах"));
        tabLayout.addTab(tabLayout.newTab().setText("Подписанные"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                               @Override
                                               public void onTabSelected(TabLayout.Tab tab) {
                                                   int position = tab.getPosition();
                                                   switch (position) {
                                                       case 0:
                                                           //all
                                                           if (adapter != null) {
                                                               adapter.filter("");
                                                           }
                                                           break;
                                                       case 1:
                                                           //in process
                                                           break;
                                                       case 2:
                                                           //subscribed

                                                           if (adapter != null) {
                                                               if (Backendless.UserService.CurrentUser() != null) {
                                                                   adapter.getMyTours(Backendless.UserService.CurrentUser());
                                                               }
                                                           }
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
                                           }

        );
    }

    private void initListView(View v) {

        listView = (ListView) v.findViewById(R.id.listView);
        getTours(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewClicked(position);

            }
        });

    }

    private void generateSpinnerArrays() {
        Backendless.Data.of(Category.class).find(new AsyncCallback<BackendlessCollection<Category>>() {
            @Override
            public void handleResponse(BackendlessCollection<Category> response) {
                categories = response.getCurrentPage();
                categories.add(0, new Category("Все"));
                DataManager.getInstance().setCategories(categories);
                categorySpinner.setAdapter(new CategoryAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories));
                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Category category = (Category) parent.getItemAtPosition(position);
                        if (adapter != null) {
                            adapter.filterByCategory(category);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        Backendless.Data.of(Location.class).find(new AsyncCallback<BackendlessCollection<Location>>() {
            @Override
            public void handleResponse(BackendlessCollection<Location> response) {
                locations = response.getCurrentPage();
                locations.add(0, new Location("Все"));
                DataManager.getInstance().setLocations(locations);
                locationSpinner.setAdapter(new LocationAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, locations));
                locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Location location = (Location) parent.getItemAtPosition(position);
                        if (adapter != null) {
                            adapter.filterByLocation(location);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    public void getTours(String sortOptions) {
        showDialog(getResources().getString(R.string.dialogTitleSavingInfo), getResources().getString(R.string.dialogMessage));
        final AsyncCallback<BackendlessCollection<Tour>> callback = new AsyncCallback<BackendlessCollection<Tour>>() {
            public void handleResponse(BackendlessCollection<Tour> toursList) {
                tours = toursList.getCurrentPage();
                Log.d("tours retr", toursList.toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (listView != null) {
                    adapter = new OperatorToursFeedAdapter(getActivity(), tours);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("error", backendlessFault.getMessage());
                new AlertDialog.Builder(getActivity())
                        .setTitle("Ошибка")
                        .setMessage("Пожалуйста, попробуйте еще раз.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };

        if (sortOptions != null) {
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            QueryOptions queryOptions = new QueryOptions();
            queryOptions.addSortByOption(sortOptions);
            dataQuery.setQueryOptions(queryOptions);
            Backendless.Persistence.of(Tour.class).find(dataQuery, callback);
        } else

        {
            Backendless.Data.of(Tour.class).find(callback);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onListViewClicked(int position) {
        if (listener != null) {

            listener.onTourFragmentInteraction(tours.get(position));
        }
    }


    private void showDialog(String title, String message) {
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTourFragmentInteractionListener) {
            listener = (OnTourFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnTourFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTourFragmentInteraction(Tour tour);
    }

}


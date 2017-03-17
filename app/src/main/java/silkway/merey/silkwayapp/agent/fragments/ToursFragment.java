package silkway.merey.silkwayapp.agent.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.agent.activities.AddTourActivity;
import silkway.merey.silkwayapp.agent.activities.MainActivity;
import silkway.merey.silkwayapp.agent.adapters.CategoryAdapter;
import silkway.merey.silkwayapp.agent.adapters.LocationAdapter;
import silkway.merey.silkwayapp.agent.adapters.ToursFeedAdapter;
import silkway.merey.silkwayapp.classes.Category;
import silkway.merey.silkwayapp.classes.Location;
import silkway.merey.silkwayapp.classes.Tour;


public class ToursFragment extends Fragment {


    //buttons
    private Button sortByPriceButton;
    private Button sortByTopButton;
    private Button sortByDateButton;
    private FloatingActionButton floatingButton;

    //lists
    private List<Tour> tours = new ArrayList<>();
    private List<Category> categories;
    private List<Location> locations;

    //primitiv
    private boolean topUp = true;
    private boolean priceUp = true;
    private boolean dateUp = true;

    //spinners
    private Spinner locationSpinner;
    private Spinner categorySpinner;


    private ListView listView;
    private OnTourFragmentInteractionListener mListener;
    private ToursFeedAdapter adapter;
    private TabLayout tabLayout;
    private SearchView searchView;
    private ProgressDialog dialog;

    public ToursFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ToursFragment newInstance() {
        ToursFragment fragment = new ToursFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

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
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                                          }

        );
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
                categorySpinner.setAdapter(new CategoryAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories));

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        Backendless.Data.of(Location.class).find(new AsyncCallback<BackendlessCollection<Location>>() {
            @Override
            public void handleResponse(BackendlessCollection<Location> response) {
                locations = response.getCurrentPage();
                locationSpinner.setAdapter(new LocationAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, locations));
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    public void getTours(String sortOptions) {
        showDialog();
        final AsyncCallback<BackendlessCollection<Tour>> callback = new AsyncCallback<BackendlessCollection<Tour>>() {
            public void handleResponse(BackendlessCollection<Tour> toursList) {
                tours = toursList.getCurrentPage();
                Log.d("tours retr", toursList.toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (listView != null) {
                    adapter = new ToursFeedAdapter(getActivity(), tours);
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

        if (sortOptions != null)

        {
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

    private void showDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Загружаю фото");
        dialog.setMessage("Пожалуйста, подождите");
        dialog.show();
    }

    public interface OnTourFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTourFragmentInteraction(Tour tour);
    }
}

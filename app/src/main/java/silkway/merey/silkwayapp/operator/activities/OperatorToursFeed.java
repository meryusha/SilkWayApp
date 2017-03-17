package silkway.merey.silkwayapp.operator.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import silkway.merey.silkwayapp.agent.adapters.ToursFeedAdapter;
import silkway.merey.silkwayapp.classes.Category;
import silkway.merey.silkwayapp.classes.Location;
import silkway.merey.silkwayapp.classes.Tour;

public class OperatorToursFeed extends AppCompatActivity {
    private List<Tour> tours = new ArrayList<>();
    //buttons
    private Button sortByPriceButton;
    private Button sortByTopButton;
    private Button sortByDateButton;

    private ListView listView;
    private ToursFeedAdapter adapter;
    private TabLayout tabLayout;
    private SearchView searchView;
    private Spinner locationSpinner;
    private Spinner categorySpinner;
    private Toolbar toolbar;
    private boolean topUp = true;
    private boolean priceUp = true;
    private boolean dateUp = true;
    private ProgressDialog dialog;
    private List<Category> categories;
    private List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_tours_feed);
        initViews();
    }


    private void initViews() {
        initListView();
        setToolbar();
        initTabs();
        initSearchView();
        initSpinners();
        initSort();
    }


    private void initSort() {
        sortByDateButton = (Button) findViewById(R.id.sortByDate);
        sortByPriceButton = (Button) findViewById(R.id.sortByPrice);
        sortByTopButton = (Button) findViewById(R.id.sortByTop);

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

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Туры");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initSpinners() {
        categorySpinner = (Spinner) findViewById(R.id.typeSpinner);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        generateSpinnerArrays();

    }


    private void initSearchView() {
        searchView = (SearchView) findViewById(R.id.searchView);
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

    private void initTabs() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Все"));
        tabLayout.addTab(tabLayout.newTab().setText("Подписанные"));
        tabLayout.addTab(tabLayout.newTab().setText("В перегововрах"));
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

    private void initListView() {

        listView = (ListView) findViewById(R.id.listView);
        getTours(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ontItemClicked(position);

            }
        });

    }

    private void generateSpinnerArrays() {
        Backendless.Data.of(Category.class).find(new AsyncCallback<BackendlessCollection<Category>>() {
            @Override
            public void handleResponse(BackendlessCollection<Category> response) {
                categories = response.getCurrentPage();
                categorySpinner.setAdapter(new CategoryAdapter(OperatorToursFeed.this, android.R.layout.simple_spinner_dropdown_item, categories));

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        Backendless.Data.of(Location.class).find(new AsyncCallback<BackendlessCollection<Location>>() {
            @Override
            public void handleResponse(BackendlessCollection<Location> response) {
                locations = response.getCurrentPage();
                locationSpinner.setAdapter(new LocationAdapter(OperatorToursFeed.this, android.R.layout.simple_spinner_dropdown_item, locations));
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
                    adapter = new ToursFeedAdapter(OperatorToursFeed.this, tours);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("error", backendlessFault.getMessage());
                new AlertDialog.Builder(OperatorToursFeed.this)
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

    private void ontItemClicked(int position) {
        DataManager.getInstance().setCurrentTour(tours.get(position));
        Intent intent = new Intent(this, TourDetailsActivityOperator.class);
        startActivity(intent);


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Загружаю фото");
        dialog.setMessage("Пожалуйста, подождите");
        dialog.show();
    }
}

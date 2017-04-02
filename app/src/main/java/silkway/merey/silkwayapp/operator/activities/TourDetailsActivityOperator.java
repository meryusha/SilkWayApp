package silkway.merey.silkwayapp.operator.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.bumptech.glide.Glide;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.DataManager;
import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.agent.activities.AgentOfferActivity;
import silkway.merey.silkwayapp.agent.activities.TourEditActivity;
import silkway.merey.silkwayapp.agent.adapters.AgentViewOfferListViewAdapter;
import silkway.merey.silkwayapp.agent.adapters.SlidingImagesAdapter;
import silkway.merey.silkwayapp.agent.adapters.TimetableTabsAdapter;
import silkway.merey.silkwayapp.classes.Constants;
import silkway.merey.silkwayapp.classes.TimeInstance;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.classes.TourPhoto;
import silkway.merey.silkwayapp.classes.TourProposal;

public class TourDetailsActivityOperator extends AppCompatActivity {
    private Tour tour;
    private ViewPager pager;
    private Toolbar toolbar;
    private Button toogleButtonOffers;
    private TabLayout tabLayout;
    private List<TourProposal> tourProposals = new ArrayList<>();
    private List<TimeInstance> slots = new ArrayList<>();
    private ExpandableRelativeLayout expandableRelativeLayoutOffers;
    private ProgressDialog dialog;
    private TimetableTabsAdapter adapter;
    private ListView offersListView;
    private ViewPager timetableViewPager;
    private List<TourPhoto> tourImages = new ArrayList<>();
    private TextView authorNameTextView;
    private ImageView authorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details_operator);
        DataManager.getInstance().checkIfUserIsLoggedIn(this);
        tour = DataManager.getInstance().getCurrentTour();
        if (tour == null) {
            finish();
        }
        initViews();


    }

    private void initViews() {

        initAuthorInfo();
        setToolbar();
        initPhotos();
        initDetails();
        initTimetable();
        //  initStatistics();
        initOffers();

    }

    private void initAuthorInfo() {
        authorImageView = (ImageView) findViewById(R.id.profile_image);
        authorNameTextView = (TextView) findViewById(R.id.profileName);
        Glide.with(this).load(tour.getAuthor().getProperty("avatarUrl")).centerCrop().into(authorImageView);
        authorNameTextView.setText("" + tour.getAuthor().getProperty("name"));
    }

    private void initOffers() {
        toogleButtonOffers = (Button) findViewById(R.id.toogleButtonOffers);
        expandableRelativeLayoutOffers = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutOffers);
        expandableRelativeLayoutOffers.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                toogleButtonOffers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
            }

            @Override
            public void onPreClose() {
                toogleButtonOffers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_left_black_24dp, 0);
            }
        });
        offersListView = (ListView) findViewById(R.id.offersListView);
        offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewClicked(position);
            }
        });
        getOffers();
    }

    private void initTimetable() {
        //arrayList
        final AsyncCallback<BackendlessCollection<TimeInstance>> callback = new AsyncCallback<BackendlessCollection<TimeInstance>>() {
            public void handleResponse(BackendlessCollection<TimeInstance> response) {

                slots = response.getCurrentPage();
                //   Log.d("someERRRROR", slots.size() + "");
                adapter = new TimetableTabsAdapter(getSupportFragmentManager());
                timetableViewPager = (ViewPager) findViewById(R.id.viewpager);
                timetableViewPager.setAdapter(adapter);
                timetableViewPager.setOffscreenPageLimit(Constants.MAX_ADDED_DAYS);
                tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(timetableViewPager);
                adapter.buildTimetable(slots, tabLayout);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("someERRRROR", backendlessFault.getMessage());
                DataManager.getInstance().showError(TourDetailsActivityOperator.this);

            }
        };
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        String whereClause = "tour.objectId = '" + DataManager.getInstance().getCurrentTour().getObjectId() + "'";
        dataQuery.setWhereClause(whereClause);
        // showDialog(getResources().getString(R.string.dialogTitleLoadingInfo), getResources().getString(R.string.dialogMessage));
        Backendless.Persistence.of(TimeInstance.class).find(dataQuery, callback);
    }

    private void initDetails() {
        TextView tourNameTextView = (TextView) findViewById(R.id.tourNameTextView);
        TextView tourDescriptionTextView = (TextView) findViewById(R.id.tourDescriptionTextView);
        TextView tourLocationTextView = (TextView) findViewById(R.id.tourLocationTextView);
        TextView tourNumberPeopleTextView = (TextView) findViewById(R.id.tourNumberPeopleTextView);
        TextView tourPriceTextView = (TextView) findViewById(R.id.tourPriceTextView);
        TextView tourRequirementsTextView = (TextView) findViewById(R.id.tourRequirementsTextView);
        TextView tourDurationTextView = (TextView) findViewById(R.id.tourDurationTextView);

        tourNameTextView.setText("" + tour.getTitle());
        tourDescriptionTextView.setText("" + tour.getDesc());
        tourLocationTextView.setText("Локация: " + tour.getLocation().getName());
        tourDurationTextView.setText("Продолжительность тура: " + tour.getDuration());
        tourRequirementsTextView.setText("Требования: " + tour.getRequirements());
        tourPriceTextView.setText("Цена: " + tour.getPrice());
        tourNumberPeopleTextView.setText("Количество участников: " + tour.getCapacity());
    }

    private void initPhotos() {
        final AsyncCallback<BackendlessCollection<TourPhoto>> callback = new AsyncCallback<BackendlessCollection<TourPhoto>>() {
            public void handleResponse(BackendlessCollection<TourPhoto> response) {
                tourImages = response.getCurrentPage();
                DataManager.getInstance().setTourImages(tourImages);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                initViewPager();
                //  offersListView.setAdapter(new AgentViewOfferListViewAdapter(TourDetailsActivity.this, tourImages));
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("error", backendlessFault.getMessage());

            }
        };
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        String whereClause = "tour.objectId = '" + DataManager.getInstance().getCurrentTour().getObjectId() + "'";
        dataQuery.setWhereClause(whereClause);
        showDialog(getResources().getString(R.string.dialogTitleLoadingInfo), getResources().getString(R.string.dialogMessage));
        Backendless.Persistence.of(TourPhoto.class).find(dataQuery, callback);
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Тур");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViewPager() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SlidingImagesAdapter(this, tourImages));
        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void getOffers() {
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, this.getResources().getDisplayMetrics());
        final AsyncCallback<BackendlessCollection<TourProposal>> callback = new AsyncCallback<BackendlessCollection<TourProposal>>() {
            public void handleResponse(BackendlessCollection<TourProposal> response) {
                tourProposals = response.getCurrentPage();
                ViewGroup.LayoutParams params = offersListView.getLayoutParams();
                params.height = tourProposals.size() * height;
                offersListView.setLayoutParams(params);
                offersListView.requestLayout();
                offersListView.setAdapter(new AgentViewOfferListViewAdapter(TourDetailsActivityOperator.this, tourProposals));
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("error", backendlessFault.getMessage());
                DataManager.getInstance().showError(TourDetailsActivityOperator.this);

            }
        };
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        String whereClause = "tour.objectId = '" + DataManager.getInstance().getCurrentTour().getObjectId() + "'";
        dataQuery.setWhereClause(whereClause);
        Backendless.Persistence.of(TourProposal.class).find(dataQuery, callback);


    }

    public void onClickToogleOffers(View view) {

        expandableRelativeLayoutOffers.toggle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.account_settings:
                Intent intent = new Intent(this, TourEditActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void onListViewClicked(int position) {
        DataManager.getInstance().setCurrentTourProposal(tourProposals.get(position));
        Intent intent = new Intent(this, AgentOfferActivity.class);
        startActivity(intent);
    }

    private void showDialog(String title, String message) {
        dialog = new ProgressDialog(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
}

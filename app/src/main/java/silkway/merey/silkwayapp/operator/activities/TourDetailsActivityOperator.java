package silkway.merey.silkwayapp.operator.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import silkway.merey.silkwayapp.agent.adapters.SlidingImagesAdapter;
import silkway.merey.silkwayapp.agent.adapters.TimetableTabsAdapter;
import silkway.merey.silkwayapp.classes.TimeInstance;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.classes.TourPhoto;

public class TourDetailsActivityOperator extends AppCompatActivity {
    private Tour tour;
    private ViewPager pager;
    private Toolbar toolbar;
    private Button toogleButtonOffers;
    private TabLayout tabLayout;
    private final int MAX_ADDED_DAYS = 10;
    private ExpandableRelativeLayout expandableRelativeLayoutOffers;
    private ProgressDialog dialog;
    private ViewPager timetableViewPager;
    private List<TourPhoto> tourImages = new ArrayList<>();
    private TextView authorNameTextView;
    private ImageView authorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details_operator);
        initViews();

    }

    private void initViews() {
        tour = DataManager.getInstance().getCurrentTour();
        initAuthorInfo();
        setToolbar();
        initPhotos();
        initDetails();
        //  initSchedule();
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
    }

    private void initTimetable() {

        //arrayList
        List<TimeInstance> slots = new ArrayList<>();
        slots.add(new TimeInstance(11, 30, 12, 00, "Сбор"));
        slots.add(new TimeInstance(13, 00, 14, 00, "Обед"));
        slots.add(new TimeInstance(18, 30, 19, 00, "Отъезд"));
        TimetableTabsAdapter adapter = new TimetableTabsAdapter(getSupportFragmentManager());
        timetableViewPager = (ViewPager) findViewById(R.id.viewpager);
        timetableViewPager.setAdapter(adapter);
        timetableViewPager.setOffscreenPageLimit(MAX_ADDED_DAYS);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(timetableViewPager);
        adapter.addFragment(tabLayout, slots);
        adapter.addFragment(tabLayout, slots);

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
        tourDescriptionTextView.setText("" + tour.getDescription());
        tourLocationTextView.setText("Локация: " + tour.getLocation().getName());
        tourDurationTextView.setText("Продолжительность тура: " + tour.getDuration());
        tourRequirementsTextView.setText("Требования: " + tour.getRequirements());
        tourPriceTextView.setText("Цена: " + tour.getPrice());
        tourNumberPeopleTextView.setText("Количество участников: " + tour.getCapacity());
    }

    private void initPhotos() {

        showDialog();
        final AsyncCallback<BackendlessCollection<TourPhoto>> callback = new AsyncCallback<BackendlessCollection<TourPhoto>>() {
            public void handleResponse(BackendlessCollection<TourPhoto> response) {
                tourImages = response.getCurrentPage();
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
        Backendless.Persistence.of(TourPhoto.class).find(dataQuery, callback);
    }

    private void onListViewClicked(int position) {
        Intent intent = new Intent(this, AgentOfferActivity.class);
        startActivity(intent);
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

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Загружаю фото");
        dialog.setMessage("Пожалуйста, подождите");
        dialog.show();
    }
}

package silkway.merey.silkwayapp.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.adapters.AgentViewOfferListViewAdapter;
import silkway.merey.silkwayapp.adapters.CustomPagerAdapter;
import silkway.merey.silkwayapp.adapters.PhotosListViewAdapter;
import silkway.merey.silkwayapp.classes.TimetableDay;
import silkway.merey.silkwayapp.classes.TourImage;
import silkway.merey.silkwayapp.data.DataHolder;
import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.adapters.SlidingImagesAdapter;
import silkway.merey.silkwayapp.classes.Tour;

public class TourDetailsActivity extends AppCompatActivity {
    private Tour tour;
    private ArrayList<Tour> tours;
    private ViewPager pager;
    private Toolbar toolbar;
    private final Integer[] IMAGES = {R.drawable.nature, R.drawable.nature, R.drawable.nature, R.drawable.nature};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private Button toogleButtonStats;
    private Button toogleButtonSched;
    private Button toogleButtonOffers;
    private ExpandableRelativeLayout expandableRelativeLayoutStats;
    private ExpandableRelativeLayout expandableRelativeLayoutSched;
    private ExpandableRelativeLayout expandableRelativeLayoutOffers;
    private ListView photosListView;
    private ViewPager timetableViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);
        initViews();

    }

    private void initViews() {
        setToolbar();
        initPhotos();
        initDetails();
        initSchedule();
        initTimetable();
        initStatistics();
        initOffers();
        tours = DataHolder.getInstance().getTours();
        initViewPager();


        expandableRelativeLayoutStats.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                toogleButtonStats.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
            }

            @Override
            public void onPreClose() {
                toogleButtonStats.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_left_black_24dp, 0);
            }
        });


    }

    private void initSchedule() {
        expandableRelativeLayoutSched.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                toogleButtonSched.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
            }

            @Override
            public void onPreClose() {
                toogleButtonSched.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_left_black_24dp, 0);
            }
        });
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

    private void initStatistics() {
        toogleButtonStats = (Button) findViewById(R.id.toogleButtonStats);
        expandableRelativeLayoutStats = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutStatistics);
    }

    private void initTimetable() {
        toogleButtonSched = (Button) findViewById(R.id.toogleButtonSched);
        expandableRelativeLayoutSched = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutSchedule);
        //arrayList
        ArrayList<TimetableDay> days = new ArrayList<>();
        days.add(new TimetableDay(1));
        days.add(new TimetableDay(2));
        days.add(new TimetableDay(3));
        timetableViewPager = (ViewPager) findViewById(R.id.viewPager);
        timetableViewPager.setAdapter(new CustomPagerAdapter(this, days));

    }

    private void initDetails() {

    }

    private void initPhotos() {
        //testing
        List<TourImage> tourImages = new ArrayList<TourImage>();
        tourImages.add(new TourImage());
        tourImages.add(new TourImage());
        tourImages.add(new TourImage());
        tourImages.add(new TourImage());
        tourImages.add(new TourImage());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, this.getResources().getDisplayMetrics());

        photosListView = (ListView) findViewById(R.id.offersListView);
        ViewGroup.LayoutParams params = photosListView.getLayoutParams();
        params.height = tourImages.size() * height;
        photosListView.setLayoutParams(params);
        photosListView.requestLayout();
        photosListView.setAdapter(new AgentViewOfferListViewAdapter(this, tourImages));
        photosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewClicked(position);
            }
        });

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
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        pager = (ViewPager) findViewById(R.id.pager);


        pager.setAdapter(new SlidingImagesAdapter(this, ImagesArray, tours));


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

    public void onClickToogleStats(View view) {
        expandableRelativeLayoutStats.toggle();
    }

    public void onClickToogleSched(View view) {

        expandableRelativeLayoutSched.toggle();
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
        }
        return super.onOptionsItemSelected(item);
    }
}

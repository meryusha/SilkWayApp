package silkway.merey.silkwayapp.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.adapters.CustomPagerAdapter;
import silkway.merey.silkwayapp.adapters.PhotosListViewAdapter;
import silkway.merey.silkwayapp.classes.TimetableDay;
import silkway.merey.silkwayapp.classes.TourImage;


public class AgentOfferActivity extends AppCompatActivity {
    private Button toogleButtonPhotos;
    private Button toogleButtonTourDesc;
    private Button toogleButtonTourProgs;
    private Toolbar toolbar;
    private ExpandableRelativeLayout expandableRelativeLayoutPhotos;
    private ExpandableRelativeLayout expandableRelativeLayoutTourDesc;
    private ExpandableRelativeLayout expandableRelativeLayoutTourProgs;
    private ListView photosListView;
    private ViewPager timetableViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_offer);
        setToolbar();
        initViews();

    }

    private void initViews() {
        toogleButtonPhotos = (Button) findViewById(R.id.toogleButtonPhotos);
        toogleButtonTourProgs = (Button) findViewById(R.id.toogleButtonTourProgs);
        toogleButtonTourDesc = (Button) findViewById(R.id.toogleButtonTourDesc);

        expandableRelativeLayoutPhotos = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutPhotos);
        expandableRelativeLayoutTourProgs = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutTourProgs);
        expandableRelativeLayoutTourDesc = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutTourDesc);


        expandableRelativeLayoutTourDesc.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                toogleButtonTourDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
            }

            @Override
            public void onPreClose() {
                toogleButtonTourDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_left_black_24dp, 0);
            }
        });
        expandableRelativeLayoutPhotos.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                toogleButtonPhotos.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
            }

            @Override
            public void onPreClose() {
                toogleButtonPhotos.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_left_black_24dp, 0);
            }
        });
        expandableRelativeLayoutTourProgs.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                toogleButtonTourProgs.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
            }

            @Override
            public void onPreClose() {
                toogleButtonTourProgs.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_left_black_24dp, 0);
            }
        });
        photosListView = (ListView) findViewById(R.id.photosListView);


        //testing
        List<TourImage> tourImages = new ArrayList<TourImage>();
        tourImages.add(new TourImage());
        tourImages.add(new TourImage());
        //tourImages.add(new TourImage());
        // tourImages.add(new TourImage());
        //  tourImages.add(new TourImage());

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, this.getResources().getDisplayMetrics());

        photosListView = (ListView) findViewById(R.id.photosListView);
        ViewGroup.LayoutParams params = photosListView.getLayoutParams();
        params.height = tourImages.size() * height;
        photosListView.setLayoutParams(params);
        photosListView.requestLayout();
        photosListView.setAdapter(new PhotosListViewAdapter(this, tourImages));


        ArrayList<TimetableDay> days = new ArrayList<>();
        days.add(new TimetableDay(1));
        days.add(new TimetableDay(2));
        days.add(new TimetableDay(3));
        timetableViewPager = (ViewPager) findViewById(R.id.viewPager);
        timetableViewPager.setAdapter(new CustomPagerAdapter(this, days));
    }

    private void setToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClickTooglePhotos(View view) {
        expandableRelativeLayoutPhotos.toggle();
    }

    public void onClickToogleTourDesc(View view) {
        expandableRelativeLayoutTourDesc.toggle();
    }

    public void onClickToogleTourProgs(View view) {
        expandableRelativeLayoutTourProgs.toggle();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

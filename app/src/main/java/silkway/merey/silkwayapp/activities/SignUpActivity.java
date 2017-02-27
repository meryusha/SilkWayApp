package silkway.merey.silkwayapp.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.adapters.CustomPagerAdapter;
import silkway.merey.silkwayapp.adapters.SlidingImagesAdapter;
import silkway.merey.silkwayapp.classes.TimetableDay;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.data.DataHolder;

public class SignUpActivity extends AppCompatActivity {
    private ViewPager timetableViewPager;
    private ViewPager pager;
    private ArrayList<Tour> tours;
    private final Integer[] IMAGES = {R.drawable.nature, R.drawable.nature, R.drawable.nature, R.drawable.nature};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViewPager();
    }

    private void initViewPager() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        pager = (ViewPager) findViewById(R.id.pager);

        tours = DataHolder.getInstance().getTours();
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
}

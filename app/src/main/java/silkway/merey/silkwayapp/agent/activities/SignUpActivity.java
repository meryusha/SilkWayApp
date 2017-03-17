package silkway.merey.silkwayapp.agent.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.Tour;

public class SignUpActivity extends AppCompatActivity {
    private ViewPager timetableViewPager;
    private ViewPager pager;
    private List<Tour> tours;
    //private final Integer[] IMAGES = {R.drawable.nature, R.drawable.nature, R.drawable.nature, R.drawable.nature};
    //private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

}

package silkway.merey.silkwayapp.agent.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.agent.adapters.CreateTimetableViewPagerAdapter;
import silkway.merey.silkwayapp.classes.TourPhoto;

public class AgentOfferEditActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    //buttons
    private Button toogleButtonPhotos;
    private Button toogleButtonTourDesc;
    private Button toogleButtonTourProgs;

    // private Button addImageButton;
    private Button addDayButton;
    private Button removeDayButton;
    private LinearLayout layout;
    private int dayCounter = 1;
    //exp layouts
    private ExpandableRelativeLayout expandableRelativeLayoutPhotos;
    private ExpandableRelativeLayout expandableRelativeLayoutTourDesc;
    private ExpandableRelativeLayout expandableRelativeLayoutTourProgs;

    private Toolbar toolbar;
    private ViewPager timetableViewPager;
    private final int MAX_ADDED_DAYS = 10;
    private TabLayout tabLayout;
    private List<View> images;
    private ProgressDialog dialog;
    private EditText addTourEditText;
    private ImageView currentImage;
    private CreateTimetableViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_offer_edit);
        initViews();

    }

    private void initViews() {
        setToolbar();
        initPhotos();
        initTimetable();
        initDescription();
    }

    private void initDescription() {

        toogleButtonTourDesc = (Button) findViewById(R.id.toogleButtonTourDesc);


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
    }

    private void initTimetable() {
        toogleButtonTourProgs = (Button) findViewById(R.id.toogleButtonTourProgs);
        expandableRelativeLayoutTourProgs = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutTourProgs);

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


        addDayButton = (Button) findViewById(R.id.addButton);
        removeDayButton = (Button) findViewById(R.id.removeButton);
        adapter = new CreateTimetableViewPagerAdapter(getSupportFragmentManager());
        timetableViewPager = (ViewPager) findViewById(R.id.viewpager);
        timetableViewPager.setAdapter(adapter);
        timetableViewPager.setOffscreenPageLimit(MAX_ADDED_DAYS);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(timetableViewPager);
        adapter.addFragment(tabLayout);

        addDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayCounter <= MAX_ADDED_DAYS) {
                    adapter.addFragment(tabLayout);
                    dayCounter++;
                }
            }
        });
        removeDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayCounter > 1) {
                    dayCounter--;
                    adapter.removeFragment(tabLayout);
                }
            }
        });

    }

    private void initPhotos() {
        toogleButtonPhotos = (Button) findViewById(R.id.toogleButtonPhotos);
        expandableRelativeLayoutPhotos = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutPhotos);
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


        //testing
        List<TourPhoto> tourImages = new ArrayList<TourPhoto>();
        tourImages.add(new TourPhoto());
        tourImages.add(new TourPhoto());
        //tourImages.add(new TourPhoto());
        // tourImages.add(new TourPhoto());
        //  tourImages.add(new TourPhoto());
        images = new ArrayList<>();
        addImage();
        addImage();


    }

    private void addImage() {
        layout = (LinearLayout) findViewById(R.id.proposal_edit_photo);
        final View child = getLayoutInflater().inflate(R.layout.add_tour_photos, null);
        layout.addView(child);
        images.add(child);
        TextView deleteTextView = (TextView) child.findViewById(R.id.photoDeleteTextView);
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(child);
                images.remove(child);

            }
        });
        final ImageView iv = (ImageView) child.findViewById(R.id.addTourImageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImage = iv;
                loadImage();
            }

        });


        addTourEditText = (EditText) child.findViewById(R.id.addTourEditText);


    }


    private void loadImage() {
        //open media and load photo
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/+");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getIntent.setType("image/+");
        Intent chooseIntent = Intent.createChooser(getIntent, "Выберите фотографию");
        chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooseIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentImage.setImageBitmap(bitmap);
                    dialog = new ProgressDialog(this);
                    dialog.setTitle("Загружаю фотографию");
                    dialog.setMessage("Пожалуйста, подождите");
                    dialog.show();

                }
            }
        }
    }

    private void setToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Изменить предложение");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
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


package silkway.merey.silkwayapp.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.adapters.CreateTimetableViewPagerAdapter;

public class AddTourActivity extends AppCompatActivity {

    //ints
    private static final int PICK_IMAGE = 1;
    private int addedImages = 0;
    private final int MAX_ADDED_IMAGES = 5;
    private int dayCounter = 1;
    private final int MAX_ADDED_DAYS = 10;

    //buttons
    private Button addImageButton;
    private Button addDayButton;
    private Button removeDayButton;

    //Images
    private ImageView currentImage;
    private ImageView addTourImageView;

    //editTexts
    private EditText addTourEditText;

    private Toolbar toolbar;
    private ViewPager timetableViewPager;
    private LinearLayout layout;
    private List<View> images;
    private ProgressDialog dialog;
    private TabLayout tabLayout;
    private CreateTimetableViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        initViews();

    }

    private void initViews() {
        setToolbar();
        initPhotos();
        initVideo();
        initTimetable();
        initInfo();
    }

    private void initInfo() {

    }

    private void initTimetable() {
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


    private void initVideo() {

    }

    private void initPhotos() {
        images = new ArrayList<>();
        addImageButton = (Button) findViewById(R.id.addImageButton);
        addTourImageView = (ImageView) findViewById(R.id.addTourImageView);
        currentImage = addTourImageView;
        addTourImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
        findViewById(R.id.photoDeleteTextView).setEnabled(false);
        findViewById(R.id.photoDeleteTextView).setVisibility(View.INVISIBLE);
        addTourEditText = (EditText) findViewById(R.id.addTourEditText);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddImageClickButton();
            }
        });
        layout = (LinearLayout) findViewById(R.id.photos_layout);
    }

    private void onAddImageClickButton() {
        if (addedImages <= MAX_ADDED_IMAGES) {
            addedImages++;
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
        } else {
            //show toast
            //    Toast.makeText("Не более 5 фотографий!").show();
        }
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
        toolbarTextView.setText("Создать тур");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


}

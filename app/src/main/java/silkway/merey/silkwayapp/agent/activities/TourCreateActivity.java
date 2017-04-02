package silkway.merey.silkwayapp.agent.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.DataManager;
import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.agent.adapters.CreateTimetableViewPagerAdapter;
import silkway.merey.silkwayapp.classes.Constants;
import silkway.merey.silkwayapp.classes.Location;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.classes.TourPhoto;

public class TourCreateActivity extends AppCompatActivity {

    //ints

    private int dayCounter = 1;

    //buttons
    private Button addImageButton;
    private Button addDayButton;
    private Button removeDayButton;
    private Button createTourButton;
    private Button cancelButton;
    private BackendlessFile backendlessFile;
    //Images
    private ImageView currentImage;
    private ImageView addTourImageView;

    //editTexts
    private EditText tourNameEditText;
    private EditText tourDescriptionEditText;
    private EditText tourPriceEditText;
    private EditText tourLocationEditText;
    private EditText tourNumberPeopleEditText;
    private EditText tourDurationEditText;
    private EditText tourRequirementsEditText;
    private EditText tourSeasonEditText;

    private Toolbar toolbar;
    private Bitmap mainBitmap;
    private ViewPager timetableViewPager;
    private LinearLayout layout;
    private List<View> images;
    private ProgressDialog dialog;
    private TabLayout tabLayout;
    private CreateTimetableViewPagerAdapter adapter;
    private Tour tour;
    private BackendlessUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        initViews();
        DataManager.getInstance().checkIfUserIsLoggedIn(this);

    }

    private void initViews() {
        setToolbar();
        initPhotos();
        initVideo();
        initTimetable();
        initInfo();
        initButtons();
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

    private void initPhotos() {
        images = new ArrayList<>();
        layout = (LinearLayout) findViewById(R.id.photos_layout);

        initMainPhoto();
        addImageButton = (Button) findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageButtonOnClick();
            }
        });

    }

    private void initMainPhoto() {
        addTourImageView = (ImageView) findViewById(R.id.addTourImageView);
        currentImage = addTourImageView;

        addTourImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageFromMedia(Constants.PICK_MAIN_IMAGE);
            }
        });
        mainBitmap = null;
        findViewById(R.id.photoDeleteTextView).setEnabled(false);
        findViewById(R.id.photoDeleteTextView).setVisibility(View.INVISIBLE);
        findViewById(R.id.addTourEditText).setEnabled(false);
        findViewById(R.id.addTourEditText).setVisibility(View.INVISIBLE);

    }


    private void initVideo() {

    }


    private void initTimetable() {
        addDayButton = (Button) findViewById(R.id.addButton);
        removeDayButton = (Button) findViewById(R.id.removeButton);
        adapter = new CreateTimetableViewPagerAdapter(getSupportFragmentManager());
        timetableViewPager = (ViewPager) findViewById(R.id.viewpager);
        timetableViewPager.setAdapter(adapter);
        timetableViewPager.setOffscreenPageLimit(Constants.MAX_ADDED_DAYS);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(timetableViewPager);
        adapter.addFragment(tabLayout);

        addDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayCounter <= Constants.MAX_ADDED_DAYS) {
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

    private void initInfo() {
        tourDescriptionEditText = (EditText) findViewById(R.id.tourDescriptionEditText);
        tourSeasonEditText = (EditText) findViewById(R.id.tourSeasonEditText);
        tourDurationEditText = (EditText) findViewById(R.id.tourDurationEditText);
        tourLocationEditText = (EditText) findViewById(R.id.tourLocationEditText);
        tourNumberPeopleEditText = (EditText) findViewById(R.id.tourNumberPeopleEditText);
        tourPriceEditText = (EditText) findViewById(R.id.tourPriceEditText);
        tourNameEditText = (EditText) findViewById(R.id.tourNameEditText);
        tourRequirementsEditText = (EditText) findViewById(R.id.tourRequirementsEditText);

    }

    private void initButtons() {
        createTourButton = (Button) findViewById(R.id.createTourButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        createTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTour();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonOnClick();
            }
        });

    }


    //additional methods

    //close activity
    private void cancelButtonOnClick() {
        finish();
    }


    private void addImageButtonOnClick() {
        if (images.size() <= Constants.MAX_ADDED_IMAGES) {
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
                    loadImageFromMedia(Constants.PICK_IMAGE);
                }

            });
        } else {
            //show toast
            //    Toast.makeText("Не более 5 фотографий!").show();
        }
    }


    private void loadImageFromMedia(int code) {
        //open media and load photo
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/+");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getIntent.setType("image/+");
        Intent chooseIntent = Intent.createChooser(getIntent, "Выберите фотографию");
        chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooseIntent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_IMAGE || requestCode == Constants.PICK_MAIN_IMAGE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        if (requestCode == Constants.PICK_MAIN_IMAGE) {
                            mainBitmap = bitmap;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentImage.setImageBitmap(bitmap);


                }
            }
        }

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void createTour() {
        //createTourSaveInfo();
        user = Backendless.UserService.CurrentUser();
        String name = tourNameEditText.getText().toString();
        String description = tourDescriptionEditText.getText().toString();
        String locationName = tourLocationEditText.getText().toString();
        String numberPeople = tourNumberPeopleEditText.getText().toString();
        String price = tourPriceEditText.getText().toString();
        String duration = tourDurationEditText.getText().toString();
        String requirements = tourRequirementsEditText.getText().toString();
        String season = tourSeasonEditText.getText().toString();

        boolean digitsOnly = TextUtils.isDigitsOnly(price);
        if (TextUtils.isEmpty(name)) {
            tourNameEditText.setError("Введите название");
            return;
        }
        if (!digitsOnly || TextUtils.isEmpty(price)) {
            tourPriceEditText.setError("Введите цену в числовом формате");
            return;
        }
        if (TextUtils.isEmpty(locationName)) {
            tourLocationEditText.setError("Введите расположение");
            return;
        }
        digitsOnly = TextUtils.isDigitsOnly(numberPeople);
        if (!digitsOnly || TextUtils.isEmpty(numberPeople)) {
            tourNumberPeopleEditText.setError("Введите количество");
            return;
        }
        if (TextUtils.isEmpty(duration)) {
            tourDurationEditText.setError("Введите прододжительность");
            return;
        }


        Location location = new Location(locationName);
        int index = DataManager.getInstance().checkIfLocationExists(location);
        if (index >= 0) {
            location = DataManager.getInstance().getLocations().get(index);
        }

        tour = new Tour();
        tour.setPrice(Integer.parseInt(price));
        tour.setLocation(location);
        tour.setRequirements(requirements);
        tour.setAuthor(user);
        tour.setDuration(duration);
        tour.setDesc(description);
        tour.setCapacity(Integer.parseInt(numberPeople));
        tour.setTitle(name);
        tour.setSeason(season);
        updateMainImage();

    }

    private void updateMainImage() {
        if (mainBitmap != null) {
            Backendless.Files.Android.upload(mainBitmap, Bitmap.CompressFormat.JPEG, 100, "item" + user.getObjectId() + System.currentTimeMillis(), "items", new AsyncCallback<BackendlessFile>() {
                @Override
                public void handleResponse(BackendlessFile response) {
                    Log.d("Add Post", "uploaded file");
                    backendlessFile = response;
                    tour.setAvatarUrl(backendlessFile.getFileURL());
                    showDialog(getResources().getString(R.string.dialogTitleSavingInfo), getResources().getString(R.string.dialogMessage));
                    saveTour();

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.e("AddPost", "error uploading file" + fault.getMessage());
                    DataManager.getInstance().showError(TourCreateActivity.this);
                }
            });
        } else {
            showDialog(getResources().getString(R.string.dialogTitleSavingInfo), getResources().getString(R.string.dialogMessage));
            saveTour();

        }
    }

    private void saveTour() {
        Backendless.Persistence.of(Tour.class).save(tour, new AsyncCallback<Tour>() {
            @Override
            public void handleResponse(Tour response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                savePhotos();
                adapter.storeFragmentList(tour);
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("CreateTour", "Failed to post" + fault.getMessage());
                DataManager.getInstance().showError(TourCreateActivity.this);

            }
        });
    }


    private void savePhotos() {
        showDialog(getResources().getString(R.string.dialogTitleLoadingPhoto), getResources().getString(R.string.dialogMessage));
        for (int i = 0; i < images.size(); i++) {
            View v = images.get(i);
            ImageView iv = (ImageView) v.findViewById(R.id.addTourImageView);
            Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
            uploadImage(bitmap, v);
        }

    }

    private void uploadImage(Bitmap bitmap, final View v) {
        user = Backendless.UserService.CurrentUser();
        Backendless.Files.Android.upload(bitmap, Bitmap.CompressFormat.JPEG, 100, "item" + user.getObjectId() + System.currentTimeMillis(), "items", new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
                Log.d("Add Post", "uploaded file");
                backendlessFile = response;
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                EditText descriptionEditText = (EditText) v.findViewById(R.id.addTourEditText);
                String description = descriptionEditText.getText().toString();
                TourPhoto photo = new TourPhoto(tour, backendlessFile.getFileURL(), description);
                savePhoto(photo);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("AddPost", "error uploading file" + fault.getMessage());
                DataManager.getInstance().showError(TourCreateActivity.this);
            }
        });
    }

    private void savePhoto(TourPhoto photo) {
        Backendless.Persistence.of(TourPhoto.class).save(photo, new AsyncCallback<TourPhoto>() {
            @Override
            public void handleResponse(TourPhoto response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                DataManager.getInstance().showError(TourCreateActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    private void showDialog(String title, String message) {
        dialog = new ProgressDialog(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
}
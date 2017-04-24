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
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.DataManager;
import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.agent.adapters.CreateTimetableViewPagerAdapter;
import silkway.merey.silkwayapp.classes.Constants;
import silkway.merey.silkwayapp.classes.Location;
import silkway.merey.silkwayapp.classes.TimeInstance;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.classes.TourPhoto;
import silkway.merey.silkwayapp.classes.TourProposal;
import silkway.merey.silkwayapp.classes.TourProposalPhoto;

/**
 * Created by Merey on 17.04.17.
 */

public class TourProposalCreateActivity extends AppCompatActivity {

    //ints
    private int dayCounter = 1;
    private int imageIndex = -1;

    private BackendlessFile backendlessFile;
    //Images
    private ImageView currentImage;

    //editTexts
    private EditText tourNameEditText;
    private EditText tourDescriptionEditText;
    private EditText tourPriceEditText;
    private EditText tourLocationEditText;
    private EditText tourNumberPeopleEditText;
    private EditText tourDurationEditText;
    private EditText tourRequirementsEditText;
    private EditText tourSeasonEditText;

    //lists
    private List<TourPhoto> tourImages;
    // private List<TourProposalPhoto> tourProposalImages;
    private List<View> imageViews;
    private List<Boolean> imageBoolList;

    private TourProposal newTourProposal;
    private LinearLayout layout;
    private ProgressDialog dialog;
    private Bitmap mainBitmap;
    private TabLayout tabLayout;
    private CreateTimetableViewPagerAdapter adapter;
    private Tour tour;

    private List<TimeInstance> timeInstances;
    private BackendlessUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_edit);
        DataManager.getInstance().checkIfUserIsLoggedIn(this);
        tour = DataManager.getInstance().getCurrentTour();

        if (tour == null) {
            finish();
        }
        initViews();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Редактировать тур");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void initPhotos() {
        imageViews = new ArrayList<>();
        imageBoolList = new ArrayList<>();
        layout = (LinearLayout) findViewById(R.id.photos_layout);

        //load avatar
        initMainPhoto();

        //if we want to add new images
        Button addImageButton = (Button) findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddImageClickButton(null);
            }
        });


        //loads already existing images with description
        tourImages = DataManager.getInstance().getTourImages();
        //  tourProposalImages = new ArrayList<>();
        if (tourImages != null) {
            for (int i = 0; i < tourImages.size(); i++) {
                onAddImageClickButton(tourImages.get(i));
            }
        } else {
            tourImages = new ArrayList<>();
        }
    }

    private void initMainPhoto() {
        mainBitmap = null;
        ImageView addTourImageView = (ImageView) findViewById(R.id.addTourImageView);
        Glide.with(this).load(tour.getAvatarUrl()).centerCrop().into(addTourImageView);
        currentImage = addTourImageView;
        addTourImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageFromMedia(Constants.PICK_MAIN_IMAGE, -1);
            }
        });
        findViewById(R.id.photoDeleteTextView).setEnabled(false);
        findViewById(R.id.photoDeleteTextView).setVisibility(View.INVISIBLE);
        findViewById(R.id.addTourEditText).setEnabled(false);
        findViewById(R.id.addTourEditText).setVisibility(View.INVISIBLE);

    }


    private void initVideo() {
        // tour = DataManager.getInstance().getCurrentTour();
    }


    private void initTimetable() {
        Button addDayButton = (Button) findViewById(R.id.addButton);
        Button removeDayButton = (Button) findViewById(R.id.removeButton);
        adapter = new CreateTimetableViewPagerAdapter(getSupportFragmentManager());
        ViewPager timetableViewPager = (ViewPager) findViewById(R.id.viewpager);
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
        tourDescriptionEditText.setText(tour.getDesc());

        tourSeasonEditText = (EditText) findViewById(R.id.tourSeasonEditText);
        tourSeasonEditText.setText(tour.getSeason());

        tourDurationEditText = (EditText) findViewById(R.id.tourDurationEditText);
        tourDurationEditText.setText(tour.getDuration());

        tourLocationEditText = (EditText) findViewById(R.id.tourLocationEditText);
        tourLocationEditText.setText(tour.getLocation().getName());

        tourNumberPeopleEditText = (EditText) findViewById(R.id.tourNumberPeopleEditText);
        tourNumberPeopleEditText.setText(tour.getCapacity() + "");

        tourPriceEditText = (EditText) findViewById(R.id.tourPriceEditText);
        tourPriceEditText.setText(tour.getPrice() + "");

        tourNameEditText = (EditText) findViewById(R.id.tourNameEditText);
        tourNameEditText.setText(tour.getTitle());

        tourRequirementsEditText = (EditText) findViewById(R.id.tourRequirementsEditText);
        tourRequirementsEditText.setText(tour.getRequirements());

    }


    private void initButtons() {
        Button saveTourButton = (Button) findViewById(R.id.saveTourButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        saveTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTour();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

    }

    private void cancel() {
        finish();
    }


    private void onAddImageClickButton(final TourPhoto tourPhoto) {
        if (imageViews.size() <= Constants.MAX_ADDED_IMAGES) {
            final View child = getLayoutInflater().inflate(R.layout.add_tour_photos, null);
            layout.addView(child);
            imageViews.add(child);
            imageBoolList.add(false);
            TextView deleteTextView = (TextView) child.findViewById(R.id.photoDeleteTextView);
            deleteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tourPhoto != null) {
                        deleteImage(tourPhoto);
                        tourImages.remove(tourPhoto);
                    }
                    final int index = imageViews.indexOf(child);
                    imageViews.remove(child);
                    if (index < imageBoolList.size()) {
                        imageBoolList.remove(index);
                    }
                    layout.removeView(child);
                }
            });

            final ImageView iv = (ImageView) child.findViewById(R.id.addTourImageView);
            if (tourPhoto != null) {
                Glide.with(this).load(tourPhoto.getUrl()).centerCrop().into(iv);
                EditText descriptionEditText = (EditText) child.findViewById(R.id.addTourEditText);
                descriptionEditText.setText(tourPhoto.getDesc());
            }
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentImage = iv;
                    final int index = imageViews.indexOf(child);
                    imageIndex = index;
                    loadImageFromMedia(Constants.PICK_IMAGE, index);
                }
            });
        } else {
            //show toast
            //    Toast.makeText("Не более 5 фотографий!").show();
        }
    }

    private void deleteImage(TourPhoto tourPhoto) {
        showDialog(getResources().getString(R.string.dialogTitleSavingInfo), getResources().getString(R.string.dialogMessage));
        Backendless.Persistence.of(TourPhoto.class).remove(tourPhoto,
                new AsyncCallback<Long>() {
                    public void handleResponse(Long response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    public void handleFault(BackendlessFault fault) {
                        // an error has occurred, the error code can be
                        // retrieved with fault.getCode()
                        DataManager.getInstance().showError(TourProposalCreateActivity.this);
                    }
                });
    }


    private void loadImageFromMedia(int code, int index) {
        //open media and load photo
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/+");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getIntent.setType("image/+");
        Intent chooseIntent = Intent.createChooser(getIntent, "Выберите фотографию");
        chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        //   chooseIntent.putExtra("index", index);
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

                        //   int index = data.getIntExtra("index", -1);
                        int index = imageIndex;
                        Log.d("HERE IS INDEX", index + "");
                        if (index >= 0 && index < imageBoolList.size()) {
                            imageBoolList.remove(index);
                            imageBoolList.add(index, true);
                        }
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


    private void saveTour() {
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
        newTourProposal = new TourProposal();
        newTourProposal.setTour(tour);
        newTourProposal.setCategory(tour.getCategory());
        newTourProposal.setFrom(Backendless.UserService.CurrentUser());
        newTourProposal.setTo(tour.getAuthor());
        newTourProposal.setCapacity(Integer.parseInt(numberPeople));
        newTourProposal.setTitle(name);
        newTourProposal.setDesc(description);
        newTourProposal.setDuration(duration);
        newTourProposal.setRequirements(requirements);
        newTourProposal.setPrice(Integer.parseInt(price));
        newTourProposal.setSeason(season);
        newTourProposal.setLocation(location);
        newTourProposal.setAvatarUrl(tour.getAvatarUrl());
        updateMainImage();


    }

    private void saveTourInBackend() {
        showDialog(getResources().getString(R.string.dialogTitleSavingInfo), getResources().getString(R.string.dialogMessage));
        Backendless.Persistence.of(TourProposal.class).save(newTourProposal, new AsyncCallback<TourProposal>() {
            @Override
            public void handleResponse(TourProposal response) {
                Log.d("Add Post", "EDITED TOUR" + response.getPrice());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                savePhotos();
                adapter.storeFragmentList(tour);
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("TourEdit", "Failed to post" + fault.getMessage());
                DataManager.getInstance().showError(TourProposalCreateActivity.this);
            }
        });

    }

    private void updateMainImage() {
        if (mainBitmap != null) {
            user = Backendless.UserService.CurrentUser();
            Backendless.Files.Android.upload(mainBitmap, Bitmap.CompressFormat.JPEG, 100, "item" + user.getObjectId() + System.currentTimeMillis(), "items", new AsyncCallback<BackendlessFile>() {
                @Override
                public void handleResponse(BackendlessFile response) {
                    Log.d("Add Post", "uploaded file");
                    backendlessFile = response;
                    newTourProposal.setAvatarUrl(backendlessFile.getFileURL());

                    // showDialog(getResources().getString(R.string.dialogTitleSavingInfo), getResources().getString(R.string.dialogMessage));
                    saveTourInBackend();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.e("AddPost", "error uploading file" + fault.getMessage());
                    DataManager.getInstance().showError(TourProposalCreateActivity.this);
                }
            });
        } else {
            saveTourInBackend();
        }
    }


    private void savePhotos() {
        showDialog(getResources().getString(R.string.dialogTitleLoadingPhoto), getResources().getString(R.string.dialogMessage));
        for (int i = 0; i < imageViews.size(); i++) {
            View v = imageViews.get(i);
            ImageView iv = (ImageView) v.findViewById(R.id.addTourImageView);
            EditText descriptionEditText = (EditText) v.findViewById(R.id.addTourEditText);
            String description = descriptionEditText.getText().toString();
            Log.d("NU", imageBoolList.get(i).toString());
            if (imageBoolList.get(i)) {
                Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                uploadImage(bitmap, description, i);
            } else {
                if (i < tourImages.size()) {
                    TourPhoto photo = tourImages.get(i);
                    TourProposalPhoto newPhoto = new TourProposalPhoto();
                    newPhoto.setUrl(photo.getUrl());
                    newPhoto.setDesc(description);
                    newPhoto.setTourProposal(newTourProposal);
                    savePhoto(newPhoto);
                }
            }
        }

    }


    private void uploadImage(Bitmap bitmap, final String description, final int index) {
        user = Backendless.UserService.CurrentUser();
        Backendless.Files.Android.upload(bitmap, Bitmap.CompressFormat.JPEG, 100, "item" + System.currentTimeMillis(), "items", new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(BackendlessFile response) {
                        Log.d("Add Post", "uploaded file");
                        backendlessFile = response;
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        TourProposalPhoto photo = new TourProposalPhoto();
                        photo.setTourProposal(newTourProposal);
                        photo.setDesc(description);
                        photo.setUrl(backendlessFile.getFileURL());
                        savePhoto(photo);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("TourEdit", "error uploading file" + fault.getMessage());
                        DataManager.getInstance().showError(TourProposalCreateActivity.this);
                    }
                }

        );
    }


    private void savePhoto(TourProposalPhoto photo) {
        Backendless.Persistence.of(TourProposalPhoto.class).save(photo, new AsyncCallback<TourProposalPhoto>() {
            @Override
            public void handleResponse(TourProposalPhoto response) {
                Log.d("Saved TourPhoto", "sucess" + response.getDesc());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("PROPOSAL", "fail" + fault.getMessage());
                DataManager.getInstance().showError(TourProposalCreateActivity.this);
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

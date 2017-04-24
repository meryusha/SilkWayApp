package silkway.merey.silkwayapp.agent.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.DataManager;
import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.agent.adapters.PhotosListViewAdapter;
import silkway.merey.silkwayapp.agent.adapters.TimetableTabsAdapter;
import silkway.merey.silkwayapp.classes.Constants;
import silkway.merey.silkwayapp.classes.TimeInstance;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.classes.TourPhoto;
import silkway.merey.silkwayapp.classes.TourProposal;


public class TourProposalActivity extends AppCompatActivity {
    //buttons
    private Button toogleButtonPhotos;
    private Button toogleButtonTourDesc;
    private Button toogleButtonTourProgs;
    private Button editOfferButton;

    //exp layouts
    private ExpandableRelativeLayout expandableRelativeLayoutPhotos;
    private ExpandableRelativeLayout expandableRelativeLayoutTourDesc;
    private ExpandableRelativeLayout expandableRelativeLayoutTourProgs;

    //TextViews
    private TextView proposalAuthorNameTextView;
    private TextView proposalAuthorDescriptionTextView;

    //lists
    private List<TourPhoto> tourImages = new ArrayList<>();

    private ProgressDialog dialog;
    private Toolbar toolbar;
    private ListView photosListView;
    private ViewPager timetableViewPager;
    private ImageView proposalAuthorImageView;
    private TourProposal tourProposal;
    private Tour tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_offer);
        tourProposal = DataManager.getInstance().getCurrentTourProposal();
        tour = DataManager.getInstance().getCurrentTour();
        initViews();

    }

    private void initViews() {
        initAuthor();
        setToolbar();
        initPhotos();
        initTimetable();
        initDescription();
        initButtons();
    }

    private void initAuthor() {
        proposalAuthorImageView = (ImageView) findViewById(R.id.proposalAuthorImageView);
        proposalAuthorNameTextView = (TextView) findViewById(R.id.proposalAuthorName);
        proposalAuthorDescriptionTextView = (TextView) findViewById(R.id.proposalAuthorDescription);
        Glide.with(this).load(tourProposal.getFrom().getProperty("avatarUrl")).centerCrop().into(proposalAuthorImageView);
        proposalAuthorNameTextView.setText("" + tourProposal.getFrom().getProperty("name"));
        proposalAuthorDescriptionTextView.setText("" + tourProposal.getFrom().getProperty("desc"));
        proposalAuthorDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Предложение");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

        getPhotos();


    }

    private void getPhotos() {

        final AsyncCallback<BackendlessCollection<TourPhoto>> callback = new AsyncCallback<BackendlessCollection<TourPhoto>>() {
            public void handleResponse(BackendlessCollection<TourPhoto> response) {
                tourImages = response.getCurrentPage();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                photosListView = (ListView) findViewById(R.id.photosListView);
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, TourProposalActivity.this.getResources().getDisplayMetrics());

                photosListView = (ListView) findViewById(R.id.photosListView);
                ViewGroup.LayoutParams params = photosListView.getLayoutParams();
                params.height = tourImages.size() * height;
                photosListView.setLayoutParams(params);
                photosListView.requestLayout();
                photosListView.setAdapter(new PhotosListViewAdapter(TourProposalActivity.this, tourImages));
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("error", backendlessFault.getMessage());
                DataManager.getInstance().showError(TourProposalActivity.this);

            }
        };
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        String whereClause = "tour.objectId = '" + DataManager.getInstance().getCurrentTour().getObjectId() + "'";
        dataQuery.setWhereClause(whereClause);
        showDialog(getResources().getString(R.string.dialogTitleLoadingInfo), getResources().getString(R.string.dialogMessage));
        Backendless.Persistence.of(TourPhoto.class).find(dataQuery, callback);
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


        List<TimeInstance> slots = new ArrayList<>();
        slots.add(new TimeInstance(null, 11, 30, 12, 00, "Сбор", 1));
        slots.add(new TimeInstance(null, 13, 00, 14, 00, "Обед", 1));
        slots.add(new TimeInstance(null, 18, 30, 19, 00, "Отъезд", 2));


        TimetableTabsAdapter adapter = new TimetableTabsAdapter(getSupportFragmentManager());
        timetableViewPager = (ViewPager) findViewById(R.id.viewpager);
        timetableViewPager.setAdapter(adapter);
        timetableViewPager.setOffscreenPageLimit(Constants.MAX_ADDED_DAYS);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(timetableViewPager);
        adapter.addFragment(tabLayout, slots);
        adapter.addFragment(tabLayout, slots);
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

        TextView tourNameTextView = (TextView) findViewById(R.id.tourNameTextView);
        TextView tourDescriptionTextView = (TextView) findViewById(R.id.tourDescriptionTextView);
        TextView tourLocationTextView = (TextView) findViewById(R.id.tourLocationTextView);
        TextView tourNumberPeopleTextView = (TextView) findViewById(R.id.tourNumberPeopleTextView);
        TextView tourPriceTextView = (TextView) findViewById(R.id.tourPriceTextView);
        TextView tourRequirementsTextView = (TextView) findViewById(R.id.tourRequirementsTextView);
        TextView tourDurationTextView = (TextView) findViewById(R.id.tourDurationTextView);
        TextView tourSeasonTextView = (TextView) findViewById(R.id.tourSeasonTextView);

        tourNameTextView.setText("" + tourProposal.getTitle());
        tourDescriptionTextView.setText("" + tourProposal.getDesc());
        tourLocationTextView.setText("Локация: " + tourProposal.getLocation().getName());
        tourDurationTextView.setText("Продолжительность тура: " + tourProposal.getDuration());
        tourRequirementsTextView.setText("Требования: " + tourProposal.getRequirements());
        tourPriceTextView.setText("Цена: " + tourProposal.getPrice());
        tourSeasonTextView.setText("Сезон: " + tourProposal.getSeason());
        tourNumberPeopleTextView.setText("Количество участников: " + tourProposal.getCapacity());
    }


    private void initButtons() {
        editOfferButton = (Button) findViewById(R.id.makeOfferButton);
        editOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOffer();
            }
        });
        final Button acceptProposalButton = (Button) findViewById(R.id.acceptOfferButton);
        acceptProposalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptProposal();
            }
        });
    }

    private void acceptProposal() {
        Intent intent = new Intent(this, TermsOfUseActivity.class);
        startActivity(intent);
    }

    private void editOffer() {
        Intent intent = new Intent(this, TourProposalEditActivity.class);
        startActivity(intent);
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

    private void showDialog(String title, String message) {
        dialog = new ProgressDialog(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }


}

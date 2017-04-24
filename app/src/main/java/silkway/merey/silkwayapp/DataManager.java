package silkway.merey.silkwayapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

import silkway.merey.silkwayapp.classes.Category;
import silkway.merey.silkwayapp.classes.Location;
import silkway.merey.silkwayapp.classes.TimeInstance;
import silkway.merey.silkwayapp.classes.Tour;
import silkway.merey.silkwayapp.classes.TourPhoto;
import silkway.merey.silkwayapp.classes.TourProposal;
import silkway.merey.silkwayapp.classes.TourProposalPhoto;

/**
 * Created by Merey on 17.03.17.
 */

public class DataManager {
    private List<Tour> toursList;
    private Tour currentTour;
    private List<TimeInstance> timeInstances;
    private List<Category> categories;
    private List<Location> locations;
    private static final DataManager instance = new DataManager();
    private ProgressDialog dialog;
    private TourProposal currentTourProposal;
    private List<TourPhoto> tourImages;
    private List<TourProposalPhoto> currentTourProposalImages;


    public static DataManager getInstance() {
        return instance;
    }


    public void saveTime(TimeInstance slot) {
        Backendless.Persistence.of(TimeInstance.class).save(slot, new AsyncCallback<TimeInstance>() {
            @Override
            public void handleResponse(TimeInstance response) {

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("SLOTS", "Failed to post" + fault.getMessage());
                //showError();

            }
        });
    }

    public void showError(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Ошибка")
                .setMessage("Пожалуйста, попробуйте еще раз.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void checkIfUserIsLoggedIn(Context context) {
        BackendlessUser user = Backendless.UserService.CurrentUser();
        if (user == null) {
            Intent intent = new Intent(context, StartActivity.class);
            context.startActivity(intent);
        }
    }

    public int checkIfLocationExists(Location location) {
        if (locations != null) {
            for (int i = 0; i < locations.size(); i++) {
                Location loc = locations.get(i);
                if (loc.getName().equals(location.getName())) {
                    return i;
                }
            }
        }
        return -1;
    }


    public TourProposal getCurrentTourProposal() {
        return currentTourProposal;
    }

    public void setCurrentTourProposal(TourProposal currentTourProposal) {
        this.currentTourProposal = currentTourProposal;
    }

    public List<TourPhoto> getTourImages() {
        return tourImages;
    }

    public void setTourImages(List<TourPhoto> tourImages) {
        this.tourImages = tourImages;
    }

    public List<Tour> getToursList() {
        return toursList;
    }

    public void setToursList(List<Tour> toursList) {
        this.toursList = toursList;
    }

    public Tour getCurrentTour() {
        return currentTour;
    }

    public void setCurrentTour(Tour currentTour) {
        this.currentTour = currentTour;
    }

    public List<TimeInstance> getTimeInstances() {
        return timeInstances;
    }

    public void setTimeInstances(List<TimeInstance> timeInstances) {
        this.timeInstances = timeInstances;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<TourProposalPhoto> getCurrentTourProposalImages() {
        return currentTourProposalImages;
    }

    public void setCurrentTourProposalImages(List<TourProposalPhoto> currentTourProposalImages) {
        this.currentTourProposalImages = currentTourProposalImages;
    }
}


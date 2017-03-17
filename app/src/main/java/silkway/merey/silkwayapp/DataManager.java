package silkway.merey.silkwayapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.agent.adapters.ToursFeedAdapter;
import silkway.merey.silkwayapp.agent.fragments.ToursFragment;
import silkway.merey.silkwayapp.classes.TimeInstance;
import silkway.merey.silkwayapp.classes.Tour;

/**
 * Created by Merey on 17.03.17.
 */

public class DataManager {
    private List<Tour> toursList;
    private Tour currentTour;
    private List<TimeInstance> timeInstances;
    private static final DataManager instance = new DataManager();
    private ProgressDialog dialog;
    private ToursFragment.OnTourFragmentInteractionListener mListener;
    private ToursFeedAdapter adapter;

    public static DataManager getInstance() {
        return instance;
    }

    //returns true if successfully created
    public void createTour(Tour tour, final Context context) {
        showDialog(context);
        Backendless.Persistence.of(Tour.class).save(tour, new AsyncCallback<Tour>() {
            @Override
            public void handleResponse(Tour response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("AddPost", "Failed to post" + fault.getMessage());
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
        });

    }

    private void showDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Загружаю фото");
        dialog.setMessage("Пожалуйста, подождите");
        dialog.show();
    }

    public void getTours(String whereClause, final Context context, final ListView listView, final ToursFeedAdapter adapter) {
        showDialog(context);
        toursList = new ArrayList<>();
        final AsyncCallback<BackendlessCollection<Tour>> callback = new AsyncCallback<BackendlessCollection<Tour>>() {
            public void handleResponse(BackendlessCollection<Tour> tours) {
                toursList = tours.getCurrentPage();
                Log.d("tours retr", toursList.toString());
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (listView != null) {
                   // adapter = new ToursFeedAdapter(context, toursList);
                    listView.setAdapter(adapter);
                }


            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
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
        };
        if (whereClause != null)

        {
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setWhereClause(whereClause);
            Backendless.Persistence.of(Tour.class).find(dataQuery, callback);
        } else

        {
            Backendless.Data.of(Tour.class).find(callback);
        }


    }


    public void getTimeInstances(String tourId, final Context context) {
        showDialog(context);
        timeInstances = new ArrayList<>();
        final AsyncCallback<BackendlessCollection<TimeInstance>> callback = new AsyncCallback<BackendlessCollection<TimeInstance>>() {
            public void handleResponse(BackendlessCollection<TimeInstance> slots) {
                timeInstances = slots.getCurrentPage();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
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
        };

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause("tour.objectId = '" + tourId + "'");
        Backendless.Persistence.of(TimeInstance.class).find(dataQuery, callback);

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
}


package silkway.merey.silkwayapp.data;

import java.util.ArrayList;

import silkway.merey.silkwayapp.classes.Tour;

/**
 * Created by Merey on 11.02.17.
 */

public class DataHolder {
    private static DataHolder instance;

    private Tour currentTour;
    private ArrayList<Tour> tours;

    private DataHolder() {

    }

    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public Tour getCurrentTour() {
        return currentTour;
    }

    public void setCurrentTour(Tour currentTour) {
        this.currentTour = currentTour;
    }

    public ArrayList<Tour> getTours() {
        return tours;
    }

    public void setTours(ArrayList<Tour> tours) {
        this.tours = tours;
    }
}

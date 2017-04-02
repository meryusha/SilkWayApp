package silkway.merey.silkwayapp.agent.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.Constants;

public class TourProgramTimetableCreateFragment extends Fragment {

    private static final String DAY = "day";

    // TODO: Rename and change types of parameters
    private int day;
    private Button addTimeSlotButton;
    private LinearLayout layout;
    private List<View> slots;
    //  private List<TimeInstance> timeInstances;
    private static int hours = 0;
    private static int minutes = 0;
    // private static Map<View, TimeInstance> map = new HashMap<>();
    private static TextView currentTextView;
    private Button closeDayButton;
    // private EditText timeDescription;
    //  private static View currentView;


    public TourProgramTimetableCreateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TourProgramTimetableCreateFragment newInstance(int day) {
        TourProgramTimetableCreateFragment fragment = new TourProgramTimetableCreateFragment();
        Bundle args = new Bundle();
        args.putInt(DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getInt(DAY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tour_program_timetable_create, container, false);
        initViews(v);
        return v;
    }

    private void initViews(View v) {
        addTimeSlotButton = (Button) v.findViewById(R.id.addTimeButton);
        layout = (LinearLayout) v.findViewById(R.id.timetableTimeLayout);
        slots = new ArrayList<>();
        //timeInstances = new ArrayList<>();
        addTimeSlotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimeSlot();
            }
        });

    }


    private void addTimeSlot() {
        if (slots.size() <= Constants.MAX_ADDED_SLOTS) {
            final View child = getActivity().getLayoutInflater().inflate(R.layout.timetable_tabs_create_row, null);
            layout.addView(child);
            slots.add(child);
            //   timeInstances.add(new TimeInstance(DataManager.getInstance().getCurrentTour(), ));
            final TextView startTextView = (TextView) child.findViewById(R.id.startTimeTextView);
            startTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickerDialog();
                    Log.d("Merey", "I should go second");
                    currentTextView = startTextView;
                    //  currentView = child;
                }
            });
            final TextView endTextView = (TextView) child.findViewById(R.id.endTimeTextView);
            endTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickerDialog();
                    Log.d("Merey", "I should go second");
                    currentTextView = endTextView;
                    //   currentView = child;
                }
            });
            ImageView removeTimeSlotImageView = (ImageView) child.findViewById(R.id.removeTimeSlotButton);
            removeTimeSlotImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.removeView(child);
                    slots.remove(child);
                    Log.d("here is", slots.size() + "");
                }
            });


        } else {

        }
    }

    private static void setPickedTime() {
        currentTextView.setText(hours + ":" + minutes);
        //   if(map.containsKey(currentView)){
        //     map.get(currentView)
        // }
        //   map.put(currentView, )
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // listener = null;
    }


    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            hours = hourOfDay;
            minutes = minute;
            setPickedTime();
            Log.d("Merey", "I should go first");
        }
    }

    public List<View> getSlots() {
        return slots;
    }

    public int getDay() {
        return day;
    }
}

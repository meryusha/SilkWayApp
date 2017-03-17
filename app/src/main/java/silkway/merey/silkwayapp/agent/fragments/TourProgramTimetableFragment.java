package silkway.merey.silkwayapp.agent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.TimeInstance;


public class TourProgramTimetableFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    // TODO: Rename and change types of parameters
    private List<TimeInstance> slots;
    private ListView listView;
    private OnFragmentInteractionListener mListener;

    public TourProgramTimetableFragment() {
        // Required empty public constructor
    }


    public static TourProgramTimetableFragment newInstance(List<TimeInstance> slots) {
        TourProgramTimetableFragment fragment = new TourProgramTimetableFragment();
        fragment.slots = new ArrayList<TimeInstance>();
        if (slots != null) {
            fragment.slots = slots;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tour_program_timetable, container, false);
        listView = (ListView) v.findViewById(R.id.timetableListView);
        listView.setAdapter(new TimetableAdapter(getActivity(), slots));
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class TimetableAdapter extends BaseAdapter {

        private List<TimeInstance> slots;
        private Context context;
        private LayoutInflater layoutInflater;

        public TimetableAdapter(Context context, List<TimeInstance> slots) {
            this.context = context;
            this.slots = slots;
            if (context != null) {
                layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            }
            if (slots == null) {
                this.slots = new ArrayList<TimeInstance>();
            }
        }

        @Override
        public int getCount() {
            return slots.size();
        }

        @Override
        public Object getItem(int position) {
            return slots.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.timetable_tabs_row, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TimeInstance slot = slots.get(position);
            viewHolder.startTimeTextView.setText(slot.getStartHour() + " : " + slot.getStartMinute());
            viewHolder.endTimeTextView.setText(slot.getEndHour() + " : " + slot.getEndMinute());
            viewHolder.descriptionTextView.setText(slot.getDescription());
            // Log.d(TAG, apartments.get(position).getImage());
            // Glide.with(context).load(images.get(position).getImageURL()).centerCrop().into(viewHolder.categoryImageView);
            return convertView;
        }

        private class ViewHolder {
            TextView startTimeTextView;
            TextView endTimeTextView;
            TextView descriptionTextView;

            public ViewHolder(View v) {
                startTimeTextView = (TextView) v.findViewById(R.id.startTimeTextView);
                endTimeTextView = (TextView) v.findViewById(R.id.endTimeTextView);
                descriptionTextView = (TextView) v.findViewById(R.id.timetableDescriptionTextView);
            }
        }

    }
}

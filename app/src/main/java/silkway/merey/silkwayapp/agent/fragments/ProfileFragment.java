package silkway.merey.silkwayapp.agent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import silkway.merey.silkwayapp.DataManager;
import silkway.merey.silkwayapp.R;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView userNameTextView;
    private TextView userDetailsTextView;
    private CircleImageView imageView;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initToolbar();
        DataManager.getInstance().checkIfUserIsLoggedIn(getActivity());
        BackendlessUser user = Backendless.UserService.CurrentUser();
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        userNameTextView = (TextView) v.findViewById(R.id.userNameTextView);
        userDetailsTextView = (TextView) v.findViewById(R.id.usersDetailsTextView);
        imageView = (CircleImageView) v.findViewById(R.id.profileImageView);
        userNameTextView.setText(user.getProperty("name").toString());
        userDetailsTextView.setText(user.getProperty("desc").toString());
        Glide.with(getActivity()).load(user.getProperty("avatarUrl")).centerCrop().into(imageView);
        return v;
    }

    private void initToolbar() {
        TextView toolbarTextView = (TextView) getActivity().findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Мой Профиль");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
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

}

package com.da.rflsneekrs.mainview;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.da.rflsneekrs.MainActivity;
import com.da.rflsneekrs.MainUnlogActivity;
import com.da.rflsneekrs.R;
import com.da.rflsneekrs.authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  Button logout;
  FirebaseAuth auth;

  public ProfileFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment ProfileFragment.
   */
  // TODO: Rename and change types and number of parameters
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
    ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    auth = FirebaseAuth.getInstance();
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

    if(auth.getCurrentUser() == null){

      Intent intent = new Intent(getActivity(), MainUnlogActivity.class);
      startActivity(intent);

    } else {

      logout = (Button) rootView.findViewById(R.id.logout_btn);
      logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          logout();
        }
      });

    }

    return rootView;
  }

  private void logout() {
    if (auth.getCurrentUser() != null)
      auth.signOut();
    Intent intent = new Intent(getActivity(), MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK));
    startActivity(intent);
  }
}
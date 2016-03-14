package com.project.squirrelobserver.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.GlobalVariables;

import java.util.zip.Inflater;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.project.squirrelobserver.fragments.ImportExportFragment.ImportExportFragmentCallbacks} interface
 * to handle interaction events.
 * Use the {@link ImportExportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImportExportFragment extends Fragment {


    private ImportExportFragmentCallbacks mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BorrowingFragment.
     */
    public static ImportExportFragment newInstance() {

        return new ImportExportFragment();
    }

    public ImportExportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_importexport, container, false);

        // Update file names if paths exist
        if (GlobalVariables.locationCSVPath != null && !GlobalVariables.locationCSVPath.isEmpty()) {
            TextView locationLabel = (TextView) view.findViewById(R.id.locationFileName);
            locationLabel.setText(GlobalVariables.locationCSVPath.substring(
                    GlobalVariables.locationCSVPath.lastIndexOf("/") + 1));
        }
        if (GlobalVariables.actorsCSVPath != null && !GlobalVariables.actorsCSVPath.isEmpty()) {
            TextView actorsLabel = (TextView) view.findViewById(R.id.actorsFileName);
            actorsLabel.setText(GlobalVariables.actorsCSVPath.substring(
                    GlobalVariables.actorsCSVPath.lastIndexOf("/") + 1));
        }
        if (GlobalVariables.behaviorsCSVPath != null && !GlobalVariables.behaviorsCSVPath.isEmpty()) {
            TextView behaviorsLabel = (TextView) view.findViewById(R.id.behaviorsFileName);
            behaviorsLabel.setText(GlobalVariables.behaviorsCSVPath.substring(
                    GlobalVariables.behaviorsCSVPath.lastIndexOf("/") + 1));
            GlobalVariables.aoBehaviors = null;
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onImportExportFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ImportExportFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ImportExportFragmentCallbacks {

        public void onImportExportFragmentInteraction(Uri uri);
    }

}

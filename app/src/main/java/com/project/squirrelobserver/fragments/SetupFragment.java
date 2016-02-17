package com.project.squirrelobserver.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.project.squirrelobserver.R;
import com.project.squirrelobserver.util.Record;
import com.project.squirrelobserver.squirrelObserver.RecordActivity;
import com.project.squirrelobserver.util.GlobalVariables;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.project.squirrelobserver.fragments.SetupFragment.SetupFragmentCallbacks} interface
 * to handle interaction events.
 * Use the {@link SetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public  class SetupFragment
        extends Fragment {


    private SetupFragmentCallbacks mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BorrowingFragment.
     */
    public static SetupFragment newInstance() {

        return new SetupFragment();
    }

    public SetupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.fragment_setup, container, false);
        final Button startButton = (Button) contentView.findViewById(R.id.startButton);
        final EditText editTextX = (EditText) contentView.findViewById(R.id.locationX);
        final EditText editTextY = (EditText) contentView.findViewById(R.id.locationY);
        final EditText editTextID = (EditText) contentView.findViewById(R.id.observerIDInput);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Start Record Activity
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                Record record = new Record(editTextID.getText().toString(), false);

                GlobalVariables.currentRecord = record;
//                intent.putExtra("Record", record);

                getActivity().startActivity(intent);
            }
        });

        editTextX.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                enableStartButtonIfReady(editTextX, editTextY, editTextID, startButton);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        editTextY.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                enableStartButtonIfReady(editTextX, editTextY, editTextID, startButton);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        editTextID.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                enableStartButtonIfReady(editTextX, editTextY, editTextID, startButton);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        // Inflate the layout for this fragment
        return contentView;
    }

    public void enableStartButtonIfReady(final EditText editTextX,
                                         final EditText editTextY,
                                         final EditText editTextID,
                                         final Button startButton) {

        boolean isXFieldReady = !editTextX.getText().toString().isEmpty();
        boolean isYFieldReady = !editTextY.getText().toString().isEmpty();
        boolean isIDFieldReady = !editTextID.getText().toString().isEmpty();

        startButton.setEnabled(isXFieldReady && isYFieldReady && isIDFieldReady);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SetupFragmentCallbacks) activity;
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
    public interface SetupFragmentCallbacks {

        public void onSetupFragmentInteraction(Uri uri);
    }

}

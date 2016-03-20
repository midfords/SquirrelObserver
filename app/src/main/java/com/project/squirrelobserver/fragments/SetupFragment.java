package com.project.squirrelobserver.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private View contentView = null;

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

        contentView = inflater.inflate(R.layout.fragment_setup, container, false);
        final Button startButton = (Button) contentView.findViewById(R.id.startButton);
        final EditText editTextID = (EditText) contentView.findViewById(R.id.observerIDInput);
        final EditText editTextInterval = (EditText) contentView.findViewById(R.id.scanIntervalInput);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Start Record Activity
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                Record record = new Record(editTextID.getText().toString(), false);

                String intervalString = editTextInterval.getText().toString();
                long intervalInMillis;
                int minutes = Integer.parseInt(intervalString.substring(0, intervalString.indexOf(":")));
                int seconds = Integer.parseInt(intervalString.substring(intervalString.indexOf(":")+1));

                intervalInMillis = ((minutes*60) + seconds)*1000;

                Bundle params = new Bundle();
                params.putBoolean("startTimer", true);
                params.putLong("scanInterval", intervalInMillis);
                intent.putExtras(params);

                GlobalVariables.scanInterval = 0;
                GlobalVariables.currentRecord = record;

                getActivity().startActivity(intent);
            }
        });

        editTextID.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                enableStartButtonIfReady(editTextID, startButton);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        // Inflate the layout for this fragment
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (contentView != null) {

            EditText editTextInterval = (EditText) contentView.findViewById(R.id.scanIntervalInput);
            EditText editTextBehaviors = (EditText) contentView.findViewById(R.id.aoBehaviorsInput);

            // Reset text in ao behaviors box
            if (editTextBehaviors != null
                    && GlobalVariables.aoBehaviors != null
                    && !GlobalVariables.aoBehaviors.isEmpty()) {

                String list = "";

                for (int i = 0; i < GlobalVariables.aoBehaviors.size(); i++) {

                    list += GlobalVariables.aoBehaviors.get(i).desc
                            + ((i == GlobalVariables.aoBehaviors.size() - 1)
                            ? "" : ", ");
                }

                editTextBehaviors.setText(list);
            }

            // Reset text in scan interval box
            if (editTextInterval != null) {

                int minute = GlobalVariables.scanIntervalMinutes;
                int second = GlobalVariables.scanIntervalSeconds;

                GlobalVariables.scanIntervalMinutes = minute;
                GlobalVariables.scanIntervalSeconds = second;

                String secondsString = (second < 10 ? "0" : "") + second;
                String minutesString = (minute < 10 ? "0" : "") + minute;
                String timeString = minutesString + ":" + secondsString;

                editTextInterval.setText(timeString);
            }
        }
    }

    public void enableStartButtonIfReady(final EditText editTextID,
                                         final Button startButton) {
        String idText = editTextID.getText().toString();
        boolean isIDFieldReady = true;
        Toast toast;

        if(idText.isEmpty()) {
            isIDFieldReady = false;
        } else {
            for(int i=0; i < idText.length(); i++) {
                if(!Character.isLetterOrDigit(idText.charAt(i))) {
                    isIDFieldReady = false;
                }
            }
            if(!isIDFieldReady) {
                toast = Toast.makeText(getActivity().getApplicationContext(), "Illegal Character in Observer ID", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        startButton.setEnabled(isIDFieldReady);
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

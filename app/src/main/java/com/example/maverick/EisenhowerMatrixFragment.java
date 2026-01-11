package com.example.maverick;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EisenhowerMatrixFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EisenhowerMatrixFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout doFirstList;
    private LinearLayout scheduleList;
    private LinearLayout delegateList;
    private LinearLayout eliminateList;

    private ImageButton btnAddDoFirst;
    private ImageButton btnAddSchedule;
    private ImageButton btnAddDelegate;
    private ImageButton btnAddEliminate;
    //private LayoutInflater inflater;

    private FirebaseDatabase database;
    private DatabaseReference eisenhowerRef;


    public EisenhowerMatrixFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EisenhowerMatrixFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EisenhowerMatrixFragment newInstance(String param1, String param2) {
        EisenhowerMatrixFragment fragment = new EisenhowerMatrixFragment();
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

        database = FirebaseDatabase.getInstance(
                "https://maverick-android-f9433-default-rtdb.asia-southeast1.firebasedatabase.app"
        );

        eisenhowerRef = database.getReference("eisenhowerMatrix");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eisenhower_matrix, container, false);

        handleDoFirstBlock(view);
        handleDelegateBlock(view);
        handleEliminateBlock(view);
        handleScheduleBlock(view);

        loadBlock("doFirst", doFirstList);
        loadBlock("schedule", scheduleList);
        loadBlock("delegate", delegateList);
        loadBlock("eliminate", eliminateList);

        return view;
    }

    private void handleDoFirstBlock(View view) {
        doFirstList = view.findViewById(R.id.do_first_list);
        btnAddDoFirst = view.findViewById(R.id.button_add_do_first);
        btnAddDoFirst.setOnClickListener(v -> addNewRow("doFirst", doFirstList));
    }

    private void handleScheduleBlock(View view) {
        scheduleList = view.findViewById(R.id.schedule_list);
        btnAddSchedule = view.findViewById(R.id.button_add_schedule);
        btnAddSchedule.setOnClickListener(v -> addNewRow("schedule", scheduleList));
    }

    private void handleDelegateBlock(View view) {
        delegateList = view.findViewById(R.id.delegate_list);
        btnAddDelegate = view.findViewById(R.id.button_add_delegate);
        btnAddDelegate.setOnClickListener(v -> addNewRow("delegate", delegateList));
    }

    private void handleEliminateBlock(View view) {
        eliminateList = view.findViewById(R.id.eliminate_list);
        btnAddEliminate = view.findViewById(R.id.button_add_eliminate);
        btnAddEliminate.setOnClickListener(v -> addNewRow("eliminate", eliminateList));
    }

    private void loadBlock(String quadrant, LinearLayout list) {

        eisenhowerRef.child(quadrant)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        list.removeAllViews();

                        for (DataSnapshot taskSnap : snapshot.getChildren()) {

                            String title =
                                    taskSnap.child("title").getValue(String.class);

                            View row = LayoutInflater.from(requireContext())
                                    .inflate(R.layout.eisenhower_row, list, false);
                            ImageButton playButton = row.findViewById(R.id.imageButton3);
                            TextView taskText = row.findViewById(R.id.textView);
                            taskText.setText(title);

                            playButton.setOnClickListener(v2 ->
                                    openPomodoro(taskText.getText().toString())
                            );

                            list.addView(row);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("EISENHOWER", "Load failed", error.toException());
                    }
                });
    }

    private void openPomodoro(String taskTitle) {
        PomodoroFragment fragment = PomodoroFragment.newInstance(taskTitle, null);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }


    private void addNewRow(String block, LinearLayout list) {
        String taskId = eisenhowerRef
                .child(block)
                .push()
                .getKey();


        eisenhowerRef
                .child(block)
                .child(taskId)
                .child("title")
                .setValue("New Task");

        //LayoutInflater inflater = LayoutInflater.from(requireContext());
        //View row = inflater.inflate(R.layout.eisenhower_row, list, false);
        //list.addView(row);
    }
}
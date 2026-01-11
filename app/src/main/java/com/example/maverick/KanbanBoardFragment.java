package com.example.maverick;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KanbanBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KanbanBoardFragment extends Fragment {

    private DatabaseReference kanbanRef;
    private FirebaseDatabase database;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KanbanBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KanbanBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KanbanBoardFragment newInstance(String param1, String param2) {
        KanbanBoardFragment fragment = new KanbanBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance(
                "https://maverick-android-f9433-default-rtdb.asia-southeast1.firebasedatabase.app"
        );

        kanbanRef = database.getReference("kanbanBoards").child("lists");

        kanbanRef = database
                .getReference("kanbanBoards")
                .child("lists");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kanban_board, container, false);

        EditText input = root.findViewById(R.id.enter_new_task_text);
        ImageButton addNewTaskListButton = root.findViewById(R.id.add_new_list_button);
        LinearLayout listContainer = root.findViewById(R.id.task_list);

        kanbanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                listContainer.removeAllViews();

                for (DataSnapshot listSnap : snapshot.getChildren()) {

                    String listId = listSnap.getKey();
                    String title = listSnap.child("title").getValue(String.class);

                    View listView = inflater.inflate(
                            R.layout.kanban_list, listContainer, false);

                    TextView titleView = listView.findViewById(R.id.textView3);
                    titleView.setText(title);

                    listView.setTag(listId);

                    setupList(listView, inflater);

                    LinearLayout taskList =
                            listView.findViewById(R.id.task_list);

                    DataSnapshot tasksSnap = listSnap.child("tasks");

                    for (DataSnapshot taskSnap : tasksSnap.getChildren()) {
                        String taskTitle =
                                taskSnap.child("title").getValue(String.class);

                        View taskView = inflater.inflate(
                                R.layout.eisenhower_row, taskList, false);

                        TextView taskText = taskView.findViewById(R.id.textView);
                        ImageButton playButton =
                                taskView.findViewById(R.id.imageButton3);

                        taskText.setText(taskTitle);

                        playButton.setOnClickListener(v ->
                                openPomodoro(taskTitle));

                        taskList.addView(taskView);
                    }

                    listContainer.addView(listView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FIREBASE", "Load failed", error.toException());
            }
        });


        addNewTaskListButton.setOnClickListener(v -> {
            String title = input.getText().toString().trim();

            if (!title.isEmpty()) {
                String listId = kanbanRef.push().getKey();

                kanbanRef.child(listId).child("title").setValue(title);

                View row = inflater.inflate(R.layout.kanban_list, listContainer, false);
                TextView titleView = row.findViewById(R.id.textView3);
                titleView.setText(title);

                row.setTag(listId);

                setupList(row, inflater);
                listContainer.addView(row);

                input.setText("");
            }
        });


        return root;
    }

    private void setupList(View listView, LayoutInflater inflater) {

        ImageButton addNewTaskButton = listView.findViewById(R.id.add_new_task_button);
        LinearLayout taskList = listView.findViewById(R.id.task_list);

        String listId = (String) listView.getTag();

        addNewTaskButton.setOnClickListener(v -> {

            View row = inflater.inflate(R.layout.eisenhower_row, taskList, false);
            ImageButton playButton = row.findViewById(R.id.imageButton3);
            TextView taskText = row.findViewById(R.id.textView);

            taskText.setText("New Task");

            String taskId = kanbanRef
                    .child(listId)
                    .child("tasks")
                    .push()
                    .getKey();

            kanbanRef
                    .child(listId)
                    .child("tasks")
                    .child(taskId)
                    .child("title")
                    .setValue(taskText.getText().toString());

            playButton.setOnClickListener(v2 ->
                    openPomodoro(taskText.getText().toString())
            );

            taskList.addView(row);
        });
    }


    private void openPomodoro(String taskTitle) {
        PomodoroFragment fragment = PomodoroFragment.newInstance(taskTitle, null);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}
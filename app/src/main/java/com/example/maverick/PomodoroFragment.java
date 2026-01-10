package com.example.maverick;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PomodoroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PomodoroFragment extends Fragment {

    private static final int TOTAL_TIME_SECONDS = 25;   // change to 25 * 60 for 25 minutes

    private TextView tvTime;
    private TextView tvLabel;
    private ImageButton btnPlay;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PomodoroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PomodoroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PomodoroFragment newInstance(String param1, String param2) {
        PomodoroFragment fragment = new PomodoroFragment();
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        tvTime = view.findViewById(R.id.textView4);
        tvLabel = view.findViewById(R.id.textView2);
        btnPlay = view.findViewById(R.id.imageButton5);
        progressBar = view.findViewById(R.id.progressBar);

        // Initial state
        tvTime.setText(String.valueOf(TOTAL_TIME_SECONDS));
        tvLabel.setText("sec left");
        progressBar.setMax(TOTAL_TIME_SECONDS);
        progressBar.setProgress(TOTAL_TIME_SECONDS);

        btnPlay.setOnClickListener(v -> {
            if (!isRunning) {
                startTimer();
            }
        });

        return view;
    }

    private void startTimer() {
        isRunning = true;
        btnPlay.setEnabled(false);
        countDownTimer = new CountDownTimer(TOTAL_TIME_SECONDS * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000L);
                tvTime.setText(String.valueOf(secondsLeft));
                progressBar.setProgress(secondsLeft, true);
            }

            @Override
            public void onFinish() {
                tvTime.setText("0");
                progressBar.setProgress(0);
                isRunning = false;
                btnPlay.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
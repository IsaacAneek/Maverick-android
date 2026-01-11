package com.example.maverick;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PomodoroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PomodoroFragment extends Fragment {

    private static final int DEFAULT_TIME_SECONDS = 25;

    private TextView tvTime;
    private TextView tvLabel;
    private ImageButton btnPlay;
    private ProgressBar progressBar;

    private EditText focusInput;
    private EditText breakInput;
    private Button focusButton;
    private Button breakButton;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private int currentTotalSeconds = DEFAULT_TIME_SECONDS;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PomodoroFragment() {
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

        focusInput = view.findViewById(R.id.focus_text_edit);
        breakInput = view.findViewById(R.id.break_text_edit);
        focusButton = view.findViewById(R.id.focus_button);
        breakButton = view.findViewById(R.id.break_button);

        tvTime.setText(String.valueOf(DEFAULT_TIME_SECONDS));
        tvLabel.setText("sec left");
        progressBar.setMax(DEFAULT_TIME_SECONDS);
        progressBar.setProgress(DEFAULT_TIME_SECONDS);

        TextView task_name = view.findViewById(R.id.pomodoro_task_name);
        task_name.setText(mParam1);


        focusButton.setOnClickListener(v -> {
            int seconds = getSecondsFromEdit(focusInput);
            if (seconds <= 0) {
                focusInput.setError("Enter a positive number");
                return;
            }
            startTimer(seconds);
        });

        breakButton.setOnClickListener(v -> {
            int seconds = getSecondsFromEdit(breakInput);
            if (seconds <= 0) {
                breakInput.setError("Enter a positive number");
                return;
            }
            startTimer(seconds);
        });

        btnPlay.setOnClickListener(v -> {
            if (!isRunning) {
                startTimer(currentTotalSeconds);
            }
        });

        return view;
    }

    private int getSecondsFromEdit(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void startTimer(int totalSeconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        currentTotalSeconds = totalSeconds;
        isRunning = true;

        btnPlay.setEnabled(false);
        focusButton.setEnabled(false);
        breakButton.setEnabled(false);

        tvTime.setText(String.valueOf(totalSeconds));
        tvLabel.setText("sec left");
        progressBar.setMax(totalSeconds);
        progressBar.setProgress(totalSeconds);

        countDownTimer = new CountDownTimer(totalSeconds * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000L);
                tvTime.setText(String.valueOf(secondsLeft));
                progressBar.setProgress(secondsLeft);
            }

            @Override
            public void onFinish() {
                tvTime.setText("0");
                progressBar.setProgress(0);
                isRunning = false;

                btnPlay.setEnabled(true);
                focusButton.setEnabled(true);
                breakButton.setEnabled(true);
            }
        }.start();
    }
}
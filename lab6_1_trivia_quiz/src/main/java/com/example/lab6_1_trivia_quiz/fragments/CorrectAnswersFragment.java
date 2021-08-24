package com.example.lab6_1_trivia_quiz.fragments;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.lab6_1_trivia_quiz.R;
import com.example.lab6_1_trivia_quiz.models.Question;
import com.example.lab6_1_trivia_quiz.viewmodel.myViewModel;

import java.util.ArrayList;

public class CorrectAnswersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    String amount, category, difficulty, type = null;
    protected ArrayList<Question> quizData;

    public CorrectAnswersFragment() {
    }

    public static CorrectAnswersFragment newInstance(int position) {
        CorrectAnswersFragment fragment = new CorrectAnswersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position;
        if (getArguments() != null)
            position = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_correct_answers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myViewModel myViewModel = new ViewModelProvider(requireActivity()).get(com.example.lab6_1_trivia_quiz.viewmodel.myViewModel.class);
        myViewModel.getQuiz(amount, category, difficulty, type).observe(getViewLifecycleOwner(), (ArrayList<Question> AllQuiz) -> {
            this.quizData = AllQuiz;

            int sumQuestions = quizData.size();

            StringBuilder listOfCorrectAnswers = new StringBuilder();
            for (int position = 0; position < sumQuestions; position++) {
                String correctAnswers = decodeHtmlString(quizData.get(position).getCorrect_answer());
                String question = decodeHtmlString(quizData.get(position).getQuestion());
                listOfCorrectAnswers.append(question).append(":\n").append(correctAnswers).append("\n").append("\n");
            }

            TextView tvCorrectAnswersList = view.findViewById(R.id.tvCorrectAnswersList);
            tvCorrectAnswersList.setText(listOfCorrectAnswers);

        });
    }

    private String decodeHtmlString (String stringWithHtmlCodes){
        Spanned decodedString;
        decodedString = Html.fromHtml(stringWithHtmlCodes, Html.FROM_HTML_MODE_LEGACY);
        return decodedString.toString();
    }

}
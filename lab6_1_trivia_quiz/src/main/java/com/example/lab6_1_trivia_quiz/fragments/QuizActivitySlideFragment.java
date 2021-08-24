package com.example.lab6_1_trivia_quiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.lab6_1_trivia_quiz.R;
import com.example.lab6_1_trivia_quiz.models.Question;
import com.example.lab6_1_trivia_quiz.viewmodel.myViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivitySlideFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private int position;
    String amount, category, difficulty, type = null;
    protected ArrayList<Question> quizData;
    private int correctAnswersCount = 0;

    public QuizActivitySlideFragment() {
    }

    public static QuizActivitySlideFragment newInstance(int position) {
        QuizActivitySlideFragment fragment = new QuizActivitySlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            this.position = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_slide_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        com.example.lab6_1_trivia_quiz.viewmodel.myViewModel myViewModel = new ViewModelProvider(requireActivity()).get(com.example.lab6_1_trivia_quiz.viewmodel.myViewModel.class);
        myViewModel.getQuiz(amount, category, difficulty, type).observe(getViewLifecycleOwner(), (ArrayList<Question> AllQuiz) -> {
            this.quizData = AllQuiz;

            TextView tvQuestionNumber = view.findViewById(R.id.tvQuestionNumber);
            TextView tvQuestionNumSlashNum = view.findViewById(R.id.tvQuestionNumSlashNum);
            TextView tvQuestion = view.findViewById(R.id.tvQuestion);

            RadioButton rbAnswer1 = view.findViewById(R.id.rbAnswer1);
            RadioButton rbAnswer2 = view.findViewById(R.id.rbAnswer2);
            RadioButton rbAnswer3 = view.findViewById(R.id.rbAnswer3);
            RadioButton rbAnswer4 = view.findViewById(R.id.rbAnswer4);

            if (quizData.isEmpty()){
                Toast.makeText(getContext(), "Ingen spørsmål hentet. Prøv annen kategory", Toast.LENGTH_SHORT).show();
            }
            if (quizData.get(position).getIncorrect_answers().size() == 2) {
                //Legg alt inn i ny liste og shuffle.
                List<String> alternatives = new ArrayList<>();
                alternatives.add(decodeHtmlString(quizData.get(position).getCorrect_answer()));
                alternatives.add(decodeHtmlString(quizData.get(position).getIncorrect_answers().get(0)));
                alternatives.add(decodeHtmlString(quizData.get(position).getIncorrect_answers().get(1)));
                alternatives.add(decodeHtmlString(quizData.get(position).getIncorrect_answers().get(2)));
                Collections.shuffle(alternatives);

                //Sett text på flere textview
                tvQuestion.setText(decodeHtmlString(quizData.get(position).getQuestion()));
                rbAnswer1.setText(alternatives.get(0));
                rbAnswer2.setText(alternatives.get(1));
                rbAnswer3.setText(alternatives.get(2));
                rbAnswer4.setText(alternatives.get(3));
            }else {
                //Sett text på flere textview
                tvQuestion.setText(decodeHtmlString(quizData.get(position).getQuestion()));
                rbAnswer1.setText(decodeHtmlString(quizData.get(position).getCorrect_answer()));
                rbAnswer2.setText(decodeHtmlString(quizData.get(position).getIncorrect_answers().get(0)));
                rbAnswer3.setVisibility(View.INVISIBLE);
                rbAnswer4.setVisibility(View.INVISIBLE);
            }
            //Sett text på flere textview
            tvQuestionNumber.setText(String.valueOf(position + 1));
            String str = ((position + 1) + "/" + quizData.size());
            tvQuestionNumSlashNum.setText(str);

            //Hent besvarelser fra radio knappene
            RadioGroup rbAnswersGroup = view.findViewById(R.id.rbAnswersGroup);
            String correctAnswer = quizData.get(position).getCorrect_answer();
            rbAnswersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    group.check(checkedId);
                    if (checkedId == rbAnswer1.getId() && correctAnswer.equals(rbAnswer1.getText().toString())) {
                        checAnswer();
                    } else if (checkedId == rbAnswer2.getId() && correctAnswer.equals(rbAnswer2.getText().toString())) {
                        checAnswer();
                    } else if (checkedId == rbAnswer3.getId() && correctAnswer.equals(rbAnswer3.getText().toString())) {
                        checAnswer();
                    } else if (checkedId == rbAnswer4.getId() && correctAnswer.equals(rbAnswer4.getText().toString())) {
                        checAnswer();
                    }
                }
            });
        });
    }

    public void checAnswer() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if (sharedPref.contains("count")) {
            correctAnswersCount = sharedPref.getInt("count", 0);
            correctAnswersCount = correctAnswersCount + 1;
        } else {
            correctAnswersCount = correctAnswersCount + 1;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("count").apply();
        editor.remove("countSize").apply();
        editor.putInt("count", correctAnswersCount);
        editor.putInt("countSize", quizData.size());
        editor.apply();
    }

    private String decodeHtmlString (String stringWithHtmlCodes){
        Spanned decodedString;
        decodedString = Html.fromHtml(stringWithHtmlCodes, Html.FROM_HTML_MODE_LEGACY);
        return decodedString.toString();
    }
}
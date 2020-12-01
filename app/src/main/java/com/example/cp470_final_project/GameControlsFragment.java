package com.example.cp470_final_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameControlsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameControlsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView levelName, gameText1, gameText2, gameText3, promptText;
    EditText gameAns1, gameAns2;
    Spinner gameAns3;
    Button hintButton, submitButton;
    ProgressBar completion;
    String[] promptsList, gameTextsList, answersList, hintsList,spinnerList;
    String prompts, gameTexts, answers, hints, entry, hint,prompt, spinners, spinString;
    JSONObject levelValues,promptValues,answerValues,hintValues,spinnerValues;
    ArrayList<String> aList, hList,pList,sList;
    Toast toast;

    public GameControlsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameControlsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameControlsFragment newInstance(String param1, String param2) {
        GameControlsFragment fragment = new GameControlsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_controls, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle;
        if (this == null) {
            bundle = getActivity().getIntent().getExtras();
        }else{
            bundle = this.getArguments();
        }



        //change level so it's passed from activity
        final int level = bundle.getInt("Level");

        //Getting items
        levelName = view.findViewById(R.id.levelTitle);
        promptText = view.findViewById(R.id.promptText);
        gameText1 = view.findViewById(R.id.gameText1);
        gameText2 = view.findViewById(R.id.gameText2);
        gameText3 = view.findViewById(R.id.gameText3);
        gameAns1 = view.findViewById(R.id.enterText1);
        gameAns2 = view.findViewById(R.id.enterText2);
        gameAns3 = view.findViewById(R.id.dropSelect1);
        hintButton = view.findViewById(R.id.hintButton);
        submitButton = view.findViewById(R.id.submitButton);
        completion = view.findViewById(R.id.levelCompletionBar);

        //Getting level values
        promptsList = getResources().getStringArray(R.array.promptList);
        gameTextsList = getResources().getStringArray(R.array.gameTextList);
        answersList = getResources().getStringArray(R.array.gameAnsList);
        hintsList = getResources().getStringArray(R.array.hintList);
        spinnerList = getResources().getStringArray(R.array.spinnerList);

        prompts = promptsList[level-1];
        gameTexts = gameTextsList[level-1];
        answers = answersList[level-1];
        hints = hintsList[level-1];
        spinners = spinnerList[level-1];
        Log.i("Fragment", "Got lists");

        //Setting text for level

        levelName.setText(R.string.level + level);
        aList = new ArrayList<String>();
        hList = new ArrayList<String>();
        pList = new ArrayList<String>();
        sList = new ArrayList<String>();
        try {
            levelValues = new JSONObject(gameTexts);
            gameText1.setText(levelValues.getString("1"));
            gameText2.setText(levelValues.getString("2"));
            gameText3.setText(levelValues.getString("3"));
            promptValues = new JSONObject(prompts);
            promptText.setText(promptValues.getString("1"));
            pList.add(promptValues.getString("1"));
            pList.add(promptValues.getString("2"));
            pList.add(promptValues.getString("3"));
            answerValues = new JSONObject(answers);
            aList.add(answerValues.getString("1"));
            aList.add(answerValues.getString("2"));
            aList.add(answerValues.getString("3"));
            hintValues = new JSONObject(hints);
            hList.add(hintValues.getString("1"));
            hList.add(hintValues.getString("2"));
            hList.add(hintValues.getString("3"));
            spinnerValues = new JSONObject(spinners);
            sList.add(spinnerValues.getString("1"));
            sList.add(spinnerValues.getString("2"));
            sList.add(spinnerValues.getString("3"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        entry = "";
        hint = hList.get(0);
        prompt = pList.get(0);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, sList);
        gameAns3.setAdapter(adapter);

        //Listeners
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v.findViewById(R.id.hintButton), hint, Snackbar.LENGTH_LONG).show();
            }//end onClick
        });//end listener

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if answers are right --> display congrats page/dialog/whatever, if not show toast to retry
                toast = Toast.makeText(getActivity(), R.string.correct, Toast.LENGTH_LONG);
                if(gameText2.getVisibility() != View.VISIBLE){
                    entry = gameAns1.getText().toString();
                    if (entry.equals(aList.get(0)) || aList.get(0).equals(" ")){
                        gameText2.setVisibility(View.VISIBLE);
                        gameAns2.setVisibility(View.VISIBLE);
                        hint = hList.get(1);
                        prompt = pList.get(1);
                        promptText.setText(prompt);
                        toast.show();
                    } else {
                        Log.i("Fragment", aList.get(0));
                        toast = Toast.makeText(getActivity(), R.string.wrong, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else if (gameText3.getVisibility() != View.VISIBLE){
                    entry = gameAns2.getText().toString();
                    if (entry.equals(aList.get(1)) || aList.get(1).equals(" ")){
                        gameText3.setVisibility(View.VISIBLE);
                        gameAns3.setVisibility(View.VISIBLE);
                        hint = hList.get(2);
                        prompt = pList.get(2);
                        promptText.setText(prompt);
                        toast.show();
                    } else {
                        toast = Toast.makeText(getActivity(), R.string.wrong, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    //Beat the level
                    entry = gameAns3.getSelectedItem().toString();
                    if (entry.equals(aList.get(2))){
                        toast.show();
                    } else {
                        toast = Toast.makeText(getActivity(), R.string.wrong, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }//end onClick
        });//end listener


    }

}
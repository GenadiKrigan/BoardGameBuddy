package com.example.boardgamebuddt.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.boardgamebuddt.MainActivity;
import com.example.boardgamebuddt.R;
import com.example.boardgamebuddt.models.Game;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddGameFragment extends Fragment {
    private TextInputEditText etName, etMinPlayer, etMaxPlayer, etBestPlayer, etAvgTime, etDifficulty, etRulesNote;
    private ChipGroup cgCategories;
    private SwitchMaterial swAlreadyPlayed;
    private Game gameToEdit = null;
    private Button btAddGame;
    public AddGameFragment() {super(R.layout.fragment_add_game);}
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.etAddGameName);
        etMinPlayer = view.findViewById(R.id.etAddMinPlayers);
        etMaxPlayer = view.findViewById(R.id.etAddMaxPlayers);
        etBestPlayer = view.findViewById(R.id.etAddBestPlayers);
        etAvgTime = view.findViewById(R.id.etAddAvgTime);
        etDifficulty = view.findViewById(R.id.etAddDifficulty);
        etRulesNote = view.findViewById(R.id.etAddRulesNote);
        cgCategories = view.findViewById(R.id.cgCategories);
        swAlreadyPlayed = view.findViewById(R.id.swAlreadyPlayed);//bool
        btAddGame = view.findViewById(R.id.btnAddGameSave);
        btAddGame.setOnClickListener(v -> saveGame());
        if (getArguments() != null && getArguments().containsKey("game_to_edit")) {
            gameToEdit = (Game) getArguments().getSerializable("game_to_edit");
            if (gameToEdit != null) {
                fillFormForEdit(gameToEdit);
            }
        }
    }

    private void saveGame() {
        String name = etName.getText().toString().trim();
        String minPlayers = etMinPlayer.getText().toString().trim();
        String maxPlayers = etMaxPlayer.getText().toString().trim();
        String bestPlayers = etBestPlayer.getText().toString().trim();
        String avgTime = etAvgTime.getText().toString().trim();
        String difficulty = etDifficulty.getText().toString().trim();
        String rulesNote = etRulesNote.getText().toString().trim();

        if (name.isEmpty() || minPlayers.isEmpty() || maxPlayers.isEmpty() || avgTime.isEmpty() || difficulty.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int min = Integer.parseInt(minPlayers);
        int max = Integer.parseInt(maxPlayers);
        int best = bestPlayers.isEmpty() ? min : Integer.parseInt(bestPlayers);
        int avg = Integer.parseInt(avgTime);
        int diff = Integer.parseInt(difficulty);

        List<String> selectedCategories = new ArrayList<>();
        for (int i = 0; i < cgCategories.getChildCount(); i++) {
            View child = cgCategories.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                if (chip.isChecked()) {
                    selectedCategories.add(chip.getText().toString());
                }
            }
        }

        if (selectedCategories.isEmpty()) {
            Toast.makeText(getContext(), "Please select at least one category", Toast.LENGTH_SHORT).show();
            return;
        }

        String firebaseId = (gameToEdit != null) ? gameToEdit.getFirebaseId() : null;
        Game game = new Game(firebaseId, name, min, max, best, avg, diff, selectedCategories, swAlreadyPlayed.isChecked(), rulesNote);

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            if (gameToEdit == null) {
                mainActivity.addGameToDB(game);
            } else {
                mainActivity.updateGameInDB(game);
            }
        }
    }
    private void fillFormForEdit(Game game) {
        etName.setText(game.getName());
        etMinPlayer.setText(String.valueOf(game.getMinPlayers()));
        etMaxPlayer.setText(String.valueOf(game.getMaxPlayers()));
        etAvgTime.setText(String.valueOf(game.getAvgPlayTime()));
        etDifficulty.setText(String.valueOf(game.getDifficulty()));
        etRulesNote.setText(game.getRulesNote());
        swAlreadyPlayed.setChecked(game.isUnplayed());

        List<String> gameCategories = game.getCategories();
        if (gameCategories != null) {
            for (int i = 0; i < cgCategories.getChildCount(); i++) {
                View child = cgCategories.getChildAt(i);
                if (child instanceof Chip) {
                    Chip chip = (Chip) child;
                    if (gameCategories.contains(chip.getText().toString())) {
                        chip.setChecked(true);
                    } else {
                        chip.setChecked(false);
                    }
                }
            }
        }

        btAddGame.setText("Update Game"); //
    }
}
package com.example.boardgamebuddt.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.boardgamebuddt.GameAdapter;
import com.example.boardgamebuddt.R;
import com.example.boardgamebuddt.models.Game;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class GameListFragment extends Fragment {

    private RecyclerView rvGameList;
    private GameAdapter adapter;
    private List<Game> gameList;
    private ProgressBar pbLoading;
    private DatabaseReference gamesRef;

    public GameListFragment() {
        super(R.layout.fragment_game_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGameList = view.findViewById(R.id.rvGameList);
        pbLoading = view.findViewById(R.id.pbLoading);

        rvGameList.setLayoutManager(new LinearLayoutManager(getContext()));
        gameList = new ArrayList<>();
        adapter = new GameAdapter(gameList);
        rvGameList.setAdapter(adapter);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        gamesRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("games");

        gamesRef.keepSynced(true);

        fetchGamesFromDB();

        adapter.setOnDeleteClickListener(game -> {
            deleteGame(game);
        });
        adapter.setOnEditClickListener(game -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("game_to_edit", game);

            androidx.navigation.Navigation.findNavController(view)
                    .navigate(R.id.action_gameListFragment_to_addGameFragment, bundle);
        });
        adapter.setOnGameClickListener(game -> {
            showGameDetailsDialog(game);
        });
    }

    private void fetchGamesFromDB() {
        pbLoading.setVisibility(View.VISIBLE);
        gamesRef.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameList.clear();
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    if (game != null) {
                        gameList.add(game);
                    }
                }
                adapter.notifyDataSetChanged();
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteGame(Game game) {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Delete Game")
                .setMessage("Are you sure you want to delete " + game.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // ביצוע המחיקה מ-Firebase בעזרת ה-ID הייחודי
                    gamesRef.child(game.getFirebaseId()).removeValue()
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(getContext(), "Game deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showGameDetailsDialog(Game game) {
        StringBuilder details = new StringBuilder();
        details.append("👥 Players: ").append(game.getMinPlayers()).append("-").append(game.getMaxPlayers());
        if (game.getBestPlayers() > 0) details.append(" (Best: ").append(game.getBestPlayers()).append(")");
        details.append("\n\n⏱ Time: ").append(game.getAvgPlayTime()).append(" min");
        details.append("\n\n🧠 Difficulty: ").append(game.getDifficulty()).append("/5");
        details.append("\n\n🏷 Categories: ").append(String.join(", ", game.getCategories()));
        details.append("\n\n✅ Played: ").append(game.isUnplayed() ? "Yes" : "No");

        if (game.getRulesNote() != null && !game.getRulesNote().isEmpty()) {
            details.append("\n\n📝 Rules/Notes:\n").append(game.getRulesNote());
        }

        new android.app.AlertDialog.Builder(getContext())
                .setTitle(game.getName())
                .setMessage(details.toString())
                .setPositiveButton("Close", null) // כפתור לסגירת החלונית
                .show();
    }
}
package com.example.boardgamebuddt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.boardgamebuddt.models.Game;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gameList;

    public interface OnGameDeleteListener {
        void onDeleteClick(Game game);
    }

    public interface OnGameEditListener {
        void onEditClick(Game game);
    }
    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    private OnGameDeleteListener onDeleteClickListener;
    private OnGameEditListener onEditClickListener;
    private OnGameClickListener onGameClickListener;

    public void setOnDeleteClickListener(OnGameDeleteListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setOnEditClickListener(OnGameEditListener listener) {
        this.onEditClickListener = listener;
    }
    public void setOnGameClickListener(OnGameClickListener listener) {
        this.onGameClickListener = listener;
    }
    public GameAdapter(List<Game> gameList) {
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_item_row, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);

        holder.tvName.setText(game.getName());
        holder.tvPlayers.setText(game.getMinPlayers() + " - " + game.getMaxPlayers() + " Players");
        holder.tvTime.setText(game.getAvgPlayTime() + " min");
        holder.tvDifficulty.setText("Difficulty: " + game.getDifficulty() + "/5");

        if (game.getCategories() != null && !game.getCategories().isEmpty()) {
            String categoriesStr = String.join(", ", game.getCategories());
            holder.tvCategories.setText(categoriesStr);
        } else {
            holder.tvCategories.setText("No categories");
        }

        holder.btnDeleteGame.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(game);
            }
        });

        // הגדרת לחיצה על כפתור העריכה
        holder.btnEditGame.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(game);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            if (onGameClickListener != null) {
                onGameClickListener.onGameClick(game);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList != null ? gameList.size() : 0;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPlayers, tvTime, tvDifficulty, tvCategories;
        ImageButton btnDeleteGame, btnEditGame;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRowGameName);
            tvPlayers = itemView.findViewById(R.id.tvRowPlayers);
            tvTime = itemView.findViewById(R.id.tvRowTime);
            tvDifficulty = itemView.findViewById(R.id.tvRowDifficulty);
            tvCategories = itemView.findViewById(R.id.tvRowCategories);

            btnDeleteGame = itemView.findViewById(R.id.btnDeleteGame);
            btnEditGame = itemView.findViewById(R.id.btnEditGame);
        }
    }
}
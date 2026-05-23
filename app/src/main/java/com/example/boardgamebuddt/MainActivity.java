package com.example.boardgamebuddt;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.boardgamebuddt.models.Game;
import com.example.boardgamebuddt.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Enable disk persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment) {
                bottomNav.setVisibility(View.GONE);
            } else {
                bottomNav.setVisibility(View.VISIBLE);
            }
        });
    }
    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            navController.navigate(R.id.action_loginFragment_to_gameListFragment);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void register(String email, String password, String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String uid = task.getResult().getUser().getUid();
                            writeUserToDB(uid, username, email);
                            Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            navController.navigate(R.id.action_registerFragment_to_loginFragment);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void writeUserToDB (String uid, String username, String email){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(uid);
        User newUser = new User(username, email, uid);
        myRef.setValue(newUser);
    }
    public void addGameToDB(@NonNull Game game){
        //Get current user
        String uid = mAuth.getCurrentUser().getUid();
        //makes a reference to teh user DB
        DatabaseReference gamesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("games");
        gamesRef.keepSynced(true);
        //create a new game ID
        String gameId = gamesRef.push().getKey();
        game.setFirebaseId(gameId);
        //add game to DB
        gamesRef.child(gameId).setValue(game)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(this, "Game added successfully!", Toast.LENGTH_LONG).show();
                        navController.popBackStack();
                    }
                    else{
                        Toast.makeText(this, "Faild to add game", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void updateGameInDB(Game game) {
        String uid = mAuth.getCurrentUser().getUid();

        DatabaseReference gamesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("games");

        gamesRef.child(game.getFirebaseId()).setValue(game)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Game updated!", Toast.LENGTH_SHORT).show();
                    navController.popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update game", Toast.LENGTH_SHORT).show();
                });
    }
}
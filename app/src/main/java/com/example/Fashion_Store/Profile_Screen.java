package com.example.Fashion_Store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Fashion_Store.Adapters.Notification_Item_Adapter;
import com.example.Fashion_Store.Models.Notification_Item_Model;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Screen extends AppCompatActivity {

    private TextView change_pic,notify_count_profile;
    private ImageView back_profile, notification_profile;
    private CircleImageView profile_pic;
    private EditText name_profile, email_profile, password_profile, number_profile, address_profile;
    private Button save_profile, logout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    Uri uri;
    int notify_countDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        back_profile = findViewById(R.id.back_profile);
        notification_profile = findViewById(R.id.notification_profile);
        profile_pic = findViewById(R.id.profile_pic);
        change_pic = findViewById(R.id.change_pic);
        notify_count_profile = findViewById(R.id.notify_count_profile);
        name_profile = findViewById(R.id.name_profile);
        email_profile = findViewById(R.id.email_profile);
        password_profile = findViewById(R.id.password_profile);
        number_profile = findViewById(R.id.number_profile);
        address_profile = findViewById(R.id.address_profile);
        save_profile = findViewById(R.id.save_profile);
        logout = findViewById(R.id.logout_profile);
        mAuth = FirebaseAuth.getInstance();

        readUserData();
        setNotificationsCountInBadge();

        back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        notification_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_notifications_dialog();


            }
        });

        change_pic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ImagePicker.with(Profile_Screen.this)
                        .crop()//Crop image(Optional), Check Customization for more option
                        .cropSquare()
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCartData();
                startActivity(new Intent(Profile_Screen.this, Login_SignUp_Container.class));
                finish();
                mAuth.signOut();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode & 0xffff, resultCode, data);

        uri = data.getData();
        profile_pic.setImageURI(uri);

    }

    public void readUserData(){

        String userID = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();

                    if (!Objects.equals(snapshot.getString("profile pic"), "")) {
                        profile_pic.setImageURI(Uri.parse(snapshot.getString("profile pic")));
                    }
                    name_profile.setText(snapshot.getString("name"));
                    email_profile.setText(snapshot.getString("email"));
                    password_profile.setText(snapshot.getString("password"));
                    number_profile.setText(snapshot.getString("number"));
                    address_profile.setText(snapshot.getString("address"));
                }

            }
        });
    }

    public void updateUserData_fireStore(){

        String userID = mAuth.getCurrentUser().getUid();

        Map<String, Object> user = new HashMap<>();
        if (uri != null) {
            user.put("profile pic", uri);
        }
        user.put("name", name_profile.getText().toString());
        user.put("email", email_profile.getText().toString());
        user.put("password", password_profile.getText().toString());
        user.put("number", number_profile.getText().toString());
        user.put("address", address_profile.getText().toString());

        db.collection("Users").document(userID).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Profile_Screen.this, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void show_notifications_dialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.notifications_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();

        TextView cancel_dialog = (TextView) dialog.findViewById(R.id.cancel_dialog);
        TextView no_notify = (TextView) dialog.findViewById(R.id.no_notify);
        RecyclerView recyclerView = dialog.findViewById(R.id.notification_rec);
        ArrayList<Notification_Item_Model> notification_item_models = new ArrayList<>();
        String userID = mAuth.getCurrentUser().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MainActivity.getInstance().add_seen_field();

        Query query = db.collection("Users").document(userID).collection("Notifications")
                .orderBy("notify", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                notification_item_models.clear();
                for (DocumentChange data : value.getDocumentChanges()){

                    DocumentSnapshot snapshot = data.getDocument();

                    Notification_Item_Model notificationItemModel = new Notification_Item_Model();
                    notificationItemModel.setNotify(snapshot.getString("notify"));
                    notification_item_models.add(notificationItemModel);

                }

                Notification_Item_Adapter notification_item_adapter = new Notification_Item_Adapter(Profile_Screen.this,notification_item_models);
                recyclerView.setAdapter(notification_item_adapter);
                notification_item_adapter.notifyDataSetChanged();


                if (notification_item_adapter.getItemCount() != 0){
                    no_notify.setVisibility(View.INVISIBLE);
                }

            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
    public void setNotificationsCountInBadge(){
        String userID = mAuth.getCurrentUser().getUid();


        db.collection("Users").document(userID).collection("Notifications").whereEqualTo("seen","").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                notify_countDB = queryDocumentSnapshots.size();
                notify_count_profile.setText(String.valueOf(notify_countDB));

                if (notify_countDB == 0){
                    notify_count_profile.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void updateData(){

        String userID = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();

                    String email_DB = snapshot.getString("email");
                    String password_DB = snapshot.getString("password");

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email_DB, password_DB);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                String email = email_profile.getText().toString();
                                String password = password_profile.getText().toString();

                                user.updateEmail(email);
                                user.updatePassword(password);
                                updateUserData_fireStore();
                            } else {
                                Toast.makeText(Profile_Screen.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });


    }
    public void deleteCartData(){

        String userID = mAuth.getCurrentUser().getUid();
        reference.child("Cart").child(userID).removeValue();
    }
}
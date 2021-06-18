package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView username;
    TextView email;
    ImageView profileImage;
    RecyclerView recyclerView;
    AdapterReceptiProfiliKorisnika adapterReceptiProfiliKorisnika;
    List<Recept> receptList;
    String userIdValue;
    Recept recept;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Context ctx = this;


        ArrayList<String> profileLista=getIntent().getExtras().getStringArrayList("List");
        recyclerView = findViewById(R.id.dataListR);
        username=findViewById(R.id.userName);
        email=findViewById(R.id.eMail);
        profileImage=findViewById(R.id.profile_image);
        receptList=new ArrayList<>();
        userIdValue = profileLista.get(5);
        username.setText(profileLista.get(4));
        email.setText(profileLista.get(0));
        Picasso.get().load(profileLista.get(3)).into(profileImage);


        CollectionReference receptRef= docRef.collection("recepti");
        receptRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("idPublishera").equals(userIdValue))
                                {

                                    GridLayoutManager gridLayoutManager= new GridLayoutManager(ctx, 1);
                                    recyclerView.setLayoutManager(gridLayoutManager);

                                    recept =new Recept(document.getId(), document.getString("naziv"),document.getString("priprema"),document.getString("sastojci"),Long.parseLong( document.get("datum").toString()),document.getString("idPublishera"),document.getString("Img"));
                                    receptList.add(recept);
                                    adapterReceptiProfiliKorisnika= new AdapterReceptiProfiliKorisnika(ctx, receptList);
                                    recyclerView.setAdapter(adapterReceptiProfiliKorisnika);

                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }
}
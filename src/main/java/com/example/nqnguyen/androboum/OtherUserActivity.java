package com.example.nqnguyen.androboum;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class OtherUserActivity extends AppCompatActivity {

    class MyPagerAdapter extends PagerAdapter {

        List<Profil> liste;
        Context context;

        public MyPagerAdapter(Context context, List<Profil> liste) {
            this.liste = liste;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // on va chercher la layout
            ViewGroup layout = (ViewGroup) View.inflate(context, R.layout.other_user_fragment,
                    null);

            // on l'ajoute à la vue
            container.addView(layout);

            // on le remplit en fonction du profil
            remplirLayout(layout, liste.get(position));
            // et on retourne ce layout
            return layout;
        }

        @Override
        public int getCount() {
            return liste.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private void remplirLayout(ViewGroup layout,Profil p) {
            TextView tvEmail = (TextView) layout.findViewById(R.id.textView3);
            ImageView ivAvatar = (ImageView) layout.findViewById(R.id.imageView3);
            ImageView ivIsConnected = (ImageView) layout.findViewById(R.id.imageView4);

            // on télécharge dans le premier composant l'image du profil
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference photoRef = storage.getReference().child(p.getEmail() + "/photo.jpg");
            if (photoRef != null) {
                GlideApp.with(context)
                        .load(photoRef)
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.baseline_person_black_48dp)
                        .into(ivAvatar);
            }
            if (!p.isConnected()) {
                ivIsConnected.setVisibility(View.GONE);
            }
            // on positionne le email dans le TextView
            tvEmail.setText(p.getEmail());
            Log.v("Androboum","bingo : "+p.getEmail()) ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
/*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        final List<Profil> userList = new ArrayList<>();

        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference().child("Users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    userList.add(child.getValue(Profil.class));
                    //Log.v("AndroBoum", "test : "+child.getValue(Profil.class).getEmail());
                }

                //Champs a remplir
                TextView tvEmail = (TextView) findViewById(R.id.textView3);
                ImageView ivAvatar = (ImageView) findViewById(R.id.imageView3);
                ImageView ivIsConnected = (ImageView) findViewById(R.id.imageView4);

                // on obtient l'intent utilisé pour l'appel
                Intent intent = getIntent();
                // on va chercher la valeur du paramètre position, et on
                // renvoie zéro si ce paramètre n'est pas positionné (ce qui ne devrait
                // pas arriver dans notre cas).
                int position = intent.getIntExtra("position",0);

                //User courant
                Profil p = userList.get(position);

                //Remplir les champs
                tvEmail.setText(p.getEmail());

                // on télécharge dans le premier composant l'image du profil
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference photoRef = storage.getReference().child(p.getEmail() + "/photo.jpg");
                if (photoRef != null) {
                    GlideApp.with(getApplicationContext())
                            .load(photoRef)
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.baseline_person_black_48dp)
                            .into(ivAvatar);
                }

                // si l'utilisateur n'est pas connecté, on rend invisible le troisième
                // composant
                if (!p.isConnected) {
                    ivIsConnected.setVisibility(View.GONE);
                }

                //Log.v("AndroBoum", "test : "+userList.get(position).getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.v("AndroBoum", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);

*/

        final List<Profil> userList = new ArrayList<>();
        final MyPagerAdapter adapter = new MyPagerAdapter(this, userList);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);

        // on obtient l'intent utilisé pour l'appel
        Intent intent = getIntent();

        // on va chercher la valeur du paramètre position, et on
        // renvoie zéro si ce paramètre n'est pas positionné (ce qui ne devrait
        // pas arriver dans notre cas).
        final int position = intent.getIntExtra("position", 0);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    userList.add(child.getValue(Profil.class));
                }
                adapter.notifyDataSetChanged();
                pager.setCurrentItem(position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.v("AndroBoum", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
        pager.setAdapter(adapter);
    }
}
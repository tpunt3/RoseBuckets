package edu.rosehulman.punttj.rosebuckets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import edu.rosehulman.punttj.rosebuckets.fragments.AboutFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.BucketListFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.BucketListItemFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.BucketListSubItemFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.LoginFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.SubItemDetailFragment;
import edu.rosehulman.punttj.rosebuckets.model.BucketList;
import edu.rosehulman.punttj.rosebuckets.model.BucketListItem;
import edu.rosehulman.punttj.rosebuckets.model.SubItem;
import edu.rosehulman.rosefire.Rosefire;
import edu.rosehulman.rosefire.RosefireResult;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoginFragment.OnLoginListener,
        BucketListFragment.OnBLSelectedListener, BucketListItemFragment.OnBLItemSelectedListener,
        BucketListSubItemFragment.OnSubItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private FloatingActionButton fab;
    private static final int RC_SIGN_IN = 1;
    private static final int RC_ROSEFIRE_LOGIN = 2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private OnCompleteListener mOnCompleteListener;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mFirebaseRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate called in main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        initializeListeners();
        initializeGoogle();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        String savedString = (savedInstanceState == null) ? "this is null" : savedInstanceState.toString();

        Log.d("Main Activity", "saved Instance state: " + savedString);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new LoginFragment();
            ft.add(R.id.content_main, fragment);
            ft.commit();
        }
    }

    private void initializeListeners(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    SharedPreferencesUtils.setCurrentUser(MainActivity.this, user.getUid());
                    SharedPreferencesUtils.setCurrentUserName(MainActivity.this, user.getUid());
                    switchToBLFragment();
                }else {
                    switchToLoginFragment();
                }
            }
        };

        mOnCompleteListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    showLoginError("Login failed");
                }
            }
        };
    }

    private void initializeGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            FragmentManager fm = getSupportFragmentManager();
            if(fm.getBackStackEntryCount() > 1) {
                if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equals("login")) {
                    fab.setVisibility(View.GONE);
                }
                if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equals("subItem")) {
                    fab.setVisibility(View.VISIBLE);
                }
            }

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment switchTo = null;

        if(mAuth.getCurrentUser() != null) {
            switch (id) {
                case R.id.nav_about:
                    switchTo = new AboutFragment();
                    fab.setVisibility(View.GONE);
                    break;
                case R.id.nav_home:
                    switchTo = new BucketListFragment();
                    fab.setVisibility(View.VISIBLE);
                    break;
                case R.id.log_out:
                    logOut();
            }
        }

        if(switchTo != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i ++){
                getSupportFragmentManager().popBackStackImmediate();
            }

            ft.replace(R.id.content_main, switchTo);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                showLoginError("Google signin failed");
            }
        } else if (requestCode == RC_ROSEFIRE_LOGIN) {
            RosefireResult result = Rosefire.getSignInResultFromIntent(data);
            if (result.isSuccessful()){
                mAuth.signInWithCustomToken(result.getToken())
                        .addOnCompleteListener(this, mOnCompleteListener);
            }
        } else if (requestCode == Constants.RC_PHOTO_ACTIVITY) {
            Log.d("Main Activity", "request Code is PHOTO ACTIVITY, MainActivity onActivityResult");
            String subUid = SharedPreferencesUtils.getCurrentSubItem(this);
            Log.d("SUB UID", subUid);
            DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("subItems/" + subUid);

            childRef.addListenerForSingleValueEvent(new SubEventListener());
        }
        else{
            Log.d("Main Acitivy", "else case, MainActivity onActivityResult");
            String subUid = SharedPreferencesUtils.getCurrentSubItem(this);
            DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("subItems/"+subUid);

            childRef.addListenerForSingleValueEvent(new SubEventListener());

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebase", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, mOnCompleteListener);
    }

    private void switchToLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, new LoginFragment(), "Login");
        ft.commit();
    }

    private void switchToBLFragment() {
        fab.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("login");
        ft.commit();
    }

    @Override
    public void onGoogleLogin() {
        //DONE: Log user in with Google account
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onRosefireLogin() {
        //DONE: Log user in with RoseFire account
        Intent signInIntent = Rosefire.getSignInIntent(this, Constants.ROSEFIRE_REGISTRY_TOKEN);
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN);
    }

    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        loginFragment.onLoginError(message);
    }

    @Override
    public void onBLSelected(BucketList bl) {
        fab.setVisibility(View.VISIBLE);
        SharedPreferencesUtils.setCurrentBucketList(this, bl.getKey());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListItemFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("bl");
        ft.commit();
    }

    @Override
    public void onBLItemSelected(BucketListItem item) {
        fab.setVisibility(View.VISIBLE);
        SharedPreferencesUtils.setCurrentBucketListItem(this, item.getKey());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListSubItemFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("blItem");
        ft.commit();
    }

    @Override
    public void onSubItemSelected(SubItem subItem) {
        fab.setVisibility(View.GONE);
        SharedPreferencesUtils.setCurrentSubItem(this, subItem.getKey());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = SubItemDetailFragment.newInstance(subItem);
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("subItem");
        ft.commit();
    }

    private void logOut() {
        mAuth.signOut();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showLoginError("Google connection failed");
    }

    private void uploadPhoto(String path){
        StorageReference myRef = mStorageRef.child("images/"+path);

        final ProgressDialog df = new ProgressDialog(this);
        df.setTitle("Uploading");
        df.show();

        File file = new File(path);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        myRef.putStream(fs)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        df.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        df.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        df.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }

    class SubEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            SubItem item = dataSnapshot.getValue(SubItem.class);
            uploadPhoto(item.getPath());
            onSubItemSelected(item);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}

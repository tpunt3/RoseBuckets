package edu.rosehulman.punttj.rosebuckets.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.rosehulman.punttj.rosebuckets.Constants;
import edu.rosehulman.punttj.rosebuckets.PhotoUtils;
import edu.rosehulman.punttj.rosebuckets.R;
import edu.rosehulman.punttj.rosebuckets.model.SubItem;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubItemDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubItemDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SUB_ITEM = "subItem";



    // TODO: Rename and change types of parameters
    private SubItem mSubItem;
    private TextView mTitleView;
    private ImageView mImageView;
    private TextView mCommentView;

    private Bitmap mBitmap;

    private DatabaseReference subRef;
    private Query query;

    private StorageReference mStorageRef;



    public SubItemDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param subItem mSubItem
     * @return A new instance of fragment SubItemDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubItemDetailFragment newInstance(SubItem subItem) {
        SubItemDetailFragment fragment = new SubItemDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(SUB_ITEM, subItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubItem = getArguments().getParcelable(SUB_ITEM);
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_item_detail, container, false);
        mTitleView = (TextView) view.findViewById(R.id.fragment_detail_title);
        //mTitleView.setText(mSubItem.getTitle());

        mImageView = (ImageView) view.findViewById(R.id.sub_item_photo);

        mCommentView = (TextView) view.findViewById(R.id.sub_item_comment_textView);
        //mCommentView.setText(mSubItem.getComments());


        subRef = FirebaseDatabase.getInstance().getReference().child("subItems").child(mSubItem.getKey());
        subRef.addChildEventListener(new DetailChildEventListener());

        Button editButton = (Button) view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDetail();
            }
        });

        Button addPicButton = (Button) view.findViewById(R.id.add_pic_button);
        addPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        
        if(!(mSubItem.getPath() == null) && !(mSubItem.getPath().equals(""))) {
            mBitmap = BitmapFactory.decodeFile(mSubItem.getPath());
            int width = 512;
            int height = 512;
            mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
            mImageView.setImageBitmap(mBitmap);
        }
        return view;

    }

    private void editDetail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        View view = getLayoutInflater(this.getArguments()).inflate(R.layout.dialog_edit, null, false);
        builder.setView(view);

        final EditText captionET = (EditText) view.findViewById(R.id.dialog_edit_text);


        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String caption = captionET.getText().toString();
                mSubItem.setComments(caption);
                subRef.setValue(mSubItem);
                showCurrentItem();
            }
        });
        builder.create().show();

    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = PhotoUtils.getOutputMediaUri(getString(R.string.app_name));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(cameraIntent, Constants.RC_PHOTO_ACTIVITY);
        mSubItem.setPath(uri.getPath());
        subRef.setValue(mSubItem);

    }

    private void showCurrentItem() {
        mTitleView.setText(mSubItem.getTitle());
        mCommentView.setText(mSubItem.getComments());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Constants.RC_PHOTO_ACTIVITY) {
            mBitmap = BitmapFactory.decodeFile(mSubItem.getPath());
            int width = 512;
            int height = 512;
            mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
            mImageView.setImageBitmap(mBitmap);
            subRef.setValue(mSubItem);
            return;
        }

    }

    class DetailChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.getKey().equals("comments")){
                String comment = dataSnapshot.getValue(String.class);
                mSubItem.setComments(comment);
                mCommentView.setText(comment);
            }else if(dataSnapshot.getKey().equals("path")){
                String path = dataSnapshot.getValue(String.class);
                mSubItem.setPath(path);
            }else if(dataSnapshot.getKey().equals("title")){
                String title = dataSnapshot.getValue(String.class);
                mSubItem.setTitle(title);
                mTitleView.setText(title);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.getKey().equals("comments")){
                String comment = dataSnapshot.getValue(String.class);
                mSubItem.setComments(comment);
                mCommentView.setText(comment);
            }else if(dataSnapshot.getKey().equals("path")){
                String path = dataSnapshot.getValue(String.class);
                mSubItem.setPath(path);
            }else if(dataSnapshot.getKey().equals("title")){
                String title = dataSnapshot.getValue(String.class);
                mSubItem.setTitle(title);
                mTitleView.setText(title);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}

package edu.rosehulman.punttj.rosebuckets.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_item_detail, container, false);
        mTitleView = (TextView) view.findViewById(R.id.fragment_detail_title);
        mTitleView.setText(mSubItem.getTitle());

        mImageView = (ImageView) view.findViewById(R.id.sub_item_photo);

        mCommentView = (TextView) view.findViewById(R.id.sub_item_comment_textView);
        mCommentView.setText(mSubItem.getComments());

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
                Log.d("subItem", "OK pressed");
                String caption = captionET.getText().toString();
                mSubItem.setComments(caption);
                showCurrentItem();
            }
        });
        builder.create().show();

    }

    private void takePhoto() {
        Log.d(Constants.PHOTO_TAG, "takePhoto() started");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = PhotoUtils.getOutputMediaUri(getString(R.string.app_name));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        Log.d(Constants.PHOTO_TAG, "Path: " + uri.getPath());
        startActivityForResult(cameraIntent, Constants.RC_PHOTO_ACTIVITY);
        mSubItem.setPath(uri.getPath());
    }

    private void loadFromGallery() {
        Log.d(Constants.PHOTO_TAG, "loadFromGallery() started");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, Constants.PICK_FROM_GALLERY_REQUEST);
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
            Log.d(Constants.PHOTO_TAG, "yay! we're good till this far");
            mBitmap = BitmapFactory.decodeFile(mSubItem.getPath());
            int width = 512;
            int height = 512;
            mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
            mImageView.setImageBitmap(mBitmap);
            Log.d(Constants.PHOTO_TAG, "trying to see how far we get");
        }

        if (requestCode == Constants.PICK_FROM_GALLERY_REQUEST) {
            Log.d(Constants.PHOTO_TAG, "Back up from the gallery");
            Uri uri = data.getData();
            String realPath = getRealPathFromUri(uri);
            mBitmap = BitmapFactory.decodeFile(realPath);
            int width = 512;
            int height = 512;
            mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
            mImageView.setImageBitmap(mBitmap);
            mSubItem.setPath(realPath);

        }


    }


    private String getRealPathFromUri(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getContext(), contentUri,
                projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        cursor.moveToFirst();
        int columnIndex = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        return cursor.getString(columnIndex);
    }
}

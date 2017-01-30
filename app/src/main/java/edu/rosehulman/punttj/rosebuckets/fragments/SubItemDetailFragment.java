package edu.rosehulman.punttj.rosebuckets.fragments;


import android.content.DialogInterface;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.rosehulman.punttj.rosebuckets.R;
import edu.rosehulman.punttj.rosebuckets.model.SubItem;

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

    private void showCurrentItem() {
        mTitleView.setText(mSubItem.getTitle());
        mCommentView.setText(mSubItem.getComments());
    }


}

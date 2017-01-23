package edu.rosehulman.punttj.rosebuckets.fragments;


import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView titleView = (TextView) view.findViewById(R.id.fragment_detail_title);
        titleView.setText(mSubItem.getTitle());

        ImageView imageview = (ImageView) view.findViewById(R.id.sub_item_photo);

        TextView comments = (TextView) view.findViewById(R.id.sub_item_comment_textView);
        comments.setText(mSubItem.getComments());

        return view;

    }

}

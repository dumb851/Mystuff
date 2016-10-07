package com.zubrid.mystuff.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

public class SelectLabelDialogFragment extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        ArrayList<CharSequence> labelsArrayList = new ArrayList<>();

//        CharSequence[] items = {"Google","Safari","Yahoo"};

        labelsArrayList.add("123");

        CharSequence[] items = labelsArrayList.toArray(new CharSequence[labelsArrayList.size()]);
//        labelsArrayList.add("456");
//        labelsArrayList.add("789");

        boolean[] itemChecked = new boolean[labelsArrayList.size()];
        itemChecked[0] = false;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        //builder.setTitle(R.string.pick_toppings)
        builder.setTitle("title labels")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(items, itemChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                    }
                });

//                        setMultiChoiceItems(mSelectedItems, null,
//                        new DialogInterface.OnMultiChoiceClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which,
//                                                boolean isChecked) {
//                                if (isChecked) {
//                                    // If the user checked the item, add it to the selected items
//                                    mSelectedItems.add(which);
//                                } else if (mSelectedItems.contains(which)) {
//                                    // Else, if the item is already in the array, remove it
//                                    mSelectedItems.remove(Integer.valueOf(which));
//                                }
//                            }
//                        })
                // Set the action buttons
//                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User clicked OK, so save the mSelectedItems results somewhere
//                        // or return them to the component that opened the dialog
//                        ...
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        ...
//                    }
//                });

        return builder.create();
    }
}

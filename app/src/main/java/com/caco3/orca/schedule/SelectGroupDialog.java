package com.caco3.orca.schedule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.caco3.orca.R;
import com.caco3.orca.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A dialog where user is able to select a group schedule for he wants to see using {@link Spinner}.
 * Parent fragment has to implement {@link OnGroupSelected} interface
 * Don't instantiate this dialog directly, use static factory method {@link #with(ArrayList)}
 * @see OnGroupSelected
 * @see #with(ArrayList)
 */
public class SelectGroupDialog extends DialogFragment {
    /**
     * Private key for list of groups which will be used to populate {@link #selectGroupSpinner}
     */
    private static final String KEY_GROUP_NAMES_LIST
            = "group_names";

    @BindView(R.id.dialog_select_group_spinner)
    Spinner selectGroupSpinner;

    /**
     * Fragment that creates this dialog has to implement this interface.
     * Otherwise {@link ClassCastException} will be thrown
     *
     * Callback for parent fragment.
     *
     * When user has selected a group invokes {@link #onGroupSelected(String)} method
     */
    /*package*/ interface OnGroupSelected {
        /**
         * Method which will be invoked on parent fragment, when user clicks ok
         * @param selectedGroupName selected group name
         */
        void onGroupSelected(String selectedGroupName);
    }

    /**
     * Static factory method creates new instance and adds as argument provided list
     * which will be used to populate spinner
     * @param groupNames to populate spinner with
     *                   type is <code>ArrayList<String></code> instead of <code>List<String></code>
     *                   because we will retrieve it using {@link Bundle#getStringArrayList(String)}
     * @return {@link SelectGroupDialog}
     */
    public static SelectGroupDialog with(ArrayList<String> groupNames) {
        Bundle args = new Bundle(1);
        args.putStringArrayList(KEY_GROUP_NAMES_LIST, groupNames);
        SelectGroupDialog dialog = new SelectGroupDialog();
        dialog.setArguments(args);

        return dialog;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_select_group_dialog, null, false);
        ButterKnife.bind(this, view);


        List<String> list
                = Preconditions.checkNotNull(getArguments()
                .getStringArrayList(KEY_GROUP_NAMES_LIST), "provided null instead of ArrayList of strings. " +
                "Did you instantiated this dialog using static factory method?");
        final String[] groupNames = list.toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, groupNames);
        selectGroupSpinner.setAdapter(adapter);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.select_group)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pos = selectGroupSpinner.getSelectedItemPosition();
                        ((OnGroupSelected)getTargetFragment())
                                .onGroupSelected(groupNames[pos]);
                        dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }
}

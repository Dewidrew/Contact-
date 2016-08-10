package ayp.aug.contact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.io.Serializable;
import java.util.UUID;

import ayp.aug.contact.model.Contact;
import ayp.aug.contact.model.ContactLab;

/**
 * Created by Hattapong on 8/10/2016.
 */
public class ContactDeleteDialog extends DialogFragment {
    private static final String CONTACT_ID = "contact_id";
    private ContactFragment.Callbacks callback;

    public static ContactDeleteDialog newInstance(Contact contact){
        ContactDeleteDialog contactDeleteDialog = new ContactDeleteDialog();
        Bundle args = new Bundle();
        args.putSerializable(CONTACT_ID, contact.getUuid());
        contactDeleteDialog.setArguments(args);
        return contactDeleteDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final UUID contact = (UUID) getArguments().getSerializable(CONTACT_ID);
        callback = (ContactFragment.Callbacks)getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ContactLab.getInstance(getActivity()).deleteContact(contact);
                callback.onDelete();
                dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }
}

package ayp.aug.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.UUID;

import ayp.aug.contact.model.Contact;
import ayp.aug.contact.model.ContactLab;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactFragment extends Fragment {

    private static final String CONTACT_ID = "Contact_ID";
    private static final int REQUEST_CAPTURE_PHOTO = 1123;

    private EditText editTextName;
    private EditText editTextTel;
    private EditText editTextEmail;
    private Button deleteButton;
    private ImageView imageView;
    private ImageButton imageButton;
    private File photoFile;
    private Contact contact;

    private Callbacks callbacks;

    public UUID getContactId() {
        if(this.contact != null){
            return this.contact.getUuid();
        }
        return null;
    }

    public void getUpdateUI() {
        reloadContactDB();
    }

    private void reloadContactDB() {
        ContactLab contactLab = ContactLab.getInstance(getActivity());

        if (getArguments().get(CONTACT_ID) != null) {
            UUID contactId = (UUID) getArguments().getSerializable(CONTACT_ID);
            contact = ContactLab.getInstance(getActivity()).getContactById(contactId);
        } else {
            Contact contact = new Contact();
            contactLab.addContact(contact);
            this.contact = contact;
        }
    }


    public interface Callbacks {
        void onUpdated(Contact contact);
        void onDelete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CAPTURE_PHOTO) {
            updateImageView();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public static ContactFragment newInstance(UUID uuid) {

        Bundle args = new Bundle();
        args.putSerializable(CONTACT_ID,uuid);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reloadContactDB();
        photoFile = ContactLab.getInstance(getActivity()).getPhotoFile(contact);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_edit_detail,container,false);

        editTextName = (EditText) v.findViewById(R.id.edit_text_name);
        editTextName.setText(contact.getName());
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contact.setName(s.toString());
                updateContact();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextTel = (EditText) v.findViewById(R.id.edit_text_tel);
        editTextTel.setText(contact.getTelephoneNo());
        editTextTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contact.setTelephoneNo(s.toString());
                updateContact();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextEmail = (EditText) v.findViewById(R.id.edit_text_email);
        editTextEmail.setText(contact.getEmail());
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contact.setEmail(s.toString());
                updateContact();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        deleteButton = (Button) v.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            FragmentManager fm = getFragmentManager();
                ContactDeleteDialog contactDeleteDialog = ContactDeleteDialog.newInstance(contact);
                contactDeleteDialog.show(fm, "deleteDialog");
            }
        });

        imageButton = (ImageButton) v.findViewById(R.id.image_button);
        imageView = (ImageView) v.findViewById(R.id.image_view);

        PackageManager packageManager = getActivity().getPackageManager();

        final Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        final boolean canTakePhoto = photoFile != null
                && captureImageIntent.resolveActivity(packageManager) != null;

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(photoFile);
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        //On click -> start activity for camera
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImageIntent, REQUEST_CAPTURE_PHOTO);
            }
        });

        updateImageView();

        return v;
    }

    private void updateContact() {
        ContactLab.getInstance(getActivity()).updateContact(contact);
        callbacks.onUpdated(contact);
    }

    protected void updateImageView() {
        if (photoFile == null || !photoFile.exists()) {
            imageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
            imageView.setImageBitmap(bitmap);
        }
    }

}

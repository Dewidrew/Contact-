package ayp.aug.contact;

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

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactFragment extends Fragment {

    private static final int REQUET_CAPTURE_PHOTO = 137;

    private EditText editTextName;
    private EditText editTextTel;
    private EditText editTextEmail;
    private Button deleteButton;
    private ImageView imageView;
    private ImageButton imageButton;
    private File photoFile;

    private Callbacks callbacks;

    public interface Callbacks {
        void onUpdated();
        void onDelete();
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

    public void upDateData() {
        /*CrimeLab.getInstance(getActivity()).updateCrime(crime); //update crime in DB
        callbacks.onCrimeUpdated(crime);*/ //TODO UPDATE DATABASE
    }

    public static ContactFragment newInstance() {

        Bundle args = new Bundle();

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*photoFile = //TODO GET PHOTO FILE FROME DATABASE*/

        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_edit_detail,container,false);

        editTextName = (EditText) v.findViewById(R.id.edit_text_name);
        editTextName.setText("");
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextTel = (EditText) v.findViewById(R.id.edit_text_tel);
        editTextTel.setText("");
        editTextTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextEmail = (EditText) v.findViewById(R.id.edit_text_email);
        editTextEmail.setText("");
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        deleteButton = (Button) v.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*CrimeLab.getInstance(getActivity()).deleteCrime(crime.getId());*/ //TODO DELETE DATA
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
                startActivityForResult(captureImageIntent, REQUET_CAPTURE_PHOTO);
            }
        });

        updateImageView();

        return v;
    }

    private void updateUI() {
        /*UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrimesById(crimeId);*/ //TODO GET DATA
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

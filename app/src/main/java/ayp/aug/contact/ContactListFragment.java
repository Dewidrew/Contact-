package ayp.aug.contact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import ayp.aug.contact.model.Contact;
import ayp.aug.contact.model.ContactLab;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactListFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1231;
    private RecyclerView _contactRecycleView;
    private ContactAdapter _contactAdapter;
    private Callbacks callbacks;
    private TextView emptyTxt;
    private Contact contact;
    protected static int colume;

    public interface Callbacks {
        void onContactSelected(Contact contact);
        void onOpenSelectFirst();
        void onSetColumn();
    }

    public static ContactListFragment newInstance() {

        Bundle args = new Bundle();

        ContactListFragment fragment = new ContactListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
        callbacks.onOpenSelectFirst();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_contact:
                Contact contact = new Contact();
                ContactLab.getInstance(getActivity()).addContact(contact);

                //support tablet
                updateUI();
                callbacks.onContactSelected(contact);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_show_contact, container, false);
        callbacks.onSetColumn();
        _contactRecycleView = (RecyclerView) v.findViewById(R.id.contact_recycler_view);
        _contactRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), colume));

        emptyTxt = (TextView) v.findViewById(R.id.contact_list_null);

        updateUI();

        return v;
    }

    public void updateUI() {
        final ContactLab contactLab = ContactLab.getInstance(getActivity());

        List<Contact> contacts = contactLab.getContact(); // create list

        if (_contactAdapter == null) {
            _contactAdapter = new ContactAdapter(contacts); // set list to Adapter
            _contactRecycleView.setAdapter(_contactAdapter); // set Adapter to recycleview

        } else {
            _contactAdapter.setContact(contactLab.getContact());
            _contactAdapter.notifyDataSetChanged();

        }

        if (contactLab.getContact().size() == 0) {
            emptyTxt.setVisibility(View.VISIBLE);
        } else {
            emptyTxt.setVisibility(View.GONE);
        }

    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
        private List<Contact> contacts;

        public ContactAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_contact, parent, false);
            return new ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(ContactHolder holder, int position) {
            Contact contact = contacts.get(position);
            holder.bind(contact);
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        public void setContact(List<Contact> contact) {
            this.contacts = contact;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContactLab.getInstance(getActivity()).getContact().get(ContactLab.getInstance(getActivity()).getContact().size()-1).getName() == null){

            ContactLab.getInstance(getActivity())
                    .deleteContact(ContactLab
                            .getInstance(getActivity())
                            .getContact()
                            .get(ContactLab.getInstance(getActivity()).getContact().size()-1)
                            .getUuid());
        }

        updateUI();
    }

    private class ContactHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private ImageView imageView;
        private TextView name;
        private Contact contact;
        private File photoFile;


        public ContactHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image_contact_list);
            name = (TextView) v.findViewById(R.id.list_item_contact_title);
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);

        }

        public void bind(Contact contact) {
            this.contact = contact;
            name.setText(contact.getName());
            photoFile = ContactLab.getInstance(getActivity()).getPhotoFile(contact);
            updatePhotoView();
        }

        private void updatePhotoView() {
            if (photoFile == null || !photoFile.exists()) {
                imageView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
                imageView.setImageBitmap(bitmap);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            callbacks.onContactSelected(contact);
            return true;
        }

        @Override
        public void onClick(View view) {

            if (contact.getTelephoneNo() != null) {
                onCalling(contact);
            }
    }
    }

    public void onCalling(Contact contact) {
        this.contact = contact;
        if (!hasCallPermission()) {
            callSuspect();
        }
    }

    private boolean hasCallPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return true;
        }
        return false;
    }

    private void callSuspect() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contact.getTelephoneNo()));
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callSuspect();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }
}

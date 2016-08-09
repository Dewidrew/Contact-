package ayp.aug.contact;

import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ayp.aug.contact.model.Contact;
import ayp.aug.contact.model.ContactLab;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactListFragment extends Fragment {
    private RecyclerView _contactRecycleView;
    private ContactAdapter _contactAdapter;


    public static ContactListFragment newInstance() {

        Bundle args = new Bundle();

        ContactListFragment fragment = new ContactListFragment();
        fragment.setArguments(args);
        return fragment;
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
//                callbacks.onCrimeSelected(crime);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_show_contact, container, false);

        _contactRecycleView = (RecyclerView) v.findViewById(R.id.contact_recycler_view);
        _contactRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

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

    }

    private class ContactAdapter extends RecyclerView.Adapter {
        private List<Contact> contacts;

        public ContactAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_contact, parent, false);
            return new ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        public void setContact(List<Contact> contact) {
            this.contacts = contact;
        }
    }

    private class ContactHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private ImageView imageView;
        private TextView name;
        private Contact contact;

        public ContactHolder(View v) {
            super(v);
            imageView = (ImageView)v.findViewById(R.id.image_contact_list);
            name = (TextView)v.findViewById(R.id.list_item_contact_title);
            v.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}

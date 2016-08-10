package ayp.aug.contact;

import android.content.Intent;
import android.support.v4.app.Fragment;

import ayp.aug.contact.model.Contact;
import ayp.aug.contact.model.ContactDbSchema;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactListActivity extends SingleFragmentActivity implements ContactListFragment.Callbacks ,ContactFragment.Callbacks{
    @Override
    protected Fragment onCreateFragment() {
        return ContactListFragment.newInstance();
    }

    @Override
    public void onContactSelected(Contact contact) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            //single pane
            Intent intent = ContactActivity.newIntent(this, contact.getUuid());
            startActivity(intent);
        } else {

            ContactFragment currentDetailFragment = (ContactFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
            if (currentDetailFragment == null || !currentDetailFragment.getContactId().equals(contact.getUuid())) {
                //two pane
                Fragment newDetailFragment = ContactFragment.newInstance(contact.getUuid());
                //replace old fragment with new one
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetailFragment).commit();
            }else{
                currentDetailFragment.getUpdateUI();

            }
        }
    }

    @Override
    public void onOpenSelectFirst() {

    }

    @Override
    public void onUpdated(Contact contact) {
        ContactListFragment listFragment = (ContactListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onDelete() {
        ContactListFragment listFragment = (ContactListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        ContactFragment contactFragment = (ContactFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);


        listFragment.updateUI();

        //clear
        getSupportFragmentManager().beginTransaction().detach(contactFragment).commit();
    }
}

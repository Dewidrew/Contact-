package ayp.aug.contact;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import ayp.aug.contact.model.Contact;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactActivity extends SingleFragmentActivity implements ContactFragment.Callbacks{
    private static final String CONTACT_ID = "contact_id";
    private UUID _contactId;

    @Override
    protected Fragment onCreateFragment() {
        getSupportActionBar().setTitle("Edit");
        _contactId = (UUID) getIntent().getSerializableExtra(CONTACT_ID);
        return ContactFragment.newInstance(_contactId);
    }

    public static Intent newIntent(Context activity, UUID id) {
        Intent intent = new Intent(activity, ContactActivity.class);
        intent.putExtra(CONTACT_ID, id);
        return intent;
    }


    @Override
    public void onUpdated(Contact contact) {

    }

    @Override
    public void onDelete() {
        finish();
    }
}

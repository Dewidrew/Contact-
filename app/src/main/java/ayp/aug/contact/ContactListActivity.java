package ayp.aug.contact;

import android.support.v4.app.Fragment;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment onCreateFragment() {
        return ContactListFragment.newInstance();
    }
}

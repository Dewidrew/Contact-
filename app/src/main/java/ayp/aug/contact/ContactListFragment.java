package ayp.aug.contact;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactListFragment extends Fragment{
    public static ContactListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ContactListFragment fragment = new ContactListFragment();
        fragment.setArguments(args);
        return fragment;
    }


}

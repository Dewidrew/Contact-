package ayp.aug.contact;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        //Create Fragment Container
        FragmentManager fm = getSupportFragmentManager();
        Fragment f =fm.findFragmentById(R.id.fragment_container);


        if( f == null){
            f = onCreateFragment(); // Create Fragment

            // Set Fragment to Fragment Container
            fm.beginTransaction()
                    .add(R.id.fragment_container,f)
                    .commit();
        }
    }
    protected abstract Fragment onCreateFragment();
}

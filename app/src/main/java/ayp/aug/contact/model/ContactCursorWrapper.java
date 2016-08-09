package ayp.aug.contact.model;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.CpuUsageInfo;

import java.util.Date;
import java.util.UUID;

import ayp.aug.contact.model.ContactDbSchema.ContactTable;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactCursorWrapper extends CursorWrapper {
    public ContactCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Contact getContact(){
        String uuidString = getString(getColumnIndex(ContactTable.Cols.UUID));
        String name = getString(getColumnIndex(ContactTable.Cols.NAME));
        String telephone = getString(getColumnIndex(ContactTable.Cols.TELEPHONE));
        String email = getString(getColumnIndex(ContactTable.Cols.EMAIL));

        Contact contact = new Contact(UUID.fromString(uuidString));
        contact.setName(name);
        contact.setEmail(email);
        contact.setTelephoneNo(telephone);

        return contact;
    }
}

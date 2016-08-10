package ayp.aug.contact.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ayp.aug.contact.model.ContactDbSchema.ContactTable;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactLab {
    private static ContactLab instance;

    private Context context;
    private SQLiteDatabase database;


    public static ContactLab getInstance(Context context) {
        if (instance == null) {
            instance = new ContactLab(context);
        }
        return instance;
    }

    private ContactLab(Context context){
        this.context = context.getApplicationContext();
        ContactBaseHelper contactBaseHelper = new ContactBaseHelper(context);
        database = contactBaseHelper.getWritableDatabase();

    }

    public static ContentValues getContentValues(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactTable.Cols.UUID, contact.getUuid().toString());
        contentValues.put(ContactTable.Cols.NAME, contact.getName());
        contentValues.put(ContactTable.Cols.EMAIL, contact.getEmail());
        contentValues.put(ContactTable.Cols.TELEPHONE, contact.getTelephoneNo());
        return contentValues;
    }

    public void addContact(Contact contact) {
        ContentValues contentValues = getContentValues(contact);
        database.insert(ContactTable.NAME,null,contentValues);
    }

    public Contact getContactById(UUID uuid) {
        ContactCursorWrapper cursor = queryContacts(ContactTable.Cols.UUID + " = ?",new String[]{uuid.toString()});

        try{
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getContact();
        }finally {
            cursor.close();
        }

    }

    public List<Contact> getContact() {
        List<Contact> contacts = new ArrayList<>();
        ContactCursorWrapper cursor = queryContacts(null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                contacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return contacts;
    }

    public void deleteContact(UUID uuid) {
        database.delete(ContactTable.NAME,ContactTable.Cols.UUID + " = ?",new String[]{uuid.toString()});

    }

    public ContactCursorWrapper queryContacts(String whereCause, String[] whereArgs){
        Cursor cursor = database.query(ContactTable.NAME,null,whereCause,whereArgs,null,null,null);
        return new ContactCursorWrapper(cursor);
    }

    public void updateContact(Contact contact){
        String uuidStr = contact.getUuid().toString();
        ContentValues contentValues = getContentValues(contact);
        database.update(ContactTable.NAME,contentValues,ContactTable.Cols.UUID + "= ?",new String[]{uuidStr});
    }

    public File getPhotoFile(Contact contact){
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFilesDir == null){
            return null;
        }

        return new File(externalFilesDir, contact.getPhotoFilename());
    }


}

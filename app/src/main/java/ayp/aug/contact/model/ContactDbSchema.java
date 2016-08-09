package ayp.aug.contact.model;

/**
 * Created by Hattapong on 8/9/2016.
 */
public class ContactDbSchema {
    public static final class ContactTable{
        public static final String NAME = "contact";

        public static final class Cols{
            public static final String UUID = "UUID";
            public static final String NAME = "name";
            public static final String EMAIL = "email";
            public static final String TELEPHONE = "telephone";
        }
    }
}
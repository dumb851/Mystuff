package com.zubrid.mystuff.database;

public class DbSchemas {

    public static final class ItemsTable {
        public static final String NAME = "items";

        public static final class Cols {

            public static final String UUID   = "uuid";
            public static final String TITLE  = "title";
            public static final String DATE   = "lastSaved";

        }
    }

    public static final class LabelsTable {
        public static final String NAME = "Labels";

        public static final class Cols {

            public static final String UUID   = "uuid";
            public static final String TITLE  = "title";
            public static final String DATE   = "lastSaved";

        }
    }

    public static final class ItemLabelsTable {
        public static final String NAME = "ItemLabels";

        public static final class Cols {

            public static final String UUID_ITEM   = "uuid_item";
            public static final String UUID_LABEL  = "uuid_label";

        }
    }
}

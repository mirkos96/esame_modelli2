package org.personalized.dashboard.utils;

/**
 * Created by sudan on 25/5/15.
 */
public class Constants {

    public static final int URL_MAX_LENGTH = 300;
    public static final int TITLE_MAX_LENGTH = 50;
    public static final int CONTENT_MAX_LENGTH = 1000;
    public static final int TEXT_MAX_LENGTH = 3000;
    public static final int TAG_MAX_LENGTH = 25;

    public static final String COOKIE_PREFIX = "coo";
    public static final int COOKIE_LENGTH = 35;

    public static final String USERS = "users";
    public static final String USER_PREFIX = "USR";

    public static final String SESSIONS = "sessions";

    public static final String BOOKMARKS = "bookmarks";
    public static final String BOOKMARK_PREFIX = "BOK";

    public static final String NOTES = "notes";
    public static final String NOTE_PREFIX = "NOT";

    public static final String PINS = "pins";
    public static final String PIN_PREFIX = "PIN";

    public static final String TODOS = "todos";
    public static final String TODO_PREFIX = "TOD";

    public static final String TASK_PREFIX = "TAS";
    public static final int MAX_TASK_SIZE = 10;

    public static final String EXPENSES = "expenses";
    public static final String EXPENSE_PREFIX = "EXP";

    public static final String DIARIES = "diaries";
    public static final String PAGE_PREFIX = "PAG";

    public static final int ID_LENGTH = 16;
    public static final int MAX_BATCH_SIZE = 20;

    public static final String ACTIVITIES = "activities";
    public static final String ACTIVITIES_PREFIX = "ACT";
    public static final int ACTIVITIES_LIMIT = 30;

    public static final String SET_OPERATION = "$set";
    public static final String ADD_TO_SET_OPERATION = "$addToSet";
    public static final String EACH = "$each";
    public static final String GREATER_THAN_EQUAL = "$gte";
    public static final String LESS_THAN_EQUAL = "$lte";
    public static final String NOT_EQUAL_TO = "$ne";
    public static final String IN = "$in";

    public static final String SEPARATOR = "     ";

    public static final int ES_LIMIT = 25;
    public static final int ES_OFFSET = 0;

    public static final int MAX_TAGS_LENGTH = 10;

    public static final String SECONDARY_SEPARATOR = "###";

    public static final String COOKIE_NAME = "ipersonal-cookie";
    public static final String AUTH_HEADER = "X-Auth-Token";
}

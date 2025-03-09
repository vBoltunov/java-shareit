package shareit.util;

@SuppressWarnings("squid:S1075")
public class PathConstants {

    private PathConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    // Hardcoded URI paths are used here intentionally as they are fixed parts of the API
    // specification and reused across multiple controllers for consistency and readability.
    public static final String ITEMS_PATH = "/items";
    public static final String ITEM_ID_PATH = "/{item-id}";
    public static final String OWNER_PATH = "/owner";
    public static final String SEARCH_PATH = "/search";
    public static final String COMMENT_PATH = "/comment";
    public static final String BOOKINGS_PATH = "/bookings";
    public static final String BOOKING_ID_PATH = "/{booking-id}";
    public static final String USERS_PATH = "/users";
    public static final String USER_ID_PATH = "/{user-id}";
    public static final String REQUESTS_PATH = "/requests";
    public static final String REQUESTS_ALL_PATH = "/all";
    public static final String REQUEST_ID_PATH = "/{request-id}";
}

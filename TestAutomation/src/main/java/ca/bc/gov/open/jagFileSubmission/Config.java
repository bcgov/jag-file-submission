package ca.bc.gov.open.jagFileSubmission;

import java.math.BigDecimal;

public class Config {

    public static final String 		SELECTED_DRIVER = Constants.CHROME_DRIVER;
    //public static final String 		SELECTED_DRIVER = Constants.FIREFOX_DRIVER;
    //public static final String 		SELECTED_DRIVER = Constants.EDGE_DRIVER;
    //public static final String		ENVIROMENT		= Constants.DEV;
    public static final String		ENVIROMENT		= Constants.DEV;


    public static String			TEST_SCENARIO						= Constants.NOT_SET;
    public static boolean			SKIP_SCREENSHOTS_OF_SHOPPING_CART	= false;				//not implemented yet

    public static final int			TIMEOUT						= 30;							//the time webdriver waits before giving up (in seconds)
    public static final int			SLEEP_TIME_FULL_REFRESH		= 3500;							//time used in Thread.sleep() in milliseconds
    public static final int			SLEEP_TIME_PART_REFRESH		= SLEEP_TIME_FULL_REFRESH/2;	//time used in Thread.sleep() in milliseconds

    //app constants
    public static BigDecimal CLAIM_CREDITS_PER_AREA 		= new BigDecimal("25");
    public static String			CLIENT_ID 					= Constants.NOT_SET;

}

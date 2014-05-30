package ir.khabarefori.Activity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import ir.khabarefori.SplashScreenActivity;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class ir.khabarefori.Activity.SplashScreenActivityTest \
 * ir.khabarefori.tests/android.test.InstrumentationTestRunner
 */
public class SplashScreenActivityTest extends ActivityUnitTestCase<SplashScreenActivity> {

    protected Intent splashScreenActivity;

    public SplashScreenActivityTest() {
        super(SplashScreenActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        splashScreenActivity = new Intent(getInstrumentation()
                .getTargetContext(), SplashScreenActivity.class);
    }

    @MediumTest
    public void test_activity_is_created() {
        assertNotNull("Intent was null", splashScreenActivity);
        startActivity(splashScreenActivity, null, null);
    }
}

package ir.khabarefori.Activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import ir.khabarefori.MyActivity;
import ir.khabarefori.service.ServiceCheckServer;

/**
 * Created by Hani on 5/27/14.
 */
public class MyActivityTest extends ActivityUnitTestCase<MyActivity> {

    public MyActivityTest() {
        super(MyActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @MediumTest
    public void test_is_activity_created() {
        Intent myActivity= new Intent(getInstrumentation()
                .getTargetContext(), MyActivity.class);
        assertNotNull("Intent was null", myActivity);
        startActivity(myActivity, null, null);
    }
}

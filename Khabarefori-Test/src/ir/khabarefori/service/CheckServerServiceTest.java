package ir.khabarefori.service;

import android.content.Intent;
import android.test.ServiceTestCase;
import ir.khabarefori.MyActivity;

/**
 * Created by Hani on 5/27/14.
 */
public class CheckServerServiceTest extends ServiceTestCase<ServiceCheckServer>{

    public CheckServerServiceTest() {
        super(ServiceCheckServer.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
}

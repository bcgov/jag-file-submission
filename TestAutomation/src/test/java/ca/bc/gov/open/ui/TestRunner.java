package ca.bc.gov.open.ui;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestRunner {

    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(VerifyNoticeSubmitted.class, FLAnewSession.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}

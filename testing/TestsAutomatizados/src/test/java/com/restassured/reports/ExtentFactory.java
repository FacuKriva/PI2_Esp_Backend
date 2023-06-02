package com.restassured.reports;

import com.aventstack.extentreports.ExtentReports;

public class ExtentFactory {

    public static ExtentReports getInstance() {
        ExtentReports extent = new ExtentReports();
        extent.setSystemInfo("Restassured", "5.3.0");
        extent.setSystemInfo("Mac Os", "Mac");
        return extent;
    }

}
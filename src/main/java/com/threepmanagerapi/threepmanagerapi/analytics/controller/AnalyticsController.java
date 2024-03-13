package com.threepmanagerapi.threepmanagerapi.analytics.controller;

import com.threepmanagerapi.threepmanagerapi.analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sales")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;
    @GetMapping("/analytics/get")
    public ResponseEntity getAnalytics(@RequestHeader("Authorization") String token) {
        return analyticsService.getAnalytics();
    }
    @GetMapping("/salesAnalytics/get")
    public ResponseEntity getsalesAnalytics(@RequestHeader("Authorization") String token) {
        return analyticsService.getChartData();
    }
}

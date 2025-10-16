package com.maybank.platform.services.restapi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Timer;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartClientData {
    private String token;
    private Integer mode;
    private String input;
    private String brand;
    private Timer timer;
}

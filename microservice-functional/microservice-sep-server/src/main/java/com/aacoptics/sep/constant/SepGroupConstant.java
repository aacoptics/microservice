package com.aacoptics.sep.constant;

import java.util.HashMap;
import java.util.Map;

public interface SepGroupConstant {
    Map<String, String> GROUP_MAP = new HashMap() {
        {
            put("01_ImagingDevice", "474A02F50AE94132420A7FCB54116797");
            put("02_UsbKey", "7CE7C1520AE9413208BAECAD7C21CE28");
            put("03_Print", "CDDE0F140AE9413244CEED812EB0CC55");
            put("04_Bluetooth+Print", "49CB77090AE94132301C204163A67C2B");
            put("05_Imaging_Print", "B7EF00910AE941324AFCF00CD3301287");
            put("06_Imaging_Print_UsbKey", "01342B530AE941323DF638E505B8C5D0");
            put("07_COM_sinal", "7B5905B10AE941324477534200DD62A5");
            put("08_only_in", "48BE50180AE9413250845E154C2BD3AC");
            put("default", "F23D06720AE94132370C311A70785B4A");
        }
    };
}

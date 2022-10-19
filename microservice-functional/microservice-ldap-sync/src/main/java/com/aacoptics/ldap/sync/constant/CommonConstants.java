package com.aacoptics.ldap.sync.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用常量信息
 *
 * @author Yan Shangqi
 */
public class CommonConstants {
    /**
     * UTF-8 字符集
     */
    public static final String WIFI_NEMPLOYEE = "CN=WiFi-Nemployee,OU=WIFI,OU=Security_Groups,OU=AACOPTICS_Groups,DC=aacoptics,DC=com";

    public static final String PRINT_MAIN = "CN=B-Print-AACOPTICS-CN-Main,OU=Print,OU=Security_Groups,OU=AACOPTICS_Groups,DC=aacoptics,DC=com";

    public static final String PRINT_SHENZHEN = "CN=B-Print-AACOPTICS-CN-Shenzhen,OU=Print,OU=Security_Groups,OU=AACOPTICS_Groups,DC=aacoptics,DC=com";

    public static final String PRINT_SHUYANG = "CN=B-Print-AACOPTICS-CN-Shuyang,OU=Print,OU=Security_Groups,OU=AACOPTICS_Groups,DC=aacoptics,DC=com";

    public static final String MAIL_DATABASE_1 = "CN=CNEU01,CN=Databases,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=AACOPTISC,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=aacoptics,DC=com";

    public static final String MAIL_DATABASE_2 = "CN=CNEU02,CN=Databases,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=AACOPTISC,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=aacoptics,DC=com";

    public static final String MAIL_DATABASE_3 = "CN=CNEU03,CN=Databases,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=AACOPTISC,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=aacoptics,DC=com";

    public static final String MAIL_DATABASE_4 = "CN=CNEU04,CN=Databases,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=AACOPTISC,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=aacoptics,DC=com";

    public static final String MAIL_DATABASE_5 = "CN=CNEU05,CN=Databases,CN=Exchange Administrative Group (FYDIBOHF23SPDLT),CN=Administrative Groups,CN=AACOPTISC,CN=Microsoft Exchange,CN=Services,CN=Configuration,DC=aacoptics,DC=com";


    public static final Map<String, String> AREA_MAP;

    static {
        AREA_MAP = new HashMap<>();
        AREA_MAP.put("1023", "OU=nanning,OU=CNS");
        AREA_MAP.put("1004", "OU=changzhou,OU=CNE");
        AREA_MAP.put("1002", "OU=shenzhen,OU=CNS");
        AREA_MAP.put("1035", "OU=chongqing,OU=CNW");
        AREA_MAP.put("1011", "OU=nanjing,OU=CNE");
        AREA_MAP.put("1025", "OU=suzhou,OU=CNE");
        AREA_MAP.put("1006", "OU=changzhou,OU=CNE");
        AREA_MAP.put("1001", "OU=shenzhen,OU=CNS");
        AREA_MAP.put("1008", "OU=suzhou,OU=CNE");
        AREA_MAP.put("1010", "OU=wuhan,OU=CNC");
        AREA_MAP.put("1005", "OU=changzhou,OU=CNE");
        AREA_MAP.put("1007", "OU=shuyang,OU=CNE");
        AREA_MAP.put("1009", "OU=SA,OU=beijing,OU=CNN");
        AREA_MAP.put("1015", "OU=SA,OU=VN,OU=AP");
        AREA_MAP.put("1021", "OU=JAP,OU=AP");
    }

    public static final Map<String, String> DEPT_MAP;

    static {
        DEPT_MAP = new HashMap<>();
        DEPT_MAP.put("30024849", "OU=TO");
        DEPT_MAP.put("30024809", "OU=IPD");
        DEPT_MAP.put("30025034", "OU=SD");
        DEPT_MAP.put("14000001", "OU=RD");
        DEPT_MAP.put("30018615", "OU=AODD");
        DEPT_MAP.put("30024848", "OU=AU");
        DEPT_MAP.put("30024837", "OU=HR");
        DEPT_MAP.put("30024825", "OU=MCPL");
        DEPT_MAP.put("30024799", "OU=LCPL");
        DEPT_MAP.put("30017022", "OU=OMPL");
        DEPT_MAP.put("30024845", "OU=PS");
        DEPT_MAP.put("30024844", "OU=FI");
        DEPT_MAP.put("30024843", "OU=IT");
        DEPT_MAP.put("30024847", "OU=ID");
        DEPT_MAP.put("30024931", "OU=LCPL");
    }
}
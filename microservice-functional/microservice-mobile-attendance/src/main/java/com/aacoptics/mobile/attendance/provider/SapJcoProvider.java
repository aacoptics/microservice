package com.aacoptics.mobile.attendance.provider;

import com.aacoptics.mobile.attendance.config.SapConnectConfig;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

@Component
@Slf4j
public class SapJcoProvider {

    @Resource
    private SapConnectConfig sapConnectConfig;

    private static String ABAP_AS = "ABAP_AS_WITHOUT_POOL";
    private JCoDestination destination;


    @PostConstruct
    public void connect() throws Exception {

        // 设置SAP的连接参数
        Properties connectProperties = new Properties();
        connectProperties.clear();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, sapConnectConfig.getHost()); //服务器IP
        connectProperties
                .setProperty(DestinationDataProvider.JCO_SYSNR, sapConnectConfig.getSystem()); //系统标识号
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,
                sapConnectConfig.getClientName()); //客户端
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, sapConnectConfig.getUserid()); //用户名
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,
                sapConnectConfig.getPassword()); //密码
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,
                sapConnectConfig.getLanguage()); //语言
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,
                sapConnectConfig.getJcoPeakLimit()); //最大连接线程

        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "0");
        connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "1000");  //此后以ms为单位的周期，目的地检查所释放的连接是否过期
        connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_PERIOD, "1000");//在此之后，目的地检查所释放的连接是否过期，以ms为单位。
        try {
            // 创建DestinationDataProvider，
            createDataFile(ABAP_AS, "jcoDestination", connectProperties);
            destination = JCoDestinationManager.getDestination(ABAP_AS);

        } catch (JCoException ex) {
            log.error("SAP连接失败", ex);
            throw new Exception("SAP连接失败" + ex.getMessage());
        }
    }


    public JCoDestination getDestination() {
        try {
            destination = JCoDestinationManager.getDestination(ABAP_AS);
        } catch (JCoException e) {
            log.error("SAP连接失败", e);
        }
        return destination;
    }


    static void createDataFile(String name, String suffix, Properties properties)
            throws Exception {
        File cfg = new File(name + "." + suffix);
        try {
            FileOutputStream fos = new FileOutputStream(cfg, false);
            properties.store(fos, "ABAP_AS_WITHOUT_POOL");
            fos.close();
        } catch (Exception e) {
            log.error("不能创建SAP连接需要的Destination文件", e);
            throw new Exception("不能创建SAP连接需要的Destination文件" + cfg.getName());
        }
    }

    public JCoFunction getFunction(JCoDestination destination, String functionName) throws Exception {
        JCoFunction function = null;
        function = destination.getRepository().getFunctionTemplate(functionName).getFunction();
        return function;
    }

    /**
     * 获取员工信息
     * 例：{"ZMOBILE":"112345678901","GESCH":"1","PERNR":"XXXXXXXX","STAT2":"3","ZEMAIL":"XXX@AACOPTICS.COM","ICNUM":"XXXXXXXXXXX","ORGEH":"30008395","PHOTO":"","ZDATE":"2022-09-17 00:00:00","NAME":"XXX"}
     * @return
     * @throws Exception
     */
    public JSONArray getEmployeeInfoList(int idFlag) throws Exception {

        JCoDestination destination = this.getDestination();
        JCoFunction function = destination.getRepository().getFunction("ZHRPAFM069");
        JCoParameterList param = function.getImportParameterList();
      /*  1：全量：取所有人员信息
        2：增量：有变更的人员数据
        3：增量：单个同步
        4：全量zhrdc026-中间表获取
        5：全量-无照片*/
        param.setValue("ID_FLAG", idFlag);
        // param.setValue("ID_DATE", "20220902");//    日期
        function.execute(destination);

        JCoParameterList exportParameterList = function.getExportParameterList();

        String ED_RESULT = exportParameterList.getString("ED_RESULT");
        String E_MESG = exportParameterList.getString("E_MESG");

        log.info("ED_RESULT=" + ED_RESULT);
        log.info("E_MESG=" + E_MESG);

        JCoTable jCoTable = exportParameterList.getTable("ET_DATA");
        JSONArray employeeJsonArray = new JSONArray();
        for (int j = 0; j < jCoTable.getNumRows(); j++) {
            jCoTable.setRow(j);
            JCoFieldIterator iterator = jCoTable.getFieldIterator();
            JSONObject jsonObject = new JSONObject();
            while (iterator.hasNextField()) {
                JCoField field = iterator.nextField();
                jsonObject.put(field.getName(), field.getValue());
            }
            employeeJsonArray.add(jsonObject);
        }
        return employeeJsonArray;
    }

    public static void main(String[] args) throws Exception {
        String host ="10.233.74.41";//服务器IP
        String clientName ="801";//客户端
        String language ="zh";//语言
        String userid ="HCM_HRBPM";//用户名
        String password ="Sap#1234";//密码
        String system ="00";//系统标识号
        String JCO_PEAK_LIMIT="100";//最大连接线程
        // 设置SAP的连接参数
        Properties connectProperties = new Properties();
        connectProperties.clear();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, host);
        connectProperties
                .setProperty(DestinationDataProvider.JCO_SYSNR, system);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,
                clientName);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, userid);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,
                password);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,
                language);
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,
                JCO_PEAK_LIMIT);



        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "0");
        connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "1000");  //此后以ms为单位的周期，目的地检查所释放的连接是否过期
        connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_PERIOD, "1000");//在此之后，目的地检查所释放的连接是否过期，以ms为单位。
        JCoDestination destination = null;

        try {
            // 创建DestinationDataProvider，
            createDataFile(ABAP_AS, "jcoDestination", connectProperties);
            destination = JCoDestinationManager.getDestination(ABAP_AS);

        } catch (JCoException ex) {
            log.error("SAP连接失败", ex);
            throw new Exception("SAP连接失败" + ex.getMessage());
        }

        JCoFunction function = destination.getRepository().getFunction("ZHRPAFM069");
        JCoParameterList param = function.getImportParameterList();
      /*  1：全量：取所有人员信息
        2：增量：有变更的人员数据
        3：增量：单个同步
        4：全量zhrdc026-中间表获取
        5：全量-无照片*/
        param.setValue("ID_FLAG", 4);
        // param.setValue("ID_DATE", "20220902");//    日期
        function.execute(destination);


        JCoParameterList exportParameterList = function.getExportParameterList();

        String ED_RESULT = exportParameterList.getString("ED_RESULT");
        String E_MESG = exportParameterList.getString("E_MESG");
        System.out.println(ED_RESULT);
        System.out.println(E_MESG);
        JCoTable ouput = exportParameterList.getTable("ET_DATA");
        JSONArray rows = new JSONArray();
        for (int j = 0; j < ouput.getNumRows(); j++) {
            ouput.setRow(j);
            JCoFieldIterator iterator = ouput.getFieldIterator();
            JSONObject row = new JSONObject();
            while (iterator.hasNextField()) {
                JCoField field = iterator.nextField();
                row.put(field.getName(), field.getValue());
            }
            System.out.println(j + "   " + row);
            rows.add(row);
        }

    }
}

package com.aacoptics.notification.utils;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SqlSourceUtil {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * sql拼接
     * @param sqlString
     * @param parameters
     * @return
     */
    public String sqlSource(String sqlString, Map<String,Object> parameters){
        MybatisConfiguration configuration = (MybatisConfiguration)sqlSessionFactory.getConfiguration();
        LanguageDriver xmlLanguageDriver=new XMLLanguageDriver();
        final StringBuilder sqlXml = new StringBuilder();
        String[]strings=new String[]{sqlString};
        for (String fragment : strings) {
            sqlXml.append(fragment);
            sqlXml.append(" ");
        }
        SqlSource sqlSource=xmlLanguageDriver.createSqlSource(configuration, sqlXml.toString().trim(), Map.class);
        BoundSql boundSql=sqlSource.getBoundSql(parameters);
        final StringBuilder sql=new StringBuilder(boundSql.getSql());
        if(parameters!=null&&!parameters.isEmpty()){
            for (Object  parameter:parameters.values()) {
                int start=sql.indexOf("?");
                sql.replace(start,start+1,parameter.toString());
            }
        }
        return sql.toString();
    }

    /**
     * 带缓存sql，直接返回sql执行结果
     * @param sqlString
     * @param apiId
     * @param param
     * @return
     */
    public List<Map<String, Object>> query(String sqlString, Long apiId, Map<String,Object> param){
        String mastpId="sql."+apiId;
        MybatisConfiguration configuration=(MybatisConfiguration)sqlSessionFactory.getConfiguration();
        // if(!configuration.hasStatement(mastpId)){

            LanguageDriver xmlLanguageDriver=new XMLLanguageDriver();
            final StringBuilder sql = new StringBuilder();
            String[]strings = new String[]{sqlString};
            for (String fragment : strings) {
                sql.append(fragment);
                sql.append(" ");
            }
            SqlSource sqlSource = xmlLanguageDriver.createSqlSource(configuration, sql.toString().trim(), Map.class);

            BoundSql boundSql= sqlSource.getBoundSql(param);
            MappedStatement ms = new MappedStatement.Builder(configuration, mastpId, sqlSource, SqlCommandType.SELECT)
                    .resultMaps(new ArrayList<ResultMap>() {
                        {
                            add(new ResultMap.Builder(configuration, "defaultResultMap", Map.class, new ArrayList<>(0))
                                    .build());
                        }
                    }).build();
            // 缓存
            configuration.addMappedStatement(ms);
        // }
        DefaultSqlSession defaultSqlSession = (DefaultSqlSession) sqlSessionFactory.openSession();
        List<Map<String, Object>> resutls = defaultSqlSession.selectList(mastpId, param);

        defaultSqlSession.close();
        configuration.setCacheEnabled(false);

        return  resutls;
    }

}

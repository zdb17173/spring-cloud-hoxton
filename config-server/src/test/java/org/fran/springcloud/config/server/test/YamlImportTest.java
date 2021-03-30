package org.fran.springcloud.config.server.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author fran
 * @Description
 * @Date 2021/3/29 11:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class YamlImportTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test(){

        URL url = YamlImportTest.class.getClassLoader().getResource("");
        String path = url.getPath() + "repo";
        File repoFolder = new File(path);
        for(File ymlFile : repoFolder.listFiles()){
            try(FileInputStream in = new FileInputStream(ymlFile)){
                byte[] b = new byte[new Long(ymlFile.length()).intValue()];
                in.read(b);
                String yml = new String(b, "UTF-8");

                //appname-profile-label.yaml   appname-profile.yaml  appname.yaml
                String[] tags = ymlFile.getName().split("-");

                String application = "app";
                String profile = "profile";
                String label = "master";
                for(int i = 0; i < tags.length; i++){
                    String tag = tags[i];
                    if(i == (tags.length-1)){
                        if(tag.endsWith(".yml") || tag.endsWith(".yaml") || tag.endsWith(".properties")){
                            tag = tag.substring(0, tag.indexOf("."));
                        }else
                            throw new IllegalArgumentException("File prefix error["+ ymlFile.getName() +"] exp: .yml | .yaml | .properties" );
                    }
                    if(i == 0)
                        application = tag;
                    if(i == 1)
                        profile = tag;
                    if(i == 2)
                        label = tag;
                    if(i == 3)
                        throw new IllegalArgumentException("File name error["+ ymlFile.getName() +"] exp:appname-profile-label.yaml   appname-profile.yaml  appname.yaml");
                }

                addToDB(application, profile, label, yml, ymlFile.getName().endsWith(".properties"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void addToDB(String appName, String profile, String label, String yml, boolean isProperties){

        //get or save application
        Long appId = getAppId(appName, profile, label);
        if(appId.intValue() == 0){
            String sql = "insert into `config_application` " +
                    "( `app_name`, `profile`, `label`,  `create_time`, `update_time`) " +
                    "values ( ?, ?, ?, ?, ?);";
            jdbcTemplate.update(sql, new Object[]{appName, profile, label, new Date(), new Date()});
            appId = getAppId(appName, profile, label);
        }
        jdbcTemplate.update("delete from `config_property` where `app_id` = ?", appId);

        Properties properties = null;
        if(isProperties){
            properties = new Properties();
            try {
                properties.load(new StringReader(yml));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();

            Resource resource = new ByteArrayResource(yml.getBytes());
            yaml.setResources(resource);

            properties = yaml.getObject();
        }


        Set<String> propertiyKey = properties.stringPropertyNames();
        properties.entrySet();

        for (Map.Entry<Object, Object> objectObjectEntry : properties.entrySet()) {

            String key = (String) objectObjectEntry.getKey();

            Object value = objectObjectEntry.getValue();

            String sql = "insert into `config_property` " +
                    "( `property_key`, `property_value`, `app_id`,  `create_time`, `update_time`) " +
                    "values ( ?, ?, ?, ?, ?);";
            jdbcTemplate.update(sql, new Object[]{key, value, appId, new Date(), new Date()});
        }
    }

    public Long getAppId(String appName, String profile, String label){
        return jdbcTemplate.query("SELECT id FROM config_application WHERE app_name = ? AND PROFILE = ? AND label = ?",
                new Object[]{appName, profile, label},
                new ResultSetExtractor<Long>() {
                    @Override
                    public Long extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        if(resultSet.next())
                            return resultSet.getLong(1);
                        else
                            return 0l;
                    }
                });
    }
}

package com.lsh.atp.web.controller;

import com.lsh.atp.core.dao.jedis.JedisHashDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhangqiang on 17/7/19.
 */
@Controller
public class SwaggerController {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerController.class);

    private static final String SWAGGER_REDIS_KEY = "swagger:ui:json";

    @Autowired
    private JedisHashDao jedisHashDao;

    @RequestMapping("/swagger")
    public ModelAndView swaggerList(){
        Project[] projects = Project.values();
        return new ModelAndView("swaggerlist").addObject("projects",projects);
    }

    @RequestMapping("/swagger-ui/{key}")
    public ModelAndView swaggerUI(@PathVariable("key") String key){
        return new ModelAndView("swagger-ui").addObject("projectKey",key);
    }

    @ResponseBody
    @RequestMapping("/swagger-ui/{key}/json")
    public String getSwggerJson(@PathVariable("key") String key){
        return this.jedisHashDao.hget(SWAGGER_REDIS_KEY,key);
    }


    public enum Project{

        GROUPON_PROVIDER("groupon","团购");

        private String key;

        private String name;

        Project(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

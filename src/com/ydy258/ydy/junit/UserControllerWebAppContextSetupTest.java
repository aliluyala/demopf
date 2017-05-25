package com.ydy258.ydy.junit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//XML���
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextHierarchy({
        @ContextConfiguration(name = "parent", locations = "classpath:/config/applicationContext.xml"),
        @ContextConfiguration(name = "child", locations = "classpath:/config/applicationContext-mvc.xml")
})

//ע����
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration(value = "src/main/webapp")
//@ContextHierarchy({
//        @ContextConfiguration(name = "parent", classes = AppConfig.class),
//        @ContextConfiguration(name = "child", classes = MvcConfig.class)
//})
public class UserControllerWebAppContextSetupTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    
    @Test
    public void testView() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/common/getCargoType.do"))
                .andReturn();
        
        Assert.assertNotNull(result.getModelAndView().getModel().get("user"));
    }
}
package org.samurai.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Developer: Jim Hazen
 * Date: 7/7/13
 * Time: 6:24 PM
 */
@WebAppConfiguration
@ContextConfiguration("classpath*:applicationContext-webmvc.xml")
public class HealthcheckControllerTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeClass
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testHealthcheck() throws Exception
    {
        mockMvc.perform(get("/healthcheck").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
               .andExpect(jsonPath("$.response").value("good"));
    }
}

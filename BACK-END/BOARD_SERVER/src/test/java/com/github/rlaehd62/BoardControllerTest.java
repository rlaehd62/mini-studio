package com.github.rlaehd62;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest
{   
	@Test
	void test(@Autowired MockMvc mvc) throws Exception
	{
		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		param.add("id", "root");
		param.add("keyword", "Edit");
		
		MvcResult result = mvc
				.perform(get("/boards").params(param))
				.andExpect(status().isOk())
				.andReturn();
		
		String str = result.getResponse().getContentAsString();
		for(String line : str.split("}")) System.out.println(line);
	}

}

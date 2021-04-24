package com.github.rlaehd62;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.rlaehd62.service.implemention.DefaultAccountService;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TokenServiceTest
{
	@Autowired private DefaultAccountService accountService;

	@Test
	@Order(1)
	void test1()
	{
		HttpServletRequest httpServletRequest = new MockHttpServletRequest();
		HttpServletResponse httpServletResponse = new MockHttpServletResponse();
		RequestVO requestVO = RequestVO.builder()
				.request(httpServletRequest)
				.response(httpServletResponse)
				.build();
		
		AccountVO vo = new AccountVO("root", "root", "rlaehd62@naver.com", "KimDongDong", Collections.emptyList()); 
		TokenVO tokenVO = accountService.verifyAccount(vo, requestVO);
		
		assertNotNull(tokenVO.getACCESS_TOKEN());
		assertNotNull(tokenVO.getREFRESH_TOKEN());
		
		System.out.println(tokenVO);
	}

}

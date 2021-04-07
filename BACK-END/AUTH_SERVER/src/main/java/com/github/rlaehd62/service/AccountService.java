package com.github.rlaehd62.service;

import javax.transaction.Transactional;

import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenVO;

public interface AccountService
{
	/**
	 * 입력된 정보를 기반으로 계정을 생성하고 이에 대한 토큰을 생성하고 등록
	 * @param vo : 생성할 계정 데이터
	 * @param requestVO : 쿠키와 헤더를 담는 Request/Response
	 * @return 성공적으로 생성 했다면 토큰 정보, 계정이 이미 존재하거나 토큰 생성 과정에 문제가 생기면 Exception
	 */
	@Transactional public TokenVO createAccount(AccountVO vo, RequestVO requestVO);
	
	/**
	 * 입력된 아이디를 기반으로 계정 정보를 반환
	 * @param id : 찾고 싶은 계정의 아이디
	 * @return : 만약 찾으면 계정 정보, 아니면 Exception
	 */
	public AccountVO getAccountVO(String id);
	
	/**
	 * 토큰에 저장된 데이터를 기반으로 해당 계정 정보를 반환
	 * @param token : 토큰 (종류 무관)
	 * @return 성공하면 게정 정보, 토큰이 이상하거나 계정을 못찾으면 Exception
	 */
	public Account getAccount(String token);
	
	/**
	 * 토큰에서 사용자 정보 추출 뒤 해당 사용자의 정보를 업데이트 <br>
	 * 기존에 갖고 있는 토큰 블랙 리스트에 등록 후 갱신된 정보로 재발급
	 * @param token : 토큰 (사용자 정보)
	 * @param vo : 수정할 데이터
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 * @return 성공하면 새롭게 발급된 토큰 정보, 토큰이 이상하거나 사용자를 못찾으면 Exception
	 */
	@Transactional public TokenVO updateAccount(String token, AccountVO vo, RequestVO requestVO);
	
	/**
	 * 토큰에서 사용자 정보를 추출하여 해당 사용자를 삭제한다<br>
	 * 기존의 발급된 토큰의 재사용을 막기 위하여 블랙 리스트에 등록
	 * @param token : 토큰 (사용자 정보)
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 */
	@Transactional public void deleteAccount(String token, RequestVO requestVO);
	
	/**
	 * 입력된 정보와 실제 정보를 비교하여 일치할 때 토큰을 발급 (로그인 기능)
	 * @param vo : 입력할 계정 데이터
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 * @return 성공할 시 발급된 토큰 정보, 실패하면 Exception
	 */
	public TokenVO verifyAccount(AccountVO vo, RequestVO requestVO);
}

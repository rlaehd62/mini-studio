package com.github.rlaehd62.service;

import java.util.List;
import java.util.Optional;

import com.github.rlaehd62.vo.AccountVO;
import com.github.rlaehd62.vo.RequestVO;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.TokenVO;

import io.jsonwebtoken.Claims;

public abstract class TokenService
{
	protected final String BLACK_LIST = "TOKEN_BLACK_LIST";

	/**
	 * 요청한 각 종류의 토큰을 생성하도록 메소드를 호출하고 이의 결과를 모두 반환
	 * @param vo : 토큰을 생성하는데 필요한 계정 데이터
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 * @param types : 생성할 토큰의 종류
	 * @return 성공 시 생성된 토큰 정보, 실패하면 Optional.Empty
	 */
	public abstract Optional<TokenVO> buildTokens(AccountVO vo, RequestVO requestVO, List<TokenType> types);
	
	/**
	 * 각 종류의 토큰을 추출하는 메소드를 호출하고 이의 결과를 모두 반환 (인증 X)
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 * @return 성공 시 모든 종류의 토큰, 실패하면 Empty
	 */
	public abstract Optional<TokenVO> packTokens(RequestVO requestVO);
	
	/**
	 * 발급된 각 토큰에 대한 삭제를 진행하는 메소드를 호출
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 */
	public abstract void unPackTokens(RequestVO requestVO);

	/**
	 * 입력된 계정 정보를 기반으로 토큰을 생성하고 이의 쿠키를 생성 (일부 종류는 REDIS에 등록된다)<br>
	 * 토큰에는 ID, USERNAME, ROLES 등의 정보가 입력
	 * @param vo : 토큰을 생성하는데 필요한 계정 데이터
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 * @param type : 생성할 토큰의 종류 (생성할 토큰의 기본 정보를 담고있다)
	 * @return 성공하면 생성된 토큰의 정보, 실패하면 Empty
	 */
	public abstract Optional<String> createToken(AccountVO vo, TokenType type, RequestVO requestVO);
	
	/**
	 * 수신한 토큰을 쿠키와 헤더에 저장 <br>
	 * 일부 종류는 REDIS에 ID와 함께 저장
	 * @param id : 토큰에서 사용된 계정의 아이디
	 * @param type : 저장할 토큰의 종류
	 * @param token : 토큰 정보
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 */
	protected abstract void saveToken(String id, TokenType type, String token, RequestVO requestVO);
	
	/**
	 * 입력받은 각 토큰의 종류에 대하여 삭제를 진행하는 메소드를 호출
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 * @param types : 삭제할 토큰의 종류
	 */
	protected abstract void deleteToken(RequestVO requestVO, List<TokenType> types);
	
	/**
	 * 입력받은 종류의 토큰의 쿠키를 삭제하고 이의 값을 블랙 리스트에 등록 (실질적인 삭제 진행) <br>
	 * 블랙 리스트 : 토큰에 대한 인증을 진행 할 때 무조건 거부 할 토큰 목록
	 * @param type : 삭제할 토큰의 종류
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 */
	protected abstract void deleteToken(TokenType type, RequestVO requestVO);
	
	/**
	 * 입력받은 종류의 토큰을 쿠키 또는 헤더에서 찾은 뒤 이 결과를 반환
	 * @param type : 찾을 토큰의 종류
	 * @param requestVO : 클라이언트의 Request/클라이언트가 받을 Response
	 * @return 토큰 값 반환, 못 찾았으면 Empty
	 */
	public abstract Optional<String> findToken(TokenType type, RequestVO requestVO);
	
	/**
	 * 입력받은 토큰에 대한 인증을 진행하고 포함된 정보를 반환 <br>
	 * 토큰이 잘못되었거나 만료되었다면 Exception 발생
	 * @param token : 인증을 진행할 토큰
	 * @return 입력한 토큰에 기입된 모든 정보, 실패 시 Empty
	 */
	public abstract Optional<Claims> verifyToken(String token);
	
	/**
	 * 토큰의 인증 및 만료 여부를 확인한다 (이제 안쓰임)
	 * @param token : 만료 여부를 확인할 토큰
	 * @return 만료 여부를 반환
	 */
	@Deprecated
	public abstract boolean isExpired(String token);
}

package com.github.rlaehd62.vo.board;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.rlaehd62.vo.Public;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardInfo
{
	@NonNull private Long ID;
	@NonNull private String context;
	@NonNull private String accountID;
	@NonNull private String accountUsername;
	private Public isPublic = Public.YES;
	
	// 요청에서 업로드 할 파일을 전달하는 요청으로만 사용됩니다.
	@JsonIgnore private List<MultipartFile> files;
	
	public void compare(BoardInfo info)
	{
		if(!context.equals(info.getContext())) context = info.getContext();
	}
}

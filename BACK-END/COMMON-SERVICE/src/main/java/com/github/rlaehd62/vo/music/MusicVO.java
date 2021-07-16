package com.github.rlaehd62.vo.music;

import java.time.LocalDateTime;

import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.entity.file.File;
import com.github.rlaehd62.entity.file.Music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MusicVO
{
	private Long ID;
	private Long fileID;
	private String title;
	private String description;
	private String lyrics;
	private String uploaderID;
	private String uploaderUsername;
	private LocalDateTime createdDate;
	
	public MusicVO(Music music)
	{
		Account owner = music.getAccount();
		File file = music.getFile();
		
		setID(music.getID());
		setFileID(file.getID());
		setTitle(music.getTitle());
		setDescription(music.getDescription());
		setLyrics(music.getLyrics());
		setUploaderID(owner.getId());
		setUploaderUsername(owner.getUsername());
		setCreatedDate(music.getCreatedDate());
	}
}

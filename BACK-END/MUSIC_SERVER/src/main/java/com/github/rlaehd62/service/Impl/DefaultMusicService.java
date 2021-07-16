package com.github.rlaehd62.service.Impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.rlaehd62.config.event.reques.AccountCheckEvent;
import com.github.rlaehd62.config.event.reques.BlockCheckEvent;
import com.github.rlaehd62.config.event.reques.FileUploadEvent;
import com.github.rlaehd62.entity.Account;
import com.github.rlaehd62.entity.File;
import com.github.rlaehd62.entity.Music;
import com.github.rlaehd62.exception.MusicError;
import com.github.rlaehd62.exception.MusicException;
import com.github.rlaehd62.repository.MusicRepository;
import com.github.rlaehd62.request.MusicDeleteRequest;
import com.github.rlaehd62.request.MusicListRequest;
import com.github.rlaehd62.request.MusicReadRequest;
import com.github.rlaehd62.request.MusicUpdateRequest;
import com.github.rlaehd62.request.MusicUploadRequest;
import com.github.rlaehd62.service.MusicService;
import com.github.rlaehd62.vo.music.MusicVO;
import com.google.common.eventbus.EventBus;

@Service
public class DefaultMusicService implements MusicService
{
	private MusicRepository musicRepository;
	private EventBus eventBus;
	
	@Autowired
	public DefaultMusicService(MusicRepository musicRepository, EventBus eventBus)
	{
		this.musicRepository = musicRepository;
		this.eventBus = eventBus;
	}

	@Override
	public Long post(MusicUploadRequest request)
	{
		String token = request.getToken();
		AccountCheckEvent accountEvent = new AccountCheckEvent(token, new Account());
		accountEvent.setPrivileged(true);
		
		eventBus.post(accountEvent);
		Account account = accountEvent.getPrivilegedResult();
		
		MultipartFile file = request.getFile();
		FileUploadEvent uploadEvent = new FileUploadEvent(file);
		eventBus.post(uploadEvent);
		
		if(!uploadEvent.isSuccessful()) throw new MusicException(MusicError.MUSIC_UPLOAD_FAILED);
		Long fileID = uploadEvent.getUploadedFileID();
		Music music = Music.builder()
				.account(account)
				.file(new File(fileID, "", ""))
				.title(request.getTitle())
				.description(request.getDescription())
				.lyrics(request.getLyrics())
				.build();
		
		musicRepository.saveAndFlush(music);
		return music.getID();
	}

	@Override
	public MusicVO update(MusicUpdateRequest request)
	{
		String token = request.getToken();
		Long musicID = request.getID();
		
		Optional<Music> optional = musicRepository.findById(musicID);
		optional.orElseThrow(() -> new MusicException(MusicError.MUSIC_NOT_FOUND));
		
		Music music = optional.get();		
		Account owner = music.getAccount();
		AccountCheckEvent checkEvent = new AccountCheckEvent(token, owner);
		
		eventBus.post(checkEvent);
		if(!checkEvent.isSuccessful()) throw new MusicException(MusicError.MUSIC_NOT_MINE);
		
		music.setTitle(request.getTitle());
		music.setDescription(request.getDescription());
		music.setLyrics(request.getLyrics());
		musicRepository.saveAndFlush(music);
		
		return new MusicVO(music);
	}

	@Override
	public boolean delete(MusicDeleteRequest request)
	{
		String token = request.getToken();
		Long musicID = request.getID();
		
		Optional<Music> optional = musicRepository.findById(musicID);
		optional.orElseThrow(() -> new MusicException(MusicError.MUSIC_NOT_FOUND));
		
		Music music = optional.get();
		Account owner = music.getAccount();
		AccountCheckEvent checkEvent = new AccountCheckEvent(token, owner);
		
		eventBus.post(checkEvent);
		if(!checkEvent.isSuccessful()) throw new MusicException(MusicError.MUSIC_NOT_MINE);
		musicRepository.delete(music);
		return true;
	}

	@Override
	public MusicVO read(MusicReadRequest request)
	{
		String token = request.getToken();
		Long musicID = request.getID();
		boolean isMine = request.isMine();
		
		Optional<Music> optional = musicRepository.findById(musicID);
		optional.orElseThrow(() -> new MusicException(MusicError.MUSIC_NOT_FOUND));
		
		Music music = optional.get();
		Account owner = music.getAccount();
		
		AccountCheckEvent checkEvent = new AccountCheckEvent(token, owner);
		if(isMine) eventBus.post(checkEvent);
		
		BlockCheckEvent event = new BlockCheckEvent(token, owner);
		eventBus.post(event);

		if(isMine && !checkEvent.isSuccessful()) throw new MusicException(MusicError.MUSIC_NOT_MINE);
		else if(event.isSuccessful()) throw new MusicException(MusicError.MUSIC_NOT_MINE);
		
		return new MusicVO(music);
	}

	@Override
	public List<MusicVO> list(MusicListRequest request)
	{
		String uploaderID = request.getID();
		String token = request.getToken();

		List<Music> list = musicRepository.findAllByAccount_IdOrderByIDDesc(uploaderID);
		if(list.isEmpty()) return Collections.emptyList();
		
		Account owner = list.get(0).getAccount();
		BlockCheckEvent event = new BlockCheckEvent(token, owner);
		eventBus.post(event);
		
		if(event.isSuccessful()) throw new MusicException(MusicError.MUSIC_NOT_MINE);
		return list.stream()
				.map(music -> new MusicVO(music))
				.collect(Collectors.toList());			
	}
	
}

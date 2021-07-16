package com.github.rlaehd62.controller;

import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.Impl.DefaultProfileService;
import com.github.rlaehd62.service.ProfileService;
import com.github.rlaehd62.vo.resource.board.BoardDeleteRequest;
import com.github.rlaehd62.vo.resource.board.BoardFileUploadRequest;
import com.github.rlaehd62.vo.resource.board.BoardListVO;
import com.github.rlaehd62.vo.resource.board.BoardRequest;
import com.github.rlaehd62.vo.resource.profile.ProfileDeleteRequest;
import com.github.rlaehd62.vo.resource.profile.ProfileUploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/profiles")
public class ProfileController
{
    private ProfileService service;

    @Autowired
    public ProfileController(DefaultProfileService service)
    {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<?> upload(@RequestAttribute("ACCESS_TOKEN") String token, @RequestParam MultipartFile file) throws IOException
    {
        if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
        ProfileUploadRequest request = new ProfileUploadRequest(token, file);
        return ResponseEntity.ok(service.upload(request));
    }

    @DeleteMapping("")
    public ResponseEntity<?> delete(@RequestAttribute("ACCESS_TOKEN") String token)
    {
        if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
        ProfileDeleteRequest request = new ProfileDeleteRequest(token);
        service.delete(request);
        return ResponseEntity.ok("프로필 사진을 성공적으로 삭제 했습니다.");
    }

    @GetMapping("")
    public ResponseEntity<?> get(@RequestAttribute("ACCESS_TOKEN") String token)
    {
        if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
        return ResponseEntity.ok(service.get(token));
    }
}

package com.github.rlaehd62.controller;

import com.github.rlaehd62.exception.TokenError;
import com.github.rlaehd62.exception.TokenException;
import com.github.rlaehd62.service.FavorService;
import com.github.rlaehd62.service.TokenService;
import com.github.rlaehd62.vo.MusicGenre;
import com.github.rlaehd62.vo.request.favor.FavorListRequest;
import com.github.rlaehd62.vo.request.favor.FavorToggleEvent;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/favors")
public class FavorController
{
    private FavorService favorService;
    private TokenService tokenService;

    @Autowired
    public FavorController(FavorService favorService, TokenService tokenService)
    {
        this.favorService = favorService;
        this.tokenService = tokenService;
    }

    @GetMapping("")
    public ResponseEntity<?> getList
            (
                    @RequestAttribute("ACCESS_TOKEN") String access_token,
                    @RequestParam(required = false) String token
            )
    {
        String key = Objects.nonNull(access_token) ? access_token : token;
        Optional<Claims> op = tokenService.verifyToken(key);
        if(!op.isPresent()) throw new TokenException(TokenError.ILLEGAL_TOKEN);

        FavorListRequest request = new FavorListRequest(key);
        return ResponseEntity.ok(favorService.getFavorList(request));
    }

    @PostMapping("")
    public ResponseEntity<?> toggle
            (
                    @RequestAttribute("ACCESS_TOKEN") String token,
                    @RequestParam(required = false, defaultValue = "NONE") MusicGenre genre,
                    @Context HttpServletRequest request,
                    @Context HttpServletResponse response
            )
    {
        if(Objects.isNull(token)) throw new TokenException(TokenError.ACCESS_TOKEN_NOT_FOUND);
        FavorToggleEvent event = new FavorToggleEvent(token, genre);
        favorService.toggle(event);
        return ResponseEntity.ok("선호장르 토글");
    }
}

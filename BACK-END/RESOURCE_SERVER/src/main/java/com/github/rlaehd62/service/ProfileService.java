package com.github.rlaehd62.service;

import com.github.rlaehd62.vo.resource.profile.ProfileDeleteRequest;
import com.github.rlaehd62.vo.resource.profile.ProfileUploadRequest;
import com.github.rlaehd62.vo.resource.profile.ProfileVO;

import java.io.IOException;
import java.util.Optional;

public interface ProfileService
{
    ProfileVO upload(ProfileUploadRequest request) throws IOException;
    Optional<ProfileVO> get(String TOKEN);
    void delete(ProfileDeleteRequest request);
}

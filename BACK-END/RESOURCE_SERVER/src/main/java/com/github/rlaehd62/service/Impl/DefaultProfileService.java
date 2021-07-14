package com.github.rlaehd62.service.Impl;

import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.entity.file.File;
import com.github.rlaehd62.entity.file.Profile;
import com.github.rlaehd62.entity.repository.ProfileRepository;
import com.github.rlaehd62.exception.FileError;
import com.github.rlaehd62.exception.FileException;
import com.github.rlaehd62.messaging.DataFetcher;
import com.github.rlaehd62.service.FileService;
import com.github.rlaehd62.service.ProfileService;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.account.AccountInfo;
import com.github.rlaehd62.vo.resource.FileUploadRequest;
import com.github.rlaehd62.vo.resource.profile.ProfileDeleteRequest;
import com.github.rlaehd62.vo.resource.profile.ProfileUploadRequest;
import com.github.rlaehd62.vo.resource.profile.ProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service("ProfileService")
public class DefaultProfileService implements ProfileService
{
    private ProfileRepository repository;
    private FileService fileService;
    private DataFetcher fetcher;
    private Function<String, Supplier<HttpEntity<?>>> function;

    @Autowired
    public DefaultProfileService(DataFetcher fetcher, ProfileRepository repository, FileService fileService)
    {
        this.fileService = fileService;
        this.repository = repository;
        this.fetcher = fetcher;

        function = token ->
                () ->
                {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set(TokenType.ACCESS.getName(), token);
                    return new HttpEntity<>("", headers);
                };
    }

    @Override
    public ProfileVO upload(ProfileUploadRequest request)
    {
        String TOKEN = request.getToken();
        MultipartFile file = request.getFile();
        Optional<AccountInfo> optional = fetcher.fetch("AUTH-SERVICE/tokens/verify?token="+TOKEN, HttpMethod.GET, function.apply(TOKEN), AccountInfo.class);

        return optional.map(AccountInfo::getId)
                .map(id ->
                {
                    try
                    {
                        repository.findByAccount_Id(id).ifPresent(profile -> repository.delete(profile));
                        FileUploadRequest fRequest = new FileUploadRequest(file);
                        return fileService.uploadFile(fRequest, tempFile ->
                                {
                                    Profile profile = Profile
                                            .builder()
                                            .account(new Account(id))
                                            .file(tempFile)
                                            .build();
                                    repository.save(profile);

                                    return
                                            ProfileVO
                                            .builder()
                                            .fileID(tempFile.getID())
                                            .build();
                                });
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        throw new FileException(FileError.FILE_UPLOAD_FAILURE);
                    }
                })
                .orElseThrow(() -> new FileException(FileError.FILE_UPLOAD_FAILURE));
    }

    @Override
    public Optional<ProfileVO> get(String TOKEN)
    {
        Optional<AccountInfo> optional = fetcher.fetch("AUTH-SERVICE/tokens/verify?token="+TOKEN, HttpMethod.GET, function.apply(TOKEN), AccountInfo.class);
        return Optional.ofNullable
                (
                        optional.map(AccountInfo::getId)
                                .map(id -> repository.findByAccount_Id(id))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .map(Profile::getFile)
                                .map(file -> ProfileVO.builder().fileID(file.getID()).build())
                                .orElseGet(null)
                );
    }

    @Override
    public void delete(ProfileDeleteRequest request)
    {
        String TOKEN = request.getToken();
        Optional<AccountInfo> optional = fetcher.fetch("AUTH-SERVICE/tokens/verify?token="+TOKEN, HttpMethod.GET, function.apply(TOKEN), AccountInfo.class);

        optional
                .map(AccountInfo::getId)
                .ifPresent(id ->
                {
                    Optional<Profile> optionalProfile = repository.findByAccount_Id(id);
                    optionalProfile.ifPresent(profile -> repository.delete(profile));
                    optionalProfile.orElseThrow(() -> new FileException(FileError.FILE_NOT_FOUND));
                });
    }
}

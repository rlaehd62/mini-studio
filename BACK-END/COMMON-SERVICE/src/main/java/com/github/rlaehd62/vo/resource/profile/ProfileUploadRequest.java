package com.github.rlaehd62.vo.resource.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ProfileUploadRequest
{
    {
        String TEMP_ID = UUID.randomUUID().toString();
        FILE_ID = TEMP_ID.replaceAll("-", "");
    }

    private String FILE_ID;
    @NonNull private String token;
    @NonNull MultipartFile file;
}

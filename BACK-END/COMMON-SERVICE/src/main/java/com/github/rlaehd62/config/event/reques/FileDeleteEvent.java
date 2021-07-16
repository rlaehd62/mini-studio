package com.github.rlaehd62.config.event.reques;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FileDeleteEvent
{
	@NonNull Long fileID;
}

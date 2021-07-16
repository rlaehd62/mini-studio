package com.github.rlaehd62.vo.request.favor;

import com.github.rlaehd62.vo.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavorToggleEvent
{
	private String token;
	private MusicGenre favor;
}

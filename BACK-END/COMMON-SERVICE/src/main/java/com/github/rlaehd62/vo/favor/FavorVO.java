package com.github.rlaehd62.vo.favor;

import com.github.rlaehd62.vo.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FavorVO
{
	private Long ID;
	private MusicGenre genre;
}

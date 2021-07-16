package com.github.rlaehd62.vo;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum MusicGenre
{
    NONE("NONE"),
    BLUES("BLUES"),
    CLASSICAL("CLASSICAL"),
    COUNTRY("COUNTRY"),
    DISCO("DISCO"),
    HIPHOP("HIPHOP"),
    JAZZ("JAZZ"),
    METAL("METAL"),
    POP("POP"),
    REGGAE("REGGAE"),
    ROCK("ROCK");

    MusicGenre(String value)
    {
        this.value = value;
    }

    private String value;
}

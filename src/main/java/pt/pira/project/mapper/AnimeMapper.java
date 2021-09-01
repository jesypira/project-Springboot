package pt.pira.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pt.pira.project.domain.Anime;
import pt.pira.project.requests.AnimePostRequestBody;
import pt.pira.project.requests.AnimePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostRequestBody animePostRequestsBody);

    public abstract Anime toAnime(AnimePutRequestBody animePutRequestsBody);
}

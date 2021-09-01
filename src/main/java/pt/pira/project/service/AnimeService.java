package pt.pira.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.pira.project.domain.Anime;
import pt.pira.project.exception.BadRequestException;
import pt.pira.project.mapper.AnimeMapper;
import pt.pira.project.repository.AnimeRepository;
import pt.pira.project.requests.AnimePostRequestBody;
import pt.pira.project.requests.AnimePutRequestBody;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }

    public List<Anime> listAllNonPageable() {
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public Anime findById(long id) {
        return animeRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("Anime not found!"));
    }

    @Transactional
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    public void delete(long id) {
        animeRepository.delete(findById(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        try{
            Anime savedAnime = findById(animePutRequestBody.getId());
            Anime anime =  AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
            anime.setId(savedAnime.getId());
            animeRepository.save(anime);
        }catch(ResponseStatusException e){
            throw e;
        }
    }


}

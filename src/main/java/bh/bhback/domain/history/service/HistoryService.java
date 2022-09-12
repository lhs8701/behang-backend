package bh.bhback.domain.history.service;

import bh.bhback.domain.history.dto.AreaInfoDto;
import bh.bhback.domain.history.entity.AreaCode;
import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.repository.PostJpaRepository;
import bh.bhback.domain.user.entity.User;
import bh.bhback.global.error.advice.exception.CPostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class HistoryService {

    private final PostJpaRepository postJpaRepository;

    public int getIndexFromAreaCode(int areaCode) {
        return (areaCode > 30) ? areaCode - 22 : areaCode;
    }

    public int getAreaCodeFromIndex(int idx) {
        return (idx > 8) ? idx + 22 : idx;
    }

    public List<AreaInfoDto> getThumbnail(User user) {
        Integer[] count = new Integer[18];
        Arrays.fill(count, 0);
        List<AreaInfoDto> areaInfoList = new ArrayList<>();
        List<Post> postList = postJpaRepository.findAllByUser(user).orElseThrow(CPostNotFoundException::new);
        for (Post post : postList) {
            int areaCode = post.getPlace().getAreaCode();
            count[getIndexFromAreaCode(areaCode)]++;
        }
        for (int i = 1; i < count.length; i++) {
            areaInfoList.add(new AreaInfoDto(AreaCode.find(getAreaCodeFromIndex(i)).getName(), count[i]));
        }
        return areaInfoList;
    }
}

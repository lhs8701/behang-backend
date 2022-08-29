package bh.bhback.domain.report.service;

import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.repository.PostJpaRepository;
import bh.bhback.domain.report.entity.Report;
import bh.bhback.domain.report.repository.ReportRepository;
import bh.bhback.domain.user.entity.User;
import bh.bhback.domain.user.repository.UserJpaRepository;
import bh.bhback.global.error.advice.exception.CPostNotFoundException;
import bh.bhback.global.error.advice.exception.CReportDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private static final int REPORT_LIMIT = 50;

    public void createReport(Long postId, User user) {

        Post post = postJpaRepository.findById(postId).orElseThrow(CPostNotFoundException::new);
        List<Report> reportList = post.getReportList();
        if (post.getReportList().stream().anyMatch(reportList::contains))
            throw new CReportDuplicatedException();

        if (reportList.size() == REPORT_LIMIT - 1) {
            postJpaRepository.deleteById(post.getId());
            return;
        }

        Report report = Report.builder()
                .post(post)
                .user(user)
                .build();

        reportJpaRepository.save(report);


//
//        /*
//        처음 생각한 로직
//        postId에 대한 신고목록을 가져오기
//        목록들에서 user가 이미 있는지 확인하기
//         */
//
//        /*
//        위의 로직에 대한 의문
//        신고 목록을 전부 가져오면 50개 중복시 삭제라면 상당히 많을텐데 그러면 상당히 많은 데이터 들을 다뤄야 한다
//        User가 신고한 내역들을 가져와서 뒤져보는게 훨씬 효율적인 방법이 아닐까?
//
//        ==> 근데 이 로직의 문제점
//
//        새로 Report를 생성해야 하는경우 -> 유저가 신고한 내역이 없을때, 있지만 해당 post에 대한 report가 아직 없을때
//        거절할때 -> 이미 해당 post에 대한 report가 있을때
//
//        이렇게 새로 report를 만들 경우를 두번에 거쳐서 걸러 줘야함
//         */
//
//        Long userId = user.getUserId();
//        List<Report> reportList = userJpaRepository.findByUserIdJoinReport(userId).getReportList();
//        int reportSize = reportList.size();
//
//        //이 사람의 신고 내역이 하나도 없을떄 => 새로 Report를 생성
//        if (reportSize == 0) {
//            Post post = postJpaRepository.findById(postId).get();
//
//            Report report = Report.builder()
//                    .post(post)
//                    .user(user)
//                    .build();
//
//            reportJpaRepository.save(report);
//
//        } else {
//            boolean isExist = false;
//
//            for (int i = 0; i < reportSize; i++) {
//                Long reportPostId = reportList.get(i).getPost().getId();
//
//                if (reportPostId == postId) {
//                    isExist = true;
//                    break;
//                }
//            }
//
//            //이미 있는 경우
//            if (isExist) {
//                Exception e = new Exception("이미 신고했습니다");
//                throw e;
//
//            } else { //새로 Report 생성
//                Post post = postJpaRepository.findById(postId).get();
//
//                Report report = Report.builder()
//                        .post(post)
//                        .user(user)
//                        .build();
//
//                reportJpaRepository.save(report);
//            }
//        }
    }
}

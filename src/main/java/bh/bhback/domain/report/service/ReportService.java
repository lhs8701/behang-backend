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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private static final int REPORT_LIMIT = 50;

    public void createReport(Long postId, User user) {

        log.info("1");
        Post post = postJpaRepository.findById(postId).orElseThrow(CPostNotFoundException::new);
        List<Report> reportList = post.getReportList();
        if (post.getReportList().stream().anyMatch(reportList::contains)) {
            log.info("2");
            throw new CReportDuplicatedException();
        }
        log.info("3");

        if (reportList.size() == REPORT_LIMIT - 1) {
            postJpaRepository.deleteById(post.getId());
            return;
        }

        Report report = Report.builder()
                .post(post)
                .user(user)
                .build();
        log.info("4");

        reportJpaRepository.save(report);
        log.info("5");

    }

    //test용
    public int getReportCount(Long postId){
        Post post = postJpaRepository.findById(postId).orElseThrow(CPostNotFoundException::new);
        return post.getReportList().size();
    }
}

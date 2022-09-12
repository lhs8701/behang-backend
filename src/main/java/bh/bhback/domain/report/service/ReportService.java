package bh.bhback.domain.report.service;

import bh.bhback.domain.post.entity.Post;
import bh.bhback.domain.post.repository.PostJpaRepository;
import bh.bhback.domain.post.service.PostService;
import bh.bhback.domain.report.entity.Report;
import bh.bhback.domain.report.repository.ReportJpaRepository;
import bh.bhback.domain.user.entity.User;
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

    private final ReportJpaRepository reportJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final PostService postService;
    private static final int REPORT_LIMIT = 50;

    public void createReport(Long postId, User user) {

        Post post = postJpaRepository.findById(postId).orElseThrow(CPostNotFoundException::new);
        List<Report> reportList = post.getReportList();
        if (post.getReportList().stream().anyMatch(reportList::contains)) {
            throw new CReportDuplicatedException();
        }

        if (reportList.size() == REPORT_LIMIT - 1) {
            postService.deletePost(post.getId(), post.getUser());
            return;
        }

        Report report = Report.builder()
                .post(post)
                .user(user)
                .build();

        reportJpaRepository.save(report);
    }

    public int getReportCount(Long postId){
        Post post = postJpaRepository.findById(postId).orElseThrow(CPostNotFoundException::new);
        return post.getReportList().size();
    }
}

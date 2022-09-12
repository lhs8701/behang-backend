package bh.bhback.domain.report.repository;

import bh.bhback.domain.report.entity.Report;
import bh.bhback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportJpaRepository extends JpaRepository<Report, Long> {
    void deleteAllByUser(User user);
}

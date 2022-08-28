package bh.bhback.domain.report.repository;

import bh.bhback.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository  extends JpaRepository<Report, Long> {

}

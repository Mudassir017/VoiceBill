package com.voicebill.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUserId(Long userId);

    @Query("select count(i) from Invoice i where i.userId = :uid")
    long countByUserId(@Param("uid") Long uid);

    @Query("select coalesce(sum(i.total), 0) from Invoice i where i.userId = :uid")
    BigDecimal sumTotalByUserId(@Param("uid") Long uid);

    @Query("select i from Invoice i where i.userId = :uid order by i.id desc")
    List<Invoice> findRecentByUserId(@Param("uid") Long uid, org.springframework.data.domain.Pageable pageable);
}

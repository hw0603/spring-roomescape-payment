package roomescape.domain.reservation;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomescape.domain.payment.Payment;
import roomescape.domain.member.Member;
import roomescape.domain.schedule.ReservationTime;
import roomescape.domain.schedule.Schedule;
import roomescape.domain.theme.Theme;
import roomescape.exception.UnauthorizedException;

@Entity
@Table(name = "waiting")
@EntityListeners(AuditingEntityListener.class)
public class ReservationWaiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Theme theme;

    @Embedded
    private Schedule schedule;

    @OneToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Payment payment;

    @CreatedDate
    private LocalDateTime createdAt;

    protected ReservationWaiting() {
    }

    public ReservationWaiting(Member member, Theme theme, Schedule schedule, Payment payment) {
        this.member = member;
        this.theme = theme;
        this.schedule = schedule;
        this.payment = payment;
    }

    public ReservationWaiting(Member member, Theme theme, Schedule schedule) {
        this.member = member;
        this.theme = theme;
        this.schedule = schedule;
    }

    public void checkCancelAuthority(long memberId) {
        if (member.getId() != memberId) {
            throw new UnauthorizedException("예약 대기를 취소할 권한이 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getMemberName() {
        return member.getMemberName().getValue();
    }

    public Theme getTheme() {
        return theme;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Payment getPayment() {
        return payment;
    }

    public LocalDate getScheduleDate() {
        return schedule.getDate();
    }

    public ReservationTime getReservationTime() {
        return schedule.getReservationTime();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

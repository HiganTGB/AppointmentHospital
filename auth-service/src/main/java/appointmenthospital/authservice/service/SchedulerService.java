package appointmenthospital.authservice.service;

import appointmenthospital.authservice.model.entity.*;
import appointmenthospital.authservice.model.entity.QAppointment;
import appointmenthospital.authservice.repository.AppointmentRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.*;

import static appointmenthospital.authservice.model.entity.QDoctor.doctor;
import static java.time.LocalTime.of;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.time.Duration.ofMinutes;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    @Value("#{T(java.time.LocalTime).parse('${scheduleService.firstStart}')}")
    private LocalTime _firstStart;
    @Value("#{T(java.time.LocalTime).parse('${scheduleService.firstEnd}')}")
    private LocalTime _firstEnd;
    @Value("#{T(java.time.LocalTime).parse('${scheduleService.lastStart}')}")
    private LocalTime _lastStart;
    @Value("#{T(java.time.LocalTime).parse('${scheduleService.lastEnd}')}")
    private LocalTime _lastEnd;
    @Value("#{T(java.time.Duration).ofMinutes(${scheduleService.bigStepGap})}")
    private Duration _bigStepGap;
    @Value("#{T(java.time.Duration).ofMinutes(${scheduleService.stepGap})}")
    private Duration _stepGap;
    private final AppointmentRepository _appointmentRepository;
    private final QAppointment appointment = QAppointment.appointment;

    public LocalTime getFirstStart() {
        return _firstStart;
    }

    public LocalTime getFirstEnd() {
        return _firstEnd;
    }

    public LocalTime getLastStart() {
        return _lastStart;
    }

    public LocalTime getLastEnd() {
        return _lastEnd;
    }

    public Duration getBigStepGap() {
        return _bigStepGap;
    }

    public Duration getStepGap() {
        return _stepGap;
    }

    //    private LocalTime scaledEndOf(LocalTime end, LocalTime start) {
//        Duration bigStepGap = _bigStepGap;
//        Duration difference = Duration.between(start, end);
//        long remainderMinutes = difference.toMinutes() % bigStepGap.toMinutes();
//        return end.plusMinutes(remainderMinutes);
//    }
    public Iterable<SchedulerPart> getParts() {
        return new SchedulerPartIterable(this);
//        if (_parts != null) {
//            return _parts;
//        }
//
//        synchronized (this) {
//            if (_parts != null) {
//                return _parts;
//            }
//
//            List<SchedulerPart> list = new ArrayList<>();
//            long id = 1;
//
//            LocalTime b = _firstStart;
//            LocalTime e = _firstEnd;
//            LocalTime m;
//            while ((m = b.plus(_bigStepGap)) .isBefore(e)) {
//                SchedulerPart part=new SchedulerPart();
//                part.setId(id++);
//                part.setStart(b);
//                part.setEnd(b=m);
//                list.add(part);
//            }
//            b = _lastStart;
//            e = _lastEnd;
//            while ((m = b.plus(_bigStepGap)) .isBefore(e)) {
//                SchedulerPart part=new SchedulerPart();
//                part.setId(id++);
//                part.setStart(b);
//                part.setEnd(b=m);
//                list.add(part);
//            }
//
//            _parts = list;
//        }
//        return _parts;
    }

    public Iterable<SchedulerAllocation> getAllocations() {
        return new SchedulerAllocationIterable(this);
//        if(_allocations!=null) return _allocations;
//        synchronized (this)
//        {
//            if(_allocations!=null) return _allocations;
//            List<SchedulerAllocation> allocationsList = new ArrayList<>();
//            LocalTime start = _firstStart;
//            LocalTime end = scaledEndOf(_firstEnd, start);
//            long id=1;
//            while (start.isBefore(end)) {
//                SchedulerAllocation schedulerAllocation=new SchedulerAllocation();
//                schedulerAllocation.setId(id++);
//                schedulerAllocation.setAtTime(start);
//                allocationsList.add(schedulerAllocation);
//                start = start.plusMinutes(_stepGap.toMinutes());
//            }
//            start = _lastStart;
//            end = scaledEndOf(_lastEnd, start);
//            while (start.isBefore(end)) {
//                SchedulerAllocation schedulerAllocation=new SchedulerAllocation();
//                schedulerAllocation.setId(id++);
//                schedulerAllocation.setAtTime(start);
//                allocationsList.add(schedulerAllocation);
//                start = start.plusMinutes(_stepGap.toMinutes());
//            }
//            _allocations=allocationsList;
//
//        }
//        return _allocations;
    }

    public SchedulerAllocation allocate(long doctorId, LocalDate date, LocalTime start, LocalTime end) {
        LocalDate now = LocalDate.now();
        if (date.compareTo(now) <= 0) return null;
        if (start.compareTo(end) >= 0) return null;
        if ((start.compareTo(_firstStart) < 0
                || end.compareTo(_firstEnd) > 0)
                && (start.compareTo(_lastStart) < 0
                || end.compareTo(_lastEnd) > 0))
            return null;

        Iterator<Appointment> it = _appointmentRepository.findAll(
                appointment.doctor.id.eq(doctorId)
                        .and(appointment.atTime.goe(LocalDateTime.of(date, start)))
                        .and(appointment.atTime.lt(LocalDateTime.of(date, end))),
                appointment.atTime.asc()
        ).iterator();
        if (!it.hasNext()) {
            for (SchedulerAllocation allocation : getAllocations()) {
                LocalTime atTime = allocation.atTime;
                if (start.compareTo(atTime) <= 0
                        && atTime.compareTo(end) < 0)
                    return allocation;
            }
            return null;
        }

        LocalTime last;
        do last = it.next().getAtTime().toLocalTime(); while (it.hasNext());
        last = last.plus(_stepGap);
        for (SchedulerAllocation allocation : getAllocations()) {
            if (allocation.atTime.equals(last))
                return allocation;
        }
        return null;
    }

    public SchedulerAllocation allocate(long doctorId) {
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        LocalDate dnow = now.toLocalDate();
        LocalTime tnow = now.toLocalTime();

        Iterator<Appointment> ait = _appointmentRepository.findAll(
                appointment.doctor.id.eq(doctorId)
                        .and(appointment.atTime.gt(now))
                        .and(appointment.atTime.lt(LocalDateTime.of(
                                dnow.plusDays(1), LocalTime.ofSecondOfDay(0)))),
                appointment.atTime.asc()
        ).iterator();

        Iterator<SchedulerAllocation> bit = getAllocations().iterator();

        SchedulerAllocation curAllocation = null;
        while (bit.hasNext()) {
            curAllocation = bit.next();
            if (curAllocation.atTime.compareTo(tnow) > 0) break;
        }

        while (true) {
            if (!ait.hasNext()) return curAllocation;
            if (!bit.hasNext()) return null;
            if (curAllocation.atTime.compareTo(
                    ait.next().getAtTime().toLocalTime()) < 0)
                return curAllocation;
            curAllocation = bit.next();
        }
    }
}
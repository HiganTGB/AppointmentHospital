package appointmenthospital.authservice.service;

import appointmenthospital.authservice.model.entity.Doctor;
import appointmenthospital.authservice.model.entity.QAppointment;
import appointmenthospital.authservice.model.entity.SchedulerAllocation;
import appointmenthospital.authservice.model.entity.SchedulerPart;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDate;
import java.time.LocalTime;
import static java.time.LocalTime.of;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.time.Duration.ofMinutes;

public class SchedulerService {

    private int initFlags;
    @Value("${#{ T(java.time.LocalTime).parse('${scheduleService.firstStart}')}")
    private LocalTime _firstStart;
    @Value("${#{ T(java.time.LocalTime).parse('${scheduleService.firstEnd}')}")
    private LocalTime _firstEnd;
    @Value("${#{ T(java.time.LocalTime).parse('${scheduleService.lastStart}')}")
    private LocalTime _lastStart;
    @Value("${#{ T(java.time.LocalTime).parse('${scheduleService.lastEnd}')}")
    private LocalTime _lastEnd;
    @Value("${#{ T(java.time.Duration).ofMinutes('${scheduleService.bigStepGap}')}")
    private Duration _bigStepGap;
    @Value("${#{ T(java.time.Duration).ofMinutes('${scheduleService.stepGap}')}")
    private Duration _stepGap;
    private List<SchedulerPart> _parts;
    private List<SchedulerAllocation> _allocations;
    private QAppointment appointment=QAppointment.appointment;

    private LocalTime scaledEndOf(LocalTime end, LocalTime start) {
        Duration bigStepGap = _bigStepGap;
        Duration difference = Duration.between(start, end);
        long remainderMinutes = difference.toMinutes() % bigStepGap.toMinutes();
        return end.plusMinutes(remainderMinutes);
    }
    public Iterable<SchedulerPart> getParts() {
        if (_parts != null) {
            return _parts;
        }

        synchronized (this) {
            if (_parts != null) {
                return _parts;
            }

            List<SchedulerPart> list = new ArrayList<>();
            long id = 1;

            LocalTime b = _firstStart;
            LocalTime e = _firstEnd;
            LocalTime m;
            while ((m = b.plus(_bigStepGap)) .isBefore(e)) {
                SchedulerPart part=new SchedulerPart();
                part.setId(id++);
                part.setStart(b);
                part.setEnd(b=m);
                list.add(part);
            }
            b = _lastStart;
            e = _lastEnd;
            while ((m = b.plus(_bigStepGap)) .isBefore(e)) {
                SchedulerPart part=new SchedulerPart();
                part.setId(id++);
                part.setStart(b);
                part.setEnd(b=m);
                list.add(part);
            }

            this._parts = list;
        }
        return _parts;
    }
    public List<SchedulerAllocation> getAllocations() {
        if(_allocations!=null) return _allocations;
        synchronized (this)
        {
            if(_allocations!=null) return _allocations;
            List<SchedulerAllocation> allocationsList = new ArrayList<>();
            LocalTime start = _firstStart;
            LocalTime end = scaledEndOf(_firstEnd, start);
            long id=1;
            while (start.isBefore(end)) {
                SchedulerAllocation schedulerAllocation=new SchedulerAllocation();
                schedulerAllocation.setId(id++);
                schedulerAllocation.setAtTime(start);
                allocationsList.add(schedulerAllocation);
                start = start.plusMinutes(_stepGap.toMinutes());
            }
            start = _lastStart;
            end = scaledEndOf(_lastEnd, start);
            while (start.isBefore(end)) {
                SchedulerAllocation schedulerAllocation=new SchedulerAllocation();
                schedulerAllocation.setId(id++);
                schedulerAllocation.setAtTime(start);
                allocationsList.add(schedulerAllocation);
                start = start.plusMinutes(_stepGap.toMinutes());
            }
            this._allocations=allocationsList;

        }
        return this._allocations;
    }
}

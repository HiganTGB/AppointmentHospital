package appointmenthospital.authservice.model.entity;

import appointmenthospital.authservice.service.SchedulerService;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public class SchedulerAllocationIterable implements Iterable<SchedulerAllocation>, Iterator<SchedulerAllocation> {
    private SchedulerService _s;
    private LocalTime _sfe, _sle, _v;
    private long _id;
    private SchedulerAllocation _allocation;

    public SchedulerAllocationIterable(SchedulerService schedulerService) {
        if (schedulerService == null)
            throw new IllegalArgumentException("schedulerService must not be null");
        _s = schedulerService;
        _sfe = scaledEndOf(_s.getFirstEnd(), _s.getFirstStart());
        _sle = scaledEndOf(_s.getLastEnd(), _s.getLastStart());
        _id = 0;
    }

    public Iterator<SchedulerAllocation> iterator() {
        return new SchedulerAllocationIterable(_s);
    }

    public boolean hasNext() {
        if (_id != 0) {
            if (_v.compareTo(_sle) >= 0) return false;
            else _v = _v.plus(_s.getStepGap());
        } else _v = _s.getFirstStart();
        if (_v.compareTo(_sfe) >= 0 && _v.compareTo(_s.getLastStart()) < 0)
            _v = _s.getLastStart();
        ++_id;
        _allocation = null;
        return true;
    }

    public SchedulerAllocation next() {
        if (_id == 0) throw new IllegalStateException("Iterator has not been started.");
        if (_v.compareTo(_s.getLastEnd()) >= 0) throw new IllegalStateException("Iterator has been stopped.");
        SchedulerAllocation allocation = _allocation;
        if (allocation == null) {
            allocation = new SchedulerAllocation();
            allocation.setId(_id);
            allocation.setAtTime(_v);
            _allocation = allocation;
        }
        return allocation;
    }

    private LocalTime scaledEndOf(LocalTime end, LocalTime start) {
        return end.minusMinutes(
                ChronoUnit.MINUTES.between(start, end)
                        % _s.getBigStepGap().toMinutes());
    }
}
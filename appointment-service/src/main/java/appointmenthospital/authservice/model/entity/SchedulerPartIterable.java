package appointmenthospital.authservice.model.entity;

import appointmenthospital.authservice.service.SchedulerService;

import java.time.LocalTime;
import java.util.Iterator;

public class SchedulerPartIterable implements Iterable<SchedulerPart>, Iterator<SchedulerPart> {
    private SchedulerService _s;
    private LocalTime _b, _e;
    private long _id;
    private SchedulerPart _part;

    public SchedulerPartIterable(SchedulerService schedulerService) {
        if (schedulerService == null)
            throw new IllegalArgumentException("schedulerService must not be null");
        _s = schedulerService;
        _id = 0;
    }

    public Iterator<SchedulerPart> iterator() {
        return new SchedulerPartIterable(_s);
    }

    public boolean hasNext() {
        _b = _id != 0 ? _e : _s.get_firstStart();
        _e = _b.plus(_s.get_bigStepGap());
        if (_b.compareTo(_s.get_lastEnd()) >= 0) return false;
        if (_e.compareTo(_s.get_firstEnd()) >= 0 && _b.compareTo(_s.get_lastStart()) < 0) {
            _b = _s.get_lastStart();
            _e = _b.plus(_s.get_bigStepGap());
        }
        ++_id;
        _part = null;
        return true;
    }

    public SchedulerPart next() {
        if (_id == 0) throw new IllegalStateException("Iterator has not been started.");
        if (_b.compareTo(_s.get_lastEnd()) >= 0) throw new IllegalStateException("Iterator has been stopped.");
        SchedulerPart part = _part;
        if (part == null) {
            part = new SchedulerPart();
            part.setId(_id);
            part.setStart(_b);
            part.setEnd(_e);
            _part = part;
        }
        return part;
    }
}
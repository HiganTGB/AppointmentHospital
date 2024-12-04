package appointmenthospital.thymeleafclient.model.entity;

import java.util.*;

public class PermissionIterable implements Iterable<Permission>, Iterator<Permission> {
    private static final Permission[] permissions;

    static {
        Permission[] values = Permission.values();
        int i, maxCode;
        for (i = values.length, maxCode = values[--i].getCode(); i > 0; ) {
            int code = values[--i].getCode();
            if (code > maxCode) maxCode = code;
        }
        permissions = new Permission[maxCode + 1];
        for (i = values.length; i > 0; ) {
            Permission value = values[--i];
            permissions[value.getCode()] = value;
        }
    }

    private final byte[] _permissions;
    private int _byteIndex, _bitIndex;

    public PermissionIterable(byte[] permissions) {
        if (permissions == null || permissions.length == 0)
            throw new IllegalArgumentException("permissions must not be null or empty");
        _permissions = permissions;
        _byteIndex = -1;
    }

    public Iterator<Permission> iterator() {
        return new PermissionIterable(_permissions);
    }

    public boolean hasNext() {
        int x = _byteIndex, y = _bitIndex, l = _permissions.length;
        if (x >= l) return false;
        while (true) {
            while (true) {
                if (--y < 0) {
                    ++x;
                    y = 8;
                    break;
                }
                if ((_permissions[x] >>> (y & 7) & 1) != 0) {
                    _byteIndex = x;
                    _bitIndex = y;
                    return true;
                }
            }
            while (true) {
                if (x >= l) {
                    _byteIndex = x;
                    _bitIndex = y;
                    return false;
                }
                if (_permissions[x] != 0) break;
                ++x;
            }
        }
    }

    public Permission next() {
        if ((~_byteIndex) == 0) throw new IllegalStateException("Iterator has not been started.");
        if (_byteIndex >= _permissions.length) throw new IllegalStateException("Iterator has been stopped.");
        return permissions[_byteIndex << 3 | ~_bitIndex & 7];
    }
}
package com.salespage.salespageservice.domains.utils;


import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class LockManager {
    public static final String LOCK_PREFIX = "isport_lock";
    public static final String LOCK_PREFIX_GAME = "isport:game:lock";

    public static final int TIME_LOCK_IN_SECOND = 2;

    @Autowired
    private RedissonClient client;

    public RLock startLockUpdateUser(ObjectId userId) {
        RLock lock = client.getLock(LOCK_PREFIX + ":user:" + userId);
        lock.lock(TIME_LOCK_IN_SECOND, TimeUnit.SECONDS);
        return lock;
    }

    public RLock startLockUser(List<Integer> userIds) {
        RLock[] lockUsers = new RLock[userIds.size()];

        for (int i = 0; i < userIds.size(); i++) {
            lockUsers[i] = client.getLock(LOCK_PREFIX + ":user:" + userIds.get(i));
        }

        RLock lock = client.getMultiLock(lockUsers);
        lock.lock(30, TimeUnit.SECONDS);
        return lock;
    }

    public RLock startLockFixture(Long fixtureId) {
        RLock lock = client.getLock(LOCK_PREFIX + ":fixture:" + fixtureId);
        lock.lock(TIME_LOCK_IN_SECOND, TimeUnit.SECONDS);
        return lock;
    }

    public RLock startLockFixture(List<Long> fixtures) {
        RLock[] lockFixture = new RLock[fixtures.size()];

        for (int i = 0; i < fixtures.size(); i++) {
            lockFixture[i] = client.getLock(LOCK_PREFIX + ":fixture:" + fixtures.get(i));
        }

        RLock lock = client.getMultiLock(lockFixture);
        lock.lock(10, TimeUnit.SECONDS);
        return lock;
    }

    public void unLock(RLock lock) {
        if (lock != null) lock.unlockAsync();
    }

    public RLock startLockSpinItem(Integer itemId) {
        RLock lock = client.getLock(LOCK_PREFIX_GAME + ":item:" + itemId);
        lock.lock(TIME_LOCK_IN_SECOND, TimeUnit.SECONDS);
        return lock;
    }
}

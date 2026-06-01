package com.example.appointmentservice.service;

import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.common_exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Service for distributed locking on time slots using Redis.
 * 
 * Prevents race condition when multiple users try to book the same time slot
 * by ensuring only one user can hold the lock at a time.
 */
@Service
public class TimeSlotLockService {

    private static final Logger logger = LoggerFactory.getLogger(TimeSlotLockService.class);

    private static final String LOCK_PREFIX = "timeslot:lock:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${appointment.lock.timeout-minutes:5}")
    private int lockTimeoutMinutes;

    /**
     * Acquire a distributed lock for a time slot.
     * 
     * Uses Redis SETNX (SET if Not eXists) with TTL to ensure:
     * - Only one user can hold the lock at a time
     * - Lock is automatically released after timeout to prevent deadlock
     * 
     * @param timeSlotId The ID of the time slot to lock
     * @param lockId A unique identifier for this lock holder (typically UUID)
     * @return true if lock was acquired, false if already locked by another user
     */
    public boolean acquireLock(String timeSlotId, String lockId) {
        String key = LOCK_PREFIX + timeSlotId;
        Duration ttl = Duration.ofMinutes(lockTimeoutMinutes);
        
        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(key, lockId, ttl);
        
        boolean result = Boolean.TRUE.equals(acquired);
        logger.debug("Acquire lock for timeSlot {}: {}", timeSlotId, result);
        
        return result;
    }

    /**
     * Acquire a distributed lock with custom TTL.
     * 
     * @param timeSlotId The ID of the time slot to lock
     * @param lockId A unique identifier for this lock holder
     * @param ttl How long the lock should be held
     * @return true if lock was acquired, false if already locked
     */
    public boolean acquireLock(String timeSlotId, String lockId, Duration ttl) {
        String key = LOCK_PREFIX + timeSlotId;
        
        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(key, lockId, ttl);
        
        boolean result = Boolean.TRUE.equals(acquired);
        logger.debug("Acquire lock for timeSlot {} with TTL {}: {}", timeSlotId, ttl, result);
        
        return result;
    }

    /**
     * Release a distributed lock.
     * 
     * Only the lock holder (matching lockId) can release the lock.
     * This prevents accidentally releasing another user's lock.
     * 
     * @param timeSlotId The ID of the time slot
     * @param lockId The lock ID that was used when acquiring
     * @return true if lock was released, false if lock doesn't exist or belongs to another owner
     */
    public boolean releaseLock(String timeSlotId, String lockId) {
        String key = LOCK_PREFIX + timeSlotId;
        String currentLockId = redisTemplate.opsForValue().get(key);
        
        if (lockId == null) {
            logger.warn("Attempted to release lock with null lockId for timeSlot: {}", timeSlotId);
            return false;
        }
        
        if (lockId.equals(currentLockId)) {
            redisTemplate.delete(key);
            logger.debug("Released lock for timeSlot: {}", timeSlotId);
            return true;
        }
        
        logger.warn("Failed to release lock for timeSlot {}: lockId mismatch (expected: {}, actual: {})",
                timeSlotId, lockId, currentLockId);
        return false;
    }

    /**
     * Check if a time slot is currently locked.
     * 
     * @param timeSlotId The ID of the time slot
     * @return true if locked, false otherwise
     */
    public boolean isLocked(String timeSlotId) {
        String key = LOCK_PREFIX + timeSlotId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Extend the TTL of an existing lock.
     * 
     * Useful for long-running operations that need more time.
     * Only the lock owner can extend the lock.
     * 
     * @param timeSlotId The ID of the time slot
     * @param lockId The lock ID that was used when acquiring
     * @param extension How much time to add to the lock
     * @return true if extended, false if lock doesn't exist or belongs to another owner
     */
    public boolean extendLock(String timeSlotId, String lockId, Duration extension) {
        String key = LOCK_PREFIX + timeSlotId;
        String currentLockId = redisTemplate.opsForValue().get(key);
        
        if (lockId.equals(currentLockId)) {
            redisTemplate.expire(key, extension);
            logger.debug("Extended lock for timeSlot {} by {}", timeSlotId, extension);
            return true;
        }
        
        logger.warn("Failed to extend lock for timeSlot {}: lockId mismatch", timeSlotId);
        return false;
    }

    /**
     * Get the remaining TTL of a lock in seconds.
     * 
     * @param timeSlotId The ID of the time slot
     * @return TTL in seconds, or -1 if no TTL, or -2 if key doesn't exist
     */
    public Long getLockTTL(String timeSlotId) {
        String key = LOCK_PREFIX + timeSlotId;
        return redisTemplate.getExpire(key);
    }
}

package com.kevintam.content.services;

public interface CourseAuditService {
    Boolean commitAudit(Long courseId, Long companyId);
}

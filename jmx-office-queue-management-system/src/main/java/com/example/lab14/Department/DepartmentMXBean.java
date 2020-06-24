package com.example.lab14.Department;

import com.example.lab14.utils.Ticket;

public interface DepartmentMXBean {
    void registerDepartmentListener(Integer applicantId);
    void unregisterDepartmentListener(Integer applicantId);
    String issueTicket(int applicantId, String category);
    String getInfo();

    boolean addCategory(String category, Integer priority);
    boolean updateCategory(String category, Integer priority);
    boolean deleteCategory(String category);
}

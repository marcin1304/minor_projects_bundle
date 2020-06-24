package com.example.lab14;

import com.example.lab14.Applicant.Applicant;
import com.example.lab14.Applicant.ApplicantGui;
import com.example.lab14.Department.Department;
import com.example.lab14.Department.DepartmentManagementGui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static final String departmentJmxName = "com.example.lab14:name=Department";
    public static final String applicantJmxBaseName = "com.example.lab14:name=Applicant_";
    private static int applicantId = 0;
    public static void main(String[] args) {
        try {
            Department department = new Department(Integer.parseInt(args[0]));
            DepartmentManagementGui departmentManagementGui = new DepartmentManagementGui(department);

            List<ApplicantGui> applicantGuis = new ArrayList<>();
            Integer numberOfApplicants = Integer.parseInt(args[1]);

            if(numberOfApplicants > 10)
                numberOfApplicants = 10;

            IntStream.range(0, numberOfApplicants).forEach( n -> {
                Applicant applicant = new Applicant(getNewApplicantId());
                ApplicantGui applicantGui = new ApplicantGui(applicant);
                applicant.registerGui(applicantGui);
                applicantGuis.add(applicantGui);
            }
        );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getNewApplicantId() {
        return applicantId++;
    }
}

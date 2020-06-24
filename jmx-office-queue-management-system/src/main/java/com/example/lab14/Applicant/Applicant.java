package com.example.lab14.Applicant;

import com.example.lab14.Department.DepartmentMXBean;
import com.example.lab14.Main;
import com.example.lab14.utils.ApplicantStatus;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Applicant implements ApplicantGuiListener, ApplicantMXBean {
    private int id;
    private ApplicantListener applicantListener;
    private String ticket = null;
    private String status = ApplicantStatus.noTicket;
    private String postsInfo;
    private String ticketsInfo;
    private DepartmentMXBean proxy;

    public Applicant(int id) {
        this.id = id;
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            mBeanServer.registerMBean(this, new ObjectName(Main.applicantJmxBaseName + id));
            proxy = JMX.newMXBeanProxy(
                    mBeanServer,
                    new ObjectName(Main.departmentJmxName),
                    DepartmentMXBean.class);
            proxy.registerDepartmentListener(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerGui(ApplicantListener applicantListener) {
        this.applicantListener = applicantListener;
        if(postsInfo != null)
            applicantListener.postsUpdated(postsInfo);
        if(ticketsInfo != null)
            applicantListener.ticketUpdated(ticketsInfo);
    }

    @Override
    public void requestTicket(String category) {
        if(ticket == null) {
            String ticketTmp = proxy.issueTicket(id, category);
            if(!ticketTmp.equals("")) {
                ticket = ticketTmp;
                status = ApplicantStatus.waiting;
                applicantListener.ticketUpdated(ticket);
                applicantListener.statusUpdated(status);
            }
        }
    }

    @Override
    public String getInfo() {
        return proxy.getInfo();
    }

    @Override
    public void movedToPost() {
        status = ApplicantStatus.atPost;
        applicantListener.statusUpdated(status);
    }

    @Override
    public void leftPost() {
        ticket = null;
        status = ApplicantStatus.noTicket;
        applicantListener.statusUpdated(status);
        applicantListener.ticketUpdated(ticket);
    }

    @Override
    public void postsUpdate(String postsInfo) {
        if(applicantListener != null)
            applicantListener.postsUpdated(postsInfo);
        else
            this.postsInfo = postsInfo;
    }

    @Override
    public void awaitingTicketsUpdate(String ticketsInfo) {
        if(applicantListener != null)
            applicantListener.awaitingTicketsUpdated(ticketsInfo);
        else
            this.ticketsInfo = ticketsInfo;
    }

    @Override
    public void generalInfo(String info) {
        applicantListener.generalInfoUpdated(info);
    }
}

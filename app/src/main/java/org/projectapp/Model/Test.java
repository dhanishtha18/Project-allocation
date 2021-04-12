package org.projectapp.Model;

public class Test {
    private String roll_no,name,enrollment,seat_no,ut1,ut2,ut_avg,group,individual,total;

    public Test() {
    }

    public Test(String roll_no, String name, String enrollment, String seat_no, String ut1, String ut2, String ut_avg, String group, String individual, String total) {
        this.roll_no = roll_no;
        this.name = name;
        this.enrollment = enrollment;
        this.seat_no = seat_no;
        this.ut1 = ut1;
        this.ut2 = ut2;
        this.ut_avg = ut_avg;
        this.group = group;
        this.individual = individual;
        this.total = total;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getUt1() {
        return ut1;
    }

    public void setUt1(String ut1) {
        this.ut1 = ut1;
    }

    public String getUt2() {
        return ut2;
    }

    public void setUt2(String ut2) {
        this.ut2 = ut2;
    }

    public String getUt_avg() {
        return ut_avg;
    }

    public void setUt_avg(String ut_avg) {
        this.ut_avg = ut_avg;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

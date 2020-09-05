package in.afckstechnologies.afcksenquirymanagement.models;



public class StudentsAttendanceDetailsDAO {
    String id="";
    String batch_id="";
    String attendance="";
    String student_name="";
    String batch_date="";
    String AttendanceDate="";
    String numbers="";
    public StudentsAttendanceDetailsDAO()
    {
    }

    public StudentsAttendanceDetailsDAO(String id, String batch_id, String attendance, String student_name, String batch_date, String attendanceDate, String numbers) {
        this.id = id;
        this.batch_id = batch_id;
        this.attendance = attendance;
        this.student_name = student_name;
        this.batch_date = batch_date;
        AttendanceDate = attendanceDate;
        this.numbers = numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getBatch_date() {
        return batch_date;
    }

    public void setBatch_date(String batch_date) {
        this.batch_date = batch_date;
    }

    public String getAttendanceDate() {
        return AttendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        AttendanceDate = attendanceDate;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}

package in.afckstechnologies.afcksenquirymanagement.models;

public class StudentsInBatchListDAO {

    String batch_code="";
    String student_Name="";
    String first_name="";
    String mobile_no="";
    String email_id="";
    String gender="";
    String course_name="";
    String start_date="";
    String baseFees="";
    String status="";
    String previous_attendance="";
    String discontinue_reason="";
    String course_code="";
    String fees="";
    String due_amount="";
    String id="";
    String last_name="";
    String sbd_id="";
    String student_batch_cat="";
    String notes_id="";

    public StudentsInBatchListDAO()
    {
    }

    public StudentsInBatchListDAO(String batch_code, String student_Name, String first_name, String mobile_no, String email_id, String gender, String course_name, String start_date, String baseFees, String status, String previous_attendance, String discontinue_reason, String course_code, String fees, String due_amount, String id, String last_name, String sbd_id, String student_batch_cat, String notes_id) {
        this.batch_code = batch_code;
        this.student_Name = student_Name;
        this.first_name = first_name;
        this.mobile_no = mobile_no;
        this.email_id = email_id;
        this.gender = gender;
        this.course_name = course_name;
        this.start_date = start_date;
        this.baseFees = baseFees;
        this.status = status;
        this.previous_attendance = previous_attendance;
        this.discontinue_reason = discontinue_reason;
        this.course_code = course_code;
        this.fees = fees;
        this.due_amount = due_amount;
        this.id = id;
        this.last_name = last_name;
        this.sbd_id = sbd_id;
        this.student_batch_cat = student_batch_cat;
        this.notes_id = notes_id;
    }

    public String getBatch_code() {
        return batch_code;
    }

    public void setBatch_code(String batch_code) {
        this.batch_code = batch_code;
    }

    public String getStudent_Name() {
        return student_Name;
    }

    public void setStudent_Name(String student_Name) {
        this.student_Name = student_Name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getBaseFees() {
        return baseFees;
    }

    public void setBaseFees(String baseFees) {
        this.baseFees = baseFees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrevious_attendance() {
        return previous_attendance;
    }

    public void setPrevious_attendance(String previous_attendance) {
        this.previous_attendance = previous_attendance;
    }

    public String getDiscontinue_reason() {
        return discontinue_reason;
    }

    public void setDiscontinue_reason(String discontinue_reason) {
        this.discontinue_reason = discontinue_reason;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(String due_amount) {
        this.due_amount = due_amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSbd_id() {
        return sbd_id;
    }

    public void setSbd_id(String sbd_id) {
        this.sbd_id = sbd_id;
    }

    public String getStudent_batch_cat() {
        return student_batch_cat;
    }

    public void setStudent_batch_cat(String student_batch_cat) {
        this.student_batch_cat = student_batch_cat;
    }

    public String getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(String notes_id) {
        this.notes_id = notes_id;
    }
}

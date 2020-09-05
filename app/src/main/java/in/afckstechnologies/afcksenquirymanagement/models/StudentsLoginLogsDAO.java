package in.afckstechnologies.afcksenquirymanagement.models;

public class StudentsLoginLogsDAO {
    String user_id="";
    String name="";
    String mobile_no="";
    String login_time="";
    String user_inBatch="";
    String status="";
    private boolean isSelected;

    public StudentsLoginLogsDAO() {

    }

    public StudentsLoginLogsDAO(String user_id, String name, String mobile_no, String login_time, String user_inBatch, String status, boolean isSelected) {
        this.user_id = user_id;
        this.name = name;
        this.mobile_no = mobile_no;
        this.login_time = login_time;
        this.user_inBatch = user_inBatch;
        this.status = status;
        this.isSelected = isSelected;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getUser_inBatch() {
        return user_inBatch;
    }

    public void setUser_inBatch(String user_inBatch) {
        this.user_inBatch = user_inBatch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

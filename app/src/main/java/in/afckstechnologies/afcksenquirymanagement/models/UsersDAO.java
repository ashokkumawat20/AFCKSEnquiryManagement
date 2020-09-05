package in.afckstechnologies.afcksenquirymanagement.models;

public class UsersDAO {

    String id = "";
    String first_name = "";
    String last_name = "";
    String mobile_no = "";
    String email_id = "";
    String gender = "";
    String fcm_id = "";
    String created_at = "";
    String status = "";
    String Notes = "";
    String preference = "";
    String CallBack = "";
    String EnqNotes = "";
    String Nick_name = "";
    String Nationality = "";
    String DOB = "";
    String Marital_status = "";
    String profile_pic = "";
    String job_search_status = "";
    String job_program_status = "";
    String current_ctc = "";
    String expected_from_ctc = "";
    String expected_to_ctc = "";
    long m_timestamp = 0;

    public UsersDAO() { }

    public UsersDAO(String id, String first_name, String last_name, String mobile_no, String email_id, String gender, String fcm_id, String created_at, String status, String notes, String preference, String callBack, String enqNotes, String nick_name, String nationality, String DOB, String marital_status, String profile_pic, String job_search_status, String job_program_status, String current_ctc, String expected_from_ctc, String expected_to_ctc, long m_timestamp) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile_no = mobile_no;
        this.email_id = email_id;
        this.gender = gender;
        this.fcm_id = fcm_id;
        this.created_at = created_at;
        this.status = status;
        Notes = notes;
        this.preference = preference;
        CallBack = callBack;
        EnqNotes = enqNotes;
        Nick_name = nick_name;
        Nationality = nationality;
        this.DOB = DOB;
        Marital_status = marital_status;
        this.profile_pic = profile_pic;
        this.job_search_status = job_search_status;
        this.job_program_status = job_program_status;
        this.current_ctc = current_ctc;
        this.expected_from_ctc = expected_from_ctc;
        this.expected_to_ctc = expected_to_ctc;
        this.m_timestamp = m_timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getCallBack() {
        return CallBack;
    }

    public void setCallBack(String callBack) {
        CallBack = callBack;
    }

    public String getEnqNotes() {
        return EnqNotes;
    }

    public void setEnqNotes(String enqNotes) {
        EnqNotes = enqNotes;
    }

    public String getNick_name() {
        return Nick_name;
    }

    public void setNick_name(String nick_name) {
        Nick_name = nick_name;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getMarital_status() {
        return Marital_status;
    }

    public void setMarital_status(String marital_status) {
        Marital_status = marital_status;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getJob_search_status() {
        return job_search_status;
    }

    public void setJob_search_status(String job_search_status) {
        this.job_search_status = job_search_status;
    }

    public String getJob_program_status() {
        return job_program_status;
    }

    public void setJob_program_status(String job_program_status) {
        this.job_program_status = job_program_status;
    }

    public String getCurrent_ctc() {
        return current_ctc;
    }

    public void setCurrent_ctc(String current_ctc) {
        this.current_ctc = current_ctc;
    }

    public String getExpected_from_ctc() {
        return expected_from_ctc;
    }

    public void setExpected_from_ctc(String expected_from_ctc) {
        this.expected_from_ctc = expected_from_ctc;
    }

    public String getExpected_to_ctc() {
        return expected_to_ctc;
    }

    public void setExpected_to_ctc(String expected_to_ctc) {
        this.expected_to_ctc = expected_to_ctc;
    }

    public long getU_timestamp() {
        return m_timestamp;
    }

    public void setU_timestamp(long u_timestamp) {
        this.m_timestamp = u_timestamp;
    }
}

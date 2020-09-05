package in.afckstechnologies.afcksenquirymanagement.models;



public class StCenterDAO {
    String id = "";
    String s_no = "";
    String branch_name = "";
    String user_id = "";


    public StCenterDAO() {
    }
    public StCenterDAO(String id, String branch_name) {
        this.id = id;
        this.branch_name = branch_name;

    }
    public StCenterDAO(String id, String s_no, String branch_name, String user_id) {
        this.id = id;
        this.s_no = s_no;
        this.branch_name = branch_name;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

package in.afckstechnologies.afcksenquirymanagement.models;



public class CoursesTypeDAO {

    String id = "";
    String type_name = "";
    String m_timestamp="";
    public CoursesTypeDAO() {

    }

    public CoursesTypeDAO(String id, String type_name, String m_timestamp) {
        this.id = id;
        this.type_name = type_name;
        this.m_timestamp = m_timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getM_timestamp() {
        return m_timestamp;
    }

    public void setM_timestamp(String m_timestamp) {
        this.m_timestamp = m_timestamp;
    }
}

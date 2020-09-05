package in.afckstechnologies.afcksenquirymanagement.models;

/**
 * Created by admin on 2/18/2017.
 */

public class DayPrefrenceDAO {
    String id = "";
    String Prefrence = "";
    String user_id="";
    String dayprefrence_id="";
    String 	del_status="";
    String isselected="";
    String m_timestamp="";
    private boolean isSelected;

    public DayPrefrenceDAO() {
    }


    public DayPrefrenceDAO(String id, String prefrence, String isselected, boolean isSelected) {
        this.id = id;
        Prefrence = prefrence;
        this.isselected = isselected;
        this.isSelected = isSelected;
    }

    public DayPrefrenceDAO(String id, String prefrence, String m_timestamp) {
        this.id = id;
        Prefrence = prefrence;
        this.m_timestamp = m_timestamp;
    }

    public DayPrefrenceDAO(String id, String prefrence,String m_timestamp, String isselected,  boolean isSelected) {
        this.id = id;
        Prefrence = prefrence;
        this.m_timestamp = m_timestamp;
        this.isselected = isselected;
        this.isSelected = isSelected;
    }

    public DayPrefrenceDAO(String id, String user_id, String dayprefrence_id, String del_status, String m_timestamp) {
        this.id = id;
        this.user_id = user_id;
        this.dayprefrence_id = dayprefrence_id;
        this.del_status = del_status;
        this.m_timestamp = m_timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefrence() {
        return Prefrence;
    }

    public void setPrefrence(String prefrence) {
        Prefrence = prefrence;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDayprefrence_id() {
        return dayprefrence_id;
    }

    public void setDayprefrence_id(String dayprefrence_id) {
        this.dayprefrence_id = dayprefrence_id;
    }

    public String getDel_status() {
        return del_status;
    }

    public void setDel_status(String del_status) {
        this.del_status = del_status;
    }

    public String getIsselected() {
        return isselected;
    }

    public void setIsselected(String isselected) {
        this.isselected = isselected;
    }

    public String getM_timestamp() {
        return m_timestamp;
    }

    public void setM_timestamp(String m_timestamp) {
        this.m_timestamp = m_timestamp;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

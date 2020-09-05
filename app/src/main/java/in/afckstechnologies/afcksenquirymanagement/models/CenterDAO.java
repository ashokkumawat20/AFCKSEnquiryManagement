package in.afckstechnologies.afcksenquirymanagement.models;

public class CenterDAO {
    String id = "";
    String branch_name = "";
    String latitude = "";
    String longitude = "";
    String branch_short = "";
    String m_timestamp = "";
    String address = "";
    String isselected = "";

    private boolean isSelected = false;

    public CenterDAO() {
    }

    public CenterDAO(String id, String branch_name, String latitude, String longitude, String branch_short, String m_timestamp, String address, String isselected, boolean isSelected) {
        this.id = id;
        this.branch_name = branch_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.branch_short = branch_short;
        this.m_timestamp = m_timestamp;
        this.address = address;
        this.isselected = isselected;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBranch_short() {
        return branch_short;
    }

    public void setBranch_short(String branch_short) {
        this.branch_short = branch_short;
    }

    public String getM_timestamp() {
        return m_timestamp;
    }

    public void setM_timestamp(String m_timestamp) {
        this.m_timestamp = m_timestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsselected() {
        return isselected;
    }

    public void setIsselected(String isselected) {
        this.isselected = isselected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

package in.afckstechnologies.afcksenquirymanagement.models;

/**
 * Created by admin on 12/16/2016.
 */

public class CommentModeDAO {
    private String id;
    private String user_id;
    private String student_comments;
    private String date_comments;
    private String m_timestamp;

    public CommentModeDAO() {

    }

    public CommentModeDAO(String id, String student_comments, String date_comments) {
        this.id = id;
        this.student_comments = student_comments;
        this.date_comments = date_comments;

    }

    public CommentModeDAO(String id, String user_id, String student_comments, String date_comments, String m_timestamp) {
        this.id = id;
        this.user_id = user_id;
        this.student_comments = student_comments;
        this.date_comments = date_comments;
        this.m_timestamp = m_timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_comments() {
        return student_comments;
    }

    public void setStudent_comments(String student_comments) {
        this.student_comments = student_comments;
    }

    public String getDate_comments() {
        return date_comments;
    }

    public void setDate_comments(String date_comments) {
        this.date_comments = date_comments;
    }

    public String getM_timestamp() {
        return m_timestamp;
    }

    public void setM_timestamp(String m_timestamp) {
        this.m_timestamp = m_timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
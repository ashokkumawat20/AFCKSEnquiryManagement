package in.afckstechnologies.afcksenquirymanagement.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.afckstechnologies.afcksenquirymanagement.models.BatchesForStudentsDAO;
import in.afckstechnologies.afcksenquirymanagement.models.CenterDAO;
import in.afckstechnologies.afcksenquirymanagement.models.CommentModeDAO;
import in.afckstechnologies.afcksenquirymanagement.models.CoursesDAO;
import in.afckstechnologies.afcksenquirymanagement.models.CoursesTypeDAO;
import in.afckstechnologies.afcksenquirymanagement.models.DayPrefrenceDAO;
import in.afckstechnologies.afcksenquirymanagement.models.NewCoursesDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StCenterDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StCoursesDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StudentsAttendanceDetailsDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StudentsInBatchListDAO;
import in.afckstechnologies.afcksenquirymanagement.models.StudentsLoginLogsDAO;
import in.afckstechnologies.afcksenquirymanagement.models.TemplatesContactDAO;
import in.afckstechnologies.afcksenquirymanagement.models.UsersDAO;

public class JsonHelper {
    private ArrayList<UsersDAO> usersDAOArrayList = new ArrayList<UsersDAO>();
    private UsersDAO usersDAO;

    private ArrayList<StCenterDAO> stCenterDAOArrayList = new ArrayList<StCenterDAO>();
    private StCenterDAO stCenterDAO;

    private ArrayList<StCoursesDAO> stCoursesDAOArrayList = new ArrayList<StCoursesDAO>();
    private StCoursesDAO stCoursesDAO;

    private ArrayList<TemplatesContactDAO> templatesContactDAOArrayList = new ArrayList<TemplatesContactDAO>();
    private TemplatesContactDAO templatesContactDAO;

    private ArrayList<StudentsInBatchListDAO> studentsInBatchListDAOArrayList = new ArrayList<StudentsInBatchListDAO>();
    private StudentsInBatchListDAO studentsInBatchListDAO;

    //online
    private ArrayList<CenterDAO> centerDAOArrayList = new ArrayList<CenterDAO>();
    private CenterDAO centerDAO;
    private ArrayList<CommentModeDAO> commentModeDAOArrayList = new ArrayList<CommentModeDAO>();
    private CommentModeDAO commentModeDAO;
    private ArrayList<DayPrefrenceDAO> DayPrefrenceDAOArrayList = new ArrayList<DayPrefrenceDAO>();
    private DayPrefrenceDAO dayPrefrenceDAO;
    private ArrayList<NewCoursesDAO> newCoursesDAOArrayList = new ArrayList<NewCoursesDAO>();
    private NewCoursesDAO newCoursesDAO;
    private ArrayList<StudentsAttendanceDetailsDAO> studentsAttendanceDetailsDAOArrayList = new ArrayList<StudentsAttendanceDetailsDAO>();
    private StudentsAttendanceDetailsDAO studentsAttendanceDetailsDAO;
    private ArrayList<BatchesForStudentsDAO> batchDAOArrayList = new ArrayList<BatchesForStudentsDAO>();
    private BatchesForStudentsDAO batchDAO;

    private ArrayList<CenterDAO> centerDAOArrayList1 = new ArrayList<CenterDAO>();
    private CenterDAO centerDAO1;

    private ArrayList<CoursesDAO> coursesDAOArrayList = new ArrayList<CoursesDAO>();
    private CoursesDAO coursesDAO;

    private ArrayList<CoursesTypeDAO> coursesTypeDAOArrayList = new ArrayList<CoursesTypeDAO>();
    private CoursesTypeDAO coursesTypeDAO;

    private ArrayList<StudentsLoginLogsDAO> studentsLoginLogsDAOArrayList = new ArrayList<StudentsLoginLogsDAO>();
    private StudentsLoginLogsDAO studentsLoginLogsDAO;

    //usersPaser
    public ArrayList<UsersDAO> usersList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    usersDAO = new UsersDAO();
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    Date d = null;
                    try {
                        d = f.parse(object.getString("m_timestamp"));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    long timeInMillis = d.getTime();
                    usersDAO.setId(object.getString("id"));
                    usersDAO.setFirst_name(object.getString("first_name"));
                    usersDAO.setLast_name(object.getString("last_name"));
                    usersDAO.setMobile_no(object.getString("mobile_no"));
                    usersDAO.setEmail_id(object.getString("email_id"));
                    usersDAO.setGender(object.getString("gender"));
                    usersDAO.setFcm_id(object.getString("fcm_id"));
                    usersDAO.setCreated_at(object.getString("created_at"));
                    usersDAO.setStatus(object.getString("status"));
                    usersDAO.setNotes(object.getString("Notes"));
                    usersDAO.setPreference(object.getString("preference"));
                    usersDAO.setCallBack(object.getString("CallBack"));
                    usersDAO.setEnqNotes(object.getString("EnqNotes"));
                    usersDAO.setNick_name(object.getString("Nick_name"));
                    usersDAO.setNationality(object.getString("Nationality"));
                    usersDAO.setDOB(object.getString("DOB"));
                    usersDAO.setMarital_status(object.getString("Marital_status"));
                    usersDAO.setProfile_pic(object.getString("profile_pic"));
                    usersDAO.setJob_search_status(object.getString("job_search_status"));
                    usersDAO.setJob_program_status(object.getString("job_program_status"));
                    usersDAO.setCurrent_ctc(object.getString("current_ctc"));
                    usersDAO.setExpected_from_ctc(object.getString("expected_from_ctc"));
                    usersDAO.setExpected_to_ctc(object.getString("expected_to_ctc"));
                    usersDAO.setU_timestamp(timeInMillis);
                    usersDAOArrayList.add(usersDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return usersDAOArrayList;
    }

    //locationPaser
    public ArrayList<StCenterDAO> usersLocList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    stCenterDAO = new StCenterDAO();

                    stCenterDAO.setId(object.getString("id"));
                    stCenterDAO.setS_no(object.getString("s_no"));
                    stCenterDAO.setBranch_name(object.getString("branch_name"));
                    stCenterDAO.setUser_id(object.getString("user_id"));
                    stCenterDAOArrayList.add(stCenterDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stCenterDAOArrayList;
    }

    //coursePaser
    public ArrayList<StCoursesDAO> usersCourseList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    stCoursesDAO = new StCoursesDAO();

                    stCoursesDAO.setId(object.getString("id"));
                    stCoursesDAO.setS_no(object.getString("s_no"));
                    stCoursesDAO.setType_name_id(object.getString("type_name_id"));
                    stCoursesDAO.setType_name(object.getString("type_name"));
                    stCoursesDAO.setCourse_name(object.getString("course_name"));
                    stCoursesDAO.setCourse_code(object.getString("course_code"));
                    stCoursesDAO.setUser_id(object.getString("user_id"));
                    stCoursesDAOArrayList.add(stCoursesDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stCoursesDAOArrayList;
    }


    //coursePaser
    public ArrayList<TemplatesContactDAO> usersTrainerTempList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    templatesContactDAO = new TemplatesContactDAO();
                    templatesContactDAO.setID(object.getString("ID"));
                    templatesContactDAO.setSubject(object.getString("Subject"));
                    templatesContactDAO.setTag(object.getString("tag"));
                    templatesContactDAO.setTemplate_Text(object.getString("Template_Text"));
                    templatesContactDAOArrayList.add(templatesContactDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return templatesContactDAOArrayList;
    }

    //studentPaser
    public ArrayList<StudentsInBatchListDAO> studentsList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    studentsInBatchListDAO = new StudentsInBatchListDAO();
                    studentsInBatchListDAO.setId(object.getString("id"));
                    studentsInBatchListDAO.setFirst_name(object.getString("first_name"));
                    studentsInBatchListDAO.setLast_name(object.getString("last_name"));
                    studentsInBatchListDAO.setMobile_no(object.getString("mobile_no"));
                    studentsInBatchListDAO.setEmail_id(object.getString("email_id"));
                    studentsInBatchListDAO.setGender(object.getString("gender"));
                    studentsInBatchListDAO.setStudent_Name(object.getString("Students_Name"));
                    studentsInBatchListDAO.setBaseFees(object.getString("BaseFees"));
                    studentsInBatchListDAO.setCourse_name(object.getString("course_name"));
                    studentsInBatchListDAO.setCourse_code(object.getString("course_code"));
                    studentsInBatchListDAO.setStudent_batch_cat(object.getString("student_batch_cat"));
                    studentsInBatchListDAO.setSbd_id(object.getString("sbd_id"));
                    studentsInBatchListDAO.setNotes_id(object.getString("notes_id"));
                    studentsInBatchListDAO.setFees(object.getString("fees"));
                    studentsInBatchListDAO.setDue_amount(object.getString("due_amount"));
                    studentsInBatchListDAO.setStart_date(object.getString("start_date"));
                    studentsInBatchListDAO.setBatch_code(object.getString("batch_code"));
                    studentsInBatchListDAO.setStatus(object.getString("Status"));
                    studentsInBatchListDAO.setPrevious_attendance(object.getString("previous_attendance"));
                    studentsInBatchListDAO.setDiscontinue_reason(object.getString("discontinue_reason"));
                    studentsInBatchListDAOArrayList.add(studentsInBatchListDAO);

                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentsInBatchListDAOArrayList;
    }

    //online parser
    //centerPaser
    public ArrayList<CenterDAO> parseCenterList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                centerDAO = new CenterDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                centerDAO.setId(json_data.getString("id"));
                centerDAO.setBranch_name(json_data.getString("branch_name"));
                centerDAO.setAddress(json_data.getString("address"));
                centerDAO.setIsselected(json_data.getString("isselected"));
                centerDAOArrayList.add(centerDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return centerDAOArrayList;
    }


    //studentlistfeesdetailsrPaser
    public ArrayList<CommentModeDAO> parseStudentCommentDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                commentModeDAO = new CommentModeDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                commentModeDAO.setId(json_data.getString("id"));
                commentModeDAO.setStudent_comments(json_data.getString("student_comment"));
                commentModeDAO.setDate_comments(json_data.getString("display_date"));
                commentModeDAOArrayList.add(commentModeDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return commentModeDAOArrayList;
    }

    //centerPaser
    public ArrayList<DayPrefrenceDAO> parseDayPrefrenceList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                dayPrefrenceDAO = new DayPrefrenceDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                dayPrefrenceDAO.setId(json_data.getString("id"));
                dayPrefrenceDAO.setPrefrence(json_data.getString("Prefrence"));
                dayPrefrenceDAO.setIsselected(json_data.getString("isselected"));
                DayPrefrenceDAOArrayList.add(dayPrefrenceDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return DayPrefrenceDAOArrayList;
    }

    //newcoursesPaser
    public ArrayList<NewCoursesDAO> parseNewCoursesList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                newCoursesDAO = new NewCoursesDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                newCoursesDAO.setId(json_data.getString("id"));
                newCoursesDAO.setCourse_type_id(json_data.getString("course_type_id"));
                newCoursesDAO.setCourse_code(json_data.getString("course_code"));
                newCoursesDAO.setCourse_name(json_data.getString("course_name"));
                newCoursesDAO.setTime_duration(json_data.getString("time_duration"));
                newCoursesDAO.setPrerequisite(json_data.getString("prerequisite"));
                newCoursesDAO.setRecommonded(json_data.getString("recommonded"));
                newCoursesDAO.setFees(json_data.getString("fees"));
                newCoursesDAO.setIsselected(json_data.getString("isselected"));
                newCoursesDAOArrayList.add(newCoursesDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newCoursesDAOArrayList;
    }

    //studentlistattendancedetailsrPaser
    public ArrayList<StudentsAttendanceDetailsDAO> parseStudentAttendanceDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            int len = leadJsonObj.length();
            for (int i = 0; i < leadJsonObj.length(); i++) {
                String sequence = String.format("%03d", len--);
                studentsAttendanceDetailsDAO = new StudentsAttendanceDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                studentsAttendanceDetailsDAO.setBatch_id(json_data.getString("batch_id"));
                studentsAttendanceDetailsDAO.setStudent_name(json_data.getString("student_name"));
                studentsAttendanceDetailsDAO.setAttendance(json_data.getString("attendance"));
                studentsAttendanceDetailsDAO.setAttendanceDate(json_data.getString("AttendanceDate"));
                studentsAttendanceDetailsDAO.setNumbers("" + sequence);
                studentsAttendanceDetailsDAOArrayList.add(studentsAttendanceDetailsDAO);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentsAttendanceDetailsDAOArrayList;
    }

    //newcoursesPaser
    public ArrayList<BatchesForStudentsDAO> parseBatchesList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                batchDAO = new BatchesForStudentsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                batchDAO.setBatchtype(json_data.getString("batchtype"));
                batchDAO.setId(json_data.getString("id"));
                batchDAO.setStart_date(json_data.getString("new_start_date"));
                batchDAO.setTimings(json_data.getString("timings"));
                batchDAO.setFrequency(json_data.getString("frequency"));
                batchDAO.setDuration(json_data.getString("duration"));
                batchDAO.setFees(json_data.getString("fees"));
                batchDAO.setFaculty_Name(json_data.getString("faculty_Name"));
                batchDAO.setNotes(json_data.getString("Notes"));
                batchDAO.setCourse_name(json_data.getString("course_name"));
                batchDAO.setBranch_name(json_data.getString("branch_name"));
                batchDAOArrayList.add(batchDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return batchDAOArrayList;
    }

    //comingbatchPaser
    public ArrayList<BatchesForStudentsDAO> comingBatchList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    batchDAO = new BatchesForStudentsDAO();
                    batchDAO.setBatchtype(object.getString("batchtype"));
                    batchDAO.setId(object.getString("id"));
                    batchDAO.setStart_date(object.getString("new_start_date"));
                    batchDAO.setTimings(object.getString("timings"));
                    batchDAO.setFrequency(object.getString("frequency"));
                    batchDAO.setDuration(object.getString("duration"));
                    batchDAO.setFees(object.getString("fees"));
                    batchDAO.setFaculty_Name(object.getString("faculty_Name"));
                    batchDAO.setNotes(object.getString("Notes"));
                    batchDAO.setCourse_name(object.getString("course_name"));
                    batchDAO.setBranch_name(object.getString("branch_name"));
                    batchDAO.setCourse_id(object.getString("course_id"));
                    batchDAOArrayList.add(batchDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return batchDAOArrayList;
    }

    //comingbatchPaser
    public ArrayList<StudentsAttendanceDetailsDAO> studentAttendanceList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    studentsAttendanceDetailsDAO = new StudentsAttendanceDetailsDAO();
                    studentsAttendanceDetailsDAO.setId(object.getString("id"));
                    studentsAttendanceDetailsDAO.setBatch_id(object.getString("batch_id"));
                    studentsAttendanceDetailsDAO.setStudent_name(object.getString("student_name"));
                    studentsAttendanceDetailsDAO.setAttendance(object.getString("attendance"));
                    studentsAttendanceDetailsDAO.setAttendanceDate(object.getString("AttendanceDate"));
                    studentsAttendanceDetailsDAO.setBatch_date(object.getString("batch_date"));
                    studentsAttendanceDetailsDAO.setId(object.getString("id"));
                    studentsAttendanceDetailsDAOArrayList.add(studentsAttendanceDetailsDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return studentsAttendanceDetailsDAOArrayList;
    }

    //coursePaser
    public ArrayList<CenterDAO> branchesList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    centerDAO1 = new CenterDAO();
                    centerDAO1.setId(object.getString("id"));
                    centerDAO1.setBranch_name(object.getString("branch_name"));
                    centerDAO1.setBranch_short(object.getString("branch_short"));
                    centerDAO1.setAddress(object.getString("address"));
                    centerDAO1.setLatitude(object.getString("latitude"));
                    centerDAO1.setLongitude(object.getString("longitude"));
                    centerDAO1.setM_timestamp(object.getString("m_timestamp"));
                    centerDAOArrayList1.add(centerDAO1);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return centerDAOArrayList1;
    }

    //coursePaser
    public ArrayList<CoursesDAO> coursesList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    coursesDAO = new CoursesDAO();
                    coursesDAO.setId(object.getString("id"));
                    coursesDAO.setCourse_type_id(object.getString("course_type_id"));
                    coursesDAO.setCourse_name(object.getString("course_name"));
                    coursesDAO.setCourse_code(object.getString("course_code"));
                    coursesDAO.setFees(object.getString("fees"));
                    coursesDAO.setPrerequisite(object.getString("prerequisite"));
                    coursesDAO.setRecommonded(object.getString("recommonded"));
                    coursesDAO.setTime_duration(object.getString("time_duration"));
                    coursesDAO.setSyllabuspath(object.getString("syllabuspath"));
                    coursesDAO.setYou_tube_link(object.getString("you_tube_link"));
                    coursesDAO.setM_timestamp(object.getString("m_timestamp"));
                    coursesDAOArrayList.add(coursesDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return coursesDAOArrayList;
    }

    //coursePaser
    public ArrayList<CoursesTypeDAO> coursesTypeList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    coursesTypeDAO = new CoursesTypeDAO();
                    coursesTypeDAO.setId(object.getString("id"));
                    coursesTypeDAO.setType_name(object.getString("type_name"));
                    coursesTypeDAOArrayList.add(coursesTypeDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return coursesTypeDAOArrayList;
    }

    //coursePaser
    public ArrayList<DayPrefrenceDAO> dayPrefrenceList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    dayPrefrenceDAO = new DayPrefrenceDAO();
                    dayPrefrenceDAO.setId(object.getString("id"));
                    dayPrefrenceDAO.setPrefrence(object.getString("Prefrence"));
                    dayPrefrenceDAO.setM_timestamp(object.getString("m_timestamp"));
                    DayPrefrenceDAOArrayList.add(dayPrefrenceDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return DayPrefrenceDAOArrayList;
    }

    public ArrayList<DayPrefrenceDAO> usersDayPrefrenceList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    dayPrefrenceDAO = new DayPrefrenceDAO();
                    dayPrefrenceDAO.setId(object.getString("id"));
                    dayPrefrenceDAO.setUser_id(object.getString("user_id"));
                    dayPrefrenceDAO.setDayprefrence_id(object.getString("dayprefrence_id"));
                    dayPrefrenceDAO.setDel_status(object.getString("del_status"));
                    dayPrefrenceDAO.setM_timestamp(object.getString("m_timestamp"));
                    DayPrefrenceDAOArrayList.add(dayPrefrenceDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return DayPrefrenceDAOArrayList;
    }

    public ArrayList<CommentModeDAO> studentCommentsList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);
        try {
            JSONObject jsonObject = new JSONObject(ListResponse);

            if (!jsonObject.isNull("dataList")) {
                JSONArray jsonArray = jsonObject.getJSONArray("dataList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    commentModeDAO = new CommentModeDAO();
                    commentModeDAO.setId(object.getString("id"));
                    commentModeDAO.setUser_id(object.getString("user_id"));
                    commentModeDAO.setStudent_comments(object.getString("student_comment"));
                    commentModeDAO.setDate_comments(object.getString("comments_date"));
                    commentModeDAO.setM_timestamp(object.getString("m_timestamp"));
                    commentModeDAOArrayList.add(commentModeDAO);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return commentModeDAOArrayList;
    }

    public ArrayList<StudentsLoginLogsDAO> studentLoginLogsList(String ListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", ListResponse);

        try {
            JSONArray leadJsonObj = new JSONArray(ListResponse);
            for (int i = 0; i < leadJsonObj.length(); i++) {
                JSONObject object = leadJsonObj.getJSONObject(i);
                studentsLoginLogsDAO = new StudentsLoginLogsDAO();
                studentsLoginLogsDAO.setUser_id(object.getString("user_id"));
                studentsLoginLogsDAO.setMobile_no(object.getString("mobile_no"));
                studentsLoginLogsDAO.setName(object.getString("name"));
                studentsLoginLogsDAO.setLogin_time(object.getString("login_time"));
                studentsLoginLogsDAO.setUser_inBatch(object.getString("user_inBatch"));
                studentsLoginLogsDAO.setStatus(object.getString("status"));
                studentsLoginLogsDAOArrayList.add(studentsLoginLogsDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return studentsLoginLogsDAOArrayList;
    }
}

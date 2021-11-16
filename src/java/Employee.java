import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ReferencedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;

import javax.faces.validator.ValidatorException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
@ManagedBean
@SessionScoped
@FacesValidator("Validator")
@ReferencedBean
public class Employee implements Validator, Serializable {
DBCon connection=new DBCon();
    String message;
    String param1;
    private String EmployeeCode;
    private String EmployeeName;
    private String EmployeeAddress;
    private String EmployeeEmail;

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public Employee() {
        this.setEmployeeAddress(" ");
        this.setEmployeeCode(" ");
        this.setEmployeeEmail(" ");
        this.setEmployeeName(" ");
        this.setMessage(" ");
    }

    public Employee(String EmployeeCode, String EmployeeName, String EmployeeAddress, String EmployeeEmail) {
        this.EmployeeCode = EmployeeCode;
        this.EmployeeName = EmployeeName;
        this.EmployeeAddress = EmployeeAddress;
        this.EmployeeEmail = EmployeeEmail;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public void setEmployeeCode(String EmployeeCode) {
        this.EmployeeCode = EmployeeCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String EmployeeName) {
        this.EmployeeName = EmployeeName;
    }

    public String getEmployeeAddress() {
        return EmployeeAddress;
    }

    public void setEmployeeAddress(String EmployeeAddress) {
        this.EmployeeAddress = EmployeeAddress;
    }

    public String getEmployeeEmail() {
        return EmployeeEmail;
    }

    public void setEmployeeEmail(String EmployeeEmail) {
        this.EmployeeEmail = EmployeeEmail;
    }
    
    //--------------------------------------Method For Add New Employee into the DataBase---------------------------------------------------------------------
  

    public String add() throws ClassNotFoundException, SQLException {
       
       
        

        PreparedStatement pstmt = connection.Connenctivity()
                .prepareStatement("insert into employee(employee_code,employee_name,employee_address,employee_email) values(?,?,?,?)");

        pstmt.setString(1, this.getEmployeeCode());
        pstmt.setString(2, this.getEmployeeName());
        pstmt.setString(3, this.getEmployeeAddress());
        pstmt.setString(4, this.getEmployeeEmail());
        pstmt.execute();

        pstmt.close();
       connection.Connenctivity().close();

        this.setEmployeeAddress(" ");
        this.setEmployeeCode(" ");
        this.setEmployeeEmail(" ");
        this.setEmployeeName(" ");
return "index.xhtml";
    }
//************************************Method For Validation Of duplicated User And It`s Message**********************************
    public void validatesuccesOrfailure(FacesContext context, UIComponent comp,
            Object value) throws ClassNotFoundException, SQLException {

       String mno = (String) value;

        PreparedStatement pstmt2 = connection.Connenctivity()

                .prepareStatement("select * from employee");
        try {
            ResultSet result = pstmt2.executeQuery();
            while (result.next()) {

                if (mno.equals(result.getString("employee_code"))) {
                    FacesMessage msg
                            = new FacesMessage("Duplicated User", "Change employee Code");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//**********************************Built In method For Validate the Exsit Email*********************************************
    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {

       
        String mno = (String) value;

        PreparedStatement pstmt3= null;
        try {
            pstmt3 = connection.Connenctivity()
                    .prepareStatement("select * from employee");
        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
        Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
    }
        try {
            ResultSet result = pstmt3.executeQuery();
            while (result.next()) {

                if (mno.equals(result.getString("employee_email"))) {
                    FacesMessage msg
                            = new FacesMessage("Email Exsit", "Try Another Email");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);

                }
            }
        } catch (SQLException s) {
        }
    }
//_---------------------------------------Method Of Retriving All The Employees Stored In DataBase----------------------------------------
    public List<Employee> getEmployees() throws ClassNotFoundException {
        List<Employee> listOfEmployees = new ArrayList<>();

    
        PreparedStatement pstmt4 = null;
        try {
            pstmt4 = connection.Connenctivity()
                    .prepareStatement("select * from employee");
        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ResultSet result = pstmt4.executeQuery();
            while (result.next()) {

                listOfEmployees.add(new Employee(result.getString("employee_code"), result.getString("employee_name"), result.getString("employee_address"), result.getString("employee_email")));

            }

        } catch (SQLException s) {
            s.printStackTrace();
        }
        return listOfEmployees;
    }

  //---------------------Method For redirect to the Edit Page With The Parameter----------------------------------------
    FacesContext fc = FacesContext.getCurrentInstance();
    Map<String, Object> session = fc.getExternalContext().getSessionMap();

    public String edit() {

        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        param1 = params.get("code");
        session.put("par", param1);
        System.out.println(param1);
        return "edit.xhtml";
    }

}

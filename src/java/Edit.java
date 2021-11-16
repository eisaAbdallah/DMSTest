import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ReferencedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


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
@ReferencedBean
public class Edit {

 DBCon Connectivity = new DBCon();
    String editContent;
     String param;

    public String getEditContent() {
        return editContent;
    }

    public void setEditContent(String editContent) {
        this.editContent = editContent;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
// ------------------------------------Method Of Updatinbg the  Desired Employee for the update Process-----------------------------------------
    public String editEmployeeData() throws ClassNotFoundException {

      
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        PreparedStatement pstmt = null;

        param = requestMap.get("code");

        PreparedStatement pstmt2 = null;

        try {

            pstmt = Connectivity.Connenctivity()
                    .prepareStatement("update employee set employee_address = ? where employee_code = ?");
            pstmt.setString(1, this.getEditContent());
            pstmt.setString(2, param);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "index.xhtml";
    }

}

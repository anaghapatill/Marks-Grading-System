import controllers.ClassHomePageController;
import controllers.CourseStudentController;
import controllers.LoginController;
import models.ImportExcel;
import models.LoggedData;
import models.Student;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import views.*;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

    	LoggedData.InitData();
    	MainPanelView.initialize();
    	LoginController lg = new LoginController();
    	String filePath = "";


    }
}
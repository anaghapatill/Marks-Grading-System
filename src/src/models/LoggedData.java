package models;

import MySql.DBManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class LoggedData {
	
	private static Professor prof;
	private static ArrayList<Professor> profList;
	//private static ArrayList<Course> activeCourseList;
	private static Course selectedCourse;
	private static Task selectedTask;
	private static SubTask selectedSubTask; 
	private static GradingSystem GS;
	private static ArrayList<CourseTemplate> cTList;
	private static ArrayList<CourseSection> courseSectionList;

	public static int subTaskID = 1;
    private static DBManager dbManager;

	public  static void  InitData()
	{
		dbManager = new DBManager();
		dbManager.connect();

		courseSectionList = new ArrayList<>();
		courseSectionList = dbManager.readALLSections();
		

	}
	
	public static boolean Login(String email, String pswd)
	{
		boolean res = dbManager.readGradingSystem(email, pswd);
		return res;

	}

	public static void  RefreshGradingSystem()
	{
		dbManager.LoadGradingSystem( prof);
	}

	public static Professor getProf() {
		return prof;
	}

	public static void setProf(Professor prof) {
		LoggedData.prof = prof;
	}


	public static Course getSelectedCourse() {
		return LoggedData.selectedCourse;
	}

	public static void setSelectedCourse(Course selectedCourse) {
		LoggedData.selectedCourse = selectedCourse;
	}

	public static Task getSelectedTask() {
		return selectedTask;
	}

	public static void setSelectedTask(Task selectedTask) {
		LoggedData.selectedTask = selectedTask;
	}

	public static SubTask getSelectedSubTask() {
		return selectedSubTask;
	}

	public static void setSelectedSubTask(SubTask selectedSubTask) {
		LoggedData.selectedSubTask = selectedSubTask;
	}

	public static GradingSystem getGradingSystem() {
		return GS;
	}

	public static void setGradingSystem(GradingSystem gS) {
		GS = gS;
	}

	public static DBManager getDbManager() {
		return dbManager;
	}

	public static ArrayList<CourseSection> getCourseSectionList() {
		return courseSectionList;
	}
}

package controllers;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.mysql.jdbc.log.Log;
import models.CourseSection;
import models.Grade;
import models.LoggedData;
import models.Student;
import org.apache.poi.hpsf.Section;
import views.MainPanelView;
import views.SubTaskGrade;

public class SubTaskGradeController {
	
	private SubTaskGrade subTaskGrade;
	AbstractTableModel tableModel;
	private JPanel parentPanel;
	public SubTaskGradeController()
	{

		subTaskGrade = new SubTaskGrade(LoggedData.getSelectedSubTask().getName(),
				LoggedData.getSelectedSubTask().getMeanGrade(),LoggedData.getSelectedSubTask().getMedianPercentage() * 100,
				LoggedData.getSelectedSubTask().getStandardDeviation());
		parentPanel = MainPanelView.getParentPanel();
		parentPanel.removeAll();
		parentPanel.revalidate();
		parentPanel.repaint();
		parentPanel.add(subTaskGrade, BorderLayout.CENTER);
		//fillStudentData();
		bindData();
		bindTable();






	}

	private void initController()
	{



		subTaskGrade.getHomeButton().addActionListener(l -> backHome());
		subTaskGrade.getBackButton().addActionListener(l -> back());
		subTaskGrade.getSaveButton().addActionListener(l -> save());
		subTaskGrade.getTable().getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				System.out.println("comment");
				if (e.getType() == TableModelEvent.UPDATE) {
					int row = e.getFirstRow();
					int column = e.getColumn();
					int id = (int) subTaskGrade.getTable().getModel().getValueAt(row, 0);
					Student student = null;
					for (var cSc : LoggedData.getSelectedCourse().getAllStudents()) {
						if (cSc.getId() == id) {
							student = cSc;
							break;
						}
					}

					String update = (String)subTaskGrade.getTable().getModel().getValueAt(row, column);
					Grade DBGrade = LoggedData.getDbManager().readGradeByStudentAndSubtaskId(student.getId(), LoggedData.getSelectedSubTask().getId());
					if (DBGrade == null) {
						LoggedData.getSelectedSubTask().addNewGrade(student);

					}
					switch (column) {
						case 2:
							Float score = LoggedData.getSelectedSubTask().getTotalPointsAvailable()-Float.parseFloat(update);
							LoggedData.getSelectedSubTask().getGrade(student).setAbsolutePointsScored(score);
							LoggedData.getDbManager().UpdateGradeByStudentIdandSubtaskId(LoggedData.getSelectedSubTask().getId(), score, student.getId());
							break;
						case 3:
							Float score1 = Float.parseFloat(update);
							if (LoggedData.getSelectedSubTask().getGrade(student) == null)
							{
								LoggedData.getSelectedSubTask().setStudentsGrade(student, score1);

							}
							else {
								LoggedData.getSelectedSubTask().getGrade(student).setAbsolutePointsScored(score1);

							}
							LoggedData.getDbManager().UpdateGradeByStudentIdandSubtaskId(LoggedData.getSelectedSubTask().getId(), score1, student.getId());
							break;
						case 4:
							String comment = update;
							System.out.println(comment);
							LoggedData.getSelectedSubTask().getGrade(student).setComment(comment);
							LoggedData.getDbManager().UpdateGradeCommentByStudentAndSubtaskId(LoggedData.getSelectedSubTask().getId(), comment, student.getId());
							break;
						case 5:
							Float scored2 = Float.parseFloat(update);
							LoggedData.getSelectedSubTask().setStudentsGrade(student, scored2);
							float actualScore = scored2 * LoggedData.getSelectedSubTask().getTotalPointsAvailable();
							LoggedData.getDbManager().UpdateGradeByStudentIdandSubtaskId(LoggedData.getSelectedSubTask().getId(), actualScore, student.getId());
							break;
						case 6:
							Float bonus = Float.parseFloat(update);
							LoggedData.getSelectedSubTask().getGrade(student).setBonusPoints(bonus);
							LoggedData.getDbManager().UpdateGradeBonusByStudentAndSubtaskId(LoggedData.getSelectedSubTask().getId(), bonus, student.getId());
							break;

					}






					//subTaskGrade.setTable();
					//subTaskGrade.getTable().getModel().fire


				}
			}
		});
		subTaskGrade.getSectionCombo().addItemListener(event -> {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				//System.out.println(ItemEvent.SELECTED+" 123");
				//String section = (String)event.getItem();
				//System.out.println(section);
				bindTable();

			}
		}
		);

	}



	public void bindData() {

		JComboBox j = subTaskGrade.getSectionCombo();

		for (CourseSection c : LoggedData.getCourseSectionList()) {
			//for (CourseSection cs : LoggedData.getSelectedCourse().getCourseSections()) {
			j.addItem(c);
		}

	}


	private void bindTable() {
		for (CourseSection c : LoggedData.getSelectedCourse().getCourseSections()) {
			var section = (CourseSection) subTaskGrade.getSectionCombo().getSelectedItem();
			//if(section == null){
			//	section = LoggedData.getCourseSectionList().get(0);
			//}
			if (c.getId() == section.getId()){


				int columSize = 7;
				String col[] = new String[7];
				col[0] = "Id";
				col[1] = "Student's Name";
				col[2] = "Point Deducted";
				col[3] = "Point Scored";
				//col[4] = "Student Status";
				col[4] = "Comment";
				//col[5] = "Group ID";
				col[5] = "% Score";
				col[6] = "Bonus Point";

				tableModel = new DefaultTableModel(col, 0) {
				};


				ArrayList<Student> studentList = c.getStudents();
				//System.out.println(studentList);
				//studentCount= studentCount+ studentList.size();
				if (studentList != null) {
					for (Student s : studentList) {
						Grade g = LoggedData.getDbManager().readGradeByStudentAndSubtaskId(s.getId(), LoggedData.getSelectedSubTask().getId());
						if (g == null) {
							System.out.println("null");
						}

						Object[] objs = new Object[columSize];
						objs[0] = s.getId();
						objs[1] = s.getName();
						if (g != null) {
							objs[2] = String.valueOf(LoggedData.getSelectedSubTask().getTotalPointsAvailable() - g.getAbsolutePointsScored());
						} else {
							objs[2] = LoggedData.getSelectedSubTask().getTotalPointsAvailable();
						}
						if (g != null) {
							objs[3] = String.valueOf(g.getAbsolutePointsScored());
						} else {
							objs[3] = 0;
						}
						if (g != null) {
							objs[4] = g.getComment();
						} else {
							objs[4] = "";
						}


						objs[5] = String.format("%.2f", LoggedData.getSelectedSubTask().getStudentsGrade(s) * 100);
						if (g != null) {
							objs[6] = g.getBonusPoints();
						} else {
							objs[6] = 0;
						}

						//objs[3] = subTask.getGrades().get(0).getAbsolutePointsScored();

						((DefaultTableModel) tableModel).addRow(objs);
					}
				}


				subTaskGrade.setTable(tableModel);
				//double mean = DoubleSummaryStatistics.

				//String stat = "Statistic:   Mean: "+ LoggedData.getGradingSystem().getActiveCourses().get(0). + "   Median: " + median + "   " + "Standard Deviation: " + standardDeviation
				//subTaskGrade.setStatisticLabel("");
			}
		}

		initController();
	}

	
	private void backHome()
	{
		CourseListController cLc = new CourseListController();
	}

	private void save()
	{

	}

	private void back()
	{
		ClassHomePageController cHP = new ClassHomePageController("");
	}
	


}

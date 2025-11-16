
package Services;
import BackEnd.*;
import databse.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;


public class InstructorService extends JFrame {
    
     private String currentInstructorId;
    private List<Course> courses;
    private List<Lesson> lessons;
    private List<Student> students;

    private JTable coursesTable;
    private JTable lessonsTable;
    private JTable studentsTable;
    private DefaultTableModel coursesTableModel;
    private DefaultTableModel lessonsTableModel;
    private DefaultTableModel studentsTableModel;

    private JButton createCourseButton;
    private JButton editCourseButton;
    private JButton deleteCourseButton;
    private JButton addLessonButton;
    private JButton editLessonButton;
    private JButton deleteLessonButton;
    private JButton viewStudentsButton;

    public InstructorService(String instructorId) {
        this.currentInstructorId = instructorId;
        initializeData();
        initializeUI();
        loadInstructorCourses();
    }

    private void initializeData() {
        courses = JsonDatabaseManager.loadCourses();
        lessons = JsonDatabaseManager.loadLessons();
        students = JsonDatabaseManager.loadStudents();
    }

    private void initializeUI() {
        setTitle("Instructor Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Courses", createCoursesPanel());
        tabbedPane.addTab("Lessons", createLessonsPanel());
        tabbedPane.addTab("Enrolled Students", createStudentsPanel());

        add(tabbedPane);
    }

   
}

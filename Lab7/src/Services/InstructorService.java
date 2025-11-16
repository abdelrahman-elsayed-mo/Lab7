
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
    
      private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Title", "Description", "Students", "Lessons"};
        coursesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        coursesTable = new JTable(coursesTableModel);
        JScrollPane scrollPane = new JScrollPane(coursesTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        createCourseButton = new JButton("Create Course");
        editCourseButton = new JButton("Edit Course");
        deleteCourseButton = new JButton("Delete Course");
        viewStudentsButton = new JButton("View Enrolled Students");

        buttonPanel.add(createCourseButton);
        buttonPanel.add(editCourseButton);
        buttonPanel.add(deleteCourseButton);
        buttonPanel.add(viewStudentsButton);

        createCourseButton.addActionListener(e -> createCourse());
        editCourseButton.addActionListener(e -> editCourse());
        deleteCourseButton.addActionListener(e -> deleteCourse());
        viewStudentsButton.addActionListener(e -> viewEnrolledStudents());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLessonsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Title", "Course", "Order", "Content Preview"};
        lessonsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lessonsTable = new JTable(lessonsTableModel);
        JScrollPane scrollPane = new JScrollPane(lessonsTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addLessonButton = new JButton("Add Lesson");
        editLessonButton = new JButton("Edit Lesson");
        deleteLessonButton = new JButton("Delete Lesson");

        buttonPanel.add(addLessonButton);
        buttonPanel.add(editLessonButton);
        buttonPanel.add(deleteLessonButton);

        addLessonButton.addActionListener(e -> addLesson());
        editLessonButton.addActionListener(e -> editLesson());
        deleteLessonButton.addActionListener(e -> deleteLesson());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Student ID", "Name", "Email", "Course"};
        studentsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentsTable = new JTable(studentsTableModel);
        JScrollPane scrollPane = new JScrollPane(studentsTable);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }


    

   
}

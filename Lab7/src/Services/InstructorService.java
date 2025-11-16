
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

        private void loadInstructorCourses() {
        coursesTableModel.setRowCount(0);
        for (Course course : courses) {
            if (course.getInstructorId().equals(currentInstructorId)) {
                int studentCount = course.getEnrolledStudentIds().size();
                int lessonCount = course.getLessons().size();
                coursesTableModel.addRow(new Object[]{
                    course.getId(),
                    course.getTitle(),
                    course.getDescription(),
                    studentCount + " students",
                    lessonCount + " lessons"
                });
            }
        }
        loadAllLessons();
        loadEnrolledStudents();
    }

    private void loadAllLessons() {
        lessonsTableModel.setRowCount(0);
        for (Lesson lesson : lessons) {
            Course course = findCourseById(lesson.getCourseId());
            if (course != null && course.getInstructorId().equals(currentInstructorId)) {
                String contentPreview = lesson.getContent().length() > 50 ? 
                    lesson.getContent().substring(0, 50) + "..." : lesson.getContent();
                lessonsTableModel.addRow(new Object[]{
                    lesson.getId(),
                    lesson.getTitle(),
                    course.getTitle(),
                    lesson.getOrder(),
                    contentPreview
                });
            }
        }
    }

    private void loadEnrolledStudents() {
        studentsTableModel.setRowCount(0);
        for (Course course : courses) {
            if (course.getInstructorId().equals(currentInstructorId)) {
                for (String studentId : course.getEnrolledStudentIds()) {
                    Student student = findStudentById(studentId);
                    if (student != null) {
                        studentsTableModel.addRow(new Object[]{
                            student.getId(),
                            student.getName(),
                            student.getEmail(),
                            course.getTitle()
                        });
                    }
                }
            }
        }
    }

    private void createCourse() {
        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        Object[] message = {
            "Title:", titleField,
            "Description:", descriptionScroll
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create New Course", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course title cannot be empty!");
                return;
            }

            Course newCourse = new Course(
                generateId(),
                title,
                description,
                currentInstructorId
            );

            courses.add(newCourse);
            JsonDatabaseManager.saveCourses(courses);
            loadInstructorCourses();
            JOptionPane.showMessageDialog(this, "Course created successfully!");
        }
    }

    private void editCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit.");
            return;
        }

        String courseId = (String) coursesTableModel.getValueAt(selectedRow, 0);
        Course course = findCourseById(courseId);
        if (course == null) return;

        JTextField titleField = new JTextField(course.getTitle());
        JTextArea descriptionArea = new JTextArea(course.getDescription(), 5, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        Object[] message = {
            "Title:", titleField,
            "Description:", descriptionScroll
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Course", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course title cannot be empty!");
                return;
            }

            course.setTitle(title);
            course.setDescription(description);
            
            JsonDatabaseManager.saveCourses(courses);
            loadInstructorCourses();
            JOptionPane.showMessageDialog(this, "Course updated successfully!");
        }
    }
    
    private void deleteCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.");
            return;
        }

        String courseId = (String) coursesTableModel.getValueAt(selectedRow, 0);
        Course course = findCourseById(courseId);
        if (course == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete the course: " + course.getTitle() + "?\nThis will also delete all associated lessons.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            courses.remove(course);
            lessons.removeIf(lesson -> lesson.getCourseId().equals(courseId));
            JsonDatabaseManager.saveCourses(courses);
            JsonDatabaseManager.saveLessons(lessons);
            loadInstructorCourses();
            JOptionPane.showMessageDialog(this, "Course deleted successfully!");
        }
    }

    private void addLesson() {
        List<Course> instructorCourses = getInstructorCourses();
        if (instructorCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please create a course first before adding lessons.");
            return;
        }

        JComboBox<Course> courseCombo = new JComboBox<>(instructorCourses.toArray(new Course[0]));
        JTextField titleField = new JTextField();
        JSpinner orderSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JTextArea contentArea = new JTextArea(10, 30);
        contentArea.setLineWrap(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);

        Object[] message = {
            "Course:", courseCombo,
            "Title:", titleField,
            "Order:", orderSpinner,
            "Content:", contentScroll
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Lesson", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            Course selectedCourse = (Course) courseCombo.getSelectedItem();
            
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and content cannot be empty!");
                return;
            }

            Lesson newLesson = new Lesson(
                generateId(),
                title,
                content,
                (Integer) orderSpinner.getValue(),
                selectedCourse.getId()
            );

            lessons.add(newLesson);
            JsonDatabaseManager.saveLessons(lessons);
            loadAllLessons();
            JOptionPane.showMessageDialog(this, "Lesson added successfully!");
        }
    }

    private void editLesson() {
        int selectedRow = lessonsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to edit.");
            return;
        }

        String lessonId = (String) lessonsTableModel.getValueAt(selectedRow, 0);
        Lesson lesson = findLessonById(lessonId);
        if (lesson == null) return;

        List<Course> instructorCourses = getInstructorCourses();
        JComboBox<Course> courseCombo = new JComboBox<>(instructorCourses.toArray(new Course[0]));
        courseCombo.setSelectedItem(findCourseById(lesson.getCourseId()));

        JTextField titleField = new JTextField(lesson.getTitle());
        JSpinner orderSpinner = new JSpinner(new SpinnerNumberModel(lesson.getOrder(), 1, 100, 1));
        JTextArea contentArea = new JTextArea(lesson.getContent(), 10, 30);
        contentArea.setLineWrap(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);

        Object[] message = {
            "Course:", courseCombo,
            "Title:", titleField,
            "Order:", orderSpinner,
            "Content:", contentScroll
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Lesson", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            Course selectedCourse = (Course) courseCombo.getSelectedItem();
            
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and content cannot be empty!");
                return;
            }

            lesson.setTitle(title);
            lesson.setContent(content);
            lesson.setOrder((Integer) orderSpinner.getValue());
            lesson.setCourseId(selectedCourse.getId());
            
            JsonDatabaseManager.saveLessons(lessons);
            loadAllLessons();
            JOptionPane.showMessageDialog(this, "Lesson updated successfully!");
        }
    }




    

   
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databse;


import BackEnd.Course;
import BackEnd.Student;
import BackEnd.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JsonDatabaseManager {

    private static final String USERS_FILE = "users.json";
    private static final String COURSES_FILE = "courses.json";

    private Map<String, User> userDatabase;
    private Map<String, Course> courseDatabase;

    public JsonDatabaseManager() {
        this.userDatabase = new HashMap<>();
        this.courseDatabase = new HashMap<>();
    }

    public User getUserByEmail(String email) {
        for (User user : userDatabase.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
    
    public User getUserById(String userId) {
        return userDatabase.get(userId);
    }

    public User getUserByUsername(String username) {
        for (User user : userDatabase.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public void saveUser(User user) {
        userDatabase.put(user.getUserId(), user);
    }

    public String generateNewUserId() {
        return UUID.randomUUID().toString();
    }

    public void saveCourse(Course course) {
        courseDatabase.put(course.getCourseId(), course);
    }
    
    public void deleteCourse(String courseId) {
        courseDatabase.remove(courseId);
    }

    public Course getCourseById(String courseId) {
        return courseDatabase.get(courseId);
    }
    
    public Collection<Course> getAllCourses() {
        return courseDatabase.values();
    }

    public List<Student> getStudentsByCourseId(String courseId) {
        List<Student> enrolledStudents = new ArrayList<>();
        Course course = getCourseById(courseId);
        if (course != null) {
            for (String studentId : course.getStudents()) {
                User user = getUserById(studentId);
                
                
                if (user instanceof Student student) {
                    
                    enrolledStudents.add(student);
                }
                
            }
        }
        return enrolledStudents;
    }
}
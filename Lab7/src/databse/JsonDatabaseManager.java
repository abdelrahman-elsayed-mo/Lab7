/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databse;



import BackEnd.Course;
import BackEnd.Instructor;
import BackEnd.Lesson;
import BackEnd.Student;
import BackEnd.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
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
    private Gson gson;

    public JsonDatabaseManager() {
        RuntimeTypeAdapterFactory<User> adapter = RuntimeTypeAdapterFactory
                .of(User.class, "role")
                .registerSubtype(Student.class, "Student")
                .registerSubtype(Instructor.class, "Instructor");

        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(adapter)
                .setPrettyPrinting()
                .create();

        this.userDatabase = loadUsers();
        this.courseDatabase = loadCourses();
    }

    private Map<String, User> loadUsers() {
        try (FileReader reader = new FileReader(USERS_FILE)) {
            Type type = new TypeToken<HashMap<String, User>>() {}.getType();
            Map<String, User> users = gson.fromJson(reader, type);
            return users != null ? users : new HashMap<>();
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private Map<String, Course> loadCourses() {
        try (FileReader reader = new FileReader(COURSES_FILE)) {
            Type type = new TypeToken<HashMap<String, Course>>() {}.getType();
            Map<String, Course> courses = gson.fromJson(reader, type);
            return courses != null ? courses : new HashMap<>();
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private void saveUsers() {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            gson.toJson(this.userDatabase, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCourses() {
        try (FileWriter writer = new FileWriter(COURSES_FILE)) {
            gson.toJson(this.courseDatabase, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(User user) {
        userDatabase.put(user.getUserId(), user);
        saveUsers();
    }

    public void saveCourse(Course course) {
        courseDatabase.put(course.getCourseId(), course);
        saveCourses();
    }

    public void deleteCourse(String courseId) {
        courseDatabase.remove(courseId);
        saveCourses();
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

    public String generateNewUserId() {
        return UUID.randomUUID().toString();
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
              if (user instanceof Student) {
                    Student student = (Student) user;
                    enrolledStudents.add(student);
                }
            }
        }
        return enrolledStudents;
    }
    
    public Lesson getLessonById(String lessonId) {
    for (Course course : courseDatabase.values()) {
        if (course.getLessons() != null) {
            for (BackEnd.Lesson lesson : course.getLessons()) {
                if (lesson.getLessonId().equals(lessonId)) {
                    return lesson;
                }
            }
        }
    }
    return null; // lesson not found
}
}
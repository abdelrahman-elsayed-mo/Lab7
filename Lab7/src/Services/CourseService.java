/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author YOUSSEF FATHY
 */
import BackEnd.Course;
import BackEnd.Lesson;
import databse.JsonDatabaseManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CourseService {

    private final JsonDatabaseManager dbManager;

    public CourseService(JsonDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public Collection<Course> browseAllCourses() {
        return dbManager.getAllCourses();
    }

    public Object getCourseDetails(String courseId) {
        return dbManager.getCourseById(courseId);
    }

    public List<Lesson> getCourseLessons(String courseId) {
        Course course = dbManager.getCourseById(courseId);
        if (course != null) {
            return course.getLessons();
        }
        return new ArrayList<>();
    }
}

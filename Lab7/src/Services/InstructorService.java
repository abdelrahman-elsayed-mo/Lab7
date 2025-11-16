
package Services;
import BackEnd.*;
import Utils.InputValidator;
import databse.*;

import java.util.ArrayList;
import java.util.List;

public class InstructorService {

    private JsonDatabaseManager dbManager;
    private Instructor currentInstructor;

    public InstructorService(JsonDatabaseManager dbManager, User currentUser) {
        this.dbManager = dbManager;
        if (currentUser instanceof Instructor) {
            this.currentInstructor = (Instructor) currentUser;
        } else {
            throw new IllegalArgumentException("User must be an Instructor");
        }
    }

    public Course createCourse(String title, String description) {
        if (!InputValidator.isRequiredFieldValid(title) || !InputValidator.isRequiredFieldValid(description)) {
            return null;
        }

        Course course = new Course(title, description, currentInstructor.getUserId());
        dbManager.saveCourse(course);

        currentInstructor.getCreatedCourses().add(course.getCourseId());
        dbManager.saveUser(currentInstructor);

        return course;
    }

    public boolean editCourse(String courseId, String newTitle, String newDescription) {
        Course course = (Course) dbManager.getCourseById(courseId);

        if (course == null || !course.getInstructorId().equals(currentInstructor.getUserId())) {
            return false;
        }

        if (InputValidator.isRequiredFieldValid(newTitle)) {
            course.setTitle(newTitle);
        }
        if (InputValidator.isRequiredFieldValid(newDescription)) {
            course.setDescription(newDescription);
        }

        dbManager.saveCourse(course);
        return true;
    }
    
    public boolean deleteCourse(String courseId) {
        Course course = (Course) dbManager.getCourseById(courseId);

        if (course == null || !course.getInstructorId().equals(currentInstructor.getUserId())) {
            return false;
        }
        
        dbManager.deleteCourse(courseId);
        
        currentInstructor.getCreatedCourses().remove(courseId);
        dbManager.saveUser(currentInstructor);
        
        return true;
    }

    public Lesson addLesson(String courseId, String lessonTitle, String lessonContent) {
        Course course = (Course) dbManager.getCourseById(courseId);

        if (course == null || !course.getInstructorId().equals(currentInstructor.getUserId())) {
            return null;
        }

        
        if (!InputValidator.isRequiredFieldValid(lessonTitle) || !InputValidator.isRequiredFieldValid(lessonContent)) {
            return null;
        }

        Lesson lesson = new Lesson(lessonTitle, lessonContent);
        course.addLesson(lesson);
        dbManager.saveCourse(course);

        return lesson;
    }

    public boolean editLesson(String courseId, String lessonId, String newTitle, String newContent) {
        Course course = (Course) dbManager.getCourseById(courseId);

        if (course == null || !course.getInstructorId().equals(currentInstructor.getUserId())) {
            return false;
        }

        Lesson lesson = course.getLessonById(lessonId);
        if (lesson == null) {
            return false;
        }

        if (InputValidator.isRequiredFieldValid(newTitle)) {
            lesson.setTitle(newTitle);
        }
        if (InputValidator.isRequiredFieldValid(newContent)) {
            lesson.setContent(newContent);
        }

        dbManager.saveCourse(course);
        return true;
    }

    public boolean deleteLesson(String courseId, String lessonId) {
        Course course = (Course) dbManager.getCourseById(courseId);

        if (course == null || !course.getInstructorId().equals(currentInstructor.getUserId())) {
            return false;
        }

        boolean removed = course.removeLessonById(lessonId);
        if (removed) {
            dbManager.saveCourse(course);
        }
        return removed;
    }

    public List<Student> viewEnrolledStudents(String courseId) {
        Course course = (Course) dbManager.getCourseById(courseId);

        if (course == null || !course.getInstructorId().equals(currentInstructor.getUserId())) {
            return new ArrayList<>();
        }

        return dbManager.getStudentsByCourseId(courseId);
    }
}


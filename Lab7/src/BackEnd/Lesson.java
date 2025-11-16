/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lesson {

    private String lessonId;
    private String title;
    private String content;
    private List<String> resources;

    public Lesson(String title, String content) {
        this.lessonId = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<>();
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getResources() {
        return resources;
    }

    public void addResource(String resource) {
        this.resources.add(resource);
    }
}

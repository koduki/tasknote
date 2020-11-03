/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps;

/**
 *
 * @author koduki
 */
public class Task {

    public String id;
    public String name;
    public String body;

    public Task(String id, String name, String body) {
        this.id = id;
        this.name = name;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", name=" + name + ", body=" + body + '}';
    }

}

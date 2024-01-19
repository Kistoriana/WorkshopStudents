package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class StudentStorage {
    private Map<Long, Student> studentStorageMap = new HashMap<>();
    private StudentSurnameStorage studentSurnameStorage = new StudentSurnameStorage();
    private Long currentId = 0L;
    public Long createStudent(Student student) {
        Long nextId = getNextId();
        studentStorageMap.put(nextId, student);
        studentSurnameStorage.studentCreated(nextId, student.getSurname());
        return nextId;
    }


    public boolean updateStudent(Long id, Student student) {
        if (!studentStorageMap.containsKey(id)) {
            return false;
        } else {
            String newSurname = student.getSurname();
            String oldSurname = studentStorageMap.get(id).getSurname();
            studentSurnameStorage.studentUpdated(id, oldSurname, newSurname);
            studentStorageMap.put(id, student);
            return true;
        }
    }

    public boolean deleteStudent(Long id) {
        Student removed = studentStorageMap.remove(id);
        if (removed != null) {
            String surname = removed.getSurname();
            studentSurnameStorage.studentDeleted(id, surname);
        }
        return removed != null;
    }

    public void search(String surname) {
        Set<Long> students = studentSurnameStorage.getStudentBySurnames(surname);
        if (students.isEmpty()) {
            throw new NoSuchElementException();
        }
        for(Long studentId : students) {
            Student student = studentStorageMap.get(studentId);
            System.out.println(student);
        }
    }

    public void searchFromTo(String surname1, String surname2) {
        Set<Long> studentIds = studentSurnameStorage.getStudentBySurnamesFromAndTo(surname1, surname2);
        if (studentIds.isEmpty()) {
            throw new NoSuchElementException();
        }
        Set<Student> students = new TreeSet<>((s1, s2) -> s1.getSurname().compareTo(s2.getSurname()));
        for (Long studentId : studentIds) {
            Student student = studentStorageMap.get(studentId);
            students.add(student);
        }
        for (Student student : students) {
            System.out.println(student);
        }
    }


    public Long getNextId() {
        currentId = currentId + 1;
        return currentId;
    }

    public void printAll() {
        System.out.println(studentStorageMap);
    }

    public void printMap(Map<String, Long> data) {
        data.entrySet().stream().forEach(e -> {
            System.out.println(e.getKey() + " - " + e.getValue());
        });
    }

    public Map<String, Long> getCountByCourse() {
        Map<String, Long> res = studentStorageMap.values().stream()
                .collect(Collectors.toMap(
                        s -> s.getCourse(),
                        s -> 1L,
                        (count1, count2) -> count1 + count2
                ));
        return res;
    }

    public Map<String, Long> getCountByCity() {
        Map<String, Long> res = studentStorageMap.values().stream()
                .collect(Collectors.toMap(
                   s -> s.getCity(),
                   s -> 1L,
                        (count1, count2) -> count1 + count2
                ));
        return res;
    }
}

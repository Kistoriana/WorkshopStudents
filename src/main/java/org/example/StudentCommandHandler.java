package org.example;

import java.util.Map;
import java.util.NoSuchElementException;

public class StudentCommandHandler {

    private StudentStorage studentStorage = new StudentStorage();
    public void processCommand(Command command) {
        Action action = command.getAction();
        switch (action) {
            case CREATE -> {
                processCreateCommand(command);
                break;
            }
            case UPDATE -> {
                processUpdateCommand(command);
                break;
            }
            case DELETE -> {
                processDeleteCommand(command);
                break;
            }
            case STATS_BY_COURSE -> {
                processStatsByCourseCommand(command);
                break;
            }
            case STATS_BY_CITY -> {
                processStatsByCityCommand(command);
            }
            case SEARCH -> {
                processSearchCommand(command);
                break;
            }
            default -> {
                System.out.println("Действие " + action + " не поддерживается");
            }


        }
        System.out.println("Обработка команды. Действие: " +
                command.getAction().name() + ", данные: " +
                command.getData());
    }

    private void processSearchCommand(Command command) {
        String surname = command.getData();
        String[] surnameArray = surname.split(",");
        try {
            if (surname.isEmpty()) {
                studentStorage.printAll();
            } else if (surnameArray.length == 1){
                studentStorage.search(surname);
            } else if (surnameArray.length == 2) {
                studentStorage.searchFromTo(surnameArray[0], surnameArray[1]);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Студент с фамилией " + surname + " не найден");
        }
    }
    private void processStatsByCourseCommand(Command command) {
        Map<String, Long> data = studentStorage.getCountByCourse();
        studentStorage.printMap(data);
    }

    private void processStatsByCityCommand(Command command) {
        Map<String, Long> data = studentStorage.getCountByCity();
        studentStorage.printMap(data);
    }

    private void processCreateCommand(Command command) {
        String data = command.getData();
        String[] dataArray = data.split(",");
        Student student = new Student();
        try{
            student.setSurname(dataArray[0]);
            student.setName(dataArray[1]);
            student.setCourse(dataArray[2]);
            student.setCity(dataArray[3]);
            student.setAge(Integer.valueOf(dataArray[4]));
            if(studentStorage.createStudent(student) != null) {
                studentStorage.printAll();
            } else{
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка при создании студента: " + e.getMessage());
        }
    }

    public void processUpdateCommand(Command command) {
        String data = command.getData();
        String[] dataArray = data.split(",");

        Student student = new Student();
        try {
            Long id = Long.valueOf(dataArray[0]);
            student.setSurname(dataArray[1]);
            student.setName(dataArray[2]);
            student.setCourse(dataArray[3]);
            student.setCity(dataArray[4]);
            student.setAge(Integer.valueOf(dataArray[5]));

            if (studentStorage.updateStudent(id, student)) {
                studentStorage.printAll();
            } else {
                throw new IllegalArgumentException("Студент с указанным ID не найден");
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка при обновлении студента: " + e.getMessage());
        }
    }



    public void processDeleteCommand(Command command) {
        String data = command.getData();
        Long id = Long.valueOf(data);
        try {
            if (studentStorage.deleteStudent(id)) {
                studentStorage.printAll();
            } else {
                throw new IllegalArgumentException("Студент с указанным ID не найден");
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка при удалении студента: " + e.getMessage());
        }
    }

}

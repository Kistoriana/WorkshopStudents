package org.example;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class StudentSurnameStorage {

    private TreeMap<String, Set<Long>> surnamesTreeMap = new TreeMap<>();

    public void studentCreated(Long id, String surname) {
        Set<Long> existingIds = surnamesTreeMap.getOrDefault(surname, new HashSet<>());
        existingIds.add(id);
        surnamesTreeMap.put(surname, existingIds);
    }

    public void studentDeleted(Long id, String surname) {
        surnamesTreeMap.get(surname).remove(id);
    }

    public void studentUpdated(Long id, String oldSurname, String newSurname) {
        studentDeleted(id, oldSurname);
        studentCreated(id, newSurname);
    }

    /**
     * Данный метод возвращает уникальные идентификаторы студентов,
     * чьи фамилии меньше или равны переданной.
     * @return set
     */
//    public Set<Long> getStudentBySurnamesLessOrEqualThan(String surname) {
//        Set<Long> res = surnamesTreeMap.headMap(surname, true)
//                .values()
//                .stream()
//                .flatMap(longs -> longs.stream())
//                .collect(Collectors.toSet());
//        return res;
//    }


    /**
     * - Если введена пустая строка, то вывести полный список студентов.
     * - Если введена только одна фамилия, то выполнить точный поиск студентов по фамилии.
     * - Если введены 2 фамилии, разделенные запятой (,), то вывести всех студентов, чьи фамилии в алфавитном порядке
     * >= первой фамилии и <= второй фамилии.
     * @param surname
     * @return
     */
    public Set<Long> getStudentBySurnames(String surname) {
        Set<Long> res = surnamesTreeMap.entrySet()
                .stream()
                .filter(longs -> longs.getKey().equals(surname))
                .flatMap(longs -> longs.getValue().stream())
                .collect(Collectors.toSet());
        return res;
    }
    public Set<Long> getStudentBySurnamesFromAndTo(String surname1, String surname2) {
        Set<Long> res = surnamesTreeMap.subMap(surname1, surname2 + "\0")
                .values()
                .stream()
                .flatMap(longs -> longs.stream())
                .collect(Collectors.toSet());
        return res;
    }
}

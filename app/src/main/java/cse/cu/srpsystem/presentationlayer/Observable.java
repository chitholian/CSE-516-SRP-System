package cse.cu.srpsystem.presentationlayer;

import java.util.HashSet;
import java.util.Set;

public interface Observable {
    Set<Observer> observers = new HashSet<>();

    void onUpdate();

    void addObserver(Observer observer);

    void removeObserver(Observer observer);
}

package ru.nsu.fit.pvasilev.tfjc.task6;

public class Task6 {
    public static void main(String[] args) {
        var company = new Company(6);
        var founder = new Founder(company);
        founder.start();
    }
}

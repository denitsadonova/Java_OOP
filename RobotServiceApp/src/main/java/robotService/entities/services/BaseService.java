package robotService.entities.services;

import robotService.entities.robot.Robot;
import robotService.entities.supplements.Supplement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static robotService.common.ConstantMessages.NOT_ENOUGH_CAPACITY_FOR_ROBOT;
import static robotService.common.ExceptionMessages.SERVICE_NAME_CANNOT_BE_NULL_OR_EMPTY;

public abstract class BaseService implements Service{
    private String name;
    private int capacity;
    private List<Supplement> supplements;
    private List<Robot> robots;

    protected BaseService(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        supplements = new ArrayList<>();
        robots = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException(SERVICE_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }

    }

    @Override
    public Collection<Robot> getRobots() {
        return this.robots;
    }

    @Override
    public Collection<Supplement> getSupplements() {
        return this.supplements;
    }

    @Override
    public void addRobot(Robot robot) {
        if (robots.size() == capacity) {
            throw new IllegalStateException(NOT_ENOUGH_CAPACITY_FOR_ROBOT);
        }
        robots.add(robot);

    }

    @Override
    public void removeRobot(Robot robot) {
robots.remove(robot);
    }

    @Override
    public void addSupplement(Supplement supplement) {
supplements.add(supplement);
    }

    @Override
    public void feeding() {
robots.forEach(Robot::eating);
    }

    @Override
    public int sumHardness() {
        return supplements.stream().mapToInt(Supplement::getHardness).sum();
    }

    @Override
    public String getStatistics() {

        StringBuilder robotsNames = new StringBuilder();
        if (robots.size() > 0) {
            for (Robot robot : robots) {
                String name1 = robot.getName();
                robotsNames.append(name1 + " ");
            }

        } else {
            robotsNames.append("none");
        }


        return String.format("%s %s:%n" +
                "Robots: %s%n" +
                "Supplements: %d Hardness: %d", getName(), this.getClass().getSimpleName(),
                robotsNames.toString().trim()
        , supplements.size(), sumHardness());
    }
}

package robotService.core;

import robotService.entities.robot.FemaleRobot;
import robotService.entities.robot.MaleRobot;
import robotService.entities.robot.Robot;
import robotService.entities.services.MainService;
import robotService.entities.services.SecondaryService;
import robotService.entities.services.Service;
import robotService.entities.supplements.MetalArmor;
import robotService.entities.supplements.PlasticArmor;
import robotService.entities.supplements.Supplement;
import robotService.repositories.SupplementRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static robotService.common.ConstantMessages.*;
import static robotService.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {

    private SupplementRepository supplements;
    private List<Service> services;

    public ControllerImpl() {
        supplements = new SupplementRepository();
        services = new ArrayList<>();
    }

    @Override
    public String addService(String type, String name) {
        Service service;
        switch (type){
            case "MainService":
                service = new MainService(name);
                break;
            case "SecondaryService":
                service = new SecondaryService(name);
                break;
            default: throw new NullPointerException(INVALID_SERVICE_TYPE);

        }
        services.add(service);
        return String.format(SUCCESSFULLY_ADDED_SERVICE_TYPE, type);
    }

    @Override
    public String addSupplement(String type) {
        Supplement supplement;
        switch (type) {
            case "PlasticArmor":
                supplement = new PlasticArmor();
                break;
            case "MetalArmor":
                supplement = new MetalArmor();
                break;
            default: throw new IllegalArgumentException(INVALID_SUPPLEMENT_TYPE);
        }
        supplements.addSupplement(supplement);
        return String.format(SUCCESSFULLY_ADDED_SUPPLEMENT_TYPE, type);
    }

    @Override
    public String supplementForService(String serviceName, String supplementType) {
        Service service = services.stream().filter(s -> s.getName().equals(serviceName)).findFirst().orElse(null);
        Supplement first = supplements.findFirst(supplementType);
        if (first == null) {
            throw new IllegalArgumentException(String.format(NO_SUPPLEMENT_FOUND, supplementType)); }

        if (services.contains(service)) {
            assert service != null;
            service.addSupplement(first);
            supplements.removeSupplement(first);
        }
        return String.format(SUCCESSFULLY_ADDED_SUPPLEMENT_IN_SERVICE, supplementType, serviceName);


    }

    @Override
    public String addRobot(String serviceName, String robotType, String robotName, String robotKind, double price) {
        Robot robot;
        switch (robotType){
            case "FemaleRobot":
                robot = new FemaleRobot(robotName, robotKind, price);
                break;
            case "MaleRobot":
                robot = new MaleRobot(robotName, robotKind, price);
                break;
            default: throw new IllegalArgumentException(INVALID_ROBOT_TYPE);
        }
        Service service = services.stream().filter(s -> s.getName().equals(serviceName)).findFirst().orElse(null);
if (service != null) {
    String simpleName = service.getClass().getSimpleName();
    boolean menCanLive = simpleName.equals("MainService") && robotType.equals("MaleRobot");
    boolean femaleCanLive = simpleName.equals("SecondaryService") && robotType.equals("FemaleRobot");

    if (menCanLive || femaleCanLive) {
    service.addRobot(robot);
    return String.format(SUCCESSFULLY_ADDED_ROBOT_IN_SERVICE, robotType, serviceName);
}

    }
        return UNSUITABLE_SERVICE;
    }


    @Override
    public String feedingRobot(String serviceName) {
        Service service = services.stream().filter(s -> s.getName().equals(serviceName)).findFirst().orElse(null);
        Collection<Robot> robots = service != null ? service.getRobots() : null;
        if (service != null) {
            service.feeding();
        }
        return String.format(FEEDING_ROBOT, robots != null ? robots.size() : 0);
    }

    @Override
    public String sumOfAll(String serviceName) {
        Service service = services.stream().filter(s -> s.getName().equals(serviceName)).findFirst().orElse(null);
        double sumRobots = service != null ? service.getRobots().stream().mapToDouble(Robot::getPrice).sum() : 0;
        double sumSupplements = service != null ? service.getSupplements().stream().mapToDouble(Supplement::getPrice).sum() : 0;
        return String.format(VALUE_SERVICE,serviceName, sumRobots + sumSupplements);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        for (Service service : services) {
            String statistics = service.getStatistics();
            sb.append(statistics);
            sb.append(System.lineSeparator());
        }
      return sb.toString().trim();
    } }
